package com.didi.chameleon.weex.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.weex.richtextcomponent.CmlRichTextComponent;

import org.apache.weex.WXSDKInstance;
import org.apache.weex.ui.action.BasicComponentData;
import org.apache.weex.ui.component.WXComponent;
import org.apache.weex.ui.component.WXComponentProp;
import org.apache.weex.ui.component.WXVContainer;

import java.util.HashMap;
import java.util.Map;

public class CmlWeexRichText extends WXComponent<CmlRichTextComponent>
        implements CmlRichTextComponent.Action {

    private static final String TAG = "CmlWeexRichText";

    public CmlWeexRichText(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    public CmlWeexRichText(WXSDKInstance instance, WXVContainer parent, int type, BasicComponentData basicComponentData) {
        super(instance, parent, type, basicComponentData);
    }

    @Override
    protected CmlRichTextComponent initComponentHostView(@NonNull Context context) {
        return new CmlRichTextComponent(context, this);
    }

    @WXComponentProp(name = CmlRichTextComponent.PROP)
    public void setInfo(final String infoJson) {
        if (TextUtils.isEmpty(infoJson)) {
            CmlLogUtil.e(TAG, "CmlRichTextComponent setInfo data is null !");
            return;
        }
        CmlRichTextComponent textView = getHostView();
        textView.setInfo(infoJson);
    }

    @Override
    public void updateSize(int width, int height) {
        Map<String, Object> info = new HashMap<>();
        info.put("width", width);
        info.put("height", height);
        fireEvent(CmlRichTextComponent.LAYOUT, info);
    }

    @Override
    public void onClick(Map<String, Object> info) {
        String link = info != null && info.containsKey("url") ? String.valueOf(info.get("url")) : "";
        if (!TextUtils.isEmpty(link)) {
            CmlEnvironment.getNavigatorAdapter().navigator(getContext(), link);
        } else {
            fireEvent(CmlRichTextComponent.CLICK, info);
        }
    }

}
