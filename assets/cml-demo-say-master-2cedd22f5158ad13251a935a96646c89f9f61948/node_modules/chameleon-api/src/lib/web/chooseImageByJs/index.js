import canvasResize from './canvasResize/index.js';

export default class ChooseImageByJs {
  constructor(opt = {}) {
    this.opt = opt;
    this.fileInput = document.createElement("INPUT");
    this.fileInput.name = 'file';
    this.fileInput.id = 'file-input-' + ~~(Math.random() * 1e6);
    this.fileInput.type = 'file';
    this.fileInput.style.display = 'none';
    this.fileInput.setAttribute('accept', "image/*");
    document.body.appendChild(this.fileInput);
    this.init();
  }
  init() {
    this.bindEvent();
  }
  bindEvent() {
    this.fileInput.addEventListener('change', (e) => {
      this.onFileChange(e);
    }, false);
  }
  chooseImage(params = {
    quality: 0.8
  }, callback) {
    this.chooseImageCallback = callback;
    this.fileInput.click();
  }
  //文件选择事件
  onFileChange(e) {
    var me = this;
    var file = e.target.files[0];
    if (file === undefined) {
      return;
    }
    var ext = e.target.value.match(/\.(png|jpg|jpeg|gif)$/i)[1];
    this.fileInfo = {
      ext: ext,
      type: file.type,
      name: file.name,
      size: file.size
    }


    me.opt.onFileSelect ? me.opt.onFileSelect(e, next) : next();

    function next(isClip) {
      //在iOS下，来自拍摄的图片当获取宽高时它们的值是反向的，而canvasResize有做标准化处理。
      //这一步android消耗2000ms左右、iOS消耗500ms左右
      isClip = isClip === undefined ? true : isClip;
      var max = Math.max(Math.max(me.opt.outputWidth, me.opt.outputHeight), 1000);
      canvasResize(file, {
        width: max,
        height: max,
        crop: false,
        quality: me.opt.quality,
        rotate: 0,
        callback: function (dataURL, width, height) {
          me.compress({
            dataURL: dataURL
          }, function (minData) {
            me.chooseImageCallback(minData);
          });
          me.fileInput.value = '';
        }
      });

    }
  }
  //压缩图片
  compress(data, cb) {
    var me = this;
    canvasResize(data, {
      fileType: me.fileInfo.type,
      width: me.opt.outputWidth,
      height: me.opt.outputHeight,
      crop: false,
      quality: 80,
      rotate: 0,
      callback: function (data, width, height) {
        cb(data);
      }
    });
  }
}
