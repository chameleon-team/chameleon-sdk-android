package cn.zkml.care.member.module.audio.module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//import app.eeui.framework.extend.module.eeuiPage;
//import app.eeui.framework.ui.eeui;
import cn.zkml.care.member.module.eeuiHtml;
import eeui.android.audio.event.AudioEvent;
import eeui.android.audio.service.BackService;
import eeui.android.audio.service.MusicService;

@CmlModule(alias = "audio2")
public class CmlAudioModule2 extends WXModule {

    private static CmlCallback callback;

    private boolean isBool = false;

    public CmlAudioModule2() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @CmlMethod(alias = "create", uiThread = false)
    public void create(final CmlCallback<JSONObject> callback){
        JSONObject result = new JSONObject();
        callback.onCallback(result);
    }

    @CmlMethod(alias = "play")
    public void play(String url) {
        url = eeuiHtml.repairUrl(mWXSDKInstance, url);
        if (MusicService.getService().playNext(url)) {
            return;
        }
        Intent in = new Intent(mWXSDKInstance.getContext(), BackService.class);
        in.putExtra("url", url);
        in.putExtra("loop", isBool);
        mWXSDKInstance.getContext().startService(in);
    }

    @CmlMethod(alias = "pause")
    public void pause() {
        MusicService.getService().pause();
    }

    @CmlMethod(alias = "stop")
    public void stop() {
        MusicService.getService().stop();
    }

    @CmlMethod(alias = "seek")
    public void seek(int msec) {
        MusicService.getService().seek(msec);
    }

    @CmlMethod(alias = "isPlay")
    public boolean isPlay() {
        return MusicService.getService().isPlay();
    }

    @CmlMethod(alias = "volume")
    public void volume(int volume) {
        MusicService.getService().volume(volume);
    }

    @CmlMethod(alias = "loop")
    public void loop(boolean loop) {
        isBool = loop;
        MusicService.getService().setLoop(isBool);
    }

    @CmlMethod(alias = "setCallback")
    public void setCallback(CmlCallback call) {
        callback = call;
    }

    @CmlMethod(alias = "getDuration")
    public void getDuration(String url, JSCallback call) {
        new PlayAsyncTask().execute(url, call);
    }

    @SuppressLint("StaticFieldLeak")
    private class PlayAsyncTask extends AsyncTask<Object, Integer, Object> {
        @Override
        protected Object doInBackground(Object... objects) {
            String url = eeuiHtml.repairUrl(mWXSDKInstance, String.valueOf(objects[0]));
            JSCallback call = (JSCallback) objects[1];
            MediaPlayer player = new MediaPlayer();
            try {
                if (url.startsWith("file://assets/")) {
                    AssetFileDescriptor assetFile = CmlEngine.getInstance().getAppContext().getAssets().openFd(url.substring(14));
                    player.setDataSource(assetFile.getFileDescriptor(), assetFile.getStartOffset(), assetFile.getLength());
                }else{
                    player.setDataSource(url);
                }
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            float duration = player.getDuration();
            player.release();
            if (call != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("duration", duration);
                data.put("url", url);
                call.invoke(data);
            }
            return null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AudioEvent e) {
        if (callback == null) {
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("url", e.url == null ? "" : e.url);
        data.put("current", e.current);
        data.put("duration", e.total);
        data.put("percent", (e.total == 0 ? 0 : e.current / (float) e.total));
        switch (e.state) {
            case AudioEvent.STATE_STARTPLAY:
                data.put("status", "start");
                break;
            case AudioEvent.STATE_PLAY:
                data.put("status", "play");
                break;
            case AudioEvent.STATE_COMPELETE:
                data.put("status", "compelete");
                break;
            case AudioEvent.STATE_ERROR:
                data.put("status", "error");
                break;
            case AudioEvent.STATE_SEEK_COMPELETE:
                data.put("status", "seek");
                break;
            case AudioEvent.STATE_BufferingUpdate:
                data.put("status", "buffering");
                break;
            default:
                return;
        }
        //callback.invokeAndKeepAlive(data);
        callback.onCallback(data)
    }
}
