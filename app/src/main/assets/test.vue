<template>
  <div>
    <gcanvas ref="canvas_holder" style="width:750;height:750;background-color:rgba(255,0,0,1)"></gcanvas>
  </div>
</template>
<style>
.text {
  font-size: 20;
}
</style>
<script>
import { enable, WeexBridge, Image as GImage } from "../src/index.js";

export default {
  mounted: function() {
    var gcanvas = enable(this.$refs.canvas_holder, {bridge: WeexBridge});
    var ctx = gcanvas.getContext("2d");

    ctx.fillStyle = 'red';
    ctx.fillRect(0, 0, 100, 100);

    ctx.fillStyle = 'black';
    ctx.fillRect(100, 100, 100, 100);
    ctx.fillRect(25, 210, 700, 5);

    ctx.arc(450, 200, 100, 0, Math.PI * 2, true);
    ctx.fill();

    var image = new GImage();
    image.src = 'https://gw.alicdn.com/tfs/TB1KwRTlh6I8KJjy0FgXXXXzVXa-225-75.png';
    image.onload = function(){
      ctx.drawImage(image, 100, 300);
    }
  }
}

</script>