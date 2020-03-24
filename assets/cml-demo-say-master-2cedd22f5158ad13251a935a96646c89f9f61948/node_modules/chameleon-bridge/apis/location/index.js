import { callNative } from '../../core/index.js'

const moduleName = 'cml';
const methodName = 'getLocationInfo';

export function getLocationInfo(param, cb) {
  callNative(moduleName, methodName, param, function (res) {
    /**
     * lat:number
     * lng:number
     */
    cb(res);
  })
}

getLocationInfo.prototype.moduleName = moduleName;
getLocationInfo.prototype.methodName = methodName;