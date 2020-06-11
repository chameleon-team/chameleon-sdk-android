package eeui.android.audio.event;

public class AudioEvent {

    public String url;
    public int current;
    public int total;
    public int state = 0;

    public AudioEvent(String url, int current, int total, int state) {
        this.url = url;
        this.current = current;
        this.total = total;
        this.state = state;
    }

    public AudioEvent(String url, int current, int total) {
        this.url = url;
        this.current = current;
        this.total = total;
    }


    public AudioEvent(String url, int state) {
        this.url = url;
        this.state = state;
    }

    public final static int STATE_PREPARED = 0;
    public final static int STATE_STARTPLAY = 1;
    public final static int STATE_PLAY = 2;
    public final static int STATE_COMPELETE = 3;
    public final static int STATE_ERROR = 4;
    public final static int STATE_SEEK_COMPELETE = 5;
    public final static int STATE_BufferingUpdate = 6;


}
