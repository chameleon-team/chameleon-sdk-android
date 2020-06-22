/**
 * 重新加载weex页面
 */
import { callNative } from '../../core/index.js'

const moduleName = 'cml';
const methodName = 'reloadPage';

export function reload(param = {}) {
  /**
   * url
   */
  callNative(moduleName, methodName, param, () => { });
}

reload.prototype.moduleName = moduleName;
reload.prototype.methodName = methodName;