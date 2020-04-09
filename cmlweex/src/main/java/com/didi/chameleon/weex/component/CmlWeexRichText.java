package com.didi.chameleon.weex.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.weex.richtextcomponent.CmlRichTextComponent;
import com.didi.chameleon.weex.util.CmlWeexUtil;

import org.apache.weex.WXSDKInstance;
import org.apache.weex.common.Constants;
import org.apache.weex.ui.action.BasicComponentData;
import org.apache.weex.ui.component.WXComponent;
import org.apache.weex.ui.component.WXComponentProp;
import org.apache.weex.ui.component.WXVContainer;

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
        CmlWeexUtil.updateSize(this, width, height);
    }

    @Override
    public void onClick(Map<String, Object> info) {
        fireEvent(Constants.Event.CLICK, info);
    }

    @Override
    public int getMaxHeight() {
        return getParent() != null ? getParent().getViewPortWidth() : 0;
    }

}
