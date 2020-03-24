import { callNative } from '../../core/index.js'

const cModuleName = 'cml';
const cMethodName = 'canIUse';

export function canIUse({ method }, cb) {
  const moduleName = this[method] && this[method].prototype.moduleName;
  const methodName = this[method] && this[method].prototype.methodName;

  if (module && method) {
    callNative(cModuleName, cMethodName, {
      module: moduleName,
      method: methodName
    }, cb);
  } else {
    this[method] ? cb(true) : cb(false);
  }

}

canIUse.prototype.moduleName = cModuleName;
canIUse.prototype.methodName = cMethodName;