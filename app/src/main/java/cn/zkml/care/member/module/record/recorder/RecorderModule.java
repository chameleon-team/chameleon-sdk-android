package cn.zkml.care.member.module.record.recorder;

import android.media.AudioFormat;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import omrecorder.AudioChunk;
import omrecorder.AudioSource;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;

import org.json.JSONException;
import org.json.JSONObject;

public class RecorderModule {
    private Recorder mRecorder;
    private File mFile;
    private boolean mIsRecording;
    private boolean mIsPausing;

    private static volatile RecorderModule instance = null;

    private RecorderModule(){
    }

    public static RecorderModule getInstance() {
        if (instance == null) {
            synchronized (RecorderModule.class) {
                if (instance == null) {
                    instance = new RecorderModule();
                }
            }
        }

        return instance;
    }

    public void start(HashMap<String, String> options, ModuleResultListener listener){
        int audioChannel = AudioFormat.CHANNEL_IN_STEREO;
        String channel = options.get("channel");
        String quality = options.get("quality");
        if (channel == null) channel = "";
        if (quality == null) quality = "";
        if (channel.equals("mono")) audioChannel = AudioFormat.CHANNEL_IN_MONO;
        int sampleRate = 22050;
        int audioBit = AudioFormat.ENCODING_PCM_16BIT;
        switch (quality) {
            case "low":
                sampleRate = 8000;
                audioBit = AudioFormat.ENCODING_PCM_8BIT;
                break;
            case "high":
                sampleRate = 44100;
                audioBit = AudioFormat.ENCODING_PCM_16BIT;
                break;
        }

        if (mIsRecording) {
            if (mIsPausing) {
                if (mRecorder != null)mRecorder.resumeRecording();
                mIsPausing = false;
                listener.onResult(null);
            } else {
                listener.onResult(Util.getError(Constant.RECORDER_BUSY, Constant.RECORDER_BUSY_CODE));
            }
        } else {
            String time_str = new Date().getTime() + "";
            try {
                mFile = Util.getFile(time_str + ".wav");
            } catch (IOException e) {
                e.printStackTrace();
                listener.onResult(Util.getError(Constant.MEDIA_INTERNAL_ERROR, Constant.MEDIA_INTERNAL_ERROR_CODE));
            }
            mRecorder = OmRecorder.wav(
                    new PullTransport.Default(getMic(audioBit, audioChannel, sampleRate), new PullTransport.OnAudioChunkPulledListener() {
                        @Override
                        public void onAudioChunkPulled(AudioChunk audioChunk) {

                        }
                    }), mFile);
            mRecorder.startRecording();
            mIsRecording = true;
            listener.onResult(null);
        }
    }

    public void pause(ModuleResultListener listener) {
        if (!mIsRecording) {
            listener.onResult(Util.getError(Constant.RECORDER_NOT_STARTED, Constant.RECORDER_NOT_STARTED_CODE));
            return;
        }
        if (mIsPausing) {
            listener.onResult(null);
            return;
        }
        if (mRecorder != null) {
            mRecorder.pauseRecording();
            mIsPausing = true;
            listener.onResult(null);
        }
    }

    public void stop(ModuleResultListener listener) {
        if (!mIsRecording) {
            listener.onResult(Util.getError(Constant.RECORDER_NOT_STARTED, Constant.RECORDER_NOT_STARTED_CODE));
            return;
        }
        if (mRecorder != null) {
            mRecorder.stopRecording();
            mIsPausing = false;
            mIsRecording = false;
            JSONObject result = new JSONObject();
            try {
                result.put("path", mFile.getAbsolutePath());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listener.onResult(result);
        }
    }

    private AudioSource getMic(int audioBit, int channel, int frequency) {
        return new AudioSource.Smart(MediaRecorder.AudioSource.MIC, audioBit, channel, frequency);
    }
}
