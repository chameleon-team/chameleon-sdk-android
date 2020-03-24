import index from './index.interface';
import { tryJsonParse } from '../../lib/utils';

export default function getSystemInfo() {
  return new Promise((resolve, reject) => {
    index.getSystemInfo(res => {
      res.extraParams = tryJsonParse(res.extraParams);
      // px2viewpx
      const pxRpxRate = 750 / res.viewportWidth;
      const viewportHeight = ( res.viewportHeight * pxRpxRate ).toFixed(3);
      const viewportWidth = ( res.viewportWidth * pxRpxRate ).toFixed(3);

      if (res.os) {
        resolve({
          ...res,
          viewportHeight,
          viewportWidth 
        });
      } else {
        reject(res);
      }
    });
  });
}