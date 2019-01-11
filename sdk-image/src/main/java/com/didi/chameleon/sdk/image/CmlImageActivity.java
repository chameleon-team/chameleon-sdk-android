package com.didi.chameleon.sdk.image;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.utils.CmlSystemUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CmlImageActivity extends FragmentActivity {

    public static final String TAG = "CmlImageActivity";

    public static final int REQUEST_CODE_SYSTEM_RESIZE_IMAGE = 104;

    private final int REQUEST_CODE_CAMERA_PERMISSION = 1;

    private static CmlImageCallback sImageCallback;

    // photo select listview
    private ListView mListView;
    private TextView mCancelTextView;
    // album request code
    private static final int REQ_ALBUM_ACTIVITY = 100;
    // camera request code
    private static final int REQ_CAMERA_ACTIVITY = 101;
    // crop request code
    private static final int REQ_CROP_ACTIVITY = 102;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    // 拍照文件
    private File mCameraFile;
    // 最终剪裁文件
    private File mOutPutFile;
    private String mOutPutFilePath;

    private RelativeLayout mUploadSelect;
    //
    private String type;

    private String width;
    private String height;
    private String quality = "";
    private boolean isNeedCut;

    private Handler mHandler;

    private String mImageType;
    private File mCropFile = null;

    public static void setImageCallback(CmlImageCallback callback) {
        sImageCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mOutPutFilePath = savedInstanceState.getString("mOutPutFile");
        }
        mHandler = new Handler();
        initData();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.down_to_up_slide_in, R.anim.up_to_down_slide_out);
        sImageCallback = null;
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            width = intent.getStringExtra("width");
            height = intent.getStringExtra("height");
            quality = intent.getStringExtra("quality");
            isNeedCut = intent.getBooleanExtra("cut", false);
        }
        mOutPutFile = CmlImageFileUtil.getPhotoOutputFile(this);
        if (mOutPutFile != null) {
            mOutPutFilePath = mOutPutFile.getAbsolutePath();
        }
        switch (type) {
            case "camera":
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != 0 || !isCameraCanUse()) {
                    // 摄像头权限还未得到用户的同意
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                    }
                    if (sImageCallback != null) {
                        sImageCallback.onCancel();
                    }
                    finish();
                } else {
                    // 摄像头权限以及有效，显示摄像头预览界面
                    mCameraFile = CmlImageFileUtil.getPhotoOutputFile(this);
                    dispatchTakePictureIntent();
                }
                break;
            case "album":
                intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(intent, REQ_ALBUM_ACTIVITY);
                break;
            default:
                overridePendingTransition(R.anim.down_to_up_slide_in, R.anim.up_to_down_slide_out);
                setContentView(R.layout.cml_image_pick_dialog);
                initView();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            CmlEnvironment.getModalTip().showToast(this, getString(R.string.cml_camera_permission_fail), 0);
            finish();
        }
    }

    private void initView() {
        mListView = findViewById(R.id.pic_menu_list);
        mUploadSelect = findViewById(R.id.bts_upload_rela);
        ArrayAdapter<String> mListAdapter = new ArrayAdapter<String>(
                this, R.layout.cml_pic_upload_list, getResources().getStringArray(R.array.cml_avatar_menu));
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(mItemClickListener);
        mCancelTextView = findViewById(R.id.cancel_text);
        mCancelTextView.setOnClickListener(cancelClickListener);
    }

    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
        }
        return canUse;
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = null;
            switch (position) {
                case 0:
                    if (Build.VERSION.SDK_INT >= 23 &&
                            !CmlSystemUtil.checkPermission(CmlImageActivity.this, Manifest.permission.CAMERA) || !isCameraCanUse()) {
                        // 摄像头权限还未得到用户的同意
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                        }
                        if (sImageCallback != null) {
                            sImageCallback.onPermissionFail();
                        }
                        finish();
                    } else {
                        mCameraFile = CmlImageFileUtil.getPhotoOutputFile(CmlImageActivity.this);
                        dispatchTakePictureIntent();
                    }
                    break;
                case 1:
                    intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                    try {
                        startActivityForResult(intent, REQ_ALBUM_ACTIVITY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    break;
            }
        }
    };

    private View.OnClickListener cancelClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (sImageCallback != null) {
                sImageCallback.onCancel();
            }
            finish();
        }
    };

    private void dispatchTakePictureIntent() {
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            if (mCameraFile != null) {
                Uri uri = CmlImageFileUtil.getUriForPath(getApplicationContext(), mCameraFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                if (CmlSystemUtil.checkPermission(this, Manifest.permission.CAMERA)) {
                    startActivityForResult(takePictureIntent, REQ_CAMERA_ACTIVITY);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CAMERA_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (mCameraFile != null && mCameraFile.length() > 0) {

                        if (TextUtils.isEmpty(width) || TextUtils.isEmpty(height) || "0".equals(width) || "0".equals(height)) {

//                            Uri imageUri = Constant.getUriForPath(getApplicationContext(), mCameraFile);
                            final Uri imageUri = Uri.fromFile(mCameraFile);
                            if (isNeedCut) {
                                startSystemCropActivity(imageUri);
                                return;
                            }
                            handlePic(imageUri);

                        } else {
                            Intent intent = new Intent(this, CmlCropActivity.class);

                            intent.setData(CmlImageFileUtil.getUriForPath(getApplicationContext(), mCameraFile));
                            intent.putExtra("width", width);
                            intent.putExtra("height", height);
                            intent.putExtra("output", mOutPutFilePath);
                            startActivityForResult(intent, REQ_CROP_ACTIVITY);
                        }
                    } else {
                        CmlImageFileUtil.deleteFile(mCameraFile);
                        if (sImageCallback != null) {
                            sImageCallback.onFail();
                        }
                        finish();
                    }
                } else {
                    CmlImageFileUtil.deleteFile(mCameraFile);
                    if (sImageCallback != null) {
                        if (Build.VERSION.SDK_INT >= 23 &&
                                !CmlSystemUtil.checkPermission(this, Manifest.permission.CAMERA) || !isCameraCanUse()) {
                            // 摄像头权限还未得到用户的同意
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                            }
                            sImageCallback.onPermissionFail();
                        } else {
                            sImageCallback.onCancel();
                        }
                    }
                    finish();
                }
                break;
            case REQ_ALBUM_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (mOutPutFile == null) {
                        finish();
                    }

                    if (TextUtils.isEmpty(width) || TextUtils.isEmpty(height) || "0".equals(width) || "0".equals(height)) {

                        final Uri imageUri = data.getData();
                        String url = CmlImageFileUtil.getPath(this, imageUri);
                        File mSaveFile = CmlImageFileUtil.getPhotoOutputFile(this);
                        CmlImageFileUtil.copyFile(url, mSaveFile.getAbsolutePath());
                        if (isNeedCut) {

                            startSystemCropActivity(Uri.fromFile(mSaveFile));
                            return;
                        }
                        handlePic(imageUri);

                    } else {
                        if (data != null) {
                            data.setClass(this, CmlCropActivity.class);
                            data.putExtra("width", width);
                            data.putExtra("height", height);
                            data.putExtra("output", mOutPutFilePath);
                            startActivityForResult(data, REQ_CROP_ACTIVITY);
                        }
                    }

                } else {
                    if (sImageCallback != null) {
                        sImageCallback.onCancel();
                    }
                    finish();
                }
                break;
            case REQ_CROP_ACTIVITY:
                if (resultCode == RESULT_OK && data != null) {
                    if (mUploadSelect != null) {
                        mUploadSelect.setVisibility(View.GONE);
                    }
                    String finalPath = data.getStringExtra(CmlCropActivity.CROP_PIC_PASS_KEY);

                    if (sImageCallback != null) {
                        sImageCallback.onSuccess(bitmapToBase64(finalPath), mImageType);
                    }
                    finish();

                } else {
                    if (sImageCallback != null) {
                        sImageCallback.onFail();
                    }
                    finish();
                }
                break;
            case RESULT_CANCELED:
                if (sImageCallback != null) {
                    sImageCallback.onCancel();
                }
                finish();
                break;

            case REQUEST_CODE_SYSTEM_RESIZE_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (null == mCropFile) {
                        break;
                    }
                    final Uri imageUri = Uri.fromFile(mCropFile);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                final String base64 = decodeSampledBitmap(imageUri, 600, 600);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (sImageCallback != null) {
                                            sImageCallback.onSuccess(base64, mImageType);
                                        }
                                        finish();
                                    }
                                });
                            } catch (Exception e) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (sImageCallback != null) {
                                            sImageCallback.onFail();
                                        }
                                        finish();
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    if (sImageCallback != null) {
                        sImageCallback.onCancel();
                    }
                    finish();
                }
                break;

            default:
                return;
        }
    }

    private void handlePic(final Uri imageUri) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    final String base64 = decodeSampledBitmap(imageUri, 600, 600);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (sImageCallback != null) {
                                sImageCallback.onSuccess(base64, mImageType);
                            }
                            finish();
                        }
                    });
                } catch (Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (sImageCallback != null) {
                                sImageCallback.onFail();
                            }
                            finish();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 打开裁剪界面
     *
     * @param uri
     */
    private void startSystemCropActivity(Uri uri) {
        try {
            Uri targetUri;
            Intent intent = new Intent("com.android.camera.action.CROP");

            if (Build.VERSION.SDK_INT >= 24) {
                File file = new File(uri.getPath());
//                targetUri = FileProvider.getUriForFile(this, providerName, file);
                targetUri = CmlImageFileUtil.getUriForPath(this, file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(targetUri, "image/*");
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String url = CmlImageFileUtil.getPath(this, uri);
                targetUri = Uri.fromFile(new File(url));
                intent.setDataAndType(targetUri, "image/*");
            } else {
                intent.setDataAndType(uri, "image/*");
                targetUri = Uri.fromFile(mCropFile);
            }
            mCropFile = CmlImageFileUtil.getPhotoOutputFile(this);
            Uri saveUri = Uri.fromFile(mCropFile);
//            Uri saveUri = Constant.getUriForPath(getApplicationContext(), mCropFile);
            intent.putExtra("crop", "true");
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            intent.putExtra("outputX", 300);
//            intent.putExtra("outputY", 300);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, REQUEST_CODE_SYSTEM_RESIZE_IMAGE);
        } catch (Exception e) {
            //防止系统ActivityNotFoundException：com.android.camera.action.CROP
            CmlEnvironment.getModalTip().showToast(this, getString(R.string.cml_sidebar_modify_error2), 0);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public String decodeSampledBitmap(Uri imageUri, int reqWidth, int reqHeight) throws FileNotFoundException {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, options);

        int degree = CmlExifUtils.getExifOrientation(CmlImageActivity.this, imageUri);
        if (degree != 0) {
            bitmap = rotateBitmap(bitmap, degree, true);
        }
        return bitmapToBase64(bitmap, options);
    }

    private Bitmap rotateBitmap(Bitmap source, int rotation, boolean recycle) {
        if (rotation == 0) return source;
        int w = source.getWidth();
        int h = source.getHeight();
        Matrix m = new Matrix();
        m.postRotate(rotation);
        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, w, h, m, true);
        if (recycle) source.recycle();
        return bitmap;
    }

    private String bitmapToBase64(String path) {
        Bitmap b = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream out = null;
        try {
            if (TextUtils.isEmpty(quality)) {
                quality = "100";
            }
            int qt = Integer.parseInt(quality);
            out = new ByteArrayOutputStream();

            b.compress(Bitmap.CompressFormat.JPEG, qt, out);
            out.flush();
            out.close();
            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (IOException e) {
            CmlLogUtil.d(TAG, e.toString());
        } catch (Exception e) {
            CmlLogUtil.d(TAG, e.toString());
        }
        return "";
    }

    private String bitmapToBase64(Bitmap b, BitmapFactory.Options options) {
        ByteArrayOutputStream out = null;
        try {
            if (TextUtils.isEmpty(quality)) {
                quality = "75";
            }
            int qt = Integer.parseInt(quality);
            out = new ByteArrayOutputStream();
            if (!TextUtils.isEmpty(mImageType) && mImageType.contains("jpeg")) {
                b.compress(Bitmap.CompressFormat.JPEG, qt, out);
                mImageType = "jpg";
            } else if (!TextUtils.isEmpty(mImageType) && mImageType.contains("png")) {
                mImageType = "png";
                b.compress(Bitmap.CompressFormat.PNG, qt, out);
            } else {
                b.compress(Bitmap.CompressFormat.JPEG, qt, out);
                String mimeType = options.outMimeType;
                if (!TextUtils.isEmpty(mimeType) && mimeType.contains("/")) {
                    String[] splits = mimeType.split("/");
                    mImageType = splits[1];
                } else {
                    mImageType = "";
                }
            }
            out.flush();
            out.close();
            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (IOException e) {
            CmlLogUtil.d(TAG, e.toString());
        } catch (Exception e) {
            CmlLogUtil.d(TAG, e.toString());
        }
        return "";
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mOutPutFile", mOutPutFilePath);
    }

    @Override
    public void onBackPressed() {
        if (sImageCallback != null) {
            sImageCallback.onCancel();
        }
        super.onBackPressed();
    }
}
