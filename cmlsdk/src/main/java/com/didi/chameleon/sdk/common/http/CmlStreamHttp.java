package com.didi.chameleon.sdk.common.http;

import android.net.Uri;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.adapter.http.CmlHttpAdapter;
import com.didi.chameleon.sdk.adapter.http.CmlHttpAdapterDefault;
import com.didi.chameleon.sdk.adapter.http.CmlRequest;
import com.didi.chameleon.sdk.adapter.http.CmlResponse;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmlStreamHttp {

    private static final String STATUS_TEXT = "statusText";
    private static final String STATUS = "status";
    private final CmlHttpAdapter mAdapter;
    static final Pattern CHARSET_PATTERN = Pattern.compile("charset=([a-z0-9-]+)");

    public CmlStreamHttp(CmlHttpAdapter adapter) {
        if (adapter == null) {
            adapter = new CmlHttpAdapterDefault();
        }
        mAdapter = adapter;
    }

    public void fetch(String method, String url, JSONObject headers, String body, String type, int timeout,
                      final CmlCallback<Map> callback, CmlCallback<Map> progressCallback) {
        if (method != null) method = method.toUpperCase();
        CmlOptions.Builder builder = new CmlOptions.Builder()
                .setMethod(!"GET".equals(method)
                        && !"POST".equals(method)
                        && !"PUT".equals(method)
                        && !"DELETE".equals(method)
                        && !"HEAD".equals(method)
                        && !"PATCH".equals(method) ? "GET" : method)
                .setUrl(url)
                .setBody(body)
                .setType(type)
                .setTimeout(timeout);

        extractHeaders(headers, builder);
        final CmlOptions options = builder.createOptions();
        sendRequest(options, new ResponseCallback() {
            @Override
            public void onResponse(CmlResponse response, Map<String, String> headers) {
                if (callback != null) {
                    Map<String, Object> resp = new HashMap<>();
                    if (response == null || "-1".equals(response.statusCode)) {
                        resp.put(STATUS, -1);
                        resp.put(STATUS_TEXT, CmlStatus.ERR_CONNECT_FAILED);
                    } else {
                        int code = Integer.parseInt(response.statusCode);
                        resp.put(STATUS, code);
                        resp.put("ok", (code >= 200 && code <= 299));
                        if (response.originalData == null) {
                            resp.put("data", null);
                        } else {
                            String respData = readAsString(response.originalData,
                                    headers != null ? getHeader(headers, "Content-Type") : ""
                            );
                            try {
                                resp.put("data", parseData(respData, options.getType()));
                            } catch (JSONException exception) {
                                CmlLogUtil.et(exception);
                                resp.put("ok", false);
                                resp.put("data", "{'err':'Data parse failed!'}");
                            }
                        }
                        resp.put(STATUS_TEXT, CmlStatus.getStatusText(response.statusCode));
                    }
                    resp.put("headers", headers);
                    callback.onCallback(resp);
                }
            }
        }, progressCallback);
    }

    Object parseData(String data, CmlOptions.Type type) throws JSONException {
        if (type == CmlOptions.Type.json) {
            return new JSONObject(data);
        } else if (type == CmlOptions.Type.jsonp) {
            if (data == null || data.isEmpty()) {
                return new JSONObject();
            }
            int b = data.indexOf("(") + 1;
            int e = data.lastIndexOf(")");
            if (b == 0 || b >= e || e <= 0) {
                return new JSONObject();
            }

            data = data.substring(b, e);
            return new JSONObject(data);
        } else {
            return data;
        }
    }

    static String getHeader(Map<String, String> headers, String key) {
        if (headers == null || key == null) {
            return null;
        }
        if (headers.containsKey(key)) {
            return headers.get(key);
        } else {
            return headers.get(key.toLowerCase());
        }
    }


    static String readAsString(byte[] data, String cType) {
        String charset = "utf-8";
        if (cType != null) {
            Matcher matcher = CHARSET_PATTERN.matcher(cType.toLowerCase());
            if (matcher.find()) {
                charset = matcher.group(1);
            }
        }
        try {
            return new String(data, charset);
        } catch (UnsupportedEncodingException e) {
            CmlLogUtil.et(e);
            return new String(data);
        }
    }


    private void extractHeaders(JSONObject headers, CmlOptions.Builder builder) {
        //set user-agent
        String UA = "cml admin";
        if (headers != null) {
            Iterator<String> iterator = headers.keys();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.equals("user-agent")) {
                    UA = headers.optString(key);
                    continue;
                }
                builder.putHeader(key, headers.optString(key));
            }
        }
        builder.putHeader("user-agent", UA);
    }


    private void sendRequest(CmlOptions options, ResponseCallback callback, CmlCallback<Map> progressCallback) {
        CmlRequest cmlRequest = new CmlRequest();
        cmlRequest.method = options.getMethod();
        cmlRequest.url = Uri.parse(options.getUrl()).toString();
        cmlRequest.body = options.getBody();
        cmlRequest.timeoutMs = options.getTimeout();

        if (options.getHeaders() != null) {
            if (cmlRequest.paramMap == null) {
                cmlRequest.paramMap = options.getHeaders();
            } else {
                cmlRequest.paramMap.putAll(options.getHeaders());
            }
        }

        mAdapter.sendRequest(cmlRequest, new StreamHttpListener(callback, progressCallback));
    }

    private interface ResponseCallback {
        void onResponse(CmlResponse response, Map<String, String> headers);
    }

    private static class StreamHttpListener implements CmlHttpAdapter.OnHttpListener {
        private ResponseCallback mCallback;
        private CmlCallback<Map> mProgressCallback;
        private Map<String, Object> mResponse = new HashMap<>();
        private Map<String, String> mRespHeaders;

        private StreamHttpListener(ResponseCallback callback, CmlCallback<Map> progressCallback) {
            mCallback = callback;
            // 目前不考虑网络请求状态的回调
//            mProgressCallback = progressCallback;
        }


        @Override
        public void onHttpStart() {
            if (mProgressCallback != null) {
                mResponse.put("readyState", 1);//readyState: number 1 OPENED 2 HEADERS_RECEIVED 3 LOADING
                mResponse.put("length", 0);
                mProgressCallback.onCallback(new HashMap<>(mResponse));
            }
        }

        @Override
        public void onHttpUploadProgress(int uploadProgress) {

        }

        @Override
        public void onHeadersReceived(int statusCode, Map<String, List<String>> headers) {
            mResponse.put("readyState", 2);
            mResponse.put("status", statusCode);

            Map<String, String> simpleHeaders = new HashMap<>();
            if (headers != null) {
                Iterator<Map.Entry<String, List<String>>> it = headers.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, List<String>> entry = it.next();
                    if (entry.getValue().size() > 0) {
                        simpleHeaders.put(entry.getKey() == null ? "_" : entry.getKey(), entry.getValue().get(0));
                    }
                }
            }

            mResponse.put("headers", simpleHeaders);
            mRespHeaders = simpleHeaders;
            if (mProgressCallback != null) {
                mProgressCallback.onCallback(new HashMap<>(mResponse));
            }
        }

        @Override
        public void onHttpResponseProgress(int loadedLength) {
            mResponse.put("length", loadedLength);
            if (mProgressCallback != null) {
                mProgressCallback.onCallback(new HashMap<>(mResponse));
            }

        }

        @Override
        public void onHttpFinish(final CmlResponse response) {
            //compatible with old sendhttp
            if (mCallback != null) {
                mCallback.onResponse(response, mRespHeaders);
            }

            if (CmlEnvironment.DEBUG) {
                CmlLogUtil.d("cmlStreamModule", response != null && response.originalData != null ? new String(response.originalData) : "response data is NUll!");
            }
        }
    }

}
