package com.didi.chameleon.sdk.adapter;

import java.util.Map;

/**
 * 统计信息实现接口
 * Created by youzicong on 2018/10/16
 */
public interface ICmlStatisticsAdapter {

    /**
     * 统计事件
     *
     * @param key 事件 key
     * @param map 事件 参数
     */
    void event(String key, Map<String, Object> map);
}
