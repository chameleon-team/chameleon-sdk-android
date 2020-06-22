import { callNative } from '../../core/index.js';

const moduleName = 'cml';
const methodName = 'rollbackWeb';

export function rollbackWeb() {
  callNative(moduleName, methodName, {}, () => { });
}

rollbackWeb.prototype.moduleName = moduleName;
rollbackWeb.prototype.methodName = methodName;
