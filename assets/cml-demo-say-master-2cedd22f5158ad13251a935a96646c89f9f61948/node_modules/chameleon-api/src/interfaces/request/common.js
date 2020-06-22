const defaultHeaders = {
  'Content-Type': 'application/x-www-form-urlencoded'
};

const defaultOptions = {
  credentials: 'same-origin'
};

const defaultType = 'form';
const contentTypeMap = {
  form: 'application/x-www-form-urlencoded',
  json: 'application/json'
};

// 处理基本的错误, 如500, 404等等
function filterStatus(res) {
  if (res.status >= 200 && res.status < 300) {
    return res;
  } else {
    let error = new Error(res.statusText);
    error.res = res;
    error.type = 'http';
    throw error;
  }
}

// 解析为json格式
function parseJSON(res) {
  return res.json()["catch"](err => {
    if (err) {
      err.type = 'json';
      err.res = res;
    }
    throw err;
  });
}

// 转换成form表单形式
function toForm(body) {
  let form = new FormData();
  Object.keys(body).forEach(key => {
    if (body[key] !== undefined) {
      form.append(key, body[key]);
    }
  });
  return form;
}

export function parseHeader(headers) {
  // fetch中的headers value为数组形式,其他端为字符串形式， 统一为字符串
  // header的key值统一为小写
  let result = {};
  Object.keys(headers).forEach(key => {
    let value = headers[key];
    if (value instanceof Array) {
      value = value[0];
    }
    result[key.toLowerCase()] = value;
  });
  return JSON.stringify(result);
}

export default {
  defaultHeaders,
  defaultOptions,
  defaultType,
  contentTypeMap,
  filterStatus,
  parseJSON,
  toForm,
  parseHeader
};
