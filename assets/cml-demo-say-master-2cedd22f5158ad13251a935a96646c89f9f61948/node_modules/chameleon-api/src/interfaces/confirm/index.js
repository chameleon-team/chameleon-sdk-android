import ui from './index.interface';

export default function confirm(opt) {
  let { message = '', confirmTitle = '确定', cancelTitle = '取消' } = opt;
  return new Promise((resolve, reject) => {
    ui.confirm({ message, confirmTitle, cancelTitle }, value => {
      resolve(value);
    }, () => {
      reject();
    });
  });
}