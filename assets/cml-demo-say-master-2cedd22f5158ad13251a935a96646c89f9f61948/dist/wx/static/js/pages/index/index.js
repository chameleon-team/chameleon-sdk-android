var __CML__GLOBAL = require("../../manifest.js");
__CML__GLOBAL.webpackJsonp([1],{

/***/ "../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/babel-loader/lib/index.js?{\"filename\":\"/Users/didi/.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/chameleon.js\"}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-loader/src/selector.js?type=script&index=0&fileType=page&media=dev&cmlType=wx&check={\"enable\":true,\"enableTypes\":[]}!./src/pages/index/index.cml":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _index = __webpack_require__("./node_modules/chameleon-api/src/interfaces/getSystemInfo/index.js");

var _index2 = _interopRequireDefault(_index);

var _stringify = __webpack_require__("./node_modules/babel-runtime/core-js/json/stringify.js");

var _stringify2 = _interopRequireDefault(_stringify);

var _index3 = __webpack_require__("./node_modules/chameleon-api/src/interfaces/showToast/index.js");

var _index4 = _interopRequireDefault(_index3);

var _classCallCheck2 = __webpack_require__("./node_modules/babel-runtime/helpers/classCallCheck.js");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("./node_modules/babel-runtime/helpers/createClass.js");

var _createClass3 = _interopRequireDefault(_createClass2);

var _chameleonBridge = __webpack_require__("./node_modules/chameleon-bridge/index.js");

var _chameleonBridge2 = _interopRequireDefault(_chameleonBridge);

var _chameleonRuntime = __webpack_require__("./node_modules/chameleon-runtime/index.js");

var _chameleonRuntime2 = _interopRequireDefault(_chameleonRuntime);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var Index = function () {
  function Index() {
    (0, _classCallCheck3.default)(this, Index);
    this.data = {
      title: "chameleon",
      winHeight: 0,
      chameleonSrc: __webpack_require__("./src/assets/images/chameleon.png")
    };
    this.computed = {};
    this.watch = {};
    this.methods = {
      // 此处的方法实现你可以封装到其他目录，作为统一扩展的api
      sayHello: function sayHello() {
        _chameleonBridge2.default.callNative('moduleDemo', // 模块名
        'sayHello', // 方法名
        { content: 'Hello Chameleon!' }, // 参数
        function (res) {} // 回调方法
        );
      }
    };
  }

  (0, _createClass3.default)(Index, [{
    key: "beforeCreate",
    value: function beforeCreate(res) {}
  }, {
    key: "created",
    value: function created(res) {}
  }, {
    key: "beforeMount",
    value: function beforeMount(res) {}
  }, {
    key: "mounted",
    value: function mounted() {
      var _this = this;

      // 主动监听客户端调用js
      _chameleonBridge2.default.listenNative('moduleDemo', // 模块名
      'NaTellJS', // 方法名
      function (res) {
        (0, _index4.default)({
          message: (0, _stringify2.default)(res),
          duration: 2000
        });
      });

      (0, _index2.default)().then(function (info) {
        _this.winHeight = Number(info.viewportHeight);
      });
    }
  }, {
    key: "beforeDestroy",
    value: function beforeDestroy(res) {}
  }, {
    key: "destroyed",
    value: function destroyed(res) {}
  }]);
  return Index;
}();

exports.default = new Index();


exports.default = _chameleonRuntime2.default.createPage(exports.default).getOptions();

/***/ }),

/***/ "../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/extract-text-webpack-plugin/dist/loader.js?{\"omit\":1,\"remove\":true}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/vue-style-loader/index.js!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/css-loader/index.js?{\"sourceMap\":false}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-css-loader/index.js?{\"platform\":\"miniapp\"}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/postcss-loader/lib/index.js?{\"sourceMap\":false,\"config\":{\"path\":\"/Users/didi/.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/configs/postcss/wx/.postcssrc.js\"}}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/less-loader/dist/cjs.js?{\"sourceMap\":false}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-css-loader/index.js?{\"media\":true,\"cmlType\":\"wx\"}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-loader/src/selector.js?type=styles&index=0&fileType=page&media=dev&cmlType=wx&check={\"enable\":true,\"enableTypes\":[]}!./src/pages/index/index.cml":
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),

/***/ "../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/process/browser.js":
/***/ (function(module, exports) {

// shim for using process in browser
var process = module.exports = {};

// cached from whatever global is present so that test runners that stub it
// don't break things.  But we need to wrap it in a try catch in case it is
// wrapped in strict mode code which doesn't define any globals.  It's inside a
// function because try/catches deoptimize in certain engines.

var cachedSetTimeout;
var cachedClearTimeout;

function defaultSetTimout() {
    throw new Error('setTimeout has not been defined');
}
function defaultClearTimeout () {
    throw new Error('clearTimeout has not been defined');
}
(function () {
    try {
        if (typeof setTimeout === 'function') {
            cachedSetTimeout = setTimeout;
        } else {
            cachedSetTimeout = defaultSetTimout;
        }
    } catch (e) {
        cachedSetTimeout = defaultSetTimout;
    }
    try {
        if (typeof clearTimeout === 'function') {
            cachedClearTimeout = clearTimeout;
        } else {
            cachedClearTimeout = defaultClearTimeout;
        }
    } catch (e) {
        cachedClearTimeout = defaultClearTimeout;
    }
} ())
function runTimeout(fun) {
    if (cachedSetTimeout === setTimeout) {
        //normal enviroments in sane situations
        return setTimeout(fun, 0);
    }
    // if setTimeout wasn't available but was latter defined
    if ((cachedSetTimeout === defaultSetTimout || !cachedSetTimeout) && setTimeout) {
        cachedSetTimeout = setTimeout;
        return setTimeout(fun, 0);
    }
    try {
        // when when somebody has screwed with setTimeout but no I.E. maddness
        return cachedSetTimeout(fun, 0);
    } catch(e){
        try {
            // When we are in I.E. but the script has been evaled so I.E. doesn't trust the global object when called normally
            return cachedSetTimeout.call(null, fun, 0);
        } catch(e){
            // same as above but when it's a version of I.E. that must have the global object for 'this', hopfully our context correct otherwise it will throw a global error
            return cachedSetTimeout.call(this, fun, 0);
        }
    }


}
function runClearTimeout(marker) {
    if (cachedClearTimeout === clearTimeout) {
        //normal enviroments in sane situations
        return clearTimeout(marker);
    }
    // if clearTimeout wasn't available but was latter defined
    if ((cachedClearTimeout === defaultClearTimeout || !cachedClearTimeout) && clearTimeout) {
        cachedClearTimeout = clearTimeout;
        return clearTimeout(marker);
    }
    try {
        // when when somebody has screwed with setTimeout but no I.E. maddness
        return cachedClearTimeout(marker);
    } catch (e){
        try {
            // When we are in I.E. but the script has been evaled so I.E. doesn't  trust the global object when called normally
            return cachedClearTimeout.call(null, marker);
        } catch (e){
            // same as above but when it's a version of I.E. that must have the global object for 'this', hopfully our context correct otherwise it will throw a global error.
            // Some versions of I.E. have different rules for clearTimeout vs setTimeout
            return cachedClearTimeout.call(this, marker);
        }
    }



}
var queue = [];
var draining = false;
var currentQueue;
var queueIndex = -1;

function cleanUpNextTick() {
    if (!draining || !currentQueue) {
        return;
    }
    draining = false;
    if (currentQueue.length) {
        queue = currentQueue.concat(queue);
    } else {
        queueIndex = -1;
    }
    if (queue.length) {
        drainQueue();
    }
}

function drainQueue() {
    if (draining) {
        return;
    }
    var timeout = runTimeout(cleanUpNextTick);
    draining = true;

    var len = queue.length;
    while(len) {
        currentQueue = queue;
        queue = [];
        while (++queueIndex < len) {
            if (currentQueue) {
                currentQueue[queueIndex].run();
            }
        }
        queueIndex = -1;
        len = queue.length;
    }
    currentQueue = null;
    draining = false;
    runClearTimeout(timeout);
}

process.nextTick = function (fun) {
    var args = new Array(arguments.length - 1);
    if (arguments.length > 1) {
        for (var i = 1; i < arguments.length; i++) {
            args[i - 1] = arguments[i];
        }
    }
    queue.push(new Item(fun, args));
    if (queue.length === 1 && !draining) {
        runTimeout(drainQueue);
    }
};

// v8 likes predictible objects
function Item(fun, array) {
    this.fun = fun;
    this.array = array;
}
Item.prototype.run = function () {
    this.fun.apply(null, this.array);
};
process.title = 'browser';
process.browser = true;
process.env = {};
process.argv = [];
process.version = ''; // empty string to avoid regexp issues
process.versions = {};

function noop() {}

process.on = noop;
process.addListener = noop;
process.once = noop;
process.off = noop;
process.removeListener = noop;
process.removeAllListeners = noop;
process.emit = noop;
process.prependListener = noop;
process.prependOnceListener = noop;

process.listeners = function (name) { return [] }

process.binding = function (name) {
    throw new Error('process.binding is not supported');
};

process.cwd = function () { return '/' };
process.chdir = function (dir) {
    throw new Error('process.chdir is not supported');
};
process.umask = function() { return 0; };


/***/ }),

/***/ "../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/webpack/buildin/module.js":
/***/ (function(module, exports) {

module.exports = function(module) {
	if(!module.webpackPolyfill) {
		module.deprecate = function() {};
		module.paths = [];
		// module.parent = undefined by default
		if(!module.children) module.children = [];
		Object.defineProperty(module, "loaded", {
			enumerable: true,
			get: function() {
				return module.l;
			}
		});
		Object.defineProperty(module, "id", {
			enumerable: true,
			get: function() {
				return module.i;
			}
		});
		module.webpackPolyfill = 1;
	}
	return module;
};


/***/ }),

/***/ "./node_modules/babel-runtime/core-js/json/stringify.js":
/***/ (function(module, exports, __webpack_require__) {

module.exports = { "default": __webpack_require__("./node_modules/core-js/library/fn/json/stringify.js"), __esModule: true };

/***/ }),

/***/ "./node_modules/chameleon-api/src/interfaces/showToast/index.interface":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _classCallCheck2 = __webpack_require__("./node_modules/babel-runtime/helpers/classCallCheck.js");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("./node_modules/babel-runtime/helpers/createClass.js");

var _createClass3 = _interopRequireDefault(_createClass2);

var _getPrototypeOf = __webpack_require__("./node_modules/babel-runtime/core-js/object/get-prototype-of.js");

