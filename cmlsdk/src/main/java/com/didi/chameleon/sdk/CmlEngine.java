package com.didi.chameleon.sdk;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.didi.chameleon.sdk.bridge.CmlProtocolProcessor;
import com.didi.chameleon.sdk.bridge.ICmlProtocolWrapper;
import com.didi.chameleon.sdk.bundle.CmlBundle;
import com.didi.chameleon.sdk.extend.CmlClipboardModule;
import com.didi.chameleon.sdk.extend.CmlCommonModule;
import com.didi.chameleon.sdk.extend.CmlModalModule;
import com.didi.chameleon.sdk.extend.CmlPositionModule;
import com.didi.chameleon.sdk.extend.CmlStorageModule;
import com.didi.chameleon.sdk.extend.CmlStreamModule;
import com.didi.chameleon.sdk.extend.CmlWebSocketModule;
import com.didi.chameleon.sdk.extend.image.CmlImageModule;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlModuleManager;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h3>SDK初始化入口</h3>
 * <p>
 * 此类是 <em>Chameleon/kəˈmiːlɪən/</em> SDK 的入口类，提供基本的初始化入口和 <em>Chameleon</em>
 * 容器的调起能力。
 * </p>
 * <p>
 * <h3>native 引擎选择</h3>
 * <p>
 * 初始化入口会根据使用的native渲染引擎，反射获取对应的引擎入口类做实际的初始化工作。native渲染引擎目前支持
 * <em>weex</em> 和 <em>react native</em> 两种。项目中具体选择使用哪一个渲染引擎，只需要在 <em>build.gradle</em>
 * 里引用不同的aar即可，<b>注意，weex 和 react native 两者不可同时引用</b>。
 * <table border="1" width="85%" align="center" cellpadding="3">
 * <thead>
 * <tr><th>native engine</th> <th>compile example</th></tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>weex 方案</td>
 * <td>
 * compile "com.didiglobal.chameleon:cmlsdk:$VERSION" <br />
 * compile "com.didiglobal.chameleon:cmlweex:$VERSION" <br />
 * compile "com.didiglobal.chameleon:cmlweb:$VERSION"
 * </td>
 * </tr>
 * <tr>
 * <td>react native 方案</td>
 * <td>
 * compile "com.didiglobal.chameleon:cmlsdk:$VERSION" <br />
 * compile "com.didiglobal.chameleon:cmlrn:$VERSION" <br />
 * compile "com.didiglobal.chameleon:cmlweb:$VERSION"
 * </td>
 * </tr>
 * </tbody>
 * </table>
 * 初始化时会先查找是否存在 <em>com.didi.chameleon.weex.CmlWeexEngine</em> 类，找不到则查找
 * <em>com.didi.chameleon.rn.CmlRnEngine</em> 类是否存在。找到实际的引擎入口后，则反射并调用其初始化方法。
 * </p>
 * <p>
 * <h3>容器调起入口</h3>
 * <p>
 * 容器调起能力入口 {@link #launchPage(Activity, String, HashMap)} 会根据用户传入的 url 调起相应的容器。
 * 则会调起native 渲染容器渲染界面。如果渲染过程中出现问题则自动调起Web 渲染容器渲染降级页面。
 * </p>
 * <p>
 * <p>
 * {@link #launchPage(Activity, String, HashMap, int, ICmlLaunchCallback)} 则是需要和调起的子页面通讯时使用的入口，被调起的子页面可以和
 * 主页面之间进行数据通信，具体可参照demo。
 * </p>
 */
public class CmlEngine {
    public static final String TAG = "CmlEngine";

    private static volatile CmlEngine instance;

    private static AtomicInteger sInstanceId = new AtomicInteger(101); // 跳过100个，防止和weex重复

    private Context mContext;
    private ICmlEngine mCmlEngine;
    private ICmlEngine mCmlWebEngine;

    private CmlEngine() {
    }

    public static CmlEngine getInstance() {
        if (instance == null) {
            synchronized (CmlEngine.class) {
                if (instance == null) {
                    instance = new CmlEngine();
                }
            }
        }
        return instance;
    }

    public String generateInstanceId() {
        return String.valueOf(sInstanceId.incrementAndGet());
    }

    /**
     * <h3>Engine 初始化</h3>
     * 反射拿到native 渲染引擎 和 Web 渲染引擎入口类，调用其初始化方法。<br />
     * Native Engine 渲染失败后会自动使用 Web Engine 进行降级。
     *
     * @param context Application 对象
     */
    public void init(Context context, ICmlConfig config) {
        this.mContext = context;
        config.configAdapter();
        config.registerModule();

        initEngine(context);

        registerModule(CmlCommonModule.class);
        registerModule(CmlClipboardModule.class);
        registerModule(CmlStorageModule.class);
        registerModule(CmlWebSocketModule.class);
        registerModule(CmlStreamModule.class);
        registerModule(CmlModalModule.class);
        registerModule(CmlPositionModule.class);
        registerModule(CmlImageModule.class);
    }

    private void initEngine(Context context) {
        Class cmlEngine = null;
        try {
            // engine 获取并初始化
            cmlEngine = Class.forName("com.didi.chameleon.weex.CmlWeexEngine");
        } catch (ClassNotFoundException e1) {
            try {
                cmlEngine = Class.forName("com.didi.chameleon.cmlthanos.CmlThanosEngine");
            } catch (ClassNotFoundException e2) {
                String error = "CmlEngine init error, engine class not found !!!";
                CmlLogUtil.e(TAG, error);
            }
        }

        if (null != cmlEngine) {
            try {
                Object objEngine = cmlEngine.newInstance();
                if (objEngine instanceof ICmlEngine) {
                    mCmlEngine = (ICmlEngine) objEngine;
                    mCmlEngine.init(context);
                } else {
                    String error = "CmlEngine init error, engine class not found !!!";
                    CmlLogUtil.e(TAG, error);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        try {
            // web engine 获取并初始化
            Class cmlWebEngine = Class.forName("com.didi.chameleon.web.CmlWebEngine");
            if (null != cmlWebEngine) {
                Object objWebEngine = cmlWebEngine.newInstance();
                if (objWebEngine instanceof ICmlEngine) {
                    mCmlWebEngine = (ICmlEngine) objWebEngine;
                    mCmlWebEngine.init(context);
                }
            }
        } catch (ClassNotFoundException e) {
            String error = "CmlWebEngine init error, engine class not found !!!";
            CmlLogUtil.e(TAG, error);
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Context getAppContext() {
        return mContext;
    }

    public ICmlEngine getCmlEngine() {
        return mCmlEngine;
    }

    public ICmlEngine getCmlWebEngine() {
        return mCmlWebEngine;
    }

    /**
     * 容器调起入口。
     *
     * @param activity 需传入activity对象
     * @param url      Chameleon url，也可以只传入一个标准的http/https地址
     * @param options  调起参数，可以传 null
     */
    public void launchPage(@NonNull Activity activity, String url, HashMap<String, Object> options) {
        if (TextUtils.isEmpty(url)) {
            CmlLogUtil.e(TAG, "CmlEngine launchPage, url is empty.");
            return;
        }

        String cmlUrl = Util.parseCmlUrl(url);
        if (!TextUtils.isEmpty(cmlUrl)) {
            if (null == mCmlEngine) {
                CmlLogUtil.e(TAG, "CmlEngine launchPage, engine class not found !!!");
                return;
            }
            mCmlEngine.launchPage(activity, url, options);
        } else if (Util.isWebPageUrl(url)) {
            if (null == mCmlWebEngine) {
                CmlLogUtil.e(TAG, "CmlWebEngine launchPage, web engine class not found !!!");
                return;
            }
            mCmlWebEngine.launchPage(activity, url, options);
        } else {
            CmlLogUtil.e(TAG, "check url, render local jsbundle make sure `CmlEnvironment.CML_DEGRADE` is been true");
        }
    }

    public void launchPage(@NonNull Activity activity, String url, HashMap<String, Object> options,
                           int requestCode, ICmlLaunchCallback launchCallback) {
        if (TextUtils.isEmpty(url)) {
            CmlLogUtil.e(TAG, "CmlEngine launchPage, url is empty.");
            return;
        }

        String cmlUrl = Util.parseCmlUrl(url);
        if (!TextUtils.isEmpty(cmlUrl)) {
            if (null == mCmlEngine) {
                CmlLogUtil.e(TAG, "CmlEngine launchPage, engine class not found !!!");
                return;
            }
            mCmlEngine.launchPage(activity, url, options, requestCode, launchCallback);
        } else if (Util.isWebPageUrl(url)) {
            if (null == mCmlWebEngine) {
                CmlLogUtil.e(TAG, "CmlWebEngine launchPage, web engine class not found !!!");
                return;
            }
            mCmlWebEngine.launchPage(activity, url, options, requestCode, launchCallback);
        }
    }

    /**
     * 初始化预加载 Js Bundle 列表
     *
     * @param preloadList 预加载列表，{@link CmlBundle}
     */
    public void initPreloadList(List<CmlBundle> preloadList) {
        if (null == mCmlEngine) {
            CmlLogUtil.e(TAG, "CmlEngine performPreload, engine class not found !!!");
            return;
        }
        mCmlEngine.initPreloadList(preloadList);
    }

    /**
     * 执行预加载, 需要先设置
     */
    public void performPreload() {
        if (null == mCmlEngine) {
            CmlLogUtil.e(TAG, "CmlEngine performPreload, engine class not found !!!");
            return;
        }
        mCmlEngine.performPreload();
    }

    /**
     * <h3>module 注册</h3>
     * 扩展自己的 module 能力时，需要在初始时通过此方法注册自己的 module。
     *
     * @param moduleClass module 类型
     * @param <T>         返回类型
     */
    public <T> void registerModule(Class<T> moduleClass) {
        CmlModuleManager.getInstance().addCmlModule(moduleClass);
    }

    public void addProtocolWrapper(ICmlProtocolWrapper wrapper) {
        CmlProtocolProcessor.addCmlProtocolWrapper(wrapper);
    }

    public <T> void callToJs(ICmlInstance instance, String module, String method, Object param, CmlCallback<T> callback) {
        CmlModuleManager.getInstance().invokeWeb(instance.getInstanceId(), module, method, param, callback);
    }

}
