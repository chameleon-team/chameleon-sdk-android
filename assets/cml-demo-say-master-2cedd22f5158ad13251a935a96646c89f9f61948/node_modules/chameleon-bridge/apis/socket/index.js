import { callNative, listenNative } from '../../core/index.js';

const moduleName = 'webSocket';
const methodName = 'WebSocket';

export function initSocket({ url }) {
  WebSocket.WebSocket(url);
  return WebSocket;
}

initSocket.prototype.moduleName = moduleName;
initSocket.prototype.methodName = methodName;

const WebSocket = {

  WebSocket(url) {
    this._callAdapter(methodName, { url });
  },

  onopen(cb) {
    this._listenAdapter('onopen', cb);
  },

  onmessage(cb) {
    this._listenAdapter('onmessage', cb);
  },

  onerror(cb) {
    this._listenAdapter('onerror', cb);
  },

  onclose(cb) {
    this._listenAdapter('onclose', cb);
  },

  send(data = {}) {
    this._callAdapter('send', data);
  },

  close(param = { code: 0, reason: 'close' }) {
    /**
     * code: 0
     * reason: string
     */
    this._callAdapter('close', param);
  },

  _callAdapter(key, param) {
    callNative(moduleName, key, param, () => { });
  },

  _listenAdapter(key, cb) {
    listenNative(moduleName, key, cb)
  }

}