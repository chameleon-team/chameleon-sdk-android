const toString = Object.prototype.toString;

/**
 * 判断是否为普通对象 ps：array fuc reg date不算
 * @param {*} value
 * @returns {Boolean} 
 */
export const isObject = function (param) {
  return toString.call(param) === '[object Object]';
};

/**
 * 红字打印
 * @param {*} value 
 */
export const redLog = function(value) {
  return console.log(`%c${value}`, 'color:red')
}

/**
 * 判断targetMap中的属性是否被checkMap的属性包含，不是则抛出错误
 * @param {Object || Array} checkMap 
 * @param {Object} targetMap 
 * @returns {Boolean}
 */
export const checkValue = function (check, targetMap) {
  if(isObject(check) || Array.isArray(check)) {
    const checkArray = isObject(check) ? Object.keys(check) : check;
    (Object.keys(targetMap)).forEach((key) => {
      if (!checkArray.includes(key)) {
        throw Error(`${key}值不合法，请检查！`)
      }
    });
  } else {
    redLog('请传入数组或对象')
    return false
  }
  return true;
}

export const isWx = process.env.platform === 'wx';

export const isWeex = process.env.platform === 'weex';

export const isWeb = process.env.platform === 'web';

export default {};