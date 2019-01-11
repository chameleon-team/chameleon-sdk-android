package com.didi.chameleon.sdk.image;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.utils.ICmlClassInit;

public class CmlImageInit implements ICmlClassInit {
    @Override
    public void init() {
        CmlEngine.getInstance().registerModule(CmlImageModule.class);
    }
}
