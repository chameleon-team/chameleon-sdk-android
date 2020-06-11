package eeui.android.audio.service;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import app.eeui.framework.ui.eeui;
import eeui.android.audio.event.AudioEvent;

@SuppressLint({"HandlerLeak", "StaticFieldLeak"})
public class MusicService {

    private Timer timer;
    private String url;
    private static MediaPlayer mPlayer = null;
    private static MusicService service;

    private class PlayAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            setUrl(strings[0]);
            setListener();
            statTimer();
            try {
                mPlayer.start();
            }catch (Exception e) {
                e.printStackTrace();
            }
            eventPost(url, AudioEvent.STATE_STARTPLAY);
            return null;
        }
    }

    public static MusicService getService() {
        if (service == null)
            service = new MusicService();
        return service;
    }

    public void release() {
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    public void setUrl(String url) {
        if (url == null || url.equals(this.url)) {
            return;
        }
        this.url = url;
        try {
            if (mPlayer != null) {
                release();
            }
            mPlayer = null;
            mPlayer = new MediaPlayer();
            if (url.startsWith("file://assets/")) {
                AssetFileDescriptor assetFile = eeui.getApplication().getAssets().openFd(url.substring(14));
                mPlayer.setDataSource(assetFile.getFileDescriptor(), assetFile.getStartOffset(), assetFile.getLength());
            }else{
                mPlayer.setDataSource(url);
            }
            mPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(String url) {
        new PlayAsyncTask().execute(url);
    }

    public boolean playNext(String url) {
        if (mPlayer == null) {
            return false;
        }
        if (url == null || url.equals(this.url)) {
            return false;
        }
        this.url = url;
        try {
            mPlayer.reset();
            if (url.startsWith("file://assets/")) {
                AssetFileDescriptor assetFile = eeui.getApplication().getAssets().openFd(url.substring(14));
                mPlayer.setDataSource(assetFile.getFileDescriptor(), assetFile.getStartOffset(), assetFile.getLength());
            }else{
                mPlayer.setDataSource(url);
            }
            mPlayer.prepare();
            statTimer();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    public void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            removeListener();
            mPlayer = null;
            this.url = null;
        }
    }

    public void seek(int msec) {
        if (mPlayer != null) {
            mPlayer.seekTo(msec);
        }
    }

    public boolean isPlay() {
        if (mPlayer != null) {
            mPlayer.isPlaying();
        }
        return false;
    }

    public void volume(int vo) {
        if (mPlayer != null) {
            mPlayer.setVolume(vo, vo);
        }
    }

    public void setLoop(boolean loop) {
        if (mPlayer != null) {
            mPlayer.setLooping(loop);
        }
    }

    public void setListener() {
        if (mPlayer != null) {
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    eventPost(url, AudioEvent.STATE_PREPARED);
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    eventPost(url, AudioEvent.STATE_COMPELETE);
                    cancelTimer();
                }
            });
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    eventPost(url, AudioEvent.STATE_ERROR);
                    cancelTimer();
                    if (i == -38) {
                        error38reStart();
                    }
                    return false;
                }
            });
            mPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer) {
                    eventPost(url, AudioEvent.STATE_SEEK_COMPELETE);
                }
            });
            mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                    eventPost(url, AudioEvent.STATE_BufferingUpdate);
                }
            });
        }
    }

    public void removeListener() {
        if (mPlayer != null) {
            mPlayer.setOnPreparedListener(null);
            mPlayer.setOnCompletionListener(null);
            mPlayer.setOnErrorListener(null);
            mPlayer.setOnSeekCompleteListener(null);
            mPlayer.setOnBufferingUpdateListener(null);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && mPlayer != null) {
                eventPost(url, AudioEvent.STATE_PLAY);
            }
            super.handleMessage(msg);
        }
    };

    private int error38reNum = 0;
    private void error38reStart() {
        error38reNum++;
        final int temp = error38reNum;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPlayer == null || temp != error38reNum) {
                    return;
                }
                statTimer();
                mPlayer.start();
            }
        }, 3000);
    }

    private void eventPost(String url, int state) {
        try {
            if (mPlayer == null) {
                EventBus.getDefault().post(new AudioEvent(url, state));
            }else{
                EventBus.getDefault().post(new AudioEvent(url, mPlayer.getCurrentPosition(), mPlayer.getDuration(), state));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelTimer() {
        if (timer != null)
            timer.cancel();
    }

    public void statTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 0, 500);
    }
}
