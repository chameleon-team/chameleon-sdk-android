import { callNative } from '../../core/index.js'

const moduleName = 'cml'
const methodName = 'setTitle'

export function setTitle(param) {
  callNative(moduleName, methodName, param, () => { })
}

setTitle.prototype.moduleName = moduleName;
setTitle.prototype.methodName = methodName;