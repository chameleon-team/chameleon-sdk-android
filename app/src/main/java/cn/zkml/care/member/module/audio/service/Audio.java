package eeui.android.audio.service;

import android.media.MediaPlayer;
import android.os.Environment;

import com.instapp.nat.media.audio.Constant;
import com.instapp.nat.media.audio.ModuleResultListener;
import com.instapp.nat.media.audio.Util;

public class Audio {
    private MediaPlayer mediaPlayer;
    private boolean mIsPausing;
    private boolean mIsPlaying;
    String mCurrentPath;
    private static volatile Audio instance = null;

    private Audio() {
    }

    public static Audio getInstance() {
        if (instance == null) {
            synchronized (Audio.class) {
                if (instance == null) {
                    instance = new Audio();
                }
            }
        }
        return instance;
    }

    public void play(String path, final ModuleResultListener listener) {
        if (listener == null) return;
        try {
            boolean valid = path.startsWith("http://") || path.startsWith("https://") || path.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (path.equals("") || !valid) {
                listener.onResult(Util.getError(Constant.MEDIA_SRC_NOT_SUPPORTED, Constant.MEDIA_SRC_NOT_SUPPORTED_CODE));
                return;
            }
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepareAsync();
            } else {
                if (mCurrentPath.equals(path)) {
                    if (mIsPausing) {
                        mediaPlayer.start();
                        mIsPlaying = true;
                        mIsPausing = false;
                        listener.onResult(null);
                    }
                    return;
                } else {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepareAsync();
                }
            }
            mCurrentPath = path;
            // listener
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mIsPlaying = true;
                    mIsPausing = false;
                    listener.onResult(null);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    switch (i1) {
                        case MediaPlayer.MEDIA_ERROR_IO:
                            listener.onResult(Util.getError(Constant.MEDIA_FILE_TYPE_NOT_SUPPORTED, Constant.MEDIA_FILE_TYPE_NOT_SUPPORTED_CODE));
                            break;
                        case MediaPlayer.MEDIA_ERROR_MALFORMED:
                        case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                            listener.onResult(Util.getError(Constant.MEDIA_DECODE_ERROR, Constant.MEDIA_DECODE_ERROR_CODE));
                            break;
                    }
                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    mediaPlayer = null;
                    mIsPlaying = false;
                    mIsPausing = false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onResult(Util.getError(e.getMessage(), 1));
        }
    }

    public void pause(ModuleResultListener listener) {
        if (!mIsPlaying) {
            listener.onResult(Util.getError(Constant.MEDIA_PLAYER_NOT_STARTED, 1));
            return;
        }
        if (mIsPausing) {
            listener.onResult(null);
            return;
        }
        mediaPlayer.pause();
        mIsPausing = true;
        listener.onResult(null);
    }

    public void stop(ModuleResultListener listener) {
        if (listener == null) return;
        if (!mIsPlaying) {
            listener.onResult(Util.getError(Constant.MEDIA_PLAYER_NOT_STARTED, 1));
            return;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            mIsPlaying = false;
            mIsPausing = false;
            listener.onResult(null);
        } else {
            listener.onResult(Util.getError(Constant.MEDIA_PLAYER_NOT_STARTED, 1));
        }
    }
}
