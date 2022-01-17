package test;

import cn.chinatelecom.kubernetes.rest.bean.K8sPodBean;
import cn.chinatelecom.kubernetes.rest.bean.K8sServiceBean;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.ArrayList;
import java.util.List;


public class k8sTest {

    private static String KubeToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6Im1QY3lNRk05V2RiakVpZHJBWnNPeGZHd0NHbDl0VnZ1NEdONENiMkExUkEifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJ2c3MtbWV0ZXItd29ya3NwYWNlIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImRlZmF1bHQtdG9rZW4tOWJmcTkiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiZGVmYXVsdCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjQwY2QxZDU3LWJlM2QtNDkyMC1iMjc3LWFkZjUzMzQwZDliOCIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDp2c3MtbWV0ZXItd29ya3NwYWNlOmRlZmF1bHQifQ.Mc1LL0xgDwecAl-LsrUcVQj8jhrkvcfdTtJQpbIgvKqtI6GetpUNetHRze18vvj688ZJSiv7a5oXO4t_qZHKjVgnCoZMvP_kU3UL1ERcfV0TrPSqw6_AllAJcIb401-mJZiEbtLN99wOKRqWkBlRxxDKir2Z6N2nmx5L24KrXkTdtXP5V5Ekq_VE9zPeySyHfgNwOgH8qdpuD73DLvQpa-LD2XHqtEhHap9IKubB1ILKZ12wk3FgRkRQbkyW_uSHLqLlJmwpDnJA_yW0bJiro06XVN9xvNdTou-2n9hRRrbiMwBhkV4XTYNcnA3Z6Bb2E9UFbf0paVtn1K-D_Vb0Rw";

    public static void main(String[] args) {
        Config config = new ConfigBuilder()
                .withMasterUrl("https://172.24.2.216:6443")
                .withTrustCerts(true)
                .withOauthToken(KubeToken)
                .build();
        KubernetesClient client = new DefaultKubernetesClient(config);

       /*
       // 接口2 测试
        PodList podList = client.pods().inNamespace("vss-meter-workspace").withLabel("app=mysql").list();
        ServiceList serviceList =  client.services().inNamespace("vss-meter-workspace").withLabel("app=mysql").list();

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
        System.out.printf(k8sService.toString());
      */

        //接口3 测试
/*
        String dep_name_1 = "test_error" ;
        String dep_name_2 = "mariadb" ;
        String ns = "vss-meter-workspace";

        Deployment deploy = client.apps().deployments().inNamespace(ns).withName(dep_name_1).get();


        try {
            Deployment updatedDeploy = client.apps().deployments().inNamespace(ns).withName(dep_name_1).edit(
                    d -> new DeploymentBuilder(d).editSpec().withReplicas(2).endSpec().build()
            );
        }catch(Exception e){
            System.out.println("未查询到结果");
            System.out.println(e.getMessage());
        }


        Deployment deploya = client.apps().deployments().inNamespace(ns).withName(dep_name_1).get();
        //System.out.println("修改后副本数: " + deploya.getSpec().getReplicas());
*/




    }

}
