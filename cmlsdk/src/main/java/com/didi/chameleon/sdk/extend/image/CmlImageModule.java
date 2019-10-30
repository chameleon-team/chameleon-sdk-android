package com.didi.chameleon.sdk.extend.image;

import android.content.Context;
import android.content.Intent;

import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlJoin;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlParam;

import org.json.JSONException;
import org.json.JSONObject;

@CmlJoin(name = "cml")
public class CmlImageModule {

    @CmlMethod(alias = "chooseImage")
    public void photograph(Context context, @CmlParam(name = "type") String type,
                           @CmlParam(name = "width") String width, @CmlParam(name = "height") String height,
                           @CmlParam(name = "cut") boolean cut, @CmlParam(name = "quality") String quality,
                           final CmlCallback<JSONObject> callback) {
        if ("choice".equals(type)) {
            type = "";
        }

        Intent intent = new Intent();
        intent.setClass(context, CmlImageActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra("cut", cut);
        intent.putExtra("quality", quality);

        CmlImageActivity.setImageCallback(new CmlImageCallback() {

            @Override
            public void onSuccess(String image, String imageType) {
                JSONObject result = new JSONObject();
                try {
                    image = image.replace("\n", "");
                    result.put("image", image);
                    result.put("type", imageType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onCallback(result);
            }

            @Override
            public void onFail() {
                callback.onError(1);
            }

            @Override
            public void onCancel() {
                callback.onError(2);
            }

            @Override
            public void onPermissionFail() {
                callback.onError(3);
            }
        });
        context.startActivity(intent);
    }

}
