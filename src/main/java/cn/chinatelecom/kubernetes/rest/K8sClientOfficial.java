package cn.chinatelecom.kubernetes.rest;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 *  fabric8io/kubernetes-client 优点: 方便使用, 代码易读易维护, 社区活跃.
 *  kubernetes-client/java 优点: kubernetes官方维护, 紧跟kubernetes版本.
 *  此处k8s client使用kubernetes-client/java
 */
public class K8sClientOfficial {
    private static final Logger log = LoggerFactory.getLogger(K8sClientOfficial.class);
    private static final ResourceBundle resource = ResourceBundle.getBundle("config");

    public static ApiClient connectFromToken() {
        String k8sMasterUrl = resource.getString(Constant.K8S_MASTER_URL);
        String k8sToken = resource.getString(Constant.K8S_MASTER_TOKEN);

        ApiClient apiClient = Config.fromToken(k8sMasterUrl, k8sToken, false);
        Configuration.setDefaultApiClient(apiClient);
        return apiClient;

    }
}
