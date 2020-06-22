import instance from './getWidth.interface';

export default function px2cpx(px) {

  if (typeof px !== 'number') {
    console.error('Parameter must be a number');
    return;
  }
  return new Promise((resolve, reject) => {
    instance.getWidth(viewportWidth => {
      let cpx = +(750 / viewportWidth * px).toFixed(3);
      resolve(cpx);
    });
  })
}
