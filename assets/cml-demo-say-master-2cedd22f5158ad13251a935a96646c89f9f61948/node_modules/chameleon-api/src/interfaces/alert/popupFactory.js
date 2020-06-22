import CDialog from './components/c-dialog/c-dialog.cml';

function createElement(id) {
  const popupContainer = document.createElement('div');
  popupContainer.setAttribute('id', id);
  document.body.appendChild(popupContainer);
}

/**
 * 在web端构建confirm/alert弹出框，二者通过type区分
 * @param {String} type 弹出框类型, alert/confirm
 * @param {Object} opt 传入的弹出框配置项， 用来代替默认配置项
 * @param {Function} successCallBack  点击确定/取消时的回调函数
 * **/
export function buildConfirm(type = 'alert', opt, successCallBack) {

  createElement('popupContainer');

  const ConfirmConstructor = Vue.extend(CDialog);

  const defaultProps = {
    type: type,
    confirmText: '确定',
    content: '',
    show: true,
    mask: true,
  };
  if (type === 'confirm') {
    defaultProps.cancelText = '取消';
  }

  const instance = new ConfirmConstructor({
    propsData: {
      ...defaultProps,
      ...opt
    }
  });

  // (instance._events['show'] || (instance._events['show'] = [])).push(function(e) {
  //   instance.show = e.detail.value;
  // });
  instance.$on('show', e => {
    instance.show = e.detail.value;
  });
  instance.$on('confirm', e => {
    successCallBack(instance.confirmText);
  });
  instance.$on('cancel', e => {
    successCallBack(instance.cancelText);
  });

  // (instance._events['confirm'] || (instance._events['confirm'] = [])).push(function() {
  //   successCallBack(instance.confirmText);
  // });
  // (instance._events['cancel'] || (instance._events['cancel'] = [])).push(function() {
  //   successCallBack(instance.cancelText);
  // });

  instance.$mount('#popupContainer');

  return instance;
}

