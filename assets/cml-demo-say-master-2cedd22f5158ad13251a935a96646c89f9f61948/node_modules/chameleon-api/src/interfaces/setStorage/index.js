import index from './index.interface';

export default function setStorage(key, value) {
  const valueType = typeof value;
  if (valueType !== 'string') {
    value = JSON.stringify(value);
  }
  if (valueType === 'undefined') {
    console.warn('The type of storage value can not be "undefined"');
    return Promise.reject('The type of storage value cannot be "undefined"');
  }
  return new Promise((resolve, reject) => {
    index.setStorage(key, value, res => {
      if (res.errno === 0) {
        resolve(res.data);
      } else {
        reject(res.errMsg);
      }
    });
  });
}