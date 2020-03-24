import index from './index.interface';

export default function removeStorage(key) {
  return new Promise((resolve, reject) => {
    index.removeStorage(key, res => {
      if (res.errno === 0) {
        resolve();
      } else {
        reject(res.errMsg);
      }
    });
  });
}
