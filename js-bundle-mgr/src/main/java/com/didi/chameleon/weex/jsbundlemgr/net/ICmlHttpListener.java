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


/**
 * limeihong
 * create at 2018/10/12
 */
public interface ICmlHttpListener {
    /**
     * 开始http请求
     */
    void onHttpStart();

    /**
     * 下载进度
     *
     * @param currentLength 当前字节数
     * @param countLength   总的字节数
     */
    void onHttpProgress(int currentLength, int countLength);

    /**
     * http请求完成，返回response
     *
     * @param response
     */
    void onHttpFinish(CmlResponse response);
}
