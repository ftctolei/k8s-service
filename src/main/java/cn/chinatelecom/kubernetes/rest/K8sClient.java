package cn.chinatelecom.kubernetes.rest;

import cn.chinatelecom.kubernetes.Constant;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

public class K8sClient {
    private static final Logger log = LoggerFactory.getLogger(K8sClient.class);
    private static ResourceBundle resource = ResourceBundle.getBundle("config");


    static KubernetesClient connect(){
        String k8sMasterUrl = resource.getString(Constant.K8S_MASTER_URL);
        String k8sToken = resource.getString(Constant.K8S_MASTER_TOKEN);

        Config config = new ConfigBuilder()
                .withMasterUrl(k8sMasterUrl)
                .withTrustCerts(true)
                .withOauthToken(k8sToken)
                .build();

        try (KubernetesClient client = new DefaultKubernetesClient(config)) {
            return client;
        }

    }
}
