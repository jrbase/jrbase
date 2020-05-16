package io.github.jrbase.backend;

import java.util.concurrent.CompletableFuture;

public interface BackendProxy {
    byte[] bGet(String buildUpKey);

    CompletableFuture<byte[]> get(String buildUpKey);

    void bPut(String buildUpKey, byte[] writeUtf8);

    byte[] bGetAndPut(String buildUpKey, byte[] writeUtf8);

    CompletableFuture<byte[]> getAndPut(String buildUpKey, byte[] writeUtf8);
}
