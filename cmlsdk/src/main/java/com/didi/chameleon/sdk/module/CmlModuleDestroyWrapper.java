package com.didi.chameleon.sdk.module;

import com.didi.chameleon.sdk.CmlInstanceManage;

public class CmlModuleDestroyWrapper implements CmlInstanceManage.CmlInstanceDestroyListener {

    static void register(String instanceId, ICmlModuleDestroy destroy) {
        CmlInstanceManage.getInstance().registerDestroyListener(instanceId, new CmlModuleDestroyWrapper(destroy));
    }

    private ICmlModuleDestroy destroy;

    private CmlModuleDestroyWrapper(ICmlModuleDestroy destroy) {
        this.destroy = destroy;
    }

    @Override
    public void onDestroy() {
        destroy.onDestroy();
    }
}
