import { dispatchProtocol, registerListen } from '../common';
var globalEvent = weex.requireModule('globalEvent');

/**
 * 注册监听通道
 */
export function init() {
  globalEvent.addEventListener('cmlBridgeChannel', function (ptc) {
    let ptcStr = ptc.protocol;
    dispatchProtocol(ptcStr);
  })
}

/**
 * 注册主动监听
 * @param {String} module 
 * @param {String} method 
 * @param {Function} callback 
 */
export function listen(module, method, callback) {
  registerListen(...arguments);
}
