package cn.kubernetes.service.rest;

import cn.kubernetes.service.rest.bean.ApiResponseBean;
import cn.kubernetes.service.rest.bean.K8sDeploymentBean;
import cn.kubernetes.service.rest.bean.K8sPodBean;
import cn.kubernetes.service.rest.bean.K8sServiceBean;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.util.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cn.kubernetes.service.rest.Constant.*;


public class RequestHandle {
    private static final Logger log = LoggerFactory.getLogger(RequestHandle.class);

    public static ApiResponseBean getPod(ApiResponseBean bean, String nameSpace, String deploymentName) {

        KubernetesClient client = K8sClient.connect();
        PodList podList = client.pods().inNamespace(nameSpace).withLabel(deploymentName).list();
        ServiceList serviceList = client.services().inNamespace(nameSpace).withLabel(deploymentName).list();

        List<K8sPodBean> k8sPodList = new ArrayList<>();
        K8sServiceBean k8sService = new K8sServiceBean();

        // TODO 这段逻辑有问题(IndexOutOfBoundsException), 待确认, (nameSpace:vss-meter-workspace, deploymentName: nginx-deployment)
        // 同一个namespace下相同的label只存在一个service
        k8sService.setClusterIP(serviceList.getItems().get(0).getSpec().getClusterIP());
        k8sService.setClusterPort(serviceList.getItems().get(0).getSpec().getPorts().get(0).getPort());

        podList.getItems().forEach(pod -> {
            K8sPodBean k8sPod = new K8sPodBean();
            k8sPod.setPodName(pod.getMetadata().getName());
            k8sPod.setPodIP(pod.getStatus().getPodIP());
            k8sPodList.add(k8sPod);
        });
        k8sService.setPodList(k8sPodList);

        bean.setCode(200);
        bean.setMessage(k8sService);
        bean.setResponseTime(Tools.getNowTime());
        return bean;
    }


    public static ApiResponseBean updatePodReplicas(ApiResponseBean bean, String nameSpace, String deploymentName, int replicas) {

        KubernetesClient client = K8sClient.connect();
        int replicasAfter;
        Deployment updatedDeploy = client.apps().deployments().inNamespace(nameSpace).withName(deploymentName).edit(
                d -> new DeploymentBuilder(d).editSpec().withReplicas(replicas).endSpec().build());
        try {
            replicasAfter = updatedDeploy.getSpec().getReplicas();
        } catch (Exception e) {
            bean.setCode(201);
            bean.setMessage("Request ERROR, Check your request and parameters. ");
            bean.setResponseTime(Tools.getNowTime());
            return bean;
        }

        K8sDeploymentBean deployment = new K8sDeploymentBean();
        deployment.setNameSpace(nameSpace);
        deployment.setDeploymentName(deploymentName);
        deployment.setReplicas(replicasAfter);

        bean.setCode(200);
        bean.setMessage(deployment);
        bean.setResponseTime(Tools.getNowTime());
        return bean;
    }


    /**
     * TODO yaml文件格式校验
     * fabric8io/kubernetes-client 20220110 适配到Kubernetes v1.23.0, 当前Kubernetes最新版本v1.23.1  .
     * 此接口换用kubernetes-client/java api.
     */
    public static ApiResponseBean updatePodViaYaml(ApiResponseBean bean, String nameSpace, String op, InputStream yamlFile) {

        ApiClient k8sOClient = K8sClientOfficial.connectFromToken();
        Reader reader = new InputStreamReader(new BufferedInputStream(yamlFile));

        switch (op) {
            case REST_OP_CREATE_DEPLOYMENT:
                V1Deployment yamlDep;
                V1Deployment createDepResult;
                try {
                    yamlDep = (V1Deployment) Yaml.load(reader);

                } catch (IOException e) {
                    log.error(e.toString());
                    bean.setCode(201);
                    bean.setMessage("[ERROR] get yaml failed: " + e.getMessage());
                    bean.setResponseTime(Tools.getNowTime());
                    return bean;
                }

                AppsV1Api api = new AppsV1Api();
                try {
                    createDepResult = api.createNamespacedDeployment(nameSpace, yamlDep, null, null, null);
                } catch (ApiException e) {
                    log.error(e.toString());
                    bean.setCode(201);
                    bean.setMessage("[ERROR] Create namespaced deployment failed: " + e.getMessage());
                    bean.setResponseTime(Tools.getNowTime());
                    return bean;
                }

                K8sDeploymentBean deployment = new K8sDeploymentBean();
                deployment.setNameSpace(nameSpace);
                deployment.setDeploymentName(Optional.ofNullable(createDepResult.getMetadata().getName()).orElse(""));
                deployment.setReplicas(Optional.ofNullable(createDepResult.getSpec().getReplicas()).orElse(0));

                bean.setCode(200);
                bean.setMessage(deployment);
                bean.setResponseTime(Tools.getNowTime());
                return bean;

            case REST_OP_CREATE_POD:
                V1Pod yamlPod;
                V1Pod createPodResult;
                try {
                    yamlPod = (V1Pod) Yaml.load(reader);

                } catch (IOException e) {
                    log.error(e.toString());
                    bean.setCode(201);
                    bean.setMessage("[ERROR] get yaml failed: " + e.getMessage());
                    bean.setResponseTime(Tools.getNowTime());
                    return bean;
                }

                CoreV1Api coreV1Api = new CoreV1Api();
                try {
                    createPodResult = coreV1Api.createNamespacedPod(nameSpace, yamlPod, null, null, null);
                } catch (ApiException e) {
                    log.error(e.toString());
                    bean.setCode(201);
                    bean.setMessage("[ERROR] Create namespaced Pod failed: " + e.getMessage());
                    bean.setResponseTime(Tools.getNowTime());
                    return bean;
                }

                K8sPodBean pod = new K8sPodBean();
                pod.setNameSpace(nameSpace);
                pod.setPodName(Optional.ofNullable(createPodResult.getMetadata().getName()).orElse(""));

                bean.setCode(200);
                bean.setMessage(pod);
                bean.setResponseTime(Tools.getNowTime());
                return bean;

            //创建service
            case REST_OP_CREATE_SERVICE:
                V1Service yamlService;
                V1Service creatServiceResult;
                try {
                    yamlService = (V1Service) Yaml.load(reader);

                } catch (IOException e) {
                    log.error(e.toString());
                    bean.setCode(201);
                    bean.setMessage("[ERROR] get yaml failed: " + e.getMessage());
                    bean.setResponseTime(Tools.getNowTime());
                    return bean;
                }

                CoreV1Api apiInstance = new CoreV1Api();
                try {
                    creatServiceResult = apiInstance.createNamespacedService(nameSpace, yamlService, null, null, null);
                } catch (ApiException e) {
                    log.error(e.toString());
                    bean.setCode(201);
                    bean.setMessage("[ERROR] Create namespaced Service failed: " + e.getMessage());
                    bean.setResponseTime(Tools.getNowTime());
                    return bean;
                }

                K8sServiceBean service = new K8sServiceBean();
                service.setNameSpace(nameSpace);
                service.setServiceName(Optional.ofNullable(creatServiceResult.getMetadata().getName()).orElse(""));

                bean.setCode(200);
                bean.setMessage(service);
                bean.setResponseTime(Tools.getNowTime());
                return bean;

            default:
                bean.setCode(201);
                bean.setMessage("[ERROR] op NOT allowed. ");
                bean.setResponseTime(Tools.getNowTime());
                return bean;
        }
    }

    public static ApiResponseBean deleteDeployment(ApiResponseBean bean, String nameSpace, String deploymentName) {
        ApiClient k8sOClient = K8sClientOfficial.connectFromToken();
        AppsV1Api api = new AppsV1Api();
        try {
            api.deleteNamespacedDeployment(deploymentName, nameSpace, null, null, null, null, null, null);
        } catch (Exception e) {
            bean.setCode(201);
            bean.setMessage("Request ERROR, Check your request and parameters. ");
            bean.setResponseTime(Tools.getNowTime());
            return bean;
        }

        K8sDeploymentBean deployment = new K8sDeploymentBean();
        deployment.setNameSpace(nameSpace);
        deployment.setDeploymentName(deploymentName);

        bean.setCode(200);
        bean.setMessage(deployment);
        bean.setResponseTime(Tools.getNowTime());
        return bean;
    }

    public static ApiResponseBean deletePod(ApiResponseBean bean, String nameSpace, String podName) {
        ApiClient k8sOClient = K8sClientOfficial.connectFromToken();
        CoreV1Api coreV1Api = new CoreV1Api();

        try {
            coreV1Api.deleteNamespacedPod(podName, nameSpace, null, null, null, null, null, null);
        } catch (Exception e) {
            bean.setCode(201);
            bean.setMessage("Request ERROR, Check your request and parameters. ");
            bean.setResponseTime(Tools.getNowTime());
            return bean;
        }

        K8sPodBean pod = new K8sPodBean();
        pod.setNameSpace(nameSpace);
        pod.setPodName(podName);

        bean.setCode(200);
        bean.setMessage(pod);
        bean.setResponseTime(Tools.getNowTime());
        return bean;
    }


}
