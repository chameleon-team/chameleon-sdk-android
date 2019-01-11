package com.didi.chameleon.sdk.bundle;

import android.support.annotation.NonNull;

/**
 * js bundle model
 * 实现优先级比较
 */
public class CmlBundle implements Comparable<CmlBundle> {
    /**
     * 预加载优先级：普通，非Wi-Fi情况下不预加载
     */
    public static final int PRIORITY_COMMON = 1;

    /**
     * 预加载优先级：强预加载（无论什么网络情况都预加载）å
     */
    public static final int PRIORITY_FORCE = 2;

    /**
     * 预加载优先级：强预加载（无论什么网络情况都预加载）+ 预解析
     * 慎用
     */
    private static final int PRIORITY_FORCE_MAX = 3;

    /**
     * js bundle的地址
     */
    public String bundle = "";

    /**
     * 预留字段
     */
    public String[] chunks;

    /**
     * 优先级，默认为CmlWXConstant.PRIORITY_FORCE
     */
    public int priority = PRIORITY_FORCE;

    @Override
    public int compareTo(@NonNull CmlBundle o) {
        return o.priority - priority;
    }
}
