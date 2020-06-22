class WxSocket {

  constructor(url) {
    this.instance = wx.connectSocket({
      url: url
    });
  }

  onopen(cb) {
    this.instance.onOpen(() => {
      cb();
    });
  }

  onmessage(cb) {
    this.instance.onMessage(res => {
      cb(res);
    });
  }

  onerror(cb) {
    this.instance.onError(err => {
      cb(err);
    });
  }

  onclose(cb) {
    this.instance.onClose(() => {
      cb();
    });
  }

  send(data) {
    data = JSON.stringify(data);
    this.instance.send({
      data
    });
  }

  close() {
    this.instance.close();
  }

}

export default WxSocket;
