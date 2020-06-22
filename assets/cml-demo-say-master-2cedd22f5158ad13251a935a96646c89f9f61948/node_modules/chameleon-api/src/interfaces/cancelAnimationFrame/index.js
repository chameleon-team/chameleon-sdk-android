import index from './index.interface';

const CancelAnimationFactory = index.init();

const animationFrame = new CancelAnimationFactory();

export default animationFrame.cancelAnimationFrame();
