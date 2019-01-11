package com.didi.chameleon.weex.richtextcomponent;

import com.didi.chameleon.sdk.utils.ICmlClassInit;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

public class CmlRichTextInit implements ICmlClassInit {

    @Override
    public void init() {
        try {
            WXSDKEngine.registerComponent("richtext", CmlRichTextComponent.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

}
