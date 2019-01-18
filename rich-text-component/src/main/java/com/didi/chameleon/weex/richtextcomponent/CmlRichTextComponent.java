package com.didi.chameleon.weex.richtextcomponent;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.weex.richtextcomponent.richinfo.CmlRichInfo;
import com.didi.chameleon.weex.richtextcomponent.richinfo.CmlRichInfoBinder;
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

/**
 * Chameleon 富文本控件
 * Created by youzicong on 2018/10/9
 */
public class CmlRichTextComponent extends WXComponent<TextView> {
    private static final String TAG = "CmlRichTextComponent";

    public CmlRichTextComponent(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    public CmlRichTextComponent(WXSDKInstance instance, WXVContainer parent, int type, BasicComponentData basicComponentData) {
        super(instance, parent, type, basicComponentData);
    }

    @Override
    protected TextView initComponentHostView(@NonNull Context context) {
        TextView textView = new TextView(context);
        textView.setTextSize(16);
        return textView;
    }

    public void updateSize(float width, float height) {
        GraphicPosition lp = getLayoutPosition();
        updateDemission(lp.getTop(), lp.getTop() + height, lp.getLeft(), lp.getLeft() + width, height, width);
        Map<String, Object> styles = new HashMap<>(4);
        styles.put(com.taobao.weex.common.Constants.Name.TOP, lp.getTop());
        styles.put(com.taobao.weex.common.Constants.Name.BOTTOM, lp.getBottom());
        styles.put(com.taobao.weex.common.Constants.Name.LEFT, lp.getLeft());
        styles.put(com.taobao.weex.common.Constants.Name.RIGHT, lp.getRight());
        updateStyle(styles);
    }

    //按照WXComponent 中的updateStyleByPesudo的写法，可以实现自适应宽高
    private void updateStyle(Map<String, Object> styles) {
        new GraphicActionUpdateStyle(getInstance(), getRef(), styles, getPadding(), getMargin(), getBorder(), false)
                .executeActionOnRender();
        if (getLayoutWidth() == 0 && getLayoutWidth() == 0) {
        } else {
            WXBridgeManager.getInstance().setStyleWidth(getInstanceId(), getRef(), getLayoutWidth());
            WXBridgeManager.getInstance().setStyleHeight(getInstanceId(), getRef(), getLayoutHeight());
        }
    }

    @WXComponentProp(name = "richMessage")
    public void setInfo(String infoJson) {
        if (TextUtils.isEmpty(infoJson)) {
            CmlLogUtil.e(TAG, "CmlRichTextComponent setInfo data is null !");
            return;
        }
        TextView textView = getHostView();
        CmlRichInfo data = CmlEnvironment.getJsonWrapper().fromJson(infoJson, CmlRichInfo.class);
        if (data != null) {
            new CmlRichInfoBinder(data)
                    .bindView(textView, new CmlRichInfoBinder.CmlRichInfoAction() {
                        @Override
                        public void onClick(View widget, String message) {
                            Map<String, Object> param = new HashMap<>();
                            param.put("msg", message);
                            param.put("index", -1);
                            fireEvent(Constants.Event.CLICK, param);
                        }

                        @Override
                        public void onSpanClick(View widget, String tag, int index) {
                            Map<String, Object> param = new HashMap<>();
                            param.put("msg", tag);
                            param.put("index", index);
                            fireEvent(Constants.Event.CLICK, param);
                        }
                    });
        }

        textView.setHighlightColor(Color.TRANSPARENT);

        //根据内容自适应
        textView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        updateSize(textView.getMeasuredWidth(), textView.getMeasuredHeight());
    }

}