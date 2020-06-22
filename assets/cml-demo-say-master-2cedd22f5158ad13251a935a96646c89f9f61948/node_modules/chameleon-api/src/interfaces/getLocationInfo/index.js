import index from './index.interface';

export default function getLocationInfo() {
  return new Promise((resolve, reject) => {
    index.getLocationInfo(res => {
      if (res.errno === 0) {
        resolve(res.data);
      } else {
        reject(res.errMsg);
      }
    });
  });
}
