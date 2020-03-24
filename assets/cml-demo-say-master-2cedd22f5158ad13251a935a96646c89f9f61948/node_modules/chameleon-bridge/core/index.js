import index from './index.interface';

// 初始化通道
index.initChannel();

export function callNative(module, method, args, callback) {
  index.callNative(...arguments);
}

export function listenNative(module, method, callback) {
  index.listenNative(...arguments);
}
