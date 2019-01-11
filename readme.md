<h1>Android SDK 简介</h1>
android 端的实现思路是采用目前比较流行的一些 native 渲染引擎作为底层支持，同时扩展一些一般工程通用的基础能力。目前支持的渲染引擎是 `weex` 和 `react native`，使用时<font color=#FF0000>二者选其一</font>作为项目的 native 渲染引擎。

# 1. 项目结构
项目一级目录结构如下：
```
|+ app sdk测试模块
|+ CmlSDKExample SDK使用示例
|+ cmlsdk SDK接入层，抽象 Chameleon 引擎能力、实现通用扩展能力
|+ cmlweex 包装 weex 渲染引擎
|+ cmlweb 包装 web 渲染引擎
|+ cmlrn 包装 react native 渲染引擎
|+ js-bundle-mgr 实现 js bundle 预加载、缓存
|+ rich-text-component 富文本组件
|+ sdk-image 图片选择、图片拍摄组件
|+ sdk-location 位置组件
```

cmlsdk 模块单独拿出来看下目录结构：
```
|- cmlsdk
    |+ adapter 定义了扩展能力的接口以及默认实现，无默认实现的能力需要第三方项目根据自己的实际业务需求去实现
    |+ bridge 定义了 js 和 native 通信的接口，实现协议相关的处理能力，以及实现了协议层使用入口
    |+ bundle js bundle 相关定义，目前只有一个类用来描述 js bundle 相关信息
    |+ common 通用能力的基础封装类
    |+ container 渲染容器的抽象能力定义
    |+ extend Chameleon 提供的一些能力
    |+ module 扩展能力管理，收集 sdk 默认提供的以及第三方用户自己实现的 module，根据 bridge 层指令执行具体某个 module 的某个 method
    |+ utils 工具类集合
    |+ widget 自定义的widget，目前只有一个 title bar，用做 webview 渲染容器的action bar
    |- CmlBaseLifecycle 生命周期的接口定义
    |- CmlConstant 常量定义
    |- CmlEngine Chameleon SDK 使用入口
    |- CmlEnvironment 运行环境和运行参数配置入口、扩展能力设置入口
    |- CmlInstanceManage 页面运行实例的管理类，每一个容器实例运行时，其对应的Instance会注册到这里
    |- ICmlEngine 引擎的抽象接口
    |- ICmlInstance 容器实例抽象接口
    |- ICmlActivityInstance 全屏容器实例抽象接口
    |- ICmlViewInstance 视图容器实例抽象接口
```

项目整体架构如下图所示：

![image](../assets/cml_doc_android_01.png)


# 2. Chameleon 使用
此部分可以参看手把手系列之<a href="../example/android_example.html">《变色龙SDK使用范例》</a>

# 3. 基础类说明

## 3.1 CmlEngine
此类是 <em>Chameleon/kəˈmiːlɪən/</em> SDK 的入口类，提供基本的初始化入口和 <em>Chameleon</em>容器的调起能力。具体包含以下能力
- SDK 初始化入口
- 调起渲染容器
- 初始化预加载列表
- 注册扩展module

## 3.2 CmlEnvironment
CmlEnvironment 主要提供了开发期间需要的一些能力，如
- 调试开关
- 降级开关
- 缓存开关

以及一些常量的定义，如
- 预加载的最大缓存
- 运行时的最大缓存

CmlEnvironment 一个非常重要的能力是提供了对 adapter 设置和获取能力，方便使用者实现自己的适配模块。如加载图片，WebSocket，图片加载，跳转等功能。开发者可以根据自己的需要，实现对应的接口并注册到SDK中使用。

| 接口 | 功能 | 默认实现 |
| :------ | :------ | :------ |
| ICmlDegradeAdapter | 降级 | 不提供默认实现，示例 CmlDegradeDefault 默认会关闭 native 渲染容器，打开 Web 容器，加载降级url |
| ICmlImgLoaderAdapter | 图片加载 | CmlDefaultImgLoaderAdapter ，默认使用 Glide，需要用户手动集成 Glide |
| CmlLoggerAdapter|日志|CmlLoggerDefault，默认使用系统 log 输出|
| ICmlNavigatorAdapter|url 跳转|默认使用 Intent.ACTION_VIEW 处理|
| ICmlStatisticsAdapter|统计信息输出|没有默认实现，不关心可以不用实现|
| ICmlWebSocketAdapter|WebSocket|CmlDefaultWebSocketAdapter，默认使用 OkHttp3，需要用户手动集成 OkHttp3|

