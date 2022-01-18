package cn.kubernetes.service.rest.bean;

import java.util.List;

public class K8sServiceBean {
    private String clusterIP;
    private int clusterPort;
    private List<K8sPodBean> podList;
    private String serviceName;
    private String nameSpace;

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getClusterIP() {
        return clusterIP;
    }

    public void setClusterIP(String clusterIP) {
        this.clusterIP = clusterIP;
    }

    public int getClusterPort() {
        return clusterPort;
    }

    public void setClusterPort(int clusterPort) {
        this.clusterPort = clusterPort;
    }

    public List<K8sPodBean> getPodList() {
        return podList;
    }

    public void setPodList(List<K8sPodBean> podList) {
        this.podList = podList;
    }
}
