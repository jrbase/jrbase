package io.github.jrbase.common.config;

/**
 * Redis configuration file class
 */
public class RedisConfigurationOption {
    private String bind = "0.0.0.0";
    private String protectedMode = "yes";
    private int port = 6379;
    private String requirePass = "";


    private String zookeeper = "192.168.100.128:2181";
    private String registerAddress = "192.168.100.128";
    private String appName = "JRBaseServer";


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }


    public String getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
    }

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public String getProtectedMode() {
        return protectedMode;
    }

    public void setProtectedMode(String protectedMode) {
        this.protectedMode = protectedMode;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRequirePass() {
        return requirePass;
    }

    public void setRequirePass(String requirePass) {
        this.requirePass = requirePass;
    }
}
