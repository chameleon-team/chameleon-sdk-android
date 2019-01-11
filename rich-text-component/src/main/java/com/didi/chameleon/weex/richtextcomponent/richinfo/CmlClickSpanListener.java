package com.didi.chameleon.weex.richtextcomponent.richinfo;

import android.view.View;

/**
 *  监听ClickSpan onClick动作，用来处理和{@link View#performClick()}点击事件冲突
 *
 * @author houzedong
 * @since 18/8/22 11:03
 */
interface CmlClickSpanListener {

    void spanClicked(View widget);
}
