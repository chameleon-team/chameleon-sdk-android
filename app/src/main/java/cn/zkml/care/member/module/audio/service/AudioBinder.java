package eeui.android.audio.service;

import android.os.Binder;

public class AudioBinder extends Binder {

    public AudioBinder(MusicService service) {
        this.service = service;
    }

    public MusicService getService() {
        return service;
    }

    public void setService(MusicService service) {
        this.service = service;
    }

    MusicService service;
}
