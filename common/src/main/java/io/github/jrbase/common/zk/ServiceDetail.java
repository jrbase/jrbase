package io.github.jrbase.common.zk;

public class ServiceDetail {
    public static final String REGISTER_ROOT_PATH = "jrbase";

    private String appName;

    public ServiceDetail() {

    }

    public ServiceDetail(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }



    @Override
    public String toString() {
        return "ServiceDetail{" +
                "appName='" + appName + '\'' +
                '}';
    }
}
