package com.didi.chameleon.weex.richtextcomponent;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.didi.chameleon.weex.richtextcomponent.richinfo.CmlRichInfo;
import com.didi.chameleon.weex.richtextcomponent.richinfo.CmlRichInfoBinder;

import java.util.HashMap;
import java.util.Map;

/**
 * Chameleon 富文本控件
 * Created by youzicong on 2018/10/9
 */
public class CmlRichTextComponent extends AppCompatTextView {
    public static final String NAME = "richtext";
    public static final String PROP = "richMessage";

    public interface Action {

        void onClick(Map<String, Object> info);

        void updateSize(int width, int height);

    }

    public Action action;

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
                        param.put("msg", message);
                        param.put("index", -1);
                        if (action != null) {
                            action.onClick(param);
                        }
                    }

                    @Override
                    public void onSpanClick(View widget, String tag, int index) {
                        Map<String, Object> param = new HashMap<>();
                        param.put("msg", tag);
                        param.put("index", index);
                        if (action != null) {
                            action.onClick(param);
                        }
                    }
                });

        setHighlightColor(Color.TRANSPARENT);

        //根据内容自适应
        measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        if (action != null) {
            action.updateSize(getMeasuredWidth(), getMeasuredHeight());
        }
    }

}