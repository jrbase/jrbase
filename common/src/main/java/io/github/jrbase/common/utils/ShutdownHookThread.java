package io.github.jrbase.common.utils;

public class ShutdownHookThread extends Thread {

    private Runnable runnable;

    public ShutdownHookThread(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        runnable.run();
        System.out.println("stop");
    }
}
