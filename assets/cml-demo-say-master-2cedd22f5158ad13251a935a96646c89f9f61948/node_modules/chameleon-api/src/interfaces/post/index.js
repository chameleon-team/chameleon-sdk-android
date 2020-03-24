import index from '../request/index.interface';
import { queryStringify, addApiPrefix, isNeedApiPrefix, tryJsonParse } from '../../lib/utils.js';

// contentType 支持json和form 不支持formdata body不知道如何处理
export default function post({
  url,
  data = {},
  header = {},
  contentType = 'form',
  setting = { apiPrefix: isNeedApiPrefix(url) },
  resDataType = 'json'
}) {
  if (setting.apiPrefix) {
    url = addApiPrefix(url);
  }
  switch (contentType) {
  case 'form':
    data = queryStringify(data);
    header = {
      ...header,
      'Content-Type': 'application/x-www-form-urlencoded'
    };
    break;
  case 'json':
    data = JSON.stringify(data);
    header = {
      ...header,
      'Content-Type': 'application/json'
    };
    break;
  }

  return new Promise(function(resolve, reject) {
    index.request({
      url,
      body: data,
      setting,
      method: 'POST',
      headers: header,
      cb: function(res) {
        let { status, data, headers } = res;
        if (status >= 200 && status < 300) {
          if (resDataType === 'json') {
            data = tryJsonParse(data);
          }
          resolve(data);
        } else {
          reject('http statusCode:' + status);
        }
      }
    });
  });
}