var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

var _promise = __webpack_require__("./node_modules/babel-runtime/core-js/promise.js");

var _promise2 = _interopRequireDefault(_promise);

var _assign = __webpack_require__("./node_modules/babel-runtime/core-js/object/assign.js");

var _assign2 = _interopRequireDefault(_assign);

var _keys2 = __webpack_require__("./node_modules/babel-runtime/core-js/object/keys.js");

var _keys3 = _interopRequireDefault(_keys2);

var _util = __webpack_require__("../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-loader/src/cml-compile/runtime/common/util.js");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var __INTERFACE__FILEPATH = "/Users/didi/cml-demo-say/node_modules/chameleon-api/src/interfaces/showToast/index.interface";
var __CML_ERROR__ = function throwError(content) {
  throw new Error("\u6587\u4EF6\u4F4D\u7F6E: " + __INTERFACE__FILEPATH + "\n            " + content);
};

var __enableTypes__ = "";
var __CHECK__DEFINES__ = {
  "types": {
    "toastOpt": {
      "message": "String",
      "duration": "Number"
    }
  },
  "interfaces": {
    "uiInterface": {
      "showToast": {
        "input": ["toastOpt"],
        "output": "Undefined"
      }
    }
  },
  "classes": {
    "Method": ["uiInterface"]
  }
};
var __OBJECT__WRAPPER__ = function __OBJECT__WRAPPER__(obj) {
  var className = obj.constructor.name;
  /* eslint-disable no-undef */
  var defines = __CHECK__DEFINES__;
  var enableTypes = __enableTypes__.split(',') || []; // ['Object','Array','Nullable']
  /* eslint-disable no-undef */
  var types = defines.types;
  var interfaceNames = defines.classes[className];
  var methods = {};

  interfaceNames && interfaceNames.forEach(function (interfaceName) {
    var keys = (0, _keys3.default)(defines.interfaces);
    keys.forEach(function (key) {
      (0, _assign2.default)(methods, defines.interfaces[key]);
    });
  });

  /**
   * 获取类型
   *
   * @param  {*}      value 值
   * @return {string}       类型
   */
  var getType = function getType(value) {
    if (value instanceof _promise2.default) {
      return "Promise";
    }
    var type = Object.prototype.toString.call(value);
    return type.replace(/\[object\s(.*)\]/g, '$1').replace(/( |^)[a-z]/g, function (L) {
      return L.toUpperCase();
    });
  };

  /**
   * 校验类型  两个loader共用代码
   *
   * @param  {*}      value 实际传入的值
   * @param  {string} type  静态分析时候得到的值得类型
   * @param  {array[string]} errList 校验错误信息  类型
   * @return {bool}         校验结果
   */

  /* eslint complexity:[2,39] */
  var checkType = function checkType(value, originType) {
    var errList = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : [];

    var isNullableReg = /_cml_nullable_lmc_/g;
    var type = originType.replace('_cml_nullable_lmc_', '');
    type === "Void" && (type = "Undefined");
    var currentType = getType(value);
    var canUseNullable = enableTypes.includes("Nullable");
    var canUseObject = enableTypes.includes("Object");
    if (currentType == 'Null') {
      if (type == "Null") {
        // 如果定义的参数的值就是 Null，那么校验通过
        errList = [];
      } else {
        // 那么判断是否是可选参数的情况
        canUseNullable && isNullableReg.test(originType) ? errList = [] : errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542Fnullable\u914D\u7F6E");
      }
      return errList;
    }
    if (currentType == 'Undefined') {
      // 如果运行时传入的真实值是undefined,那么可能改值在接口处就是被定义为 Undefined类型或者是 ?string 这种可选参数 nullable的情况；
      if (type == "Undefined") {
        errList = [];
      } else {
        canUseNullable && isNullableReg.test(originType) ? errList = [] : errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542Fnullable\u914D\u7F6E\u6216\u8005\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'String') {
      if (type == 'String') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Boolean') {
      if (type == 'Boolean') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Number') {
      if (type == 'Number') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Object') {
      if (type == 'Object') {
        !canUseObject ? errList.push("\u4E0D\u80FD\u76F4\u63A5\u5B9A\u4E49\u7C7B\u578B" + type + "\uFF0C\u9700\u8981\u4F7F\u7528\u7B26\u5408\u7C7B\u578B\u5B9A\u4E49\uFF0C\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542F\u4E86\u53EF\u4EE5\u76F4\u63A5\u5B9A\u4E49 Object \u7C7B\u578B\u53C2\u6570\uFF1B") : errList = [];
      } else if (type == 'CMLObject') {
        errList = [];
      } else {
        // 这种情况的对象就是自定义的对象；
        if (types[type]) {
          var _keys = (0, _keys3.default)(types[type]);
          // todo 这里是同样的问题，可能多传递
          _keys.forEach(function (key) {
            var subError = checkType(value[key], types[type][key], []);
            if (subError && subError.length) {
              errList = errList.concat(subError);
            }
          });
          if ((0, _keys3.default)(value).length > _keys.length) {
            errList.push("type [" + type + "] \u53C2\u6570\u4E2A\u6570\u4E0E\u5B9A\u4E49\u4E0D\u7B26");
          }
        } else {
          errList.push('找不到定义的type [' + type + ']!');
        }
      }
      return errList;
    }
    if (currentType == 'Array') {
      if (type == 'Array') {
        !canUseObject ? errList.push("\u4E0D\u80FD\u76F4\u63A5\u5B9A\u4E49\u7C7B\u578B" + type + "\uFF0C\u9700\u8981\u4F7F\u7528\u7B26\u5408\u7C7B\u578B\u5B9A\u4E49\uFF0C\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542F\u4E86\u53EF\u4EE5\u76F4\u63A5\u5B9A\u4E49 Array \u7C7B\u578B\u53C2\u6570\uFF1B") : errList = [];
      } else {
        if (types[type]) {
          // 数组元素的类型
          var itemType = types[type][0];
          for (var i = 0; i < value.length; i++) {
            var subError = checkType(value[i], itemType, []);
            if (subError && subError.length) {
              errList = errList.concat(subError);
            }
          }
        } else {
          errList.push('找不到定义的type [' + type + ']!');
        }
      }

      return errList;
    }
    if (currentType == 'Function') {
      // if (type == 'Function') {
      //   errList = [];
      // } else {
      //   errList.push(`定义了${type}类型的参数，传入的却是${currentType},请检查所传参数是否和接口定义的一致`)
      // }
      if (types[type]) {
        if (!types[type].input && !types[type].output) {
          errList.push("\u627E\u4E0D\u5230" + types[type] + " \u51FD\u6570\u5B9A\u4E49\u7684\u8F93\u5165\u8F93\u51FA");
        }
      } else {
        errList.push('找不到定义的type [' + type + ']!');
      }
      return errList;
    }
    if (currentType == 'Promise') {
      if (type == 'Promise') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Date') {
      if (type == 'Date') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'RegExp') {
      if (type == 'RegExp') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }

    return errList;
  };

  /**
   * 校验参数类型
   *
   * @param  {string} methodName 方法名称
   * @param  {Array}  argNames   参数名称列表
   * @param  {Array}  argValues  参数值列表
   * @return {bool}              校验结果
   */
  var checkArgsType = function checkArgsType(methodName, argValues) {
    var argList = void 0;

    if (getType(methodName) == 'Array') {
      // 回调函数的校验    methodName[0] 方法的名字 methodName[1]该回调函数在方法的参数索引
      argList = types[methods[methodName[0]].input[methodName[1]]].input;
      // 拿到这个回调函数的参数定义
    } else {
      argList = methods[methodName].input;
    }
    // todo 函数可能多传参数
    argList.forEach(function (argType, index) {
      var errList = checkType(argValues[index], argType, []);
      if (errList && errList.length > 0) {
        __CML_ERROR__("\n        \u6821\u9A8C\u4F4D\u7F6E: \u65B9\u6CD5" + methodName + "\u7B2C" + (index + 1) + "\u4E2A\u53C2\u6570\n        \u9519\u8BEF\u4FE1\u606F: " + errList.join('\n'));
      }
    });
    if (argValues.length > argList.length) {
      __CML_ERROR__("[" + methodName + "]\u65B9\u6CD5\u53C2\u6570\u4F20\u9012\u4E2A\u6570\u4E0E\u5B9A\u4E49\u4E0D\u7B26");
    }
  };

  /**
   * 校验返回值类型
   *
   * @param  {string} methodName 方法名称
   * @param  {*}      returnData 返回值
   * @return {bool}              校验结果
   */
  var checkReturnType = function checkReturnType(methodName, returnData) {
    var output = void 0;
    if (getType(methodName) == 'Array') {
      output = types[methods[methodName[0]].input[methodName[1]]].output;
    } else {
      output = methods[methodName].output;
    }
    // todo output 为什么可以是数组
    // if (output instanceof Array) {
    //   output.forEach(type => {

    //     //todo 而且是要有一个校验不符合就check失败？ 应该是有一个校验通过就可以吧
    //     checkType(returnData, type,[])
    //   });
    // }
    var errList = checkType(returnData, output, []);
    if (errList && errList.length > 0) {
      __CML_ERROR__("\n      \u6821\u9A8C\u4F4D\u7F6E: \u65B9\u6CD5" + methodName + "\u8FD4\u56DE\u503C\n      \u9519\u8BEF\u4FE1\u606F: " + errList.join('\n'));
    }
  };

  /**
   * 创建warpper
   *
   * @param  {string}   funcName   方法名称
   * @param  {Function} originFunc 原有方法
   * @return {Function}            包裹后的方法
   */
  var createWarpper = function createWarpper(funcName, originFunc) {
    return function () {
      var argValues = Array.prototype.slice.call(arguments).map(function (arg, index) {
        // 对传入的方法要做特殊的处理，这个是传入的callback，对callback函数再做包装
        if (getType(arg) == 'Function') {
          return createWarpper([funcName, index], arg);
        }
        return arg;
      });

      checkArgsType(funcName, argValues);

      var result = originFunc.apply(this, argValues);

      checkReturnType(funcName, result);
      return result;
    };
  };

  // 获取所有方法
  var keys = (0, _keys3.default)(methods);

  // 处理包装方法
  keys.forEach(function (key) {
    var originFunc = obj[key];
    if (!originFunc) {
      __CML_ERROR__('method [' + key + '] not found!');
      return;
    }

    if (obj.hasOwnProperty(key)) {
      obj[key] = createWarpper(key, originFunc);
    } else {
      (0, _getPrototypeOf2.default)(obj)[key] = createWarpper(key, originFunc);
    }
  });

  return obj;
};

var Method = function () {
  function Method() {
    (0, _classCallCheck3.default)(this, Method);
  }

  (0, _createClass3.default)(Method, [{
    key: "showToast",
    value: function showToast(opt) {
      var message = opt.message,
          duration = opt.duration;

      wx.showToast({
        icon: 'none',
        title: message,
        duration: duration
      });
    }
  }]);
  return Method;
}();

exports.default = __OBJECT__WRAPPER__(new Method());

(0, _util.copyProtoProperty)(exports.default);

/***/ }),

