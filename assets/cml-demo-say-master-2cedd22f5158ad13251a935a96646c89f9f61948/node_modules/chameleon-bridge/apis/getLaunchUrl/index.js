import { callNative } from '../../core/index.js'

const moduleName = 'cml';
const methodName = 'getLaunchUrl';

export function getLaunchUrl(param, cb) {
  callNative(moduleName, methodName, param, cb);
}

getLaunchUrl.prototype.moduleName = moduleName;
getLaunchUrl.prototype.methodName = methodName;