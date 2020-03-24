/**
 * 基础js方法的封装
 *
 */
function typeEqual(obj, type) {
  return Object.prototype.toString.call(obj) === '[object ' + type + ']';
}

export function isFn(obj) {
  return typeEqual(obj, 'Function');
}

export function isStr(obj) {
  return typeEqual(obj, 'String');
}

export function isNum(obj) {
  return typeof obj === 'number';
}

export function isObj(obj) {
  return typeEqual(obj, 'Object');
}

export function isArray(obj) {
  return typeEqual(obj, 'Array');
}

export function isUndefined(obj) {
  return typeEqual(obj, 'Undefined');
}

export function isEmpty(obj) {
  for (let key in obj) {
    if (obj.hasOwnProperty(key)) {
      return false;
    }
  }
  return true;
}

export function noop() { }

export function parseQuery(obj) {
  let str = '&';
  let keys = null;
  if (obj && Object.keys(obj).length > 0) {
    keys = Object.keys(obj);
    for (let i = 0; i < keys.length; i++) {
      let key = keys[i];
      str += `${key}=${encodeURIComponent(obj[key])}` + '&';
    }

  }
  return str;

}

export function queryStringify(obj) {
  let str = '&';
  let keys = null;
  if (obj && Object.keys(obj).length > 0) {
    keys = Object.keys(obj);
    for (let i = 0; i < keys.length; i++) {
      let key = keys[i];
      str += `${key}=${encodeURIComponent(obj[key])}` + '&';
    }

  }
  return str;
}

export function queryParse(search = '') {
  let arr = search.split(/(\?|&)/);
  let parmsObj = {};
  for (let i = 0; i < arr.length; i++) {
    if (arr[i].indexOf('=') !== -1) {
      let keyValue = arr[i].match(/([^=]*)=(.*)/);
      parmsObj[keyValue[1]] = keyValue[2];
    }
  }
  return parmsObj;
}

export function isNeedApiPrefix(url) {
  return /^\/[^/]/.test(url);
}

export function addApiPrefix(url) {
  if (process.env.cmlApiPrefix) {
    return process.env.cmlApiPrefix + url;
  }
  return url;
}

export function tryJsonParse(some) {
  // 这里eslint提示也先别删除\[\]
  if (isStr(some) && /[\{\[].*[\}\]]/.test(some)) {
    some = JSON.parse(some);
  }
  return some;
}

export function getQueryParamsFromUrl(url) {
  let arr = url.split(/(\?|&)/);
  let parmsObj = {};
  for (let i = 0; i < arr.length; i++) {
    if (arr[i].indexOf('=') !== -1) {
      let keyValue = arr[i].match(/([^=]*)=(.*)/);
      parmsObj[keyValue[1]] = decodeURIComponent(keyValue[2]);
    }
  }
  return parmsObj;
}

/**
 * 获取处理后的各端打开的地址
 * @param {String} url url地址
 * @return {Object} objTreated 处理好的三端地址及对象
 */
export function getOpenObj(url) {
  const webUrlWithoutQuery = url.split('?')[0];
  const queryObj = getQueryParamsFromUrl(url);
  const {
    weixin_appid = '',
    weixin_path = '',
    weixin_envVersion = '',
    weex_path = '',
    wx_addr = '',
    ...extraData
  } = queryObj;

  let objTreated = {
    weex: wx_addr ? webUrlWithoutQuery + '?weex_path=' + weex_path + queryStringify(extraData) + '&wx_addr=' + wx_addr : null,
    web: webUrlWithoutQuery + '?' + queryStringify(extraData),
    wx: {
      appId: weixin_appid,
      path: weixin_path,
      extraData: extraData,
      envVersion: weixin_envVersion
    }
  };
  return objTreated;
}

// 获得带正确连接符的url
export function getUrlWithConnector(url) {
  let connector = url.includes('?') ? '&' : '?';
  return url + connector;
}

// 获取ref的通用对象
export function getRefObj(ref) {
  let refObj = {
    webDom: '',
    id: '',
    weexRef: ''
  };
  if (process.env.platform === 'wx') {
    refObj.id = ref.id;
  } else if (process.env.platform === 'weex') {
    refObj.weexRef = ref;
  } else if (ref.$el) {
    refObj.webDom = ref.$el;
  } else {
    refObj.webDom = ref;
  }
  return refObj;
}

/**
 * 比较版本号
 * @param {String} v1 版本号1
 * @param {String} symb 比较符
 * @param {String} v2 版本号2
 */
export function compareVersion(v1, symb, v2) {
  v1 = parseVersion(v1);
  v2 = parseVersion(v2);
  if (symb.indexOf('=') !== -1 && v1 === v2) {
    return true;
  }
  if (symb.indexOf('>') !== -1 && v1 > v2) {
    return true;
  }
  if (symb.indexOf('<') !== -1 && v1 < v2) {
    return true;
  }
  return false;
}

function parseVersion(version = '') {
  version = version.split('.');
  version.length = 4;
  let ret = [];
  version.forEach(function (n) {
    n = n * 1;
    if (n) {
      ret.push(n >= 10 ? n : '0' + n);
    } else {
      ret.push('00');
    }
  });
  return parseInt(ret.join(''), 10);
}

/**
 * 判断targetMap中的属性是否被checkMap的属性包含，不是则抛出错误
 * @param {Object || Array} checkMap 
 * @param {Object} targetMap 
 * @returns {Boolean}
 */
export const checkValue = function (check, targetMap) {
  if (isObj(check) || isArray(check)) {
    const checkArray = isObj(check) ? Object.keys(check) : check;
    (Object.keys(targetMap)).forEach((key) => {
      if (!checkArray.includes(key)) {
        throw Error(`${key}值不合法，请检查！`);
      }
    });
  } else {
    // redLog('请传入数组或对象')
    return false;
  }
  return true;
};