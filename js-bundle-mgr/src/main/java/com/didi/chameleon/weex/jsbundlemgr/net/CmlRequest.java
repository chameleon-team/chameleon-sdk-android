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

import java.util.Map;

/**
 * limeihong
 * create at 2018/10/12
 */
public class CmlRequest {
    /**
     * 请求参数
     */
    public Map<String, String> paramMap;

    /**
     * 请求url
     */
    public String url;
    /**
     * 请求Method，如GET，POST，PUT，DELETE
     */
    public String method;
    /**
     * 请求body
     */
    public String body;

    /**
     * 请求超时时间，默认为3s
     */
    public int timeoutMs = 3000;

    @Override
    public String toString() {
        return "CmlRequest{" +
                "paramMap=" + paramMap +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", body='" + body + '\'' +
                ", timeoutMs=" + timeoutMs +
                '}';
    }
}
