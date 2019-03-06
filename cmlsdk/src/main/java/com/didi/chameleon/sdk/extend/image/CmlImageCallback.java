package com.didi.chameleon.sdk.extend.image;

/**
 * @author xianchaohua
 */

public interface CmlImageCallback {

    void onSuccess(String image, String imageType);

    void onFail();

    void onCancel();

    void onPermissionFail();
}
