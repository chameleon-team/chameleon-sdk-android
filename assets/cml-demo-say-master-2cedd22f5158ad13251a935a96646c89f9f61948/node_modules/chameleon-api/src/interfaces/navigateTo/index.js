import index from './index.interface';
import { queryStringify } from '../../lib/utils.js';

export default function navigateTo(opt) {
  // 转换为字符串通过多态不支持object，需改
  let query = queryStringify(opt.query) || '';
  let path = opt.path || '';
  let url = opt.url || '';

  // 不能通过直接转换类型对opt.query重新赋值的操作, 否则会造成opt原始传入对象数据被篡改的问题
  index.navigateTo({
    path,
    url,
    query
  });
}
