import { callNative } from '../../core/index.js';

const moduleName = 'modal';
const methodShowToast = 'showToast';
const methodAlert = 'alert';
const methodConfirm = 'confirm';

export function showToast(param) {
  /**
   * message
   * duration(ms)
   */
  callNative(moduleName, methodShowToast, param, () => { });
}

showToast.prototype.moduleName = moduleName;
showToast.prototype.methodName = methodShowToast;

export function alert(param, successCallBack) {
  /**
   * message
   * confirmTitle
   */
  callNative(moduleName, methodAlert, param, successCallBack);
}

alert.prototype.moduleName = moduleName;
alert.prototype.methodName = methodAlert;

export function confirm(param, successCallBack, failCallBack) {
  /**
   * message
   * confirmTitle
   * cancelTitle
   */
  callNative(moduleName, methodConfirm, param, successCallBack);
}

confirm.prototype.moduleName = moduleName;
confirm.prototype.methodName = methodConfirm;
