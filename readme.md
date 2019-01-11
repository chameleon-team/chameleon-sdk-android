
# Chameleon
Chameleon/kəˈmiːlɪən/，简写CML，中文名卡梅龙，中文意思变色龙，意味着就像变色龙一样能适应不同环境的跨端整体解决方案。

Chameleon设计理念是提供一套框架，将“多态”设计模式贯穿到工程化开发中，上层95%+代码可重用，在底层（组件、本地接口）分离差异化实现，在编译阶段分离代码，产出多端应用。

android端目前是采用Weex来渲染页面，并且提供了Chameleon设计的一套JsBundleMgr及富文本组件。目前主要有以下内容：
```
 |
 |-adapter      对外暴露adapter及其默认的实现
 |-annotation   CmlJsMethod及CmlComponentProp注解
 |-component    收敛weex component能力与Chameleon
 |-container    Chameleon中activity及view的实现
 |-module       收敛weex module于Chameleon
 |-utils        工具类包 目前只有log的实现
 |-widget       自定义控件 目前仅有activity里的标题栏控件
 |-CmlBaseLifecycle     可视页面生命周期
 |-CmlConstant      常量的管理
 |-CmlEngine        Chameleon入口类的管理
 |-CmlEnvironment   Chameleon环境的设置，如DEBUG、是否直接降级等等
 |-CmlInitAdapter   Chameleon初始化adaper，包含预加载列表、设置缓存大小等
 |-CmlInstance      Chameleon基于Weex渲染的具体实现
```

----

## adapter
加载图片，WebSocket，图片加载，跳转等功能对外提供接口，开发者可以根据自己的需要实现，通过`CmlInitAdapter.Buidler`去注册。

| 接口 | 功能 | 默认实现 |
| :------ | :------ | :------ |
| ICmlDegradeAdapter | 降级 | 没有默认实现 |
| ICmlImgLoaderAdapter | 图片加载 | CmlDefaultImgLoaderAdapter ，默认使用 Glide，需要用户手动集成 Glide |
|ICmlLoggerAdapter|日志|CmlDefaultLoggerAdapter，默认使用系统 log 输出|
|ICmlNavigatorAdapter|url 跳转|默认使用 Intent.ACTION_VIEW 处理|
|ICmlStatisticsAdapter|统计信息输出|没有默认实现，不关心可以不用实现|
|ICmlWebSocketAdapter|WebSocket|CmlDefaultWebSocketAdapter，默认使用 OkHttp3，需要用户手动集成 OkHttp3|


## component
- component扩展类必须继承CmlComponent.
- component对应的设置属性的方法必须添加注解@CmlComponentProp(name=value)
- 由于目前Chameleon是对weex的包装，所以component对应的属性方法必须是public
- component 扩展的方法可以使用 int, double, float, String, Map, List 类型的参数
- 完成component后一定要传递给CmlInitAdapter.registerComponent(Map<String, Class<? extends CmlComponent>> registerComponents)方法

## module
- module扩展必须继承CmlModule类。
- 扩展方法必须加上@CmlJSMethod(uiThread=false or true)注解。Weex会根据注解来判断当前方法是否要运行在UI线程，和当前方法是否是扩展方法。
- 由于目前Chameleon是对weex的包装，所以module对应的属性方法必须是public
- module 扩展的方法可以使用 int, double, float, String, Map, List 类型的参数
- 完成module后一定要传递给CmlInitAdapter.registerModule(String key, Class<? extends CmlModule> registerModule)方法

## CmlEngine
Chameleon入口类，主要有Weex容器的初始化，接入CmlJsBundleMgr来对JsBundle的管理（JsBundle的预加载、Js代码的获取、预加载及运行时的缓存大小设置），向Weex注册module及component。以上操作均在子线程执行。
## CmlInitAdapter
Chameleon初始化适配类
## CmlInstance
Weex对Js的渲染，渲染失败则走降级流程
## 使用方式
### compile
添加依赖。
```gradle
compile 'com.didi.chameleon:chameleon:latest.version'
```
在应用启动的时候进行初始化。
```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CmlEnvironment.DEBUG = true;//设置是否是 DEBUG

        CmlInitAdapter cmlInitAdapter = new CmlInitAdapter.Builder()
                .setDegradeAdapter(new XxxDegradAdapter())
                .addPreloadList(preloadList)
                .registerModule("xxx", XxxCmlModule.class)
                .build();

        CmlEngine.getInstance().init(this, cmlInitAdapter);
    }
}
```
### 使用CmlView
```java
    private CmlView cmlView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cml_view);
        FrameLayout flRoot = findViewById(R.id.fl_root);
        cmlView = new CmlView(this);
        flRoot.addView(cmlView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        cmlView.onCreate();
        cmlView.render(MainActivity.TEST_URL, null);
        cmlView.nativeToJs("1111", "2222");//回传数据给js
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cmlView != null) {
            cmlView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cmlView != null) {
            cmlView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cmlView != null) {
            cmlView.onDestroy();
        }
    }
```
### 使用CmlActivity
```java
CmlEngine.getInstance().launchWeexPage(this, url, null);
//or
new CmlActivity.Launch(activity, url).addOptions(null).launch();
```

----
# JsBundleMgr
JsBundleMgr是一个对js进行下载、缓存的一个模块，根据协议来实现js增量更新功能。主要有以下内容
```
 |
 |——cache    基于DiskLrucache来实现缓存功能
 |——code     js代码的获取及管理
 |——net      采用httpUrlConnect实现下载功能
 |——utils    工具包
 |——CmlJsBundleConstant  常量的管理
 |——CmlJsBundleEngine    实现了CmlJsBundleManager接口，入口类
 |——CmlJsBundleEnvironment   当前环境的设置，如debug环境等
 |——CmlJsBundleManager   实现此接口可自己定义JsBundle的管理
 |——CmlJsBundleMgrConfig    配置类，设置预加载js路径、缓存大小等，默认预加载及运行时缓存大小是4M，可自行设置
```

----
## code
对js代码进行预加载、获取、缓存的管理。在该包里，我们将拿到的url根据协议来拆分成多个url1、url2等，然后在根据url1、url2等来获取对应的js代码，首先从本地缓存里获取去寻找对应的js代码，如果不存在则从网络去下载并保存在本地
## utils
一些文件管理、拆分url、网络判断的工具类
- CmlCodeUtils：获取到的url、code的拆解及合并
- CmlFileUtils：sd卡及缓存目录的判断
- CmlLogUtils：Log的实现
- CmlNetworkUtils：当前网络状态的判断，如Wi-Fi、4g等
- CmlUtils：Md5的生成、主线程判断等等

## CmlJsBundleConstant
缓存文件名、预加载优先级的管理，预加载优先级有以下三种类型
- 普通（PRIORITY_COMMON）：非Wi-Fi情况不预加载
- 强预加载（PRIORITY_FORCE）：无论什么网络情况都预加载
- 强预加载+预解析（PRIORITY_FORCE_MAX）：目前未用到

## CmlJsBundleEngine
实现了CmlJsBundleManager接口，主要有以下三个方法
- initConfig(Context,CmlJsBundleMgrConfig)：初始化config，主要是设置预加载url、预加载缓存、运行时缓存的设置，预加载及运行时缓存默认为4M
- startPreload()：开始预加载，目前预加载成功或者失败并没有任何信息返回，只能查看log进行分析
- getWXTemplate(String,CmlGetCodeStringCallback)：获取js代码

## CmlJsBundleManager
实现此接口可以自己定义JsBundleMgr的实现

----
## 添加依赖
```gradle
compile 'com.didi.chameleon:js-bundle-mgr:latest.version'
```
## 预加载
```java
        CmlJsBundleEnvironment.DEBUG = true;
        List<CmlModel> cmlModels = new ArrayList<>();
        CmlModel model = new CmlModel();
        model.bundle = CmlUtils.parseWeexUrl(url1);
        model.priority = 2;
        cmlModels.add(model);
        model = new CmlModel();
        model.priority = 2;
        model.bundle = CmlUtils.parseWeexUrl(url2);
        cmlModels.add(model);
        CmlJsBundleMgrConfig config = new CmlJsBundleMgrConfig.Builder().setPreloadList(cmlModels).build();
        CmlJsBundleEngine.getInstance().initConfig(this, config);
        CmlJsBundleEngine.getInstance().startPreload();
```
## 获取Js代码
```java
        CmlJsBundleEngine.getInstance().initConfig(this, new CmlJsBundleMgrConfig.Builder().build());
        String url = CmlUtils.parseWeexUrl(url);
        CmlJsBundleEngine.getInstance().getWXTemplate(url, new CmlGetCodeStringCallback() {
            @Override
            public void onSuccess(String codes) {
                Log.i(TAG, "onSuccess: " + codes);
            }

            @Override
            public void onFailed(String errMsg) {
                Log.i(TAG, "onFailed: " + errMsg);
            }
        });
```
----
# 富文本组件
富文本是Chameleon里唯一一个默认注册的组件，主要有以下内容
```
 |
 |-richinfo     主要是富文本需要定义的协议、点击事件的回调等
 |-utils    工具类，主要是加载assets下默认的字体
 |-CmlRichTextComponent     继承与与CmlComponent的富文本组件
 |-CmlRichTextEngine     富文本入口类，
```
----
## richinfo
- CmlClickSpanListener：富文本点击事件回调
- CmlCustomTypefaceSpan：加载自定义字体
- CmlRichInfo：富文本协议及实现
- CmlRichInfoSpan：配合CmlRichInfo显示富文本文字，可直接设置给TextView

## utils
- CmlFontUtil：主要就是加载自定义字体，如assets下fonts包下的Barlow-Medium.ttf字体


----
# 发布 AAR
项目中 `library` 依赖于 `js-bundle-mgr`、`rich-text-component`、`common`，而`js-bundle-mgr`和`rich-text-component` 又依赖于 `common`，所以要从 `common` 开始发布，才能保证编译不报错。

发布流程如下：
1. 修改根目录下 `gradle.propeties` 文件中的 `PUBLISH` 的值为`true`，`VERSION` 的值为需要发布的版本
2. 发布 `common`，命令：`./gradlew :common:uploadArchives`
3. 发布 `js-bundle-mgr`，命令：`./gradlew :js-bundle-mgr:uploadArchives`
4. 发布 `rich-text-component`，命令：`./gradlew :rich-text-component:uploadArchives`
5. 发布 `library`，命令：`./gradlew :library:uploadArchives`