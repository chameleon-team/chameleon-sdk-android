import index from './index.interface';
export default function canIUse(metName) {
  return new Promise((resolve, reject) => {
    index.canIUse(metName, (res) => {
      if (res) {
        resolve(res);
      } else {
        reject(res);
      }
    });
  });
}
