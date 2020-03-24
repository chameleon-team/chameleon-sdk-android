import index from './index.interface';
import { queryStringify, isEmpty, addApiPrefix, isNeedApiPrefix, tryJsonParse } from '../../lib/utils.js';

export function get({
  url,
  data = {},
  header = {},
  setting = { apiPrefix: isNeedApiPrefix(url) },
  resDataType = 'json'
}) {
  if (setting.apiPrefix) {
    url = addApiPrefix(url);
  }
  let isJsonp = setting.jsonp;

  if (data && !isEmpty(data) && queryStringify(data) && !isJsonp) {
    if (url.indexOf('?') === -1) {
      url += '?';
    }
    url += queryStringify(data);
  }

  return new Promise(function(resolve, reject) {
    index.request({
      url,
      body: isJsonp ? queryStringify(data) : '',
      method: 'GET',
      setting,
      headers: header,
      cb: function(res) {
        let { status, data } = res;
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

export function post({
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
        let { status, data } = res;
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

// 为支持 DELETE / PUT 方法增加此方法
export default function request({
  url,
  data = {},
  method = 'GET',
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
      method,
      headers: header,
      cb: function(res) {
        let { status, data } = res;
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
