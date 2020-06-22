export let BridgeGlobal = (function () {
  let instance;
  function init() {
    return {
      listenCallbacks: {},
      callbackId: 0
    }
  }
  return {
    getInstance: function () {
      if (!instance) {
        instance = init();
      }
      return instance;
    }
  };
})();