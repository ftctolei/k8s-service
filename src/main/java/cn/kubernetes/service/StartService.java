package cn.kubernetes.service;

import cn.kubernetes.service.rest.Constant;
import cn.kubernetes.service.rest.JettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;


public class StartService {

    private static final Logger log = LoggerFactory.getLogger(StartService.class);


    public static void main(String[] args) throws Exception {
        ResourceBundle resource = ResourceBundle.getBundle("config");
        int port = 8080;
        String k8sMasterUrl = "https://172.24.2.216:6443";
        String k8sToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6Im1QY3lNRk05V2RiakVpZHJBWnNPeGZHd0NHbDl0VnZ1NEdONENiMkExUkEifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJ2c3MtbWV0ZXItd29ya3NwYWNlIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImRlZmF1bHQtdG9rZW4tOWJmcTkiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiZGVmYXVsdCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjQwY2QxZDU3LWJlM2QtNDkyMC1iMjc3LWFkZjUzMzQwZDliOCIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDp2c3MtbWV0ZXItd29ya3NwYWNlOmRlZmF1bHQifQ.Mc1LL0xgDwecAl-LsrUcVQj8jhrkvcfdTtJQpbIgvKqtI6GetpUNetHRze18vvj688ZJSiv7a5oXO4t_qZHKjVgnCoZMvP_kU3UL1ERcfV0TrPSqw6_AllAJcIb401-mJZiEbtLN99wOKRqWkBlRxxDKir2Z6N2nmx5L24KrXkTdtXP5V5Ekq_VE9zPeySyHfgNwOgH8qdpuD73DLvQpa-LD2XHqtEhHap9IKubB1ILKZ12wk3FgRkRQbkyW_uSHLqLlJmwpDnJA_yW0bJiro06XVN9xvNdTou-2n9hRRrbiMwBhkV4XTYNcnA3Z6Bb2E9UFbf0paVtn1K-D_Vb0Rw";

        try{
            port = Integer.parseInt(resource.getString(Constant.REST_API_SERVER_PORT));
            k8sMasterUrl = resource.getString(Constant.K8S_MASTER_URL);
            k8sToken = resource.getString(Constant.K8S_MASTER_TOKEN);
        }catch (Exception ex){
            log.warn("获取配置项出错, 使用默认配置项: jetty port:{}, k8sMasterUrl:{}, k8sToken:{}.", port, k8sMasterUrl, k8sToken);
            log.warn("获取配置项出错. Exception: {}", ex.toString());
        }
        log.info("使用以下配置项启动: port:{}, k8sMasterUrl:{}, k8sToken:{}.", port, k8sMasterUrl, k8sToken);
        JettyServer server = new JettyServer();
        server.start(port);

    }
}
