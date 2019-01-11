package com.didi.chameleon.sdk.adapter.storage;

public interface CmlStorageAdapter {

    void save(String key, String data);

    String get(String key);

    void remove(String key);
}
