package io.github.jrbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JRServerEmbedded {
    private Process redisProcess;
    private boolean isActive = false;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static List<String> arguments;

    public JRServerEmbedded() {
        initArguments();
    }

    private void initArguments() {
        String classpath = System.getProperty("java.class.path");
        arguments = new ArrayList<>();
        arguments.addAll(Arrays.asList("java", "-cp", classpath, "io.github.jrbase.JRServer"));
    }

    public JRServerEmbedded(String port) {
        initArguments();
        arguments.add(port);
    }

    public JRServerEmbedded(String host, String port) {
        initArguments();
        arguments.add(host);
        arguments.add(port);
    }

    public synchronized void start() throws IOException {
        if (isActive) {
            throw new UnsupportedOperationException("The Server has been started.");
        }
        ProcessBuilder pb = new ProcessBuilder(arguments);
        redisProcess = pb.start();
        logErrors();
        isActive = true;
    }

    private void logErrors() {
        final InputStream errorStream = redisProcess.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));

        executor.submit(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void stop() {
        if (!isActive) {
            throw new UnsupportedOperationException("The Server has been stopped.");
        }
        redisProcess.destroy();
        try {
            redisProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isActive = false;
    }

}
