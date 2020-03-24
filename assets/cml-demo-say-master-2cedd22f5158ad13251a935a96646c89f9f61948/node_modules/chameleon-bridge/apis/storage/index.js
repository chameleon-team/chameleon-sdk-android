import { callNative } from '../../core/index.js';

const moduleName = 'storage';
const methodSet = 'setStorage';
const methodGet = 'getStorage';
const methodRemove = 'removeStorage';

export function setStorage(param, cb) {
  /**
   * key
   * value
   */
  callNative(moduleName, methodSet, param, function (res) {
    /**
     * errno
     * msg
     * data
     */
    cb(res);
  });
}

setStorage.prototype.moduleName = moduleName;
setStorage.prototype.methodName = methodSet;

export function getStorage(param, cb) {
  /**
   * key
   */
  callNative(moduleName, methodGet, param, function (res) {
    /**
     * errno
     * msg
     * data
     */
    cb(res);
  });
}

getStorage.prototype.moduleName = moduleName;
getStorage.prototype.methodName = methodGet;

export function removeStorage(param, cb) {
  /**
   * key
   */
  callNative(moduleName, methodRemove, param, function (res) {
    /**
     * errno
     * msg
     * data
     */
    cb(res);
  });
}

removeStorage.prototype.moduleName = moduleName;
removeStorage.prototype.methodName = methodRemove;