/***/ "./node_modules/chameleon-api/src/interfaces/showToast/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = showToast;

var _index = __webpack_require__("./node_modules/chameleon-api/src/interfaces/showToast/index.interface");

var _index2 = _interopRequireDefault(_index);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function showToast(opt) {
  var _opt$message = opt.message,
      message = _opt$message === undefined ? '' : _opt$message,
      _opt$duration = opt.duration,
      duration = _opt$duration === undefined ? 2000 : _opt$duration;

  _index2.default.showToast({
    message: message,
    duration: duration
  });
}

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/canIUse/index.js":
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(module) {Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.canIUse = canIUse;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var cModuleName = 'cml';
var cMethodName = 'canIUse';

function canIUse(_ref, cb) {
  var method = _ref.method;

  var moduleName = this[method] && this[method].prototype.moduleName;
  var methodName = this[method] && this[method].prototype.methodName;

  if (module && method) {
    (0, _index.callNative)(cModuleName, cMethodName, {
      module: moduleName,
      method: methodName
    }, cb);
  } else {
    this[method] ? cb(true) : cb(false);
  }
}

canIUse.prototype.moduleName = cModuleName;
canIUse.prototype.methodName = cMethodName;
/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__("../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/webpack/buildin/module.js")(module)))

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/chooseImage/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.chooseImage = chooseImage;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'cml';
var methodName = 'chooseImage';

function chooseImage(param, cb) {
  /**
   * type: camera/album/choice
   * cut: true/false
   * quality: 默认100 [0-100]
   * width: 0
   * height: 0
   */
  (0, _index.callNative)(moduleName, methodName, param, function (res) {
    if (res.errno === 0 && res.data.type) {
      var data = res.data || {};
      var base64 = 'data:image/' + data.type + ';base64,' + data.image;
      res.data.base64 = base64;
    } else {
      res.data = res.data || {};
    }
    /**
    * errno: 0成功，1失败，2取消，3没权限
    * msg
    * data: {
    *   type,
    *   image,
    *   base64
    * }
    */
    cb(res);
  });
}

chooseImage.prototype.moduleName = moduleName;
chooseImage.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/clipboard/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getClipBoardData = getClipBoardData;
exports.setClipBoardData = setClipBoardData;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'clipboard';
var methodGet = 'getClipBoardData';
var methodSet = 'setClipBoardData';

function getClipBoardData() {
  var param = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  var cb = arguments[1];

  /**
   * params {}
   */
  (0, _index.callNative)(moduleName, methodGet, param, function (res) {
    /**
    * errno
    * msg
    * data
    */
    cb(res);
  });
}

getClipBoardData.prototype.moduleName = moduleName;
getClipBoardData.prototype.methodName = methodGet;

function setClipBoardData(_ref, cb) {
  var _ref$data = _ref.data,
      data = _ref$data === undefined ? '' : _ref$data;

  (0, _index.callNative)(moduleName, methodSet, {
    data: data
  }, function (res) {
    /**
    * errno
    * msg
    * data
    */
    cb(res);
  });
}

setClipBoardData.prototype.moduleName = moduleName;
setClipBoardData.prototype.methodName = methodSet;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/close/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.close = close;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'cml';
var methodName = 'closePage';

function close() {
  (0, _index.callNative)(moduleName, methodName, {}, function () {});
}

close.prototype.moduleName = moduleName;
close.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/getComponentRect/index.interface":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _classCallCheck2 = __webpack_require__("./node_modules/babel-runtime/helpers/classCallCheck.js");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("./node_modules/babel-runtime/helpers/createClass.js");

var _createClass3 = _interopRequireDefault(_createClass2);

var _getPrototypeOf = __webpack_require__("./node_modules/babel-runtime/core-js/object/get-prototype-of.js");

var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

var _promise = __webpack_require__("./node_modules/babel-runtime/core-js/promise.js");

var _promise2 = _interopRequireDefault(_promise);

var _assign = __webpack_require__("./node_modules/babel-runtime/core-js/object/assign.js");

var _assign2 = _interopRequireDefault(_assign);

var _keys2 = __webpack_require__("./node_modules/babel-runtime/core-js/object/keys.js");

var _keys3 = _interopRequireDefault(_keys2);

var _util = __webpack_require__("../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-loader/src/cml-compile/runtime/common/util.js");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var __INTERFACE__FILEPATH = "/Users/didi/cml-demo-say/node_modules/chameleon-bridge/apis/getComponentRect/index.interface";
var __CML_ERROR__ = function throwError(content) {
  throw new Error("\u6587\u4EF6\u4F4D\u7F6E: " + __INTERFACE__FILEPATH + "\n            " + content);
};

