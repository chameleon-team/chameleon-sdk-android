import index from './index.interface';
import { queryStringify } from '../../lib/utils.js';

export default function redirectTo(opt) {
  let query = '';
  let path = '';
  let url = '';
  if (opt.path) {
    path = opt.path;
  }
  if (opt.url) {
    url = opt.url;
  }
  if (typeof opt.query !== 'string') {
    query = queryStringify(opt.query);
  }
  index.redirectTo({
    path,
    url,
    query
  });
}
