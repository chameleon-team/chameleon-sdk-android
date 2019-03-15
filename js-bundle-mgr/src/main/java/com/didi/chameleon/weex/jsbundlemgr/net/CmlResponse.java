/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.didi.chameleon.weex.jsbundlemgr.net;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * limeihong
 * create at 2018/10/12
 */
public class CmlResponse {
    /**
     * 请求状态码，如返回200，304，404等等
     */
    public String statusCode;

    /**
     * 返回的数据
     */
    public byte[] data;

    /**
     * 错误码
     */
    public String errorCode;

    /**
     * 出错时的错误信息
     */
    public String errorMsg;


    public Map<String, List<String>> header;


    @Override
    public String toString() {
        return "CmlResponse{" +
                "statusCode='" + statusCode + '\'' +
                ", originalData=" + Arrays.toString(data) +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
