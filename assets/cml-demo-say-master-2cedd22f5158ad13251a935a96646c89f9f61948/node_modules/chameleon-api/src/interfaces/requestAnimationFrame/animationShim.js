const enterPageTime = Date.now();

let TARGET_FPS = 60;


let requests = Object.create(null);


let raf_handle = 0;


let timeout_handle = -1;

function onFrameTimer() {
  let cur_requests = requests;
  requests = Object.create(null);
  timeout_handle = -1;
  Object.keys(cur_requests).forEach(function(id) {
    let request = cur_requests[id];

    request(Date.now() - enterPageTime);
  });
}

export const requestAnimationFrame = (callback) => {
  let cb_handle = ++raf_handle;
  requests[cb_handle] = callback;
  if (timeout_handle === -1) {timeout_handle = setTimeout(onFrameTimer, 1000 / TARGET_FPS);}
  return cb_handle;
};

export const cancelAnimationFrame = (handle) => {
  delete requests[handle];
  if (Object.keys(requests).length === 0) {
    clearTimeout(timeout_handle);
    timeout_handle = -1;
  }
};
