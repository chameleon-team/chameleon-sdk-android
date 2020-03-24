import index from './index.interface';
import { getRefObj } from '../../lib/utils';

export default function getRect(ref, context, scrollerBox) {
  return new Promise((resolve, reject) => {
    let refObj = getRefObj(ref);
    refObj.context = context;
    refObj.scrollerBox = scrollerBox;
    index.getRect(refObj, res => {
      resolve(res);
    });
  });
}