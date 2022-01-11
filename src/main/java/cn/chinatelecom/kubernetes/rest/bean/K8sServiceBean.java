package cn.chinatelecom.kubernetes.rest.bean;

import java.util.List;

public class K8sServiceBean {
    private String clusterIP;
    private int clusterPort;
    private List<K8sPodBean> podList;

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
