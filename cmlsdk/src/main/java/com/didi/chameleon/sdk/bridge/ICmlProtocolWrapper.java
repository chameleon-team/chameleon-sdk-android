package com.didi.chameleon.sdk.bridge;

/**
 * 用于自定义协议转化
 */
public interface ICmlProtocolWrapper {

    CmlProtocol wrapper(CmlProtocol input);

}
