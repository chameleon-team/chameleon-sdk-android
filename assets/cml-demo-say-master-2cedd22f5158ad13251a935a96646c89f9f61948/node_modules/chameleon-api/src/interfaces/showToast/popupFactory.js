import CToast from './components/c-toast/c-toast.cml';

function createElement(id) {
  const popupContainer = document.createElement('div');
  popupContainer.setAttribute('id', id);
  document.body.appendChild(popupContainer);
}
// 在web端，以api的形式调用组件

/**
 * 在web端构建toast弹出层
 * @param {Object} opt 传入的弹出层配置
 * **/
export function buildToast(opt) {
  
  createElement('popupContainer');

  const ToastConstructor = Vue.extend(CToast);
  const instance = new ToastConstructor({
    propsData: {
      needIcon: false, 
      ...opt
    },
  });
  instance.$mount('#popupContainer');
  return instance;
}