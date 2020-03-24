<template>
    <div class="wx-picker-wrapper"  @touchstart="preventDefault">
        <div class="wx-picker" :style="pickerStyle" @touchstart="ontouchstart" @touchend="ontouchend" @touchmove="ontouchmove"> 
            <div class="wrapper" ref="wrapper" v-animation="animationData">
                <text 
                    v-for="(item, index) in data"
                    class="picker-item"
                    :style="itemStyle"
                    :class="[getSelectedClass(index)]">{{item}}</text>
            </div>
            <text class="picker-top"></text>
            <text class="picker-center"></text>
            <text class="picker-bottom"></text> 

        </div>
    </div>
</template>
<script type="text/javascript">
import cml from "chameleon-api"
const createAnimation = cml.createAnimation;
const getIndex = (list, item) => {
  if (list && list.length < 1) {
    return 0;
  }
  let index1 = list.findIndex(v => {
    return v === item || (v.name && v.name === item.name);
  });
  let index2 = list.indexOf(item);
  let index = Math.max(index1, index2);
  if (index < 0) {
    // throw new Error('list数组中不存在defaultValue');
    return 0;
  }
  return index;
};

export default {
  props: {
    data: {
      type: Array,
      default: []
    },
    defaultIndex: {
      type: Number,
      default: 0
    },
    wraperStyle: {
      type: String,
      default: ""
    },
    textAlign: {
      type: String,
      default: 'center'
    }
  },
  data: function() {
    return {
      startY: 0,
      endY: 0,
      currentY: 0,
      itemHeight: 72,
      selectedIndex: 0,
      _defaultValue: null,
      _startTime: 0,
      animationData: createAnimation(),
    };
  },
  computed: {
      pickerStyle() {
          return this.wraperStyle;
      },
      itemStyle() {
        let style = `text-align: ${this.textAlign}`
        return style;
      }
  },
  watch: {
    defaultIndex(newVal, oldVal) {
      this.selectedIndex = newVal;
    }
  },
  created() {
    this.selectedIndex = this.defaultIndex;
  },

  mounted() {
    this.initMove();
  },

  methods: {
    preventDefault(e) {
      e.preventDefault && e.preventDefault();
      e.stopPropagation && e.stopPropagation();
    },

    initMove() {
      this.currentY = 0;
      
      if (this.selectedIndex > 2) {
        this.currentY = -(this.selectedIndex - 2) * this.itemHeight;
      } else {
        this.currentY = (2 - this.selectedIndex) * this.itemHeight;
      }
      this.move(this.currentY, true);
    },

    getSelectedClass(index) {
      if (this.selectedIndex === index) {
        return "picker-item-selected";
      }
      return "";
    },

    ontouchstart(e) {
      this.preventDefault(e);
      if (this.data.length <= 1) {
        return;
      }
      this.startY = e.changedTouches[0].screenY;
      this._startTime = new Date().getTime();
    },

    ontouchmove(e) {
      this.preventDefault(e);
      if (this.data.length <= 1) {
        return;
      }
      const pageY = e.changedTouches[0].screenY;
      let value = parseInt(pageY - this.startY);
      const y = this.currentY + value;
      this.move(y);
    },

    ontouchend(e) {
      this.preventDefault(e);
      if (this.data.length <= 1) {
        return;
      }
      this.endY = e.changedTouches[0].screenY;
      // 实际滚动距离
      let v = parseInt(this.endY - this.startY);
      // 如果快速滑动，实际滑动距离放大5倍
      const endTime = new Date().getTime();
      if (endTime - this._startTime < 200) {
        v = v * 5;
      }
      let value = v % this.itemHeight;
      // 计算出每次拖动的36cpx整倍数
      this.currentY += v - value;

      // 正数y最大值
      const max1 = 2 * this.itemHeight;
      // 负数y最小值
      const max2 = (this.data.length - 3) * this.itemHeight;

      if (this.currentY > max1) {
        this.currentY = max1;
      } else if (this.currentY > 0 && this.currentY < max1) {
        this.currentY = this.currentY;
      } else if (this.currentY === max1) {
        this.currentY = this.currentY;
      } else if (Math.abs(this.currentY) > max2) {
        this.currentY = -max2;
      }

      this.countListIndex(this.currentY);
      this.move(this.currentY, true);
    },

    // 计算list数组索引
    countListIndex(pageY) {
      let n = pageY / this.itemHeight;
      n = n > 0 ? 2 - n : Math.abs(n) + 2;
      this.setSelectedValue(n);
    },

    // set选中值
    setSelectedValue(index) {
      const length = this.data.length;
      if (length === 0) {
        this.callback(null);
        return;
      }
      if (index < 0 || index > length - 1) {
        throw new Error("滑动取值索引数值出现错误" + index);
      }
      this.selectedIndex = index;

      this.callback(index);
    },

    // 回调wxChange
    callback(index) {
      this.$cmlEmit("selectchange", {index});
    },

    move(y, hasAnimate = false) {
      let stepObj = hasAnimate? {
        delay: 0,
        duration: 300,
        timingFunction: "ease-out"
      } : {
        delay: 0,
        duration: 0
      }
      this.animationData = createAnimation()
        .translateY(y)
        .step(stepObj)
        .export()
    }
  },
  watch: {
    "data.list"() {
      this.selectedIndex = this.defaultIndex;
      this.initMove();
    }
  }
};
</script>


<style scoped>
.wx-picker-wrapper {
  overflow: hidden;
  background-color: #fff;
}
.wx-picker {
  position: relative;
  overflow: hidden;
  background-color: #fff;
  height: 432cpx;
}
.wrapper {
  display: flex;
  overflow: hidden;
  flex-direction: column;
  background-color: #fff;
  margin-top: 32cpx;
}

.picker-item {
  flex: 1;
  text-align: center;
line-height: 72cpx;
  background-color: #fff;
  height: 72cpx;
  color: #999;
  font-size: 32cpx;
}
.picker-item-selected {
  color: #000;
}
.picker-top{
  width: 750cpx;
  height: 175cpx;
  background-image: linear-gradient(to top, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.8));
  position:absolute;
  top:0;
  left:0;
  z-index:100;
  justify-content: center;
  align-items: center;
}
.picker-bottom{
  width: 750cpx;
  height: 151cpx;
  background-image: linear-gradient(to bottom, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.8));
  position:absolute;
  bottom: 0;
  left:0;
  z-index:100;
  justify-content: center;
  align-items: center;
}
.picker-center {
  width: 750cpx;
  height: 72cpx;
  border-top-width: 1px;
  border-top-style: solid;
  border-top-color: #dcdcdc;
  border-bottom-width: 1px;
  border-bottom-style: solid;
  border-bottom-color: #dcdcdc;
  position: absolute;
  top: 176cpx;
  left: 0;
  z-index: 100;
  width: 750cpx;
  justify-content: center;
  align-items: center;
}
</style>