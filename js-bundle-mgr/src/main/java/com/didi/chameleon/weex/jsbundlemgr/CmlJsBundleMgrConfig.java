package com.didi.chameleon.weex.jsbundlemgr;


/**
 * Cml预加载配置
 * Created by youzicong on 2018/10/10
 */
public class CmlJsBundleMgrConfig {

    public long maxPreloadSize = 4 * 1024 * 1024;
    public long maxRuntimeSize = 4 * 1024 * 1024;

    private CmlJsBundleMgrConfig() {
    }

    private CmlJsBundleMgrConfig(long maxPreloadSize, long maxRuntimeSize) {
        this.maxPreloadSize = maxPreloadSize;
        this.maxRuntimeSize = maxRuntimeSize;
    }

    public static class Builder {
        /**
         * 预加载的最大缓存
         */
        private long maxPreloadSize = 4 * 1024 * 1024;
        /**
         * 运行时的最大缓存
         */
        private long maxRuntimeSize = 4 * 1024 * 1024;


        public Builder setMaxPreloadSize(long maxPreloadSize) {
            this.maxPreloadSize = maxPreloadSize;
            return this;
        }

        public Builder setMaxRuntimeSize(long maxRuntimeSize) {
            this.maxRuntimeSize = maxRuntimeSize;
            return this;
        }

        public CmlJsBundleMgrConfig build() {
            return new CmlJsBundleMgrConfig(maxPreloadSize, maxRuntimeSize);
        }
    }
}
