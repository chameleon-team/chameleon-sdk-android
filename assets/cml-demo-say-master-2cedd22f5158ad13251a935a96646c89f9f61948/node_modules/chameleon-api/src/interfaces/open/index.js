import Interface from './index.interface';

export default function open(url = '', commonPatchParams = {}, extraOptions = { closeCurrent: false}) {
  Interface.open(url, commonPatchParams, extraOptions);
}
