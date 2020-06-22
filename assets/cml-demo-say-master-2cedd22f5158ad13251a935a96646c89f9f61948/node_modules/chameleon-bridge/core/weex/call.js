import { moduleDefault } from '../const';
import { serializeProtocol, registerCallback } from '../common';
var cmlBridge = weex.requireModule('cmlBridge');

/**
 * js调用客户端
 * @param {String} action 行为方式
 * @param {String} module 模块名
 * @param {String} method 方法名
 * @param {Array}  args   参数数组
 */
export function call(action, module, method, args, callback) {
  var module = module || moduleDefault;
  const callbackId = registerCallback(module, method, callback);
  const url = serializeProtocol({
    action,
    module,
    method,
    args,
    callbackId
  });

  cmlBridge && cmlBridge.channel && cmlBridge.channel(url);
}
