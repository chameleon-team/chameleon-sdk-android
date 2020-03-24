import { dispatchProtocol, registerListen } from '../common';

/**
 * 注册监听通道
 */
export function init() {
  window.cmlBridge = {};
  window.cmlBridge.channel = function (protocol) {
    dispatchProtocol(protocol);
  }
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
