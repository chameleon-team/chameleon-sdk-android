import { callNative } from '../../core/index.js'

const moduleName = 'cml';
const methodName = 'closePage';

export function close() {
  callNative(moduleName, methodName, {}, () => { })
}

close.prototype.moduleName = moduleName;
close.prototype.methodName = methodName;