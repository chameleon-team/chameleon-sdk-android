package com.didi.chameleon.sdk.location;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.utils.ICmlClassInit;

public class CmlPositionInit implements ICmlClassInit {

    @Override
    public void init() {
        CmlEngine.getInstance().registerModule(CmlPositionModule.class);
    }

}
