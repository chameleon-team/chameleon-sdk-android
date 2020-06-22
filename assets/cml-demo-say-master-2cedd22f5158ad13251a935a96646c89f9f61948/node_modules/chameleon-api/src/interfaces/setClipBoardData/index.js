import index from '../getClipBoardData/index.interface';

export default function setClipBoardData(text) {
  return new Promise((resolve, reject) => {
    index.setClipBoardData(text, res => {
      if (res.errno === 0) {
        resolve(res.data);
      } else {
        reject(res.errMsg);
      }
    });
  });
}

