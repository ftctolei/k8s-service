package cn.kubernetes.service.rest.bean;

public class K8sPodBean {
    private String podName;
    private String podIP;

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getPodIP() {
        return podIP;
    }

    public void setPodIP(String podIP) {
        this.podIP = podIP;
    }

}
