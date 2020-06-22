package eeui.android.audio.entry;

import android.content.Context;

import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import app.eeui.framework.extend.annotation.ModuleEntry;
import eeui.android.audio.module.WeexaudioModule;

@ModuleEntry
public class audioEntry {

    /**
     * APP启动会运行此函数方法
     * @param content Application
     */
    public void init(Context content) {

        try {
            WXSDKEngine.registerModule("eeuiAudio", WeexaudioModule.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

}
