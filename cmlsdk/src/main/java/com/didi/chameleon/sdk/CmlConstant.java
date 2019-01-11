package com.didi.chameleon.sdk;

public class CmlConstant {
    /**
     * 解析链接中weex地址的key
     */
    public static final String CML_PARAM_KEY = "cml_addr";
    public static final String OLD_PARAM_KEY = "wx_addr";

    /**
     * 给weex传参数时，自定义数据在options map中的key
     */
    public static final String WEEX_OPTIONS_KEY = "query";

    /**
     * global callback 的key
     */
    public static final String GLOBAL_CALLBACK_KEY = "_beatlesCommunicate";

    /**
     * 容器中存放的url的key
     */
    public static final String WEEX_INSTANCE_URL = "url";


    /*************数据上报weex失败类型分类**********/

    /**
     * 失败类型：jsBundle 解析失败
     */
    public static final int FAILED_TYPE_RESOLVE = 1;

    /**
     * 失败类型：JS 代码运行时异常
     */
    public static final int FAILED_TYPE_RUNTIME = 2;

    /**
     * 失败类型：主动降级，包括 {@link CmlEnvironment#CML_DEGRADE} 设置为 true
     */
    public static final int FAILED_TYPE_DEGRADE = 3;

    /**
     * 失败类型：下载jsBundle失败
     */
    public static final int FAILED_TYPE_DOWNLOAD = 4;

    public static final String TAG = "cml_weex_debug";
}
