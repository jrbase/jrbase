package org.github.jrbase.config;

/**
 * Redis configuration file class
 */
public class RedisConfigurationOption {
    private String bind = "0.0.0.0";
    private String protectedMode = "yes";
    private int port = 6379;
    private String requirePass = "";

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
