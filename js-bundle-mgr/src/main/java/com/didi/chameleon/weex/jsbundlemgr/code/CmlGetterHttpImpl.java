package com.didi.chameleon.weex.jsbundlemgr.code;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.didi.chameleon.weex.jsbundlemgr.net.CmlRequest;
import com.didi.chameleon.weex.jsbundlemgr.net.CmlResponse;
import com.didi.chameleon.weex.jsbundlemgr.utils.CmlUtils;

import java.util.HashMap;
import java.util.List;

public class CmlGetterHttpImpl extends CmlGetterNetImpl {

    private SharedPreferences sp;

    private ICmlCodeGetter fileGetter;

    public CmlGetterHttpImpl(Context context, ICmlCodeGetter fileGetter) {
        this.fileGetter = fileGetter;
        sp = context.getSharedPreferences("cml_js_http", Context.MODE_PRIVATE);
    }

    @Override
    public void getCode(final String url, final CmlGetCodeCallback callback) {
        if (!fileGetter.isContainsCode(url)) {
            super.getCode(url, callback);
            return;
        }
        CmlRequest request = createRequest(url);
        mDownLoader.startDownload(request, new CmlFileDownloader.FileDownloaderListener() {

            @Override
            public void onSuccess(CmlResponse response, String template) {
                if ("304".equals(response.statusCode)) {
                    fileGetter.getCode(url, callback);
                } else if (TextUtils.isEmpty(template)) {
                    CmlGetterHttpImpl.super.getCode(url, callback);
                } else {
                    storeCallbackNewRequest(url, callback);
                    handleDownLoadResult(response, url, template);
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                fileGetter.getCode(url, callback);
            }
        });
    }

    @Override
    protected void handleDownLoadResult(CmlResponse response, String url, String template) {
        if (response != null) {
            storeResponse(url, response);
        }
        super.handleDownLoadResult(response, url, template);
    }

    private CmlRequest createRequest(String url) {
        CmlRequest request = new CmlRequest(url);
        if (request.paramMap == null) {
            request.paramMap = new HashMap<>();
        }
        String md5 = CmlUtils.generateMd5(url);
        String eTagValue = sp.getString(md5 + "_ETag", "");
        if (!TextUtils.isEmpty(eTagValue)) {
            request.paramMap.put("If-None-Match", eTagValue);
        }
        String lastModValue = sp.getString(md5 + "_Last-Modified", "");
        if (!TextUtils.isEmpty(lastModValue)) {
            request.paramMap.put("If-Modified-Since", lastModValue);
        }
        return request;
    }

    private void storeResponse(String url, CmlResponse response) {
        if (response.header == null) {
            return;
        }
        String md5 = CmlUtils.generateMd5(url);
        SharedPreferences.Editor editor = sp.edit();
        List<String> eTagValue = response.header.get("ETag");
        if (eTagValue != null && eTagValue.size() > 0) {
            editor.putString(md5 + "_ETag", eTagValue.get(0));
        }
        List<String> lastModValue = response.header.get("Last-Modified");
        if (lastModValue != null && lastModValue.size() > 0) {
            editor.putString(md5 + "_Last-Modified", lastModValue.get(0));
        }
        editor.apply();
    }
}
