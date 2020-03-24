import index from './index.interface';

const moduleName = 'dom';
const methodName = 'getComponentRect';

export function getComponentRect(module, method, args, callback) {
  index.getComponentRect(...arguments);
}

getComponentRect.prototype.moduleName = moduleName;
getComponentRect.prototype.methodName = methodName;