## 3.3 module
- 功能：通过注册module提供原生能力的扩展
- 原理：依赖bridge进行协议通信，根据不同module进行协议处理分发
- module，扩展原生能力
    + module注册
        * 必须注册CmlEngine.registerModule(Class<?> moduleClass)
        * 不强制要求添加@CmlModule,未添加时会使用默认设置
        * 不建议在运行中动态注册module
    + module名称
        * 默认使用module的类名
        * 配置module名称，添加注解@CmlModule(alias = "name")
    + module实例
        * 默认为实例全局唯一，即无论有多少instance都会使用同一个module实例
        * 配置全局性，添加注解@CmlModule(global = false)
    + module组合
        * 针对极特殊情况，允许多个class共用一个module名称
        * 必须有且只有一个class作为module，所有相关class均会使用该moduel配置
        * 其余class必须使用@CmlJoin(name = "name")，指定需要关联的moduel名称
        * 每个class实例之间无关联，仅会在使用时再创建实例
- method，提供原生能力方法
    + method注册
        * 自动注册module类中所有的public方法
        * 不强制要求添加@CmlMethod,未添加时会使用默认设置
        * 如果不希望方法被误添加，需要在方法上添加@CmlIgnore
    + method名称
        * 默认使用method方法名
        * 配置method名称，添加注解@CmlMethod(alias = "name")
    + method线程
        * 默认运行在主线程
        * 配置method线程，添加注解@CmlMethod(uiThread = false)
- param，原生能力方法所需要的参数
    + param类型
        * 针对Context、ICmlInstance等上下文类型，会根据调用环境进行查找替换
        * 对于CmlCallback的类型，会构建对应的回调，需要自行处理回调
        * 其余类型会根据bridge传递的参数进行处理
    + param参数
        * 根据birdge传递的数据，根据参数类型进行转化
        * 目前可转化的类型为JSONObject、String
        * 如果要直接转为对象，需要设置CmlJsonAdapter或接入相应json库
    + param字段
        * 只想获取传递数据中的某一个对象时，可以使用@CmlParam
        * 添加@CmlParam(name = "name")，设置该参数获取的字段
        * 添加@CmlParam(admin = "admin")，设置该参数默认值


----
# 4. JsBundleMgr
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
## 4.1 code
对js代码进行预加载、获取、缓存的管理。在该包里，我们将拿到的url根据协议来拆分成多个url1、url2等，然后在根据url1、url2等来获取对应的js代码，首先从本地缓存里获取去寻找对应的js代码，如果不存在则从网络去下载并保存在本地
## utils
一些文件管理、拆分url、网络判断的工具类
- CmlCodeUtils：获取到的url、code的拆解及合并
- CmlFileUtils：sd卡及缓存目录的判断
- CmlLogUtils：Log的实现
- CmlNetworkUtils：当前网络状态的判断，如Wi-Fi、4g等
- CmlUtils：Md5的生成、主线程判断等等

## 4.2 CmlJsBundleConstant
缓存文件名、预加载优先级的管理，预加载优先级有以下三种类型
- 普通（PRIORITY_COMMON）：非Wi-Fi情况不预加载
- 强预加载（PRIORITY_FORCE）：无论什么网络情况都预加载
- 强预加载+预解析（PRIORITY_FORCE_MAX）：目前未用到

## 4.3 CmlJsBundleEngine
实现了CmlJsBundleManager接口，主要有以下三个方法
- initConfig(Context,CmlJsBundleMgrConfig)：初始化config，主要是设置预加载url、预加载缓存、运行时缓存的设置，预加载及运行时缓存默认为4M
- startPreload()：开始预加载，目前预加载成功或者失败并没有任何信息返回，只能查看log进行分析
- getWXTemplate(String,CmlGetCodeStringCallback)：获取js代码

## 4.4 CmlJsBundleManager
实现此接口可以自己定义JsBundleMgr的实现

## 4.5 使用
### 添加依赖
```gradle
compile 'com.didi.chameleon:js-bundle-mgr:latest.version'
```
### 预加载
```java
    /**
     * 预加载的最大缓存
     */
    private static long maxPreloadSize = 4 * 1024 * 1024;
    /**
     * 运行时的最大缓存
     */
    private static long maxRuntimeSize = 4 * 1024 * 1024;

    public void preloadList(String url1, String url2){
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
        CmlJsBundleMgrConfig config = new CmlJsBundleMgrConfig.Builder()
                .setMaxPreloadSize(maxPreloadSize)
                .setMaxRuntimeSize(maxRuntimeSize)
                .build();
        CmlJsBundleEngine.getInstance().initConfig(this, config);
        CmlJsBundleEngine.getInstance().setPreloadList(cmlModels);
        CmlJsBundleEngine.getInstance().startPreload();
    }
```
### 获取Js代码
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
# 5. 富文本组件
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


