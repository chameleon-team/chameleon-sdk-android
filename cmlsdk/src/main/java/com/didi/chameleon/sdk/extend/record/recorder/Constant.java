package com.didi.chameleon.sdk.extend.record.recorder;

import android.os.Environment;

//import app.eeui.framework.ui.eeui;

public class Constant {
    //public static final String ROOT_PATH = Environment.getExternalStorageDirectory() + "/" + eui.egetApplication().getPackageName()+ "/recorder";
    public static final int DEFAULT_TIMEOUT_MS = 3000;

    //nat 自定义receiver的action
    public static final String VIDEO_PAUSE_OPERATE = "video_pause";
    public static final String VIDEO_STOP_OPERATE = "video_stop";

    //nat activity result,request codeo
    public static final int IMAGE_REQUEST_CODE = 1001;
    public static final int VIDEO_REQUEST_CODE = 1002;
    public static final int IMAGE_PICK_REQUEST_CODE = 1003;

    //nat动态权限request code
    public static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1501;
    public static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1502;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1503;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1504;
    public static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 1505;
    public static final int INTERNET_PERMISSION_REQUEST_CODE = 1506;
    public static final int WRITE_SETTINGS_REQUEST_CODE = 1507;

    //nat自定义error msg
    public static final String CALL_INVALID_ARGUMENT = "CALL_INVALID_ARGUMENT ";
    public static final String CALL_PHONE_PERMISSION_DENIED = "CALL_PHONE_PERMISSION_DENIED";
    public static final String SMS_INVALID_ARGUMENT  = "SMS_INVALID_ARGUMENT";
    public static final String MAIL_INVALID_ARGUMENT  = "MAIL_INVALID_ARGUMENT";

    public static final String GET_LOCATION_INVALID_ARGUMENT  = "GET_LOCATION_INVALID_ARGUMENT";
    public static final String WATCH_LOCATION_INVALID_ARGUMENT  = "WATCH_LOCATION_INVALID_ARGUMENT";
    public static final String LOCATION_PERMISSION_DENIED  = "LOCATION_PERMISSION_DENIED";
    public static final String LOCATION_UNAVAILABLE  = "LOCATION_UNAVAILABLE";
    public static final String LOCATION_TIMEOUT  = "LOCATION_TIMEOUT";
    public static final String LOCATION_SERVICE_BUSY  = "LOCATION_SERVICE_BUSY";

    public static final String CAMERA_PERMISSION_DENIED  = "CAMERA_PERMISSION_DENIED";
    public static final String CAMERA_BUSY  = "CAMERA_BUSY";
    public static final String CAPTURE_IMAGE_INVALID_ARGUMENT  = "CAPTURE_IMAGE_INVALID_ARGUMENT";
    public static final String CAPTURE_VIDEO_INVALID_ARGUMENT  = "CAPTURE_VIDEO_INVALID_ARGUMENT";
    public static final String CAMERA_INTERNAL_ERROR  = "CAMERA_INTERNAL_ERROR";

    public static final String RECORD_AUDIO_PERMISSION_DENIED  = "RECORD_AUDIO_PERMISSION_DENIED";
    public static final String RECORD_AUDIO_INVALID_ARGUMENT  = "RECORD_AUDIO_INVALID_ARGUMENT";
    public static final String RECORDER_BUSY  = "RECORDER_BUSY";
    public static final String RECORDER_INTERNAL_ERROR  = "RECORDER_INTERNAL_ERROR";
    public static final String RECORDER_NOT_STARTED  = "RECORDER_NOT_STARTED";

    public static final String MEDIA_NETWORK_ERROR  = "MEDIA_NETWORK_ERROR";
    public static final String MEDIA_DECODE_ERROR  = "MEDIA_DECODE_ERROR";
    public static final String MEDIA_ABORTED  = "MEDIA_ABORTED";
    public static final String MEDIA_FILE_TYPE_NOT_SUPPORTED  = "MEDIA_FILE_TYPE_NOT_SUPPORTED";
    public static final String MEDIA_SRC_NOT_SUPPORTED  = "MEDIA_SRC_NOT_SUPPORTED";
    public static final String MEDIA_PLAYER_NOT_STARTED  = "MEDIA_PLAYER_NOT_STARTED";
    public static final String MEDIA_INTERNAL_ERROR  = "MEDIA_INTERNAL_ERROR";

    public static final String FETCH_INVALID_ARGUMENT  = "FETCH_INVALID_ARGUMENT";
    public static final String FETCH_NETWORK_ERROR  = "FETCH_NETWORK_ERROR";

