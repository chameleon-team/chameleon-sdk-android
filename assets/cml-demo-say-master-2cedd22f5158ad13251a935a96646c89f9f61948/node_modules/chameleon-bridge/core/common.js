import { BridgeGlobal } from './global';
import { moduleDefault } from './const';

export function serializeProtocol({ action, module, method, args, callbackId }) {
  const param = encodeURIComponent(JSON.stringify(args));
  let url = 'cml://channel?';
  url += `module=${module}&`;
  url += `action=${action}&`;
  url += `method=${method}&`;
  url += `args=${param}&`;
  url += `callbackId=${callbackId}`;
  return url;
}

export function deserializeProtocol(ptcStr) {
  let ptcObj = {};
  let arr = ptcStr && ptcStr.split(/(\?|&)/);
  for (let i = 0; i < arr.length; i++) {
    if (~arr[i].indexOf('=')) {
      let keyValue = arr[i].match(/([^=]*)=(.*)/);
      ptcObj[keyValue[1]] = decodeURIComponent(keyValue[2]);
    }
  }

  // 获得所需的参数
  let {
    action,
    module,
    method,
    args,
    callbackId
  } = ptcObj;

  try {
    args = JSON.parse(args)
  } catch (e) {
  }

  try {
    args.data = args.data ? decodeURIComponent(args.data) : '{}'
    args.data = JSON.parse(args.data)
  } catch (e) {
  }

  const listenName = module + '_' + method;

  return {
    action, module, method, args, callbackId, listenName
  }
}

/**
 * 注册主动调用端上的回调监听
 * @param {Function} callback 回调方法体
 */
export function registerCallback(module, method, callback) {
  var module = module || moduleDefault;
  let instance = BridgeGlobal.getInstance();
  let callbackId = module + '_' + method + '_callback_' + instance.callbackId;
  instance.listenCallbacks[callbackId] = callback;
  instance.callbackId++;
  return callbackId;
}

/**
 * 注册等待端上主动调用监听方法
 * @param {Function} callback 监听方法体
 */
export function registerListen(module, method, callback) {
  let instance = BridgeGlobal.getInstance();
  var module = module || moduleDefault;
  let listenName = module + '_' + method;
  instance.listenCallbacks[listenName] = callback;
  return listenName;
}

/**
 * 处理通道协议
 * @param {string} protocol 协议
 */
export function dispatchProtocol(protocol) {
  let {
    action,
    module,
    method,
    args,
    callbackId,
    listenName
  } = deserializeProtocol(protocol);
  let instance = BridgeGlobal.getInstance();

  if (action === 'callbackToJs') {
    instance.listenCallbacks[callbackId] && instance.listenCallbacks[callbackId](args);
  }

  if (action === 'invokeJsMethod') {
    instance.listenCallbacks[listenName] && instance.listenCallbacks[listenName](args);
  }
}