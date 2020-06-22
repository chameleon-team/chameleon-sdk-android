import Interface from './index.interface';

export default function initSocket(url = '') {
  const instance = Interface.initSocket(url);
  return instance;
}
