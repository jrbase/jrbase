package org.github.jrbase.backend;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.util.ByteArray;

import java.util.List;
import java.util.Map;

public class JraftKVDecorator implements BackendProxy {

    private RheaKVStore delegate;

    public JraftKVDecorator(RheaKVStore delegate) {
        this.delegate = delegate;
    }

    @Override
    public byte[] bGet(String key) {
        return delegate.bGet(key);
    }

    @Override
    public void bPut(String key, byte[] value) {
        delegate.bPut(key, value);
    }

    @Override
    public Map<ByteArray, byte[]> bMultiGet(List<byte[]> keys) {
        return delegate.bMultiGet(keys);
    }

    @Override
    public byte[] bGetAndPut(String key, byte[] value) {
        // TODO: add async method
        return delegate.bGetAndPut(key, value);
    }
}