var __enableTypes__ = "";
var __CHECK__DEFINES__ = {
  "types": {
    "paramType": {
      "ref": "CMLObject"
    },
    "callback": {
      "input": ["CMLObject"],
      "output": "Void"
    }
  },
  "interfaces": {
    "DomInterface": {
      "getComponentRect": {
        "input": ["paramType", "callback"],
        "output": "Void"
      }
    }
  },
  "classes": {
    "Method": ["DomInterface"]
  }
};
var __OBJECT__WRAPPER__ = function __OBJECT__WRAPPER__(obj) {
  var className = obj.constructor.name;
  /* eslint-disable no-undef */
  var defines = __CHECK__DEFINES__;
  var enableTypes = __enableTypes__.split(',') || []; // ['Object','Array','Nullable']
  /* eslint-disable no-undef */
  var types = defines.types;
  var interfaceNames = defines.classes[className];
  var methods = {};

  interfaceNames && interfaceNames.forEach(function (interfaceName) {
    var keys = (0, _keys3.default)(defines.interfaces);
    keys.forEach(function (key) {
      (0, _assign2.default)(methods, defines.interfaces[key]);
    });
  });

  /**
   * 获取类型
   *
   * @param  {*}      value 值
   * @return {string}       类型
   */
  var getType = function getType(value) {
    if (value instanceof _promise2.default) {
      return "Promise";
    }
    var type = Object.prototype.toString.call(value);
    return type.replace(/\[object\s(.*)\]/g, '$1').replace(/( |^)[a-z]/g, function (L) {
      return L.toUpperCase();
    });
  };

  /**
   * 校验类型  两个loader共用代码
   *
   * @param  {*}      value 实际传入的值
   * @param  {string} type  静态分析时候得到的值得类型
   * @param  {array[string]} errList 校验错误信息  类型
   * @return {bool}         校验结果
   */

  /* eslint complexity:[2,39] */
  var checkType = function checkType(value, originType) {
    var errList = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : [];

    var isNullableReg = /_cml_nullable_lmc_/g;
    var type = originType.replace('_cml_nullable_lmc_', '');
    type === "Void" && (type = "Undefined");
    var currentType = getType(value);
    var canUseNullable = enableTypes.includes("Nullable");
    var canUseObject = enableTypes.includes("Object");
    if (currentType == 'Null') {
      if (type == "Null") {
        // 如果定义的参数的值就是 Null，那么校验通过
        errList = [];
      } else {
        // 那么判断是否是可选参数的情况
        canUseNullable && isNullableReg.test(originType) ? errList = [] : errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542Fnullable\u914D\u7F6E");
      }
      return errList;
    }
    if (currentType == 'Undefined') {
      // 如果运行时传入的真实值是undefined,那么可能改值在接口处就是被定义为 Undefined类型或者是 ?string 这种可选参数 nullable的情况；
      if (type == "Undefined") {
        errList = [];
      } else {
        canUseNullable && isNullableReg.test(originType) ? errList = [] : errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542Fnullable\u914D\u7F6E\u6216\u8005\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'String') {
      if (type == 'String') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Boolean') {
      if (type == 'Boolean') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Number') {
      if (type == 'Number') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Object') {
      if (type == 'Object') {
        !canUseObject ? errList.push("\u4E0D\u80FD\u76F4\u63A5\u5B9A\u4E49\u7C7B\u578B" + type + "\uFF0C\u9700\u8981\u4F7F\u7528\u7B26\u5408\u7C7B\u578B\u5B9A\u4E49\uFF0C\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542F\u4E86\u53EF\u4EE5\u76F4\u63A5\u5B9A\u4E49 Object \u7C7B\u578B\u53C2\u6570\uFF1B") : errList = [];
      } else if (type == 'CMLObject') {
        errList = [];
      } else {
        // 这种情况的对象就是自定义的对象；
        if (types[type]) {
          var _keys = (0, _keys3.default)(types[type]);
          // todo 这里是同样的问题，可能多传递
          _keys.forEach(function (key) {
            var subError = checkType(value[key], types[type][key], []);
            if (subError && subError.length) {
              errList = errList.concat(subError);
            }
          });
          if ((0, _keys3.default)(value).length > _keys.length) {
            errList.push("type [" + type + "] \u53C2\u6570\u4E2A\u6570\u4E0E\u5B9A\u4E49\u4E0D\u7B26");
          }
        } else {
          errList.push('找不到定义的type [' + type + ']!');
        }
      }
      return errList;
    }
    if (currentType == 'Array') {
      if (type == 'Array') {
        !canUseObject ? errList.push("\u4E0D\u80FD\u76F4\u63A5\u5B9A\u4E49\u7C7B\u578B" + type + "\uFF0C\u9700\u8981\u4F7F\u7528\u7B26\u5408\u7C7B\u578B\u5B9A\u4E49\uFF0C\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542F\u4E86\u53EF\u4EE5\u76F4\u63A5\u5B9A\u4E49 Array \u7C7B\u578B\u53C2\u6570\uFF1B") : errList = [];
      } else {
        if (types[type]) {
          // 数组元素的类型
          var itemType = types[type][0];
          for (var i = 0; i < value.length; i++) {
            var subError = checkType(value[i], itemType, []);
            if (subError && subError.length) {
              errList = errList.concat(subError);
            }
          }
        } else {
          errList.push('找不到定义的type [' + type + ']!');
        }
      }

      return errList;
    }
    if (currentType == 'Function') {
      // if (type == 'Function') {
      //   errList = [];
      // } else {
      //   errList.push(`定义了${type}类型的参数，传入的却是${currentType},请检查所传参数是否和接口定义的一致`)
      // }
      if (types[type]) {
        if (!types[type].input && !types[type].output) {
          errList.push("\u627E\u4E0D\u5230" + types[type] + " \u51FD\u6570\u5B9A\u4E49\u7684\u8F93\u5165\u8F93\u51FA");
        }
      } else {
        errList.push('找不到定义的type [' + type + ']!');
      }
      return errList;
    }
    if (currentType == 'Promise') {
      if (type == 'Promise') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Date') {
      if (type == 'Date') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'RegExp') {
      if (type == 'RegExp') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }

    return errList;
  };

  /**
   * 校验参数类型
   *
   * @param  {string} methodName 方法名称
   * @param  {Array}  argNames   参数名称列表
   * @param  {Array}  argValues  参数值列表
   * @return {bool}              校验结果
   */
  var checkArgsType = function checkArgsType(methodName, argValues) {
    var argList = void 0;

    if (getType(methodName) == 'Array') {
      // 回调函数的校验    methodName[0] 方法的名字 methodName[1]该回调函数在方法的参数索引
      argList = types[methods[methodName[0]].input[methodName[1]]].input;
      // 拿到这个回调函数的参数定义
    } else {
      argList = methods[methodName].input;
    }
    // todo 函数可能多传参数
    argList.forEach(function (argType, index) {
      var errList = checkType(argValues[index], argType, []);
      if (errList && errList.length > 0) {
        __CML_ERROR__("\n        \u6821\u9A8C\u4F4D\u7F6E: \u65B9\u6CD5" + methodName + "\u7B2C" + (index + 1) + "\u4E2A\u53C2\u6570\n        \u9519\u8BEF\u4FE1\u606F: " + errList.join('\n'));
      }
    });
    if (argValues.length > argList.length) {
      __CML_ERROR__("[" + methodName + "]\u65B9\u6CD5\u53C2\u6570\u4F20\u9012\u4E2A\u6570\u4E0E\u5B9A\u4E49\u4E0D\u7B26");
    }
  };

  /**
   * 校验返回值类型
   *
   * @param  {string} methodName 方法名称
   * @param  {*}      returnData 返回值
   * @return {bool}              校验结果
   */
  var checkReturnType = function checkReturnType(methodName, returnData) {
    var output = void 0;
    if (getType(methodName) == 'Array') {
      output = types[methods[methodName[0]].input[methodName[1]]].output;
    } else {
      output = methods[methodName].output;
    }
    // todo output 为什么可以是数组
    // if (output instanceof Array) {
    //   output.forEach(type => {

    //     //todo 而且是要有一个校验不符合就check失败？ 应该是有一个校验通过就可以吧
    //     checkType(returnData, type,[])
    //   });
    // }
    var errList = checkType(returnData, output, []);
    if (errList && errList.length > 0) {
      __CML_ERROR__("\n      \u6821\u9A8C\u4F4D\u7F6E: \u65B9\u6CD5" + methodName + "\u8FD4\u56DE\u503C\n      \u9519\u8BEF\u4FE1\u606F: " + errList.join('\n'));
    }
  };

  /**
   * 创建warpper
   *
   * @param  {string}   funcName   方法名称
   * @param  {Function} originFunc 原有方法
   * @return {Function}            包裹后的方法
   */
  var createWarpper = function createWarpper(funcName, originFunc) {
    return function () {
      var argValues = Array.prototype.slice.call(arguments).map(function (arg, index) {
        // 对传入的方法要做特殊的处理，这个是传入的callback，对callback函数再做包装
        if (getType(arg) == 'Function') {
          return createWarpper([funcName, index], arg);
        }
        return arg;
      });

      checkArgsType(funcName, argValues);

      var result = originFunc.apply(this, argValues);

      checkReturnType(funcName, result);
      return result;
    };
  };

  // 获取所有方法
  var keys = (0, _keys3.default)(methods);

  // 处理包装方法
  keys.forEach(function (key) {
    var originFunc = obj[key];
    if (!originFunc) {
      __CML_ERROR__('method [' + key + '] not found!');
      return;
    }

    if (obj.hasOwnProperty(key)) {
      obj[key] = createWarpper(key, originFunc);
    } else {
      (0, _getPrototypeOf2.default)(obj)[key] = createWarpper(key, originFunc);
    }
  });

  return obj;
};

var Method = function () {
  function Method() {
    (0, _classCallCheck3.default)(this, Method);
  }

  (0, _createClass3.default)(Method, [{
    key: "getComponentRect",
    value: function getComponentRect(param, cb) {}
  }]);
  return Method;
}();

exports.default = __OBJECT__WRAPPER__(new Method());

(0, _util.copyProtoProperty)(exports.default);

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/getComponentRect/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getComponentRect = getComponentRect;

var _index = __webpack_require__("./node_modules/chameleon-bridge/apis/getComponentRect/index.interface");

var _index2 = _interopRequireDefault(_index);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var moduleName = 'dom';
var methodName = 'getComponentRect';

function getComponentRect(module, method, args, callback) {
  _index2.default.getComponentRect.apply(_index2.default, arguments);
}

getComponentRect.prototype.moduleName = moduleName;
getComponentRect.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/getLaunchUrl/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getLaunchUrl = getLaunchUrl;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'cml';
var methodName = 'getLaunchUrl';

function getLaunchUrl(param, cb) {
  (0, _index.callNative)(moduleName, methodName, param, cb);
}

getLaunchUrl.prototype.moduleName = moduleName;
getLaunchUrl.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/getQueryObjSync/index.interface":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _classCallCheck2 = __webpack_require__("./node_modules/babel-runtime/helpers/classCallCheck.js");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("./node_modules/babel-runtime/helpers/createClass.js");

var _createClass3 = _interopRequireDefault(_createClass2);

var _getPrototypeOf = __webpack_require__("./node_modules/babel-runtime/core-js/object/get-prototype-of.js");

var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

var _promise = __webpack_require__("./node_modules/babel-runtime/core-js/promise.js");

var _promise2 = _interopRequireDefault(_promise);

var _assign = __webpack_require__("./node_modules/babel-runtime/core-js/object/assign.js");

var _assign2 = _interopRequireDefault(_assign);

var _keys2 = __webpack_require__("./node_modules/babel-runtime/core-js/object/keys.js");

var _keys3 = _interopRequireDefault(_keys2);

var _util = __webpack_require__("../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-loader/src/cml-compile/runtime/common/util.js");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var __INTERFACE__FILEPATH = "/Users/didi/cml-demo-say/node_modules/chameleon-bridge/apis/getQueryObjSync/index.interface";
var __CML_ERROR__ = function throwError(content) {
  throw new Error("\u6587\u4EF6\u4F4D\u7F6E: " + __INTERFACE__FILEPATH + "\n            " + content);
};

var __enableTypes__ = "";
var __CHECK__DEFINES__ = {
  "types": {
    "Func": {
      "input": ["CMLObject"],
      "output": "Void"
    }
  },
  "interfaces": {
    "QueryInterface": {
      "getQueryObjSync": {
        "input": [],
        "output": "CMLObject"
      }
    }
  },
  "classes": {
    "Method": ["QueryInterface"]
  }
};
var __OBJECT__WRAPPER__ = function __OBJECT__WRAPPER__(obj) {
  var className = obj.constructor.name;
  /* eslint-disable no-undef */
  var defines = __CHECK__DEFINES__;
  var enableTypes = __enableTypes__.split(',') || []; // ['Object','Array','Nullable']
  /* eslint-disable no-undef */
  var types = defines.types;
  var interfaceNames = defines.classes[className];
  var methods = {};

  interfaceNames && interfaceNames.forEach(function (interfaceName) {
    var keys = (0, _keys3.default)(defines.interfaces);
    keys.forEach(function (key) {
      (0, _assign2.default)(methods, defines.interfaces[key]);
    });
  });

  /**
   * 获取类型
   *
   * @param  {*}      value 值
   * @return {string}       类型
   */
  var getType = function getType(value) {
    if (value instanceof _promise2.default) {
      return "Promise";
    }
    var type = Object.prototype.toString.call(value);
    return type.replace(/\[object\s(.*)\]/g, '$1').replace(/( |^)[a-z]/g, function (L) {
      return L.toUpperCase();
    });
  };

  /**
   * 校验类型  两个loader共用代码
   *
   * @param  {*}      value 实际传入的值
   * @param  {string} type  静态分析时候得到的值得类型
   * @param  {array[string]} errList 校验错误信息  类型
   * @return {bool}         校验结果
   */

  /* eslint complexity:[2,39] */
  var checkType = function checkType(value, originType) {
    var errList = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : [];

    var isNullableReg = /_cml_nullable_lmc_/g;
    var type = originType.replace('_cml_nullable_lmc_', '');
    type === "Void" && (type = "Undefined");
    var currentType = getType(value);
    var canUseNullable = enableTypes.includes("Nullable");
    var canUseObject = enableTypes.includes("Object");
    if (currentType == 'Null') {
      if (type == "Null") {
        // 如果定义的参数的值就是 Null，那么校验通过
        errList = [];
      } else {
        // 那么判断是否是可选参数的情况
        canUseNullable && isNullableReg.test(originType) ? errList = [] : errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542Fnullable\u914D\u7F6E");
      }
      return errList;
    }
    if (currentType == 'Undefined') {
      // 如果运行时传入的真实值是undefined,那么可能改值在接口处就是被定义为 Undefined类型或者是 ?string 这种可选参数 nullable的情况；
      if (type == "Undefined") {
        errList = [];
      } else {
        canUseNullable && isNullableReg.test(originType) ? errList = [] : errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542Fnullable\u914D\u7F6E\u6216\u8005\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'String') {
      if (type == 'String') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Boolean') {
      if (type == 'Boolean') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Number') {
      if (type == 'Number') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Object') {
      if (type == 'Object') {
        !canUseObject ? errList.push("\u4E0D\u80FD\u76F4\u63A5\u5B9A\u4E49\u7C7B\u578B" + type + "\uFF0C\u9700\u8981\u4F7F\u7528\u7B26\u5408\u7C7B\u578B\u5B9A\u4E49\uFF0C\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542F\u4E86\u53EF\u4EE5\u76F4\u63A5\u5B9A\u4E49 Object \u7C7B\u578B\u53C2\u6570\uFF1B") : errList = [];
      } else if (type == 'CMLObject') {
        errList = [];
      } else {
        // 这种情况的对象就是自定义的对象；
        if (types[type]) {
          var _keys = (0, _keys3.default)(types[type]);
          // todo 这里是同样的问题，可能多传递
          _keys.forEach(function (key) {
            var subError = checkType(value[key], types[type][key], []);
            if (subError && subError.length) {
              errList = errList.concat(subError);
            }
          });
          if ((0, _keys3.default)(value).length > _keys.length) {
            errList.push("type [" + type + "] \u53C2\u6570\u4E2A\u6570\u4E0E\u5B9A\u4E49\u4E0D\u7B26");
          }
        } else {
          errList.push('找不到定义的type [' + type + ']!');
        }
      }
      return errList;
    }
    if (currentType == 'Array') {
      if (type == 'Array') {
        !canUseObject ? errList.push("\u4E0D\u80FD\u76F4\u63A5\u5B9A\u4E49\u7C7B\u578B" + type + "\uFF0C\u9700\u8981\u4F7F\u7528\u7B26\u5408\u7C7B\u578B\u5B9A\u4E49\uFF0C\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542F\u4E86\u53EF\u4EE5\u76F4\u63A5\u5B9A\u4E49 Array \u7C7B\u578B\u53C2\u6570\uFF1B") : errList = [];
      } else {
        if (types[type]) {
          // 数组元素的类型
          var itemType = types[type][0];
          for (var i = 0; i < value.length; i++) {
            var subError = checkType(value[i], itemType, []);
            if (subError && subError.length) {
              errList = errList.concat(subError);
            }
          }
        } else {
          errList.push('找不到定义的type [' + type + ']!');
        }
      }

      return errList;
    }
    if (currentType == 'Function') {
      // if (type == 'Function') {
      //   errList = [];
      // } else {
      //   errList.push(`定义了${type}类型的参数，传入的却是${currentType},请检查所传参数是否和接口定义的一致`)
      // }
      if (types[type]) {
        if (!types[type].input && !types[type].output) {
          errList.push("\u627E\u4E0D\u5230" + types[type] + " \u51FD\u6570\u5B9A\u4E49\u7684\u8F93\u5165\u8F93\u51FA");
        }
      } else {
        errList.push('找不到定义的type [' + type + ']!');
      }
      return errList;
    }
    if (currentType == 'Promise') {
      if (type == 'Promise') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Date') {
      if (type == 'Date') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'RegExp') {
      if (type == 'RegExp') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }

    return errList;
  };

  /**
   * 校验参数类型
   *
   * @param  {string} methodName 方法名称
   * @param  {Array}  argNames   参数名称列表
   * @param  {Array}  argValues  参数值列表
   * @return {bool}              校验结果
   */
  var checkArgsType = function checkArgsType(methodName, argValues) {
    var argList = void 0;

    if (getType(methodName) == 'Array') {
      // 回调函数的校验    methodName[0] 方法的名字 methodName[1]该回调函数在方法的参数索引
      argList = types[methods[methodName[0]].input[methodName[1]]].input;
      // 拿到这个回调函数的参数定义
    } else {
      argList = methods[methodName].input;
    }
    // todo 函数可能多传参数
    argList.forEach(function (argType, index) {
      var errList = checkType(argValues[index], argType, []);
      if (errList && errList.length > 0) {
        __CML_ERROR__("\n        \u6821\u9A8C\u4F4D\u7F6E: \u65B9\u6CD5" + methodName + "\u7B2C" + (index + 1) + "\u4E2A\u53C2\u6570\n        \u9519\u8BEF\u4FE1\u606F: " + errList.join('\n'));
      }
    });
    if (argValues.length > argList.length) {
      __CML_ERROR__("[" + methodName + "]\u65B9\u6CD5\u53C2\u6570\u4F20\u9012\u4E2A\u6570\u4E0E\u5B9A\u4E49\u4E0D\u7B26");
    }
  };

  /**
   * 校验返回值类型
   *
   * @param  {string} methodName 方法名称
   * @param  {*}      returnData 返回值
   * @return {bool}              校验结果
   */
  var checkReturnType = function checkReturnType(methodName, returnData) {
    var output = void 0;
    if (getType(methodName) == 'Array') {
      output = types[methods[methodName[0]].input[methodName[1]]].output;
    } else {
      output = methods[methodName].output;
    }
    // todo output 为什么可以是数组
    // if (output instanceof Array) {
    //   output.forEach(type => {

    //     //todo 而且是要有一个校验不符合就check失败？ 应该是有一个校验通过就可以吧
    //     checkType(returnData, type,[])
    //   });
    // }
    var errList = checkType(returnData, output, []);
    if (errList && errList.length > 0) {
      __CML_ERROR__("\n      \u6821\u9A8C\u4F4D\u7F6E: \u65B9\u6CD5" + methodName + "\u8FD4\u56DE\u503C\n      \u9519\u8BEF\u4FE1\u606F: " + errList.join('\n'));
    }
  };

  /**
   * 创建warpper
   *
   * @param  {string}   funcName   方法名称
   * @param  {Function} originFunc 原有方法
   * @return {Function}            包裹后的方法
   */
  var createWarpper = function createWarpper(funcName, originFunc) {
    return function () {
      var argValues = Array.prototype.slice.call(arguments).map(function (arg, index) {
        // 对传入的方法要做特殊的处理，这个是传入的callback，对callback函数再做包装
        if (getType(arg) == 'Function') {
          return createWarpper([funcName, index], arg);
        }
        return arg;
      });

      checkArgsType(funcName, argValues);

      var result = originFunc.apply(this, argValues);

      checkReturnType(funcName, result);
      return result;
    };
  };

  // 获取所有方法
  var keys = (0, _keys3.default)(methods);

  // 处理包装方法
  keys.forEach(function (key) {
    var originFunc = obj[key];
    if (!originFunc) {
      __CML_ERROR__('method [' + key + '] not found!');
      return;
    }

    if (obj.hasOwnProperty(key)) {
      obj[key] = createWarpper(key, originFunc);
    } else {
      (0, _getPrototypeOf2.default)(obj)[key] = createWarpper(key, originFunc);
    }
  });

  return obj;
};

var Method = function () {
  function Method() {
    (0, _classCallCheck3.default)(this, Method);
  }

  (0, _createClass3.default)(Method, [{
    key: "getQueryObjSync",
    value: function getQueryObjSync() {}
  }]);
  return Method;
}();

exports.default = __OBJECT__WRAPPER__(new Method());

(0, _util.copyProtoProperty)(exports.default);

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/getQueryObjSync/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getQueryObjSync = getQueryObjSync;

var _index = __webpack_require__("./node_modules/chameleon-bridge/apis/getQueryObjSync/index.interface");

var _index2 = _interopRequireDefault(_index);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function getQueryObjSync() {
  return _index2.default.getQueryObjSync();
}

getQueryObjSync.prototype.moduleName = 'cml';
getQueryObjSync.prototype.methodName = 'getLaunchUrl';

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/getSDKInfo/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getSDKInfo = getSDKInfo;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'cml';
var methodName = 'getSDKInfo';

function getSDKInfo(param, cb) {
  (0, _index.callNative)(moduleName, methodName, param, cb);
}

getSDKInfo.prototype.moduleName = moduleName;
getSDKInfo.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/location/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getLocationInfo = getLocationInfo;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'cml';
var methodName = 'getLocationInfo';

function getLocationInfo(param, cb) {
  (0, _index.callNative)(moduleName, methodName, param, function (res) {
    /**
     * lat:number
     * lng:number
     */
    cb(res);
  });
}

getLocationInfo.prototype.moduleName = moduleName;
getLocationInfo.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/navigator/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.navigateTo = navigateTo;
exports.redirectTo = redirectTo;
exports.navigateBack = navigateBack;
function navigateTo(opt) {
  var runtime = __webpack_require__("./node_modules/chameleon-runtime/index.js").default;

  var _runtime$getInfo = runtime.getInfo(),
      router = _runtime$getInfo.router,
      routerConfig = _runtime$getInfo.routerConfig;

  var path = opt.path,
      query = opt.query;


  if (path && router) {
    var flag = false;

    var routes = routerConfig.routes;
    for (var i = 0; i < routes.length; i++) {
      var route = routes[i];
      if (path === route.path) {
        flag = true;
        router.push({
          path: route.url
        });
        break;
      }
    }
    if (!flag) {
      router.push({
        path: router.options.routes[0].path,
        query: query
      });
    }
  } else {
    router.push({
      path: router.options.routes[0].path,
      query: query
    });
  }
}

function redirectTo(opt) {
  var runtime = __webpack_require__("./node_modules/chameleon-runtime/index.js").default;

  var _runtime$getInfo2 = runtime.getInfo(),
      router = _runtime$getInfo2.router;

  var path = opt.path,
      query = opt.query;


  router.replace({
    path: path,
    query: query
  });
}

function navigateBack(backPageNum) {
  var runtime = __webpack_require__("./node_modules/chameleon-runtime/index.js").default;

  var _runtime$getInfo3 = runtime.getInfo(),
      router = _runtime$getInfo3.router;

  router.go(backPageNum);
}

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/openPage/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.open = open;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var _utils = __webpack_require__("./node_modules/chameleon-bridge/utils.js");

var moduleName = 'cml';
var methodName = 'openPage';

function open(param) {
  /**
   * param: url, commonPatchParams, extraOptions
   */
  var url = param.url;
  var commonPatchParams = param.commonPatchParams;
  var closeCurrent = param.extraOptions && param.extraOptions.closeCurrent || false;
  url = (0, _utils.getUrlWithConnector)(url) + (0, _utils.queryStringify)(commonPatchParams);
  var openObj = (0, _utils.getOpenObj)(url);
  var urlOpen = openObj.weex ? openObj.weex : openObj.web;

  (0, _index.callNative)(moduleName, methodName, {
    url: urlOpen,
    closeCurrent: closeCurrent
  }, function () {});
}

open.prototype.moduleName = moduleName;
open.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/reload/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.reload = reload;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'cml'; /**
                         * 重新加载weex页面
                         */

var methodName = 'reloadPage';

function reload() {
  var param = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

  /**
   * url
   */
  (0, _index.callNative)(moduleName, methodName, param, function () {});
}

reload.prototype.moduleName = moduleName;
reload.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/request/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.request = request;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'stream';
var methodName = 'fetch';

function request(params) {
  var url = params.url,
      body = params.body,
      headers = params.headers,
      method = params.method,
      cb = params.cb;


  (0, _index.callNative)(moduleName, methodName, {
    url: url,
    method: method,
    headers: headers,
    body: body,
    type: 'text'
  }, function (res) {
    /**
     * errno
     * data:
     *  status
     *  statusText
     *  data
     *  headers
     */
    var errno = res.errno,
        _res$data = res.data,
        data = _res$data === undefined ? '' : _res$data;

    if (errno == 0) {
      cb(data);
    }
  });
}

request.prototype.moduleName = moduleName;
request.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/rollbackWeb/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.rollbackWeb = rollbackWeb;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'cml';
var methodName = 'rollbackWeb';

function rollbackWeb() {
  (0, _index.callNative)(moduleName, methodName, {}, function () {});
}

rollbackWeb.prototype.moduleName = moduleName;
rollbackWeb.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/socket/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.initSocket = initSocket;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'webSocket';
var methodName = 'WebSocket';

function initSocket(_ref) {
  var url = _ref.url;

  WebSocket.WebSocket(url);
  return WebSocket;
}

initSocket.prototype.moduleName = moduleName;
initSocket.prototype.methodName = methodName;

var WebSocket = {
  WebSocket: function WebSocket(url) {
    this._callAdapter(methodName, { url: url });
  },
  onopen: function onopen(cb) {
    this._listenAdapter('onopen', cb);
  },
  onmessage: function onmessage(cb) {
    this._listenAdapter('onmessage', cb);
  },
  onerror: function onerror(cb) {
    this._listenAdapter('onerror', cb);
  },
  onclose: function onclose(cb) {
    this._listenAdapter('onclose', cb);
  },
  send: function send() {
    var data = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};

    this._callAdapter('send', data);
  },
  close: function close() {
    var param = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : { code: 0, reason: 'close' };

    /**
     * code: 0
     * reason: string
     */
    this._callAdapter('close', param);
  },
  _callAdapter: function _callAdapter(key, param) {
    (0, _index.callNative)(moduleName, key, param, function () {});
  },
  _listenAdapter: function _listenAdapter(key, cb) {
    (0, _index.listenNative)(moduleName, key, cb);
  }
};

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/storage/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.setStorage = setStorage;
exports.getStorage = getStorage;
exports.removeStorage = removeStorage;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'storage';
var methodSet = 'setStorage';
var methodGet = 'getStorage';
var methodRemove = 'removeStorage';

function setStorage(param, cb) {
  /**
   * key
   * value
   */
  (0, _index.callNative)(moduleName, methodSet, param, function (res) {
    /**
     * errno
     * msg
     * data
     */
    cb(res);
  });
}

setStorage.prototype.moduleName = moduleName;
setStorage.prototype.methodName = methodSet;

function getStorage(param, cb) {
  /**
   * key
   */
  (0, _index.callNative)(moduleName, methodGet, param, function (res) {
    /**
     * errno
     * msg
     * data
     */
    cb(res);
  });
}

getStorage.prototype.moduleName = moduleName;
getStorage.prototype.methodName = methodGet;

function removeStorage(param, cb) {
  /**
   * key
   */
  (0, _index.callNative)(moduleName, methodRemove, param, function (res) {
    /**
     * errno
     * msg
     * data
     */
    cb(res);
  });
}

removeStorage.prototype.moduleName = moduleName;
removeStorage.prototype.methodName = methodRemove;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/systemInfo/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getSystemInfo = getSystemInfo;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'cml';
var methodName = 'getSystemInfo';

function getSystemInfo(param, cb) {
  (0, _index.callNative)(moduleName, methodName, param, function (res) {
    /**
     * res.data
     * {
      "scale": 3,
      "deviceWidth": 1080,
      "deviceHeight": 2340,
      "os": "android",
      "extraParams": {
        "model": "PAFM00",
        "imei": "356416465466244",
        "netType": "WIFI"
      }
    }
     */
    var _res$data = res.data,
        deviceWidth = _res$data.deviceWidth,
        deviceHeight = _res$data.deviceHeight,
        scale = _res$data.scale;

    res.data.viewportWidth = deviceWidth / scale;
    res.data.viewportHeight = deviceHeight / scale;
    cb(res);
  });
}

getSystemInfo.prototype.moduleName = moduleName;
getSystemInfo.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/title/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.setTitle = setTitle;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'cml';
var methodName = 'setTitle';

function setTitle(param) {
  (0, _index.callNative)(moduleName, methodName, param, function () {});
}

setTitle.prototype.moduleName = moduleName;
setTitle.prototype.methodName = methodName;

/***/ }),

