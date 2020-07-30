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
package com.didi.chameleon.sdk.adapter.http;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CmlHttpAdapterDefault implements ICmlHttpAdapter {

    private static final IEventReporterDelegate DEFAULT_DELEGATE = new NOPEventReportDelegate();
    private ExecutorService mExecutorService;
    protected CookieManager cookieManager = new CookieManager();

    public CmlHttpAdapterDefault() {
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
    }

    private void execute(Runnable runnable) {
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(3);
        }
        mExecutorService.execute(runnable);
    }

    @Override
    public void sendRequest(final CmlRequest request, final OnHttpListener listener) {
        if (listener != null) {
            listener.onHttpStart();
        }
        execute(new Runnable() {
            @Override
            public void run() {
                CmlResponse response = new CmlResponse();
                IEventReporterDelegate reporter = getEventReporterDelegate();
                try {
                    HttpURLConnection connection = openConnection(request, listener);
                    reporter.preConnect(connection, request.body);
                    Map<String, List<String>> headers = connection.getHeaderFields();
                    int responseCode = connection.getResponseCode();
                    if (listener != null) {
                        listener.onHeadersReceived(responseCode, headers);
                    }
                    reporter.postConnect();

                    response.statusCode = String.valueOf(responseCode);
                    if (responseCode >= 200 && responseCode <= 299) {
                        InputStream rawStream = connection.getInputStream();
                        rawStream = reporter.interpretResponseStream(rawStream);
                        response.originalData = readInputStreamAsBytes(rawStream, listener);
                    } else {
                        response.errorMsg = readInputStream(connection.getErrorStream(), listener);
                    }
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        saveCookie(request.url, headers);
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
                    if (e instanceof IOException) {
                        try {
                            reporter.httpExchangeFailed((IOException) e);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    /**
     * Opens an {@link HttpURLConnection} with parameters.
     */
    private HttpURLConnection openConnection(CmlRequest request, OnHttpListener listener) throws IOException {
        URL url = new URL(request.url);
        HttpURLConnection connection = createConnection(url);
        connection.setConnectTimeout(request.timeoutMs);
        connection.setReadTimeout(request.timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        String cookie = getCookie(request.url);
        if (!TextUtils.isEmpty(cookie)) {
            connection.setRequestProperty("Cookie", cookie);
        }

        if (request.paramMap != null) {
            Set<String> keySets = request.paramMap.keySet();
            for (String key : keySets) {
                connection.addRequestProperty(key, request.paramMap.get(key));
            }
        }

        if ("POST".equals(request.method) || "PUT".equals(request.method) || "PATCH".equals(request.method)) {
            connection.setRequestMethod(request.method);
            if (request.body != null) {
                if (listener != null) {
                    listener.onHttpUploadProgress(0);
                }
                connection.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.write(request.body.getBytes());
                out.close();
                if (listener != null) {
                    listener.onHttpUploadProgress(100);
                }
            }
        } else if (!TextUtils.isEmpty(request.method)) {
            connection.setRequestMethod(request.method);
        } else {
            connection.setRequestMethod("GET");
        }

        return connection;
    }

    private byte[] readInputStreamAsBytes(InputStream inputStream, OnHttpListener listener) throws IOException {
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
                listener.onHttpResponseProgress(readCount);
            }
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    private String readInputStream(InputStream inputStream, OnHttpListener listener) throws IOException {
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
                listener.onHttpResponseProgress(builder.length());
            }
        }
        localBufferedReader.close();
        return builder.toString();
    }

    /**
     * Create an {@link HttpURLConnection} for the specified {@code url}.
     */
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    public @NonNull
    IEventReporterDelegate getEventReporterDelegate() {
        return DEFAULT_DELEGATE;
    }


    public String getCookie(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        try {
            URI uri = URI.create(url);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                // 6.0版本以下，CookieManager内部判断的是http，故替换scheme适配版本差异
                uri = new URI("http", uri.getHost(), null, null);
            }
            List<HttpCookie> cookies = cookieManager.getCookieStore().get(uri);
            if (cookies != null && cookies.size() > 0) {
                return TextUtils.join(";", cookies);
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public void saveCookie(String url, Map<String, List<String>> responseHeaders) {
        // make sure our args are valid
        if ((TextUtils.isEmpty(url)) || (responseHeaders == null)) return;

        try {
            cookieManager.put(URI.create(url), responseHeaders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface IEventReporterDelegate {
        void preConnect(HttpURLConnection connection, @Nullable String body);

        void postConnect();

        InputStream interpretResponseStream(@Nullable InputStream inputStream);

        void httpExchangeFailed(IOException e);
    }

    private static class NOPEventReportDelegate implements IEventReporterDelegate {
        @Override
        public void preConnect(HttpURLConnection connection, @Nullable String body) {
            //do nothing
        }

        @Override
        public void postConnect() {
            //do nothing
        }

        @Override
        public InputStream interpretResponseStream(@Nullable InputStream inputStream) {
            return inputStream;
        }

        @Override
        public void httpExchangeFailed(IOException e) {
            //do nothing
        }
    }
}
