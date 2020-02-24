package org.github.jrbase.backend;

import com.alipay.sofa.jraft.rhea.util.ByteArray;

import java.util.List;
import java.util.Map;

public interface BackendProxy {
    byte[] bGet(String buildUpKey);

    void bPut(String buildUpKey, byte[] writeUtf8);

    Map<ByteArray, byte[]> bMultiGet(List<byte[]> keyList);

    byte[] bGetAndPut(String buildUpKey, byte[] writeUtf8);
}