/***/ "./node_modules/chameleon-bridge/apis/ui/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.showToast = showToast;
exports.alert = alert;
exports.confirm = confirm;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var moduleName = 'modal';
var methodShowToast = 'showToast';
var methodAlert = 'alert';
var methodConfirm = 'confirm';

function showToast(param) {
  /**
   * message
   * duration(ms)
   */
  (0, _index.callNative)(moduleName, methodShowToast, param, function () {});
}

showToast.prototype.moduleName = moduleName;
showToast.prototype.methodName = methodShowToast;

function alert(param, successCallBack) {
  /**
   * message
   * confirmTitle
   */
  (0, _index.callNative)(moduleName, methodAlert, param, successCallBack);
}

alert.prototype.moduleName = moduleName;
alert.prototype.methodName = methodAlert;

function confirm(param, successCallBack, failCallBack) {
  /**
   * message
   * confirmTitle
   * cancelTitle
   */
  (0, _index.callNative)(moduleName, methodConfirm, param, successCallBack);
}

confirm.prototype.moduleName = moduleName;
confirm.prototype.methodName = methodConfirm;

/***/ }),

/***/ "./node_modules/chameleon-bridge/core/index.interface":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _classCallCheck2 = __webpack_require__("./node_modules/babel-runtime/helpers/classCallCheck.js");

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = __webpack_require__("./node_modules/babel-runtime/helpers/createClass.js");