    public static final String DOWNLOAD_INTERNAL_ERROR  = "DOWNLOAD_INTERNAL_ERROR";
    public static final String DOWNLOAD_INVALID_ARGUMENT  = "DOWNLOAD_INVALID_ARGUMENT";
    public static final String DOWNLOAD_NETWORK_ERROR  = "DOWNLOAD_NETWORK_ERROR";
    public static final String UPLOAD_INTERNAL_ERROR  = "UPLOAD_INTERNAL_ERROR";
    public static final String UPLOAD_INVALID_ARGUMENT  = "UPLOAD_INVALID_ARGUMENT";
    public static final String UPLOAD_NETWORK_ERROR  = "UPLOAD_NETWORK_ERROR";

    public static final int CALL_PHONE_PERMISSION_DENIED_CODE = 101020;
    public static final int CALL_INVALID_ARGUMENT_CODE = 101040;
    public static final int SMS_INVALID_ARGUMENT_CODE = 102040;
    public static final int MAIL_INVALID_ARGUMENT_CODE = 103040;

    public static final int MEDIA_INTERNAL_ERROR_CODE = 110000;
    public static final int MEDIA_INVALID_ARGUMENT_CODE = 110040;
    public static final int MEDIA_NETWORK_ERROR_CODE = 110050;
    public static final int MEDIA_DECODE_ERROR_CODE = 110060;
    public static final int MEDIA_ABORTED_CODE = 110090;
    public static final int MEDIA_PLAYER_NOT_STARTED_CODE = 110100;
    public static final int MEDIA_FILE_TYPE_NOT_SUPPORTED_CODE = 110110;
    public static final int MEDIA_SRC_NOT_SUPPORTED_CODE = 110120;

    public static final int CAMERA_INTERNAL_ERROR_CODE = 120000;
    public static final int CAMERA_PERMISSION_DENIED_CODE = 120020;
    public static final int CAMERA_BUSY_CODE = 120030;
    public static final int CAPTURE_IMAGE_INVALID_ARGUMENT_CODE = 120040;
    public static final int CAPTURE_VIDEO_INVALID_ARGUMENT_CODE = 120041;

    public static final int RECORDER_INTERNAL_ERROR_CODE = 130000;
    public static final int RECORD_AUDIO_PERMISSION_DENIED_CODE = 130020;
    public static final int RECORDER_BUSY_CODE = 130030;
    public static final int RECORD_AUDIO_INVALID_ARGUMENT_CODE = 130040;
    public static final int RECORDER_NOT_STARTED_CODE = 130100;

    public static final int ALERT_AUDIO_INVALID_ARGUMENT_CODE = 141040;
    public static final int CONFIRM_AUDIO_INVALID_ARGUMENT_CODE = 142040;
    public static final int PROMPT_AUDIO_INVALID_ARGUMENT_CODE = 143040;
    public static final int TOAST_AUDIO_INVALID_ARGUMENT_CODE = 144040;

    public static final int FETCH_INTERNAL_ERROR_CODE = 151000;
    public static final int FETCH_INVALID_ARGUMENT_CODE = 151040;
    public static final int FETCH_NETWORK_ERROR_CODE = 151050;

    public static final int LOCATION_INTERNAL_ERROR_CODE = 160000;
    public static final int LOCATION_NOT_SUPPORTED_CODE = 160010;
    public static final int LOCATION_PERMISSION_DENIED_CODE = 160020;
    public static final int LOCATION_SERVICE_BUSY_CODE = 160030;
    public static final int GET_LOCATION_INVALID_ARGUMENT_CODE = 160040;
    public static final int WATCH_LOCATION_INVALID_ARGUMENT_CODE = 160041;
    public static final int LOCATION_UNAVAILABLE_CODE = 160070;
    public static final int LOCATION_TIMEOUT_CODE = 160080;

    public static final int DOWNLOAD_INTERNAL_ERROR_CODE = 152000;
    public static final int DOWNLOAD_MISSING_ARGUMENT_CODE = 152040;
    public static final int DOWNLOAD_INVALID_ARGUMENT_CODE = 152050;
    public static final int DOWNLOAD_NETWORK_ERROR_CODE = 152060;
    public static final int UPLOAD_INTERNAL_ERROR_CODE = 153000;
    public static final int UPLOAD_MISSING_ARGUMENT_CODE = 153040;
    public static final int UPLOAD_INVALID_ARGUMENT_CODE = 153050;
    public static final int UPLOAD_NETWORK_ERROR_CODE = 153060;

    //内部错误
    public static final String ERROR_NULL_CONTEXT = "Context对象不能为空";


}
