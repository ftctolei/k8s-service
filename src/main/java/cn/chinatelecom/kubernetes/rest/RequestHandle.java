package cn.chinatelecom.kubernetes.rest;

import cn.chinatelecom.kubernetes.rest.bean.ApiResponseBean;
import cn.chinatelecom.kubernetes.rest.bean.K8sDeploymentBean;
import cn.chinatelecom.kubernetes.rest.bean.K8sPodBean;
import cn.chinatelecom.kubernetes.rest.bean.K8sServiceBean;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class RequestHandle {
    private static final Logger log = LoggerFactory.getLogger(RequestHandle.class);

    public static ApiResponseBean getPod(ApiResponseBean bean, String nameSpace, String deploymentName){

        KubernetesClient client = K8sClient.connect();

        PodList podList = client.pods().inNamespace(nameSpace).withLabel(deploymentName).list();
        ServiceList serviceList = client.services().inNamespace(nameSpace).withLabel(deploymentName).list();


        List<K8sPodBean> k8sPodList = new ArrayList<>();
        K8sServiceBean k8sService = new K8sServiceBean();

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


    public static ApiResponseBean updatePodReplicas(ApiResponseBean bean, String nameSpace, String deploymentName, int replicas){

        KubernetesClient client = K8sClient.connect();
        int replicasAfter;
        Deployment updatedDeploy = client.apps().deployments().inNamespace(nameSpace).withName(deploymentName).edit(
                d -> new DeploymentBuilder(d).editSpec().withReplicas(replicas).endSpec().build());
        try {
            replicasAfter = updatedDeploy.getSpec().getReplicas();
        }catch(Exception e){
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

}
