import { callNative } from '../../core/index.js'

const moduleName = 'cml';
const methodName = 'getSDKInfo';

export function getSDKInfo(param, cb) {
  callNative(moduleName, methodName, param, cb);
}

getSDKInfo.prototype.moduleName = moduleName;
getSDKInfo.prototype.methodName = methodName;