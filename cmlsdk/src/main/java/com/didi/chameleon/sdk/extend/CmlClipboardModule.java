package com.didi.chameleon.sdk.extend;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlCallbackSimple;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.didi.chameleon.sdk.module.CmlParam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

@CmlModule(alias = "clipboard")
public class CmlClipboardModule {

    public static final String CLIP_KEY = "CML_CLIP_KEY_MAIN";

    @CmlMethod(alias = "setClipBoardData")
    public void setString(Context context, @CmlParam(name = "data") String text, CmlCallbackSimple callback) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) {
            callback.onFail();
            return;
        }
        ClipData clip = ClipData.newPlainText(CLIP_KEY, text);
        clipboard.setPrimaryClip(clip);
        callback.onSuccess();
    }

    @CmlMethod(alias = "getClipBoardData")
    public void getString(Context context, CmlCallback<String> callback) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) {
            callback.onError(CmlCallback.ERROR_DEFAULT);
            return;
        }
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            ClipData.Item item = clip.getItemAt(0);
            CharSequence text = coerceToText(context, item);
            callback.onCallback(String.valueOf(text));
        } else {
            callback.onCallback("");
        }
    }

    @Nullable
    private CharSequence coerceToText(Context context, ClipData.Item item) {
        CharSequence text = item.getText();
        if (text != null) {
            return text;
        }

        Uri uri = item.getUri();
        if (uri != null) {
            InputStreamReader reader = null;
            FileInputStream stream = null;
            try {
                AssetFileDescriptor assetFileDescriptor = context.getContentResolver().openTypedAssetFileDescriptor(uri, "text/*", null);
                if (assetFileDescriptor == null) {
                    return null;
                }
                stream = assetFileDescriptor.createInputStream();
                reader = new InputStreamReader(stream, "UTF-8");

                StringBuilder builder = new StringBuilder(128);
                char[] buffer = new char[8192];
                int len;
                while ((len = reader.read(buffer)) > 0) {
                    builder.append(buffer, 0, len);
                }
                return builder.toString();

            } catch (FileNotFoundException ignore) {
            } catch (IOException ignore) {
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignore) {
                    }
                }
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ignore) {
                    }
                }
            }

            return uri.toString();
        }

        Intent intent = item.getIntent();
        if (intent != null) {
            return intent.toUri(Intent.URI_INTENT_SCHEME);
        }

        return null;
    }

}
