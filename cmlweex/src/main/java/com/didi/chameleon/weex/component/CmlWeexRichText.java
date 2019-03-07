package com.didi.chameleon.weex.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.weex.richtextcomponent.CmlRichTextComponent;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.action.GraphicActionUpdateStyle;
import com.taobao.weex.ui.action.GraphicPosition;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

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
        GraphicPosition lp = getLayoutPosition();
        updateDemission(lp.getTop(), lp.getTop() + height, lp.getLeft(), lp.getLeft() + width, height, width);
        Map<String, Object> styles = new HashMap<>(4);
        styles.put(com.taobao.weex.common.Constants.Name.TOP, lp.getTop());
        styles.put(com.taobao.weex.common.Constants.Name.BOTTOM, lp.getBottom());
        styles.put(com.taobao.weex.common.Constants.Name.LEFT, lp.getLeft());
        styles.put(com.taobao.weex.common.Constants.Name.RIGHT, lp.getRight());
        updateStyle(styles);
    }

    @Override
    public void onClick(Map<String, Object> info) {
        fireEvent(Constants.Event.CLICK, info);
    }

    //按照WXComponent 中的updateStyleByPesudo的写法，可以实现自适应宽高
    private void updateStyle(Map<String, Object> styles) {
        new GraphicActionUpdateStyle(getInstance(), getRef(), styles, getPadding(), getMargin(), getBorder(), false)
                .executeActionOnRender();
        if (getLayoutWidth() != 0 || getLayoutHeight() != 0) {
            WXBridgeManager.getInstance().setStyleWidth(getInstanceId(), getRef(), getLayoutWidth());
            WXBridgeManager.getInstance().setStyleHeight(getInstanceId(), getRef(), getLayoutHeight());
        }
    }

}
