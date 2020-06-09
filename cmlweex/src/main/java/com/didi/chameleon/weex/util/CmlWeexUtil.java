package com.didi.chameleon.weex.util;

import org.apache.weex.bridge.WXBridgeManager;
import org.apache.weex.common.Constants;
import org.apache.weex.ui.action.GraphicActionUpdateStyle;
import org.apache.weex.ui.action.GraphicPosition;
import org.apache.weex.ui.component.WXComponent;

import java.util.HashMap;
import java.util.Map;

public class CmlWeexUtil {

    public static void updateSize(WXComponent component, int width, int height) {
        GraphicPosition lp = component.getLayoutPosition();
        component.updateDemission(lp.getTop(), lp.getTop() + height, lp.getLeft(), lp.getLeft() + width, height, width);
        Map<String, Object> styles = new HashMap<>(4);
        styles.put(Constants.Name.TOP, lp.getTop());
        styles.put(Constants.Name.BOTTOM, lp.getBottom());
        styles.put(Constants.Name.LEFT, lp.getLeft());
        styles.put(Constants.Name.RIGHT, lp.getRight());
        updateStyle(component, styles);
    }

    //按照WXComponent 中的updateStyleByPesudo的写法，可以实现自适应宽高
    private static void updateStyle(WXComponent component, Map<String, Object> styles) {
        new GraphicActionUpdateStyle(component.getInstance(), component.getRef(), styles,
                component.getPadding(), component.getMargin(), component.getBorder(), false)
                .executeActionOnRender();
        if (component.getLayoutWidth() != 0 || component.getLayoutHeight() != 0) {
            WXBridgeManager.getInstance().setStyleWidth(component.getInstanceId(), component.getRef(), component.getLayoutWidth());
            WXBridgeManager.getInstance().setStyleHeight(component.getInstanceId(), component.getRef(), component.getLayoutHeight());
        }
    }

}
