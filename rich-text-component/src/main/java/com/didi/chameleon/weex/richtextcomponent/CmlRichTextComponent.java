package com.didi.chameleon.weex.richtextcomponent;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.didi.chameleon.sdk.utils.CmlViewUtil;
import com.didi.chameleon.weex.richtextcomponent.richinfo.CmlRichInfo;
import com.didi.chameleon.weex.richtextcomponent.richinfo.CmlRichInfoBinder;

import java.util.HashMap;
import java.util.Map;

/**
 * Chameleon 富文本控件
 * Created by youzicong on 2018/10/9
 */
public class CmlRichTextComponent extends AppCompatTextView {
    public static final String CLICK = "richTextClick";
    public static final String LAYOUT = "layoutRichText";
    public static final String NAME = "richtext";
    public static final String PROP = "richMessage";

    public interface Action {

        void onClick(Map<String, Object> info);

        /**
         * 更新布局大小
         */
        void updateSize(int width, int height);

    }

    public Action action;

    private int lastWidth;

    public CmlRichTextComponent(Context context, Action action) {
        super(context);
        this.action = action;
        setTextSize(16);
    }

    public CmlRichTextComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextSize(16);
    }

    public void setInfo(String info) {
        setInfo(JSON.parseObject(info, CmlRichInfo.class));
    }

    public void setInfo(CmlRichInfo data) {
        if (data == null) {
            return;
        }
        new CmlRichInfoBinder(data)
                .bindView(this, new CmlRichInfoBinder.CmlRichInfoAction() {
                    @Override
                    public void onClick(View widget, String message) {
                        Map<String, Object> param = new HashMap<>();
                        param.put("index", -1);
                        if (action != null) {
                            action.onClick(param);
                        }
                    }

                    @Override
                    public void onSpanClick(View widget, String tag, int index, CmlRichInfo.Bean bean) {
                        Map<String, Object> param = new HashMap<>();
                        param.put("index", index);
                        if (bean != null && !bean.click && !TextUtils.isEmpty(bean.url)) {
                            param.put("url", bean.url);
                        }
                        if (action != null) {
                            action.onClick(param);
                        }
                    }
                });

        setHighlightColor(Color.TRANSPARENT);

        post(new Runnable() {
            @Override
            public void run() {
                updateSize(getWidth());
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldw != w) {
            updateSize(w);
        }
    }

    private void updateSize(int newWidth) {
        if (newWidth == 0 || newWidth == lastWidth) {
            return;
        }
        lastWidth = newWidth;
        //宽度由FE确定，高度根据宽度自适应
        measure(View.MeasureSpec.makeMeasureSpec(newWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        if (action != null) {
            int width = CmlViewUtil.px2dp(getContext(), newWidth);
            int height = CmlViewUtil.px2dp(getContext(), getMeasuredHeight());
            action.updateSize(width, height);
        }
    }
}