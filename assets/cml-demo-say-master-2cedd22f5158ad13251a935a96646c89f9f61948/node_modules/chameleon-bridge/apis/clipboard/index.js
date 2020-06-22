import { callNative } from '../../core/index.js'

const moduleName = 'clipboard';
const methodGet = 'getClipBoardData';
const methodSet = 'setClipBoardData';

export function getClipBoardData(param = {}, cb) {
  /**
   * params {}
   */
  callNative(moduleName, methodGet, param, res => {
    /**
    * errno
    * msg
    * data
    */
    cb(res);
  });
}

getClipBoardData.prototype.moduleName = moduleName;
getClipBoardData.prototype.methodName = methodGet;

export function setClipBoardData({ data = '' }, cb) {
  callNative(moduleName, methodSet, {
    data
  }, res => {
    /**
    * errno
    * msg
    * data
    */
    cb(res);
  });
}

setClipBoardData.prototype.moduleName = moduleName;
setClipBoardData.prototype.methodName = methodSet;
