export function getViewportSize() {
  let viewportWidth;
  let viewportHeight;
  if (window.innerWidth) {
    viewportWidth = window.innerWidth;
    viewportHeight = window.innerHeight;
  } else if (document.documentElement && document.documentElement.clientWidth || document.body && document.body.clientWidth) {
    viewportWidth = document.documentElement && document.documentElement.clientWidth || document.body && document.body.clientWidth;
    viewportHeight = document.documentElement && document.documentElement.clientHeight  || document.body && document.body.clientHeight; 
  }
  return {
    viewportWidth: viewportWidth,
    viewportHeight: viewportHeight
  };
}