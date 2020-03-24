class Socket {

  constructor(url) {
    this.instance = new WebSocket(url);
  }

  onopen(cb) {
    this.instance.onopen = function () {
      cb()
    };
  }

  onmessage(cb) {
    this.instance.onmessage = function (res) {
      cb(res);
    };
  }

  onerror(cb) {
    this.instance.onerror = function (err) {
      cb(err);
    };
  }

  onclose(cb) {
    this.instance.onclose = function () {
      cb();
    };
  }

  send(data = {}) {
    this.instance.send(JSON.stringify(data));
  }

  close() {
    this.instance.close();
  }

}


export default Socket;
