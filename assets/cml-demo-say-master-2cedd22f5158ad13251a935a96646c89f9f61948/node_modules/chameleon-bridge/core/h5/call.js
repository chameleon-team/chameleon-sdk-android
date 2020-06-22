import { moduleDefault } from '../const';
import { serializeProtocol, registerCallback } from '../common';

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

  schemeJump(url);
}

function schemeJump(url) {
  let iframe = document.createElement('iframe');
  iframe.src = url;
  iframe.style.display = 'none';
  document.documentElement.appendChild(iframe);
  setTimeout(() => {
    document.documentElement.removeChild(iframe);
  }, 0);
}