var _createClass3 = _interopRequireDefault(_createClass2);

var _getPrototypeOf = __webpack_require__("./node_modules/babel-runtime/core-js/object/get-prototype-of.js");

var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

var _promise = __webpack_require__("./node_modules/babel-runtime/core-js/promise.js");

var _promise2 = _interopRequireDefault(_promise);

var _assign = __webpack_require__("./node_modules/babel-runtime/core-js/object/assign.js");

var _assign2 = _interopRequireDefault(_assign);

var _keys2 = __webpack_require__("./node_modules/babel-runtime/core-js/object/keys.js");

var _keys3 = _interopRequireDefault(_keys2);

var _util = __webpack_require__("../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-loader/src/cml-compile/runtime/common/util.js");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var __INTERFACE__FILEPATH = "/Users/didi/cml-demo-say/node_modules/chameleon-bridge/core/index.interface";
var __CML_ERROR__ = function throwError(content) {
  throw new Error("\u6587\u4EF6\u4F4D\u7F6E: " + __INTERFACE__FILEPATH + "\n            " + content);
};

var __enableTypes__ = "";
var __CHECK__DEFINES__ = {
  "types": {
    "Func": {
      "input": ["CMLObject"],
      "output": "Void"
    }
  },
  "interfaces": {
    "CoreInterface": {
      "initChannel": {
        "input": [],
        "output": "Void"
      },
      "callNative": {
        "input": ["String", "String", "CMLObject", "Func"],
        "output": "Void"
      },
      "listenNative": {
        "input": ["String", "String", "Func"],
        "output": "Void"
      }
    }
  },
  "classes": {
    "Method": ["CoreInterface"]
  }
};
var __OBJECT__WRAPPER__ = function __OBJECT__WRAPPER__(obj) {
  var className = obj.constructor.name;
  /* eslint-disable no-undef */
  var defines = __CHECK__DEFINES__;
  var enableTypes = __enableTypes__.split(',') || []; // ['Object','Array','Nullable']
  /* eslint-disable no-undef */
  var types = defines.types;
  var interfaceNames = defines.classes[className];
  var methods = {};

  interfaceNames && interfaceNames.forEach(function (interfaceName) {
    var keys = (0, _keys3.default)(defines.interfaces);
    keys.forEach(function (key) {
      (0, _assign2.default)(methods, defines.interfaces[key]);
    });
  });

  /**
   * 获取类型
   *
   * @param  {*}      value 值
   * @return {string}       类型
   */
  var getType = function getType(value) {
    if (value instanceof _promise2.default) {
      return "Promise";
    }
    var type = Object.prototype.toString.call(value);
    return type.replace(/\[object\s(.*)\]/g, '$1').replace(/( |^)[a-z]/g, function (L) {
      return L.toUpperCase();
    });
  };

  /**
   * 校验类型  两个loader共用代码
   *
   * @param  {*}      value 实际传入的值
   * @param  {string} type  静态分析时候得到的值得类型
   * @param  {array[string]} errList 校验错误信息  类型
   * @return {bool}         校验结果
   */

  /* eslint complexity:[2,39] */
  var checkType = function checkType(value, originType) {
    var errList = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : [];

    var isNullableReg = /_cml_nullable_lmc_/g;
    var type = originType.replace('_cml_nullable_lmc_', '');
    type === "Void" && (type = "Undefined");
    var currentType = getType(value);
    var canUseNullable = enableTypes.includes("Nullable");
    var canUseObject = enableTypes.includes("Object");
    if (currentType == 'Null') {
      if (type == "Null") {
        // 如果定义的参数的值就是 Null，那么校验通过
        errList = [];
      } else {
        // 那么判断是否是可选参数的情况
        canUseNullable && isNullableReg.test(originType) ? errList = [] : errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542Fnullable\u914D\u7F6E");
      }
      return errList;
    }
    if (currentType == 'Undefined') {
      // 如果运行时传入的真实值是undefined,那么可能改值在接口处就是被定义为 Undefined类型或者是 ?string 这种可选参数 nullable的情况；
      if (type == "Undefined") {
        errList = [];
      } else {
        canUseNullable && isNullableReg.test(originType) ? errList = [] : errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542Fnullable\u914D\u7F6E\u6216\u8005\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'String') {
      if (type == 'String') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Boolean') {
      if (type == 'Boolean') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Number') {
      if (type == 'Number') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Object') {
      if (type == 'Object') {
        !canUseObject ? errList.push("\u4E0D\u80FD\u76F4\u63A5\u5B9A\u4E49\u7C7B\u578B" + type + "\uFF0C\u9700\u8981\u4F7F\u7528\u7B26\u5408\u7C7B\u578B\u5B9A\u4E49\uFF0C\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542F\u4E86\u53EF\u4EE5\u76F4\u63A5\u5B9A\u4E49 Object \u7C7B\u578B\u53C2\u6570\uFF1B") : errList = [];
      } else if (type == 'CMLObject') {
        errList = [];
      } else {
        // 这种情况的对象就是自定义的对象；
        if (types[type]) {
          var _keys = (0, _keys3.default)(types[type]);
          // todo 这里是同样的问题，可能多传递
          _keys.forEach(function (key) {
            var subError = checkType(value[key], types[type][key], []);
            if (subError && subError.length) {
              errList = errList.concat(subError);
            }
          });
          if ((0, _keys3.default)(value).length > _keys.length) {
            errList.push("type [" + type + "] \u53C2\u6570\u4E2A\u6570\u4E0E\u5B9A\u4E49\u4E0D\u7B26");
          }
        } else {
          errList.push('找不到定义的type [' + type + ']!');
        }
      }
      return errList;
    }
    if (currentType == 'Array') {
      if (type == 'Array') {
        !canUseObject ? errList.push("\u4E0D\u80FD\u76F4\u63A5\u5B9A\u4E49\u7C7B\u578B" + type + "\uFF0C\u9700\u8981\u4F7F\u7528\u7B26\u5408\u7C7B\u578B\u5B9A\u4E49\uFF0C\u8BF7\u786E\u8BA4\u662F\u5426\u5F00\u542F\u4E86\u53EF\u4EE5\u76F4\u63A5\u5B9A\u4E49 Array \u7C7B\u578B\u53C2\u6570\uFF1B") : errList = [];
      } else {
        if (types[type]) {
          // 数组元素的类型
          var itemType = types[type][0];
          for (var i = 0; i < value.length; i++) {
            var subError = checkType(value[i], itemType, []);
            if (subError && subError.length) {
              errList = errList.concat(subError);
            }
          }
        } else {
          errList.push('找不到定义的type [' + type + ']!');
        }
      }

      return errList;
    }
    if (currentType == 'Function') {
      // if (type == 'Function') {
      //   errList = [];
      // } else {
      //   errList.push(`定义了${type}类型的参数，传入的却是${currentType},请检查所传参数是否和接口定义的一致`)
      // }
      if (types[type]) {
        if (!types[type].input && !types[type].output) {
          errList.push("\u627E\u4E0D\u5230" + types[type] + " \u51FD\u6570\u5B9A\u4E49\u7684\u8F93\u5165\u8F93\u51FA");
        }
      } else {
        errList.push('找不到定义的type [' + type + ']!');
      }
      return errList;
    }
    if (currentType == 'Promise') {
      if (type == 'Promise') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'Date') {
      if (type == 'Date') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }
    if (currentType == 'RegExp') {
      if (type == 'RegExp') {
        errList = [];
      } else {
        errList.push("\u5B9A\u4E49\u4E86" + type + "\u7C7B\u578B\u7684\u53C2\u6570\uFF0C\u4F20\u5165\u7684\u5374\u662F" + currentType + ",\u8BF7\u68C0\u67E5\u6240\u4F20\u53C2\u6570\u662F\u5426\u548C\u63A5\u53E3\u5B9A\u4E49\u7684\u4E00\u81F4");
      }
      return errList;
    }

    return errList;
  };

  /**
   * 校验参数类型
   *
   * @param  {string} methodName 方法名称
   * @param  {Array}  argNames   参数名称列表
   * @param  {Array}  argValues  参数值列表
   * @return {bool}              校验结果
   */
  var checkArgsType = function checkArgsType(methodName, argValues) {
    var argList = void 0;

    if (getType(methodName) == 'Array') {
      // 回调函数的校验    methodName[0] 方法的名字 methodName[1]该回调函数在方法的参数索引
      argList = types[methods[methodName[0]].input[methodName[1]]].input;
      // 拿到这个回调函数的参数定义
    } else {
      argList = methods[methodName].input;
    }
    // todo 函数可能多传参数
    argList.forEach(function (argType, index) {
      var errList = checkType(argValues[index], argType, []);
      if (errList && errList.length > 0) {
        __CML_ERROR__("\n        \u6821\u9A8C\u4F4D\u7F6E: \u65B9\u6CD5" + methodName + "\u7B2C" + (index + 1) + "\u4E2A\u53C2\u6570\n        \u9519\u8BEF\u4FE1\u606F: " + errList.join('\n'));
      }
    });
    if (argValues.length > argList.length) {
      __CML_ERROR__("[" + methodName + "]\u65B9\u6CD5\u53C2\u6570\u4F20\u9012\u4E2A\u6570\u4E0E\u5B9A\u4E49\u4E0D\u7B26");
    }
  };

  /**
   * 校验返回值类型
   *
   * @param  {string} methodName 方法名称
   * @param  {*}      returnData 返回值
   * @return {bool}              校验结果
   */
  var checkReturnType = function checkReturnType(methodName, returnData) {
    var output = void 0;
    if (getType(methodName) == 'Array') {
      output = types[methods[methodName[0]].input[methodName[1]]].output;
    } else {
      output = methods[methodName].output;
    }
    // todo output 为什么可以是数组
    // if (output instanceof Array) {
    //   output.forEach(type => {

    //     //todo 而且是要有一个校验不符合就check失败？ 应该是有一个校验通过就可以吧
    //     checkType(returnData, type,[])
    //   });
    // }
    var errList = checkType(returnData, output, []);
    if (errList && errList.length > 0) {
      __CML_ERROR__("\n      \u6821\u9A8C\u4F4D\u7F6E: \u65B9\u6CD5" + methodName + "\u8FD4\u56DE\u503C\n      \u9519\u8BEF\u4FE1\u606F: " + errList.join('\n'));
    }
  };

  /**
   * 创建warpper
   *
   * @param  {string}   funcName   方法名称
   * @param  {Function} originFunc 原有方法
   * @return {Function}            包裹后的方法
   */
  var createWarpper = function createWarpper(funcName, originFunc) {
    return function () {
      var argValues = Array.prototype.slice.call(arguments).map(function (arg, index) {
        // 对传入的方法要做特殊的处理，这个是传入的callback，对callback函数再做包装
        if (getType(arg) == 'Function') {
          return createWarpper([funcName, index], arg);
        }
        return arg;
      });

      checkArgsType(funcName, argValues);

      var result = originFunc.apply(this, argValues);

      checkReturnType(funcName, result);
      return result;
    };
  };

  // 获取所有方法
  var keys = (0, _keys3.default)(methods);

  // 处理包装方法
  keys.forEach(function (key) {
    var originFunc = obj[key];
    if (!originFunc) {
      __CML_ERROR__('method [' + key + '] not found!');
      return;
    }

    if (obj.hasOwnProperty(key)) {
      obj[key] = createWarpper(key, originFunc);
    } else {
      (0, _getPrototypeOf2.default)(obj)[key] = createWarpper(key, originFunc);
    }
  });

  return obj;
};

var Method = function () {
  function Method() {
    (0, _classCallCheck3.default)(this, Method);
  }

  (0, _createClass3.default)(Method, [{
    key: "initChannel",
    value: function initChannel() {}
  }, {
    key: "callNative",
    value: function callNative(module, method, args, callback) {}
  }, {
    key: "listenNative",
    value: function listenNative(module, method, callback) {}
  }]);
  return Method;
}();

exports.default = __OBJECT__WRAPPER__(new Method());

(0, _util.copyProtoProperty)(exports.default);

/***/ }),

