package com.didi.chameleon.weex.jsbundlemgr.code;

import java.io.File;
import java.io.InputStream;

/**
 * houzedong
 * create at 2018/10/9
 */
public interface CmlDiskCache {
    /**
     * 获取本地缓存
     * @return 本地缓存文件
     */
    File get(String url);

    /**
     * 保存到磁盘缓存，从一个输入流读入
     */
    boolean save(String url, InputStream inputStream);
}
