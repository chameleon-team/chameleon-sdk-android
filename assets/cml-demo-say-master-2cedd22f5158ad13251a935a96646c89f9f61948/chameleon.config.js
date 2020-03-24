
// 设置静态资源的线上路径
const publicPath = '//www.static.chameleon.com/cml';
// 设置api请求前缀
const apiPrefix = 'https://api.chameleon.com';

cml.config.merge({
  templateLang: "cml",
  templateType: "html",
  platforms: ["web","weex","wx"],
  buildInfo: {
    wxAppId: '123456',
    wxEntryPage: '',
    webPath: 'https://api.chameleon.com/h5/index'
  },
  wx: {
    dev: {
    },
    build: {
      apiPrefix
    }
  },
  web: {
    dev: {
      analysis: false,
      console: false
    },
    build: {
      analysis: false,
      publicPath: `${publicPath}/web`,
      apiPrefix
    }
  },
  weex: {
    dev: {
    },
    build: {
      publicPath: `${publicPath}/weex`,
      apiPrefix
    },
    custom: {
      publicPath: `${publicPath}/wx`,
      apiPrefix
    }
  }
})

