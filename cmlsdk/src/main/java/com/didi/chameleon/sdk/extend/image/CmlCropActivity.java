package com.didi.chameleon.sdk.extend.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@SuppressLint("NewApi")
public class CmlCropActivity extends Activity {
    private CmlCropImageView mCropImageView;
    private CmlCropView mCropView;
    // private int mRawHeight;
    // private int mRawWidth;
    private int mOutputX = 500;
    private int mOutputY = 340;
    private String mOutputPath = null;
    private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;
    // boolean image is loaed
    private boolean mImageLoaded = false;
    // crop out pic pass key
    public static final String CROP_PIC_PASS_KEY = "CROP_PIC_PASS_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cml_crop);
        setTitlebar();
        mCropImageView = (CmlCropImageView) findViewById(R.id.src_pic);
        mCropView = (CmlCropView) findViewById(R.id.crop_view);
        initIntent();
    }

    private void setTitlebar() {
        findViewById(R.id.cml_title_left).setOnClickListener(backListener);
        findViewById(R.id.cml_title_right).setOnClickListener(rightClickListener);
    }

    private OnClickListener rightClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (mImageLoaded) {
                new CropHeadWorker().execute();
            }
        }
    };

    private OnClickListener backListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            CmlCropActivity.this.finish();
        }
    };

    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            CmlEnvironment.getModalTip().showToast(this, getString(R.string.cml_crop_image_argument_fail), 0);
            finish();
            return;
        }
        Uri imageUri = intent.getData();
        if (imageUri == null) {
            CmlEnvironment.getModalTip().showToast(this, getString(R.string.cml_crop_image_argument_fail), 0);
            finish();
            return;
        }
        mOutputPath = intent.getStringExtra("output");
        String mOutputX = intent.getStringExtra("width");
        String mOutputY = intent.getStringExtra("height");
        if (!TextUtils.isEmpty(mOutputX)) {
            this.mOutputX = TextUtils.isEmpty(mOutputX) ? 500 : Integer.valueOf(mOutputX.split("\\.")[0]);
        }
        if (!TextUtils.isEmpty(mOutputY)) {
            this.mOutputY = TextUtils.isEmpty(mOutputY) ? 340 : Integer.valueOf(mOutputY.split("\\.")[0]);
        }
        int scale = getMaxGY(this.mOutputX, this.mOutputY);
        mCropView.setWidthScale(this.mOutputX / scale);
        mCropImageView.setWidthScale(this.mOutputX / scale);
        mCropView.setHeightScale(this.mOutputY / scale);
        mCropImageView.setHeightScale(this.mOutputY / scale);
        try {
            Options opts = new Options();
            opts.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, opts);

            // mRawHeight = opts.outHeight;
            // mRawWidth = opts.outWidth;

            // 图片太小，提示重新选择
            if (opts.outHeight < this.mOutputY || opts.outWidth < this.mOutputX) {
                CmlEnvironment.getModalTip().showToast(this, getString(R.string.cml_crop_image_too_small), 0);
                finish();
                return;
            }

            Bitmap srcBitmap = null;
            // 图片分辨率相对屏幕分辨率的最大倍数
            float maxRatio = 2.0f;
            // 宽或高超过限定，进行缩放
            if (opts.outHeight > getScreenHeight() * maxRatio || opts.outWidth > getScreenWidth() * maxRatio) {
                int heightRatio = Math.round(opts.outHeight / getScreenHeight());
                int widthRatio = Math.round(opts.outWidth / getScreenWidth());
                opts.inSampleSize = Math.max(heightRatio, widthRatio);
                opts.inJustDecodeBounds = false;
                srcBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, opts);
            } else {
                // 原始大小
                srcBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            }
            // 判断是否需要对图像进行旋转处理
            // int degree = BitmapUtils.readPictureDegree(BitmapUtils
            // .getRealPathFromUri(getContentResolver(), imageUri));
            int degree = CmlExifUtils.getExifOrientation(this, imageUri);
            if (degree != 0) {
                Bitmap resBitmap = rotateBitmap(srcBitmap, degree, true);
                mCropImageView.setImageBitmap(resBitmap);
            } else {
                // 不需要旋转处理
                mCropImageView.setImageBitmap(srcBitmap);
            }
            mImageLoaded = true;

        } catch (Exception e) {
            e.printStackTrace();
            CmlEnvironment.getModalTip().showToast(this, getString(R.string.cml_crop_image_open_file_fail), 0);
            finish();
            return;
        }
    }

    class CropHeadWorker extends AsyncTask<Void, Void, Void> {

        private Bitmap mBitmap;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            File file = new File(mOutputPath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Uri saveUri = Uri.fromFile(file);
            // 裁剪
            mBitmap = mCropImageView.cropUpPercentTen(mOutputX, mOutputY);
            if (mBitmap == null) {
                return null;
            }
            // 保存到文件
            try {
                mBitmap.compress(mOutputFormat, 75, getContentResolver().openOutputStream(saveUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent intent = new Intent();
            intent.putExtra(CROP_PIC_PASS_KEY, mOutputPath);
            setResult(RESULT_OK, intent);
            if (mBitmap != null) {
                mBitmap.recycle();
            }
            finish();
        }
    }

    public static int getMaxGY(int m, int n) {
        //求最大公约数
        if (m == n) {
            return n;
        } else {
            while (m % n != 0) {
                int temp = m % n;
                m = n;
                n = temp;
            }
            return n;
        }
    }

    private int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
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
}
