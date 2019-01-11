package com.didi.chameleon.weex.richtextcomponent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.weex.richtextcomponent.richinfo.CmlRichInfo;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

/**
 * Chameleon 富文本控件
 * Created by youzicong on 2018/10/9
 */
public class CmlRichTextComponent extends WXComponent<TextView> {
    private static final String TAG = "CmlRichTextComponent";
    private static OnRouterListener sOnRouterListener;

    /**
     * 富文本控件点击回调
     */
    public static void setOnRouterListener(OnRouterListener onRouterListener) {
        sOnRouterListener = onRouterListener;
    }

    public static OnRouterListener getOnRouterListener() {
        return sOnRouterListener;
    }

    public CmlRichTextComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
        super(instance, dom, parent);
    }

    public CmlRichTextComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, int type) {
        super(instance, dom, parent, type);
    }

    @Override
    protected TextView initComponentHostView(@NonNull Context context) {
        TextView textView = new TextView(context);
        textView.setTextSize(16);
        return textView;
    }

    @WXComponentProp(name = "richMessage")
    public void setInfo(String infoJson) {
        if (TextUtils.isEmpty(infoJson)) {
            CmlLogUtil.e(TAG, "CmlRichTextComponent setInfo data is null !");
            return;
        }
        CmlRichInfo data = CmlEnvironment.getJsonWrapper().fromJson(infoJson, CmlRichInfo.class);
        if (data != null) {
            data.bindView(getHostView());
        }
    }

    public interface OnRouterListener {
        void onRoute(View view, String url);
    }
}