/***/ "./node_modules/chameleon-bridge/core/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.callNative = callNative;
exports.listenNative = listenNative;

var _index = __webpack_require__("./node_modules/chameleon-bridge/core/index.interface");

var _index2 = _interopRequireDefault(_index);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

// 初始化通道
_index2.default.initChannel();

function callNative(module, method, args, callback) {
  _index2.default.callNative.apply(_index2.default, arguments);
}

function listenNative(module, method, callback) {
  _index2.default.listenNative.apply(_index2.default, arguments);
}

/***/ }),

/***/ "./node_modules/chameleon-bridge/index.js":
/***/ (function(module, exports, __webpack_require__) {

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends2 = __webpack_require__("./node_modules/babel-runtime/helpers/extends.js");

var _extends3 = _interopRequireDefault(_extends2);

var _chooseImage = __webpack_require__("./node_modules/chameleon-bridge/apis/chooseImage/index.js");

var chooseImage = _interopRequireWildcard(_chooseImage);

var _clipboard = __webpack_require__("./node_modules/chameleon-bridge/apis/clipboard/index.js");

var clipboard = _interopRequireWildcard(_clipboard);

var _close = __webpack_require__("./node_modules/chameleon-bridge/apis/close/index.js");

var close = _interopRequireWildcard(_close);

var _location = __webpack_require__("./node_modules/chameleon-bridge/apis/location/index.js");

var location = _interopRequireWildcard(_location);

var _navigator = __webpack_require__("./node_modules/chameleon-bridge/apis/navigator/index.js");

var navigator = _interopRequireWildcard(_navigator);

var _openPage = __webpack_require__("./node_modules/chameleon-bridge/apis/openPage/index.js");

var openPage = _interopRequireWildcard(_openPage);

var _reload = __webpack_require__("./node_modules/chameleon-bridge/apis/reload/index.js");

var reload = _interopRequireWildcard(_reload);

var _request = __webpack_require__("./node_modules/chameleon-bridge/apis/request/index.js");

var request = _interopRequireWildcard(_request);

var _rollbackWeb = __webpack_require__("./node_modules/chameleon-bridge/apis/rollbackWeb/index.js");

var rollbackWeb = _interopRequireWildcard(_rollbackWeb);

var _socket = __webpack_require__("./node_modules/chameleon-bridge/apis/socket/index.js");

var socket = _interopRequireWildcard(_socket);

var _storage = __webpack_require__("./node_modules/chameleon-bridge/apis/storage/index.js");

var storage = _interopRequireWildcard(_storage);

var _systemInfo = __webpack_require__("./node_modules/chameleon-bridge/apis/systemInfo/index.js");

var systemInfo = _interopRequireWildcard(_systemInfo);

var _canIUse = __webpack_require__("./node_modules/chameleon-bridge/apis/canIUse/index.js");

var canIUse = _interopRequireWildcard(_canIUse);

var _getSDKInfo = __webpack_require__("./node_modules/chameleon-bridge/apis/getSDKInfo/index.js");

var getSDKInfo = _interopRequireWildcard(_getSDKInfo);

var _title = __webpack_require__("./node_modules/chameleon-bridge/apis/title/index.js");

var title = _interopRequireWildcard(_title);

var _index = __webpack_require__("./node_modules/chameleon-bridge/apis/getQueryObjSync/index.js");

var query = _interopRequireWildcard(_index);

var _getLaunchUrl = __webpack_require__("./node_modules/chameleon-bridge/apis/getLaunchUrl/index.js");

var getLaunchUrl = _interopRequireWildcard(_getLaunchUrl);

var _ui = __webpack_require__("./node_modules/chameleon-bridge/apis/ui/index.js");

var ui = _interopRequireWildcard(_ui);

var _index2 = __webpack_require__("./node_modules/chameleon-bridge/apis/getComponentRect/index.js");

var getComponentRect = _interopRequireWildcard(_index2);

var _index3 = __webpack_require__("./node_modules/chameleon-bridge/core/index.js");

var core = _interopRequireWildcard(_index3);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = (0, _extends3.default)({}, chooseImage, clipboard, close, location, navigator, openPage, reload, request, rollbackWeb, socket, storage, systemInfo, canIUse, getSDKInfo, title, query, getLaunchUrl, getComponentRect, ui, getComponentRect, core);

/***/ }),

