import { callNative } from '../../core/index.js'
import {
  getOpenObj,
  queryStringify,
  getUrlWithConnector
} from '../../utils';

const moduleName = 'cml';
const methodName = 'openPage';

export function open(param) {
  /**
   * param: url, commonPatchParams, extraOptions
   */
  let url = param.url;
  let commonPatchParams = param.commonPatchParams;
  let closeCurrent = (param.extraOptions && param.extraOptions.closeCurrent) || false;
  url = getUrlWithConnector(url) + queryStringify(commonPatchParams);
  let openObj = getOpenObj(url);
  const urlOpen = openObj.weex ? openObj.weex : openObj.web;

  callNative(moduleName, methodName, {
    url: urlOpen,
    closeCurrent
  }, () => { })
}

open.prototype.moduleName = moduleName;
open.prototype.methodName = methodName;