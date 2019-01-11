(function() {
    if (window.CmlBridge) {
        return;
    }

    var messagingIframe;
    var bizMessagingIframe;
    var sendMessageQueue = [];
    var receiveMessageQueue = [];
    var messageHandlers = {};

    var CML_SCHEME = 'cml://';
    var CML_BRIDGE = 'CmlBridge';
    // js 调用 native, 通知queue ready
    var CML_QUEUE_READY = CML_SCHEME + "queueReady?";
    // js 调用 native
    var CML_INVOKE_NATIVE = CML_SCHEME + "invokeNative?";
    // js 回调 native
    var CML_CALLBACK_NATIVE = CML_SCHEME + "callbackNative?";

    var responseCallbacks = {};
    var uniqueId = 1;

    // 创建消息index队列iframe
    function _createQueueReadyIframe(doc) {
        messagingIframe = doc.createElement('iframe');
        messagingIframe.style.display = 'none';
        doc.documentElement.appendChild(messagingIframe);
    }
    //创建消息体队列iframe
    function _createQueueReadyIframe4biz(doc) {
        bizMessagingIframe = doc.createElement('iframe');
        bizMessagingIframe.style.display = 'none';
        doc.documentElement.appendChild(bizMessagingIframe);
    }
    //set default messageHandler  初始化默认的消息线程
    function init(messageHandler) {
        if (CmlBridge._messageHandler) {
            throw new Error('CmlBridge.init called twice');
        }
        CmlBridge._messageHandler = messageHandler;
        var receivedMessages = receiveMessageQueue;
        receiveMessageQueue = null;
        for (var i = 0; i < receivedMessages.length; i++) {
            _dispatchMessageFromNative(receivedMessages[i]);
        }
    }

    function channel(protocol) {
        messagingIframe.src = protocol;
    }

    function channelNativeToJs(protocol) {
        console.log(protocol);
    }

    // 发送
    function send(data, responseCallback) {
        _doSend({
            data: data
        }, responseCallback);
    }

    // 注册线程 往数组里面添加值
    function registerHandler(handlerName, handler) {
        messageHandlers[handlerName] = handler;
    }
    // 调用线程
    function callHandler(handlerName, data, responseCallback) {
        _doSend({
            handlerName: handlerName,
            data: data
        }, responseCallback);
    }

    //sendMessage add message, 触发native处理 sendMessage
    function _doSend(message, responseCallback) {
        if (responseCallback) {
            var callbackId = 'cb_' + (uniqueId++) + '_' + new Date().getTime();
            responseCallbacks[callbackId] = responseCallback;
            message.callbackId = callbackId;
        }

        sendMessageQueue.push(message);
        messagingIframe.src = CML_QUEUE_READY + "module=queue_ready&method=fetchJsQueue";
    }

    // 提供给native调用,该函数作用:获取sendMessageQueue返回给native,由于android不能直接获取返回的内容,所以使用url shouldOverrideUrlLoading 的方式返回内容
    function fetchQueue() {
        var messageQueueString = JSON.stringify(sendMessageQueue);
        sendMessageQueue = [];
        //android can't read directly the return data, so we can reload iframe src to communicate with java
        bizMessagingIframe.src = CML_INVOKE_NATIVE + 'module=testModule&method=testMethod&arguments=' + encodeURIComponent(messageQueueString);
    }

    //提供给native使用,
    function _dispatchMessageFromNative(messageJSON) {
        setTimeout(function() {
            var message = JSON.parse(messageJSON);
            var responseCallback;
            //java call finished, now need to call js callback function
            if (message.responseId) {
                responseCallback = responseCallbacks[message.responseId];
                if (!responseCallback) {
                    return;
                }
                responseCallback(message.responseData);
                delete responseCallbacks[message.responseId];
            } else {
                //直接发送
                if (message.callbackId) {
                    var callbackResponseId = message.callbackId;
                    responseCallback = function(responseData) {
                        _doSend({
                            responseId: callbackResponseId,
                            responseData: responseData
                        });
                    };
                }

                var handler = CmlBridge._messageHandler;
                if (message.handlerName) {
                    handler = messageHandlers[message.handlerName];
                }
                //查找指定handler
                try {
                    handler(message.data, responseCallback);
                } catch (exception) {
                    if (typeof console != 'undefined') {
                        console.log("CmlBridge: WARNING: javascript handler threw.", message, exception);
                    }
                }
            }
        });
    }

//    //提供给native调用,receiveMessageQueue 在会在页面加载完后赋值为null,所以
//    function channel(protocol) {
//        console.log(messageJSON);
//        if (receiveMessageQueue) {
//            receiveMessageQueue.push(messageJSON);
//        }
//        _dispatchMessageFromNative(messageJSON);
//
//    }

    var CmlBridge = window.CmlBridge = {
        init: init,
        send: send,
        registerHandler: registerHandler,
        callHandler: callHandler,
        fetchQueue: fetchQueue,
        channel: channel,
        channelJsToNative: channelNativeToJs
    };

    var doc = document;
    _createQueueReadyIframe(doc);
    _createQueueReadyIframe4biz(doc);
    var readyEvent = doc.createEvent('Events');
    readyEvent.initEvent('CmlBridgeReady');
    readyEvent.bridge = CmlBridge;
    doc.dispatchEvent(readyEvent);
})();
