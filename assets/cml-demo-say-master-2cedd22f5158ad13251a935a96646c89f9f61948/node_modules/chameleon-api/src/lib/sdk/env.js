import bridge from './cmlBridge';

// 异步换同步
let isSDK = false;
bridge.getSDKInfo({}, (res) => {
  if (res.errno == 0) {
    isSDK = true;
  }
})

/**
 * 判断是在ChameleonSDK中
 * @returns {boolean}
 */
export function inSDK() {
  return isSDK;
}