/***/ "./node_modules/chameleon-bridge/utils.js":
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(process) {Object.defineProperty(exports, "__esModule", {
  value: true
});

var _objectWithoutProperties2 = __webpack_require__("./node_modules/babel-runtime/helpers/objectWithoutProperties.js");

var _objectWithoutProperties3 = _interopRequireDefault(_objectWithoutProperties2);

var _keys = __webpack_require__("./node_modules/babel-runtime/core-js/object/keys.js");

var _keys2 = _interopRequireDefault(_keys);

exports.isFn = isFn;
exports.isStr = isStr;
exports.isObj = isObj;
exports.isArray = isArray;
exports.isUndefined = isUndefined;
exports.isEmpty = isEmpty;
exports.noop = noop;
exports.parseQuery = parseQuery;
exports.queryStringify = queryStringify;
exports.queryParse = queryParse;
exports.isNeedApiPrefix = isNeedApiPrefix;
exports.addApiPrefix = addApiPrefix;
exports.tryJsonParse = tryJsonParse;
exports.getQueryParamsFromSearchStr = getQueryParamsFromSearchStr;
exports.getOpenObj = getOpenObj;
exports.getUrlWithConnector = getUrlWithConnector;
exports.compareVersion = compareVersion;

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * 基础js方法的封装
 *
 */
function typeEqual(obj, type) {
  return Object.prototype.toString.call(obj) === '[object ' + type + ']';
}

function isFn(obj) {
  return typeEqual(obj, 'Function');
}

function isStr(obj) {
  return typeEqual(obj, 'String');
}

function isObj(obj) {
  return typeEqual(obj, 'Object');
}

function isArray(obj) {
  return typeEqual(obj, 'Array');
}

function isUndefined(obj) {
  return typeEqual(obj, 'Undefined');
}

function isEmpty(obj) {
  for (key in obj) {
    if (obj.hasOwnProperty(key)) {
      return false;
    }
  }
  return true;
}

function noop() {}

function parseQuery(obj) {
  var str = '&';
  var keys = null;
  if (obj && (0, _keys2.default)(obj).length > 0) {
    keys = (0, _keys2.default)(obj);
    for (var i = 0; i < keys.length; i++) {
      var _key = keys[i];
      str += _key + '=' + encodeURIComponent(obj[_key]) + '&';
    }
  }
  return str;
}

function queryStringify(obj) {
  var str = '&';
  var keys = null;
  if (obj && (0, _keys2.default)(obj).length > 0) {
    keys = (0, _keys2.default)(obj);
    for (var i = 0; i < keys.length; i++) {
      var _key2 = keys[i];
      str += _key2 + '=' + encodeURIComponent(obj[_key2]) + '&';
    }
  }
  return str;
}

function queryParse(search) {
  search = search || '';
  var arr = search.split(/(\?|&)/);
  var parmsObj = {};
  for (var i = 0; i < arr.length; i++) {
    if (arr[i].indexOf('=') !== -1) {
      var keyValue = arr[i].match(/([^=]*)=(.*)/);
      parmsObj[keyValue[1]] = keyValue[2];
    }
  }
  return parmsObj;
}

function isNeedApiPrefix(url) {
  return (/^\/[^/]/.test(url)
  );
}

function addApiPrefix(url) {
  if (process && process.env && "http://172.22.138.92:8000") {
    return "http://172.22.138.92:8000" + url;
  }
  return url;
}

function tryJsonParse(some) {
  // 这里eslint提示也先别删除\[\]
  if (isStr(some) && /[\{\[].*[\}\]]/.test(some)) {
    some = JSON.parse(some);
  }
  return some;
}

function getQueryParamsFromSearchStr(qs) {
  var search = qs || location.search;
  var arr = search.split(/(\?|&)/);
  var parmsObj = {};
  for (var i = 0; i < arr.length; i++) {
    if (arr[i].indexOf('=') !== -1) {
      var keyValue = arr[i].match(/([^=]*)=(.*)/);
      parmsObj[keyValue[1]] = decodeURIComponent(keyValue[2]);
    }
  }
  return parmsObj;
}

/**
 * 获取处理后的各端打开的地址
 * @param {String} url url地址
 * @return {Object} objTreated 处理好的三端地址及对象
 */
function getOpenObj(url) {
  var webUrlWithoutQuery = url.split('?')[0];
  var queryObj = getQueryParamsFromSearchStr(url);
  var _queryObj$weixin_appi = queryObj.weixin_appid,
      weixin_appid = _queryObj$weixin_appi === undefined ? '' : _queryObj$weixin_appi,
      _queryObj$weixin_path = queryObj.weixin_path,
      weixin_path = _queryObj$weixin_path === undefined ? '' : _queryObj$weixin_path,
      _queryObj$weixin_envV = queryObj.weixin_envVersion,
      weixin_envVersion = _queryObj$weixin_envV === undefined ? '' : _queryObj$weixin_envV,
      _queryObj$weex_path = queryObj.weex_path,
      weex_path = _queryObj$weex_path === undefined ? '' : _queryObj$weex_path,
      _queryObj$wx_addr = queryObj.wx_addr,
      wx_addr = _queryObj$wx_addr === undefined ? '' : _queryObj$wx_addr,
      _queryObj$cml_addr = queryObj.cml_addr,
      cml_addr = _queryObj$cml_addr === undefined ? '' : _queryObj$cml_addr,
      extraData = (0, _objectWithoutProperties3.default)(queryObj, ['weixin_appid', 'weixin_path', 'weixin_envVersion', 'weex_path', 'wx_addr', 'cml_addr']);

  // weex 链接

  var weexUrl = '';
  if (cml_addr) {
    cml_addr = cml_addr + '?_cml_r=' + ~~(Math.random() * 1E5);
    cml_addr = encodeURIComponent(cml_addr);
    weexUrl = webUrlWithoutQuery + '?weex_path=' + weex_path + queryStringify(extraData) + '&cml_addr=' + cml_addr;
  }
  // 向下兼容
  if (wx_addr) {
    wx_addr = wx_addr + '?_cml_r=' + ~~(Math.random() * 1E5);
    wx_addr = encodeURIComponent(wx_addr);
    weexUrl = webUrlWithoutQuery + '?weex_path=' + weex_path + queryStringify(extraData) + '&wx_addr=' + wx_addr;
  }

  var objTreated = {
    weex: weexUrl,
    web: webUrlWithoutQuery + '?' + queryStringify(extraData),
    wx: {
      appId: weixin_appid,
      path: weixin_path,
      extraData: extraData,
      envVersion: weixin_envVersion
    }
  };
  return objTreated;
}

// 获得带正确连接符的url
function getUrlWithConnector(url) {
  var connector = url.includes('?') ? '&' : '?';
  return url + connector;
}

/**
 * 比较版本号
 * @param {String} v1 版本号1
 * @param {String} symb 比较符
 * @param {String} v2 版本号2
 */
function compareVersion(v1, symb, v2) {
  v1 = parseVersion(v1);
  v2 = parseVersion(v2);
  if (symb.indexOf('=') !== -1 && v1 === v2) {
    return true;
  }
  if (symb.indexOf('>') !== -1 && v1 > v2) {
    return true;
  }
  if (symb.indexOf('<') !== -1 && v1 < v2) {
    return true;
  }
  return false;
}

function parseVersion() {
  var version = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';

  version = version.split('.');
  version.length = 4;
  var ret = [];
  version.forEach(function (n) {
    n = n * 1;
    if (n) {
      ret.push(n >= 10 ? n : '0' + n);
    } else {
      ret.push('00');
    }
  });
  return parseInt(ret.join(''), 10);
}
/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__("../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/process/browser.js")))

/***/ }),

/***/ "./node_modules/core-js/library/fn/json/stringify.js":
/***/ (function(module, exports, __webpack_require__) {

var core = __webpack_require__("./node_modules/core-js/library/modules/_core.js");
var $JSON = core.JSON || (core.JSON = { stringify: JSON.stringify });
module.exports = function stringify(it) { // eslint-disable-line no-unused-vars
  return $JSON.stringify.apply($JSON, arguments);
};


/***/ }),

/***/ "./src/assets/images/chameleon.png":
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/chameleon_83ee00e.png";

/***/ }),

/***/ "./src/pages/index/index.cml":
/***/ (function(module, exports, __webpack_require__) {

var __cml__style0 = __webpack_require__("../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/extract-text-webpack-plugin/dist/loader.js?{\"omit\":1,\"remove\":true}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/vue-style-loader/index.js!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/css-loader/index.js?{\"sourceMap\":false}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-css-loader/index.js?{\"platform\":\"miniapp\"}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/postcss-loader/lib/index.js?{\"sourceMap\":false,\"config\":{\"path\":\"/Users/didi/.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/configs/postcss/wx/.postcssrc.js\"}}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/less-loader/dist/cjs.js?{\"sourceMap\":false}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-css-loader/index.js?{\"media\":true,\"cmlType\":\"wx\"}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-loader/src/selector.js?type=styles&index=0&fileType=page&media=dev&cmlType=wx&check={\"enable\":true,\"enableTypes\":[]}!./src/pages/index/index.cml");
var __cml__script = __webpack_require__("../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/babel-loader/lib/index.js?{\"filename\":\"/Users/didi/.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/chameleon.js\"}!../.nvm/versions/node/v8.12.0/lib/node_modules/chameleon-tool/node_modules/chameleon-loader/src/selector.js?type=script&index=0&fileType=page&media=dev&cmlType=wx&check={\"enable\":true,\"enableTypes\":[]}!./src/pages/index/index.cml");


/***/ })

},["./src/pages/index/index.cml"]);