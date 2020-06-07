package io.github.jrbase.server;

public abstract class IServer {
    public abstract  void start(String[] args);
    public abstract void shutdown();
    protected abstract void startCluster();
    protected abstract void stopCluster();
}
