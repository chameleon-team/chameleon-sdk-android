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

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

/**
 * limeihong
 * create at 2018/10/12
 */
public class CmlHttpClient {


    public void execute(final CmlRequest request, ICmlHttpListener listener) {
        if (listener != null) {
            listener.onHttpStart();
        }
        CmlResponse response = new CmlResponse();
        try {
            HttpURLConnection connection = openConnection(request);
            int responseCode = connection.getResponseCode();
            response.statusCode = String.valueOf(responseCode);
            response.header = connection.getHeaderFields();
            if (responseCode >= 200 && responseCode <= 299) {
                InputStream rawStream = connection.getInputStream();
                response.data = readInputStreamAsBytes(rawStream, connection.getContentLength(), listener);
            } else if (responseCode == 304) {
                response.data = null;
            } else {
                response.errorMsg = readInputStream(connection.getErrorStream(), connection.getContentLength(), listener);
            }
            if (listener != null) {
                listener.onHttpFinish(response);
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            response.statusCode = "-1";
            response.errorCode = "-1";
            response.errorMsg = e.getMessage();
            if (listener != null) {
                listener.onHttpFinish(response);
            }
        }
    }


    private HttpURLConnection openConnection(CmlRequest request) throws IOException {
        URL url = new URL(request.url);
        HttpURLConnection connection = createConnection(url);
        connection.setConnectTimeout(request.timeoutMs);
        connection.setReadTimeout(request.timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setRequestProperty("Accept-Encoding", "identity");

        if (request.paramMap != null) {
            Set<String> keySets = request.paramMap.keySet();
            for (String key : keySets) {
                connection.addRequestProperty(key, request.paramMap.get(key));
            }
        }

        if ("POST".equals(request.method) || "PUT".equals(request.method) || "PATCH".equals(request.method)) {
            connection.setRequestMethod(request.method);
            if (request.body != null) {
                connection.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                //TODO big stream will cause OOM; Progress callback is meaningless
                out.write(request.body.getBytes());
                out.close();
            }
        } else if (!TextUtils.isEmpty(request.method)) {
            connection.setRequestMethod(request.method);
        } else {
            connection.setRequestMethod("GET");
        }

        return connection;
    }

    /**
     * Create an {@link HttpURLConnection} for the specified {@code url}.
     */
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    private byte[] readInputStreamAsBytes(InputStream inputStream, int byteCount, ICmlHttpListener listener) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        int readCount = 0;
        byte[] data = new byte[2048];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
            readCount += nRead;
            if (listener != null) {
                listener.onHttpProgress(readCount, byteCount);
            }
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    private String readInputStream(InputStream inputStream, int byteCount, ICmlHttpListener listener) throws IOException {
        if (inputStream == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] data = new char[2048];
        int len;
        while ((len = localBufferedReader.read(data)) != -1) {
            builder.append(data, 0, len);
            if (listener != null) {
                listener.onHttpProgress(builder.length(), byteCount);
            }
        }
        localBufferedReader.close();
        return builder.toString();
    }
}
