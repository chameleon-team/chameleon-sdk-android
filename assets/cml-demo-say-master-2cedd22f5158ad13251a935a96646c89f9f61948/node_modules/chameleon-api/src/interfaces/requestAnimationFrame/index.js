import index from './index.interface';

const RequestAnimationFactory = index.init();

const animationFrame = new RequestAnimationFactory();
export default animationFrame.requestAnimationFrame();
