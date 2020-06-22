import index from './index.interface';

export default function getClipBoardData() {
  return new Promise((resolve, reject) => {
    index.getClipBoardData(res => {
      if (res.errno === 0) {
        resolve(res.data);
      } else {
        reject(res.msg);
      }
    });
  });
}

