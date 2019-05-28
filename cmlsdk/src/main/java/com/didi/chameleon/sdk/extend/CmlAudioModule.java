package com.didi.chameleon.sdk.extend;

import android.media.MediaPlayer;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlCallbackSimple;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.didi.chameleon.sdk.module.CmlParam;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CmlModule(alias = "audio", global = false)
public class CmlAudioModule {

    private static final int ERROR_ID_FAIL = 2;      //错误-playerId有误
    private static final int ERROR_STATUS_FAIL = 3;  //错误-player状态异常

    private static final String INFO_BUFFERING_CHANGE = "onBufferingChange";
    private static final String INFO_STATUS_CHANGE = "onStatusChange";

    private static final int STATUS_INIT = 1;    //初始化
    private static final int STATUS_PLAY = 2;    //播放
    private static final int STATUS_PAUSE = 3;   //暂停
    private static final int STATUS_FINISH = 4;  //结束

    private ICmlInstance instance;
    private List<MediaPlayer> playerList = new ArrayList<>(1);

    public CmlAudioModule(ICmlInstance instance) {
        this.instance = instance;
        CmlInstanceManage.getInstance().registerDestroyListener(instance.getInstanceId(), new CmlInstanceManage.CmlInstanceDestroyListener() {
            @Override
            public void onDestroy() {
                release();
            }
        });
    }

    @CmlMethod(alias = "create", uiThread = false)
    public void create(@CmlParam(name = "url") String audioUrl,
                       @CmlParam(name = "volume") double volume,
                       @CmlParam(name = "looping", admin = "0") String looping,
                       CmlCallback<JSONObject> callback) {
        final int id = playerList.size();
        MediaPlayer player = new MediaPlayer();
        playerList.add(player);
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                callJsInfo(INFO_BUFFERING_CHANGE, id, Collections.singletonMap("percent", percent));
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                callJsInfo(INFO_STATUS_CHANGE, id, Collections.singletonMap("status", STATUS_FINISH));
            }
        });

        //设置曲目
        try {
            player.setLooping("1".equals(looping));
            player.setDataSource(audioUrl);
            player.prepare();
            player.setVolume((float) volume, (float) volume);
        } catch (IOException e) {
            reportFail(callback, e);
            return;
        } catch (IllegalStateException e2) {
            reportStatusFail(callback, e2);
            return;
        }

        JSONObject result = new JSONObject();
        try {
            result.put("id", id);
            result.put("duration", player.getDuration());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        callback.onCallback(result);
        callJsInfo(INFO_STATUS_CHANGE, id, Collections.singletonMap("status", STATUS_INIT));
    }

    @CmlMethod(alias = "destroy", uiThread = false)
    public void destroy(@CmlParam(name = "id") int playerId,
                        CmlCallbackSimple callback) {
        if (unablePlayerId(playerId)) {
            reportIdFail(callback);
            return;
        }
        MediaPlayer player = playerList.get(playerId);
        player.release();
        playerList.set(playerId, null);
        callback.onSuccess();
    }

    @CmlMethod(alias = "play", uiThread = false)
    public void play(@CmlParam(name = "id") int playerId,
                     CmlCallbackSimple callback) {
        if (unablePlayerId(playerId)) {
            reportIdFail(callback);
            return;
        }
        MediaPlayer player = playerList.get(playerId);
        //开始
        try {
            player.start();
        } catch (IllegalStateException e) {
            reportStatusFail(callback, e);
            return;
        }
        callback.onSuccess();
        callJsInfo(INFO_STATUS_CHANGE, playerId, Collections.singletonMap("status", STATUS_PLAY));
    }

    @CmlMethod(alias = "pause", uiThread = false)
    public void pause(@CmlParam(name = "id") int playerId,
                      CmlCallbackSimple callback) {
        if (unablePlayerId(playerId)) {
            reportIdFail(callback);
            return;
        }
        MediaPlayer player = playerList.get(playerId);
        //暂停
        try {
            player.pause();
        } catch (IllegalStateException e) {
            reportStatusFail(callback, e);
            return;
        }
        callback.onSuccess();
        callJsInfo(INFO_STATUS_CHANGE, playerId, Collections.singletonMap("status", STATUS_PAUSE));
    }

    @CmlMethod(alias = "seekTo", uiThread = false)
    public void seekTo(@CmlParam(name = "id") int playerId,
                       @CmlParam(name = "msec") int mill,
                       CmlCallbackSimple callback) {
        if (unablePlayerId(playerId)) {
            reportIdFail(callback);
            return;
        }
        MediaPlayer player = playerList.get(playerId);
        //进度条
        try {
            player.seekTo(mill);
        } catch (IllegalStateException e) {
            reportStatusFail(callback, e);
            return;
        }
        callback.onSuccess();
    }

    @CmlMethod(alias = "currentPos", uiThread = false)
    public void currentPos(@CmlParam(name = "id") int playerId,
                           CmlCallback<JSONObject> callback) {
        if (unablePlayerId(playerId)) {
            reportIdFail(callback);
            return;
        }
        MediaPlayer player = playerList.get(playerId);
        //获取当前进度
        int currentPos = player.getCurrentPosition();
        JSONObject result = new JSONObject();
        try {
            result.put("msec", currentPos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        callback.onCallback(result);
    }

    private void release() {
        for (MediaPlayer player : playerList) {
            player.release();
        }
        playerList.clear();
    }

    private boolean unablePlayerId(int id) {
        return id < 0 || id >= playerList.size();
    }

    private void reportFail(CmlCallback callback, Exception exception) {
        CmlLogUtil.et(exception);
        callback.onError(CmlCallback.ERROR_DEFAULT, exception.getMessage());
    }

    private void reportIdFail(CmlCallback callback) {
        callback.onError(ERROR_ID_FAIL, "player id fail");
    }

    private void reportStatusFail(CmlCallback callback, Exception exception) {
        CmlLogUtil.et(exception);
        callback.onError(ERROR_STATUS_FAIL, "player status fail");
    }

    private <T> void callJsInfo(String method, int playId, Map<String, T> param) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", playId);
            if (param != null) {
                for (Map.Entry<String, T> item : param.entrySet()) {
                    jsonObject.put(item.getKey(), item.getValue());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CmlEngine.getInstance().callToJs(instance, "audio", method, jsonObject.toString(), null);
    }

}
