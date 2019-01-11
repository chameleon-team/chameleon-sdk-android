package com.didi.chameleon.sdk.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * 用于缩放裁剪的自定义ImageView视图.
 */
public class CmlCropImageView extends ImageView implements View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final int BORDERDISTANCE = CmlCropView.BORDERDISTANCE;

    public static final float DEFAULT_MAX_SCALE = 4.0f;
    public static final float DEFAULT_MID_SCALE = 2.0f;
    public static final float DEFAULT_MIN_SCALE = 1.0f;

    private float minScale = DEFAULT_MIN_SCALE;
    private float midScale = DEFAULT_MID_SCALE;
    private float maxScale = DEFAULT_MAX_SCALE;

    private MultiGestureDetector multiGestureDetector;

    private int borderWidthLength;
    private int borderHeightLength;

    private boolean isJusted;
    private int widthScale;
    private int heightScale;

    private final Matrix baseMatrix = new Matrix();
    private final Matrix drawMatrix = new Matrix();
    private final Matrix suppMatrix = new Matrix();
    private final RectF displayRect = new RectF();

    public void setWidthScale(int widthScale) {
        this.widthScale = widthScale;
    }

    public void setHeightScale(int heightScale) {
        this.heightScale = heightScale;
    }

    // private final float[] matrixValues = new float[9];

    public CmlCropImageView(Context context) {
        this(context, null);
    }

    public CmlCropImageView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public CmlCropImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);

        super.setScaleType(ScaleType.MATRIX);

        setOnTouchListener(this);

        multiGestureDetector = new MultiGestureDetector(context);

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * 依据图片宽高比例，设置图像初始缩放等级和位置
     */
    private void configPosition() {
        super.setScaleType(ScaleType.MATRIX);
        Drawable d = getDrawable();
        if (d == null) {
            return;
        }
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();
        final int drawableWidth = d.getIntrinsicWidth();
        final int drawableHeight = d.getIntrinsicHeight();

        // 边框长度，据屏幕左右边缘50px
        borderWidthLength= (int) (viewWidth - BORDERDISTANCE * 2);
        borderHeightLength = (int) (borderWidthLength*heightScale/widthScale);

        float scale = 1.0f;

        // Log.e("solo", String.format("实际宽度：%d, 实际高度：%d.", drawableWidth,
        // drawableHeight));

        minScale = Math.max((float) borderWidthLength / drawableHeight, (float) borderWidthLength / drawableWidth);
        /**
         * 保持minScale、midScale、maxScale的大小关系，防止出现BUG。
         */
        if (minScale > midScale) {
            if (minScale > maxScale) {
                // 图片太小，禁止继续放大
                midScale = minScale;
                maxScale = minScale;
            } else {
                midScale = minScale;
                maxScale = minScale * 2.0f;
            }
        }

        /**
         * 判断图片宽高比例，调整显示位置和缩放大小
         */
        // 图片宽度小于等于高度
        if (drawableWidth <= drawableHeight) {
            // 判断图片宽度是否小于边框, 缩放铺满裁剪边框
            if (drawableWidth < borderWidthLength) {
                baseMatrix.reset();
                scale = (float) borderWidthLength / drawableWidth;
                // 缩放
                baseMatrix.postScale(scale, scale);
            }
            // 图片宽度大于高度
        } else {
            if (drawableHeight < borderHeightLength) {
                baseMatrix.reset();
                scale = (float) borderHeightLength / drawableHeight;
                // 缩放
                baseMatrix.postScale(scale, scale);
            }
        }
        //图片尺寸超过显示大小，按最大比例压缩
        if (drawableHeight > viewHeight && drawableWidth > viewWidth) {
            baseMatrix.reset();
            float distanceHeight=drawableHeight-viewHeight;
            float distanceWidth=drawableWidth-viewWidth;
            if(distanceHeight>distanceWidth){
                scale = viewWidth / drawableWidth;
            }else{
                scale = viewHeight / drawableHeight;
            }
            baseMatrix.postScale(scale, scale);
        }
        // }
        // 移动居中
        baseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2, (viewHeight - drawableHeight * scale) / 2);

        resetMatrix();
        isJusted = true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        return multiGestureDetector.onTouchEvent(event);
    }

    private class MultiGestureDetector extends GestureDetector.SimpleOnGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        private final ScaleGestureDetector scaleGestureDetector;
        private final GestureDetector gestureDetector;
        private final float scaledTouchSlop;

        private VelocityTracker velocityTracker;
        private boolean isDragging;

        private float lastTouchX;
        private float lastTouchY;
        private float lastPointerCount;

        public MultiGestureDetector(Context context) {
            scaleGestureDetector = new ScaleGestureDetector(context, this);

            gestureDetector = new GestureDetector(context, this);
            gestureDetector.setOnDoubleTapListener(this);

            final ViewConfiguration configuration = ViewConfiguration.get(context);
            scaledTouchSlop = configuration.getScaledTouchSlop();
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = getScale();

            float scaleFactor = detector.getScaleFactor();
            if (getDrawable() != null && ((scale < maxScale && scaleFactor > 1.0f) || (scale > minScale && scaleFactor < 1.0f))) {
                if (scaleFactor * scale < minScale) {
                    scaleFactor = minScale / scale;
                }
                if (scaleFactor * scale > maxScale) {
                    scaleFactor = maxScale / scale;
                }
                suppMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
                checkAndDisplayMatrix();
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }

            scaleGestureDetector.onTouchEvent(event);

            /*
             * Get the center x, y of all the pointers
             */
            float x = 0, y = 0;
            final int pointerCount = event.getPointerCount();
            for (int i = 0; i < pointerCount; i++) {
                x += event.getX(i);
                y += event.getY(i);
            }
            x = x / pointerCount;
            y = y / pointerCount;

            /*
             * If the pointer count has changed cancel the drag
             */
            if (pointerCount != lastPointerCount) {
                isDragging = false;
                if (velocityTracker != null) {
                    velocityTracker.clear();
                }
                lastTouchX = x;
                lastTouchY = y;
            }
            lastPointerCount = pointerCount;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (velocityTracker == null) {
                        velocityTracker = VelocityTracker.obtain();
                    } else {
                        velocityTracker.clear();
                    }
                    velocityTracker.addMovement(event);

                    lastTouchX = x;
                    lastTouchY = y;
                    isDragging = false;
                    break;

                case MotionEvent.ACTION_MOVE: {
                    final float dx = x - lastTouchX, dy = y - lastTouchY;

                    if (isDragging == false) {
                        // Use Pythagoras to see if drag length is larger than
                        // touch slop
                        isDragging = Math.sqrt((dx * dx) + (dy * dy)) >= scaledTouchSlop;
                    }

                    if (isDragging) {
                        if (getDrawable() != null) {
                            suppMatrix.postTranslate(dx, dy);
                            checkAndDisplayMatrix();
                        }

                        lastTouchX = x;
                        lastTouchY = y;

                        if (velocityTracker != null) {
                            velocityTracker.addMovement(event);
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    lastPointerCount = 0;
                    if (velocityTracker != null) {
                        velocityTracker.recycle();
                        velocityTracker = null;
                    }
                    break;
            }

            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            try {
                float scale = getScale();
                float x = getWidth() / 2;
                float y = getHeight() / 2;

                if (scale < midScale) {
                    post(new AnimatedZoomRunnable(scale, midScale, x, y));
                } else if ((scale >= midScale) && (scale < maxScale)) {
                    post(new AnimatedZoomRunnable(scale, maxScale, x, y));
                } else {
                    post(new AnimatedZoomRunnable(scale, minScale, x, y));
                }
            } catch (Exception e) {
                // Can sometimes happen when getX() and getY() is called
            }

            return true;
        }
    }

    private class AnimatedZoomRunnable implements Runnable {
        // These are 'postScale' values, means they're compounded each iteration
        static final float ANIMATION_SCALE_PER_ITERATION_IN = 1.07f;
        static final float ANIMATION_SCALE_PER_ITERATION_OUT = 0.93f;

        private final float focalX, focalY;
        private final float targetZoom;
        private final float deltaScale;

        public AnimatedZoomRunnable(final float currentZoom, final float targetZoom, final float focalX, final float focalY) {
            this.targetZoom = targetZoom;
            this.focalX = focalX;
            this.focalY = focalY;

            if (currentZoom < targetZoom) {
                deltaScale = ANIMATION_SCALE_PER_ITERATION_IN;
            } else {
                deltaScale = ANIMATION_SCALE_PER_ITERATION_OUT;
            }
        }

        public void run() {
            suppMatrix.postScale(deltaScale, deltaScale, focalX, focalY);
            checkAndDisplayMatrix();

            final float currentScale = getScale();

            if (((deltaScale > 1f) && (currentScale < targetZoom)) || ((deltaScale < 1f) && (targetZoom < currentScale))) {
                // We haven't hit our target scale yet, so post ourselves
                // again
                postOnAnimation(CmlCropImageView.this, this);

            } else {
                // We've scaled past our target zoom, so calculate the
                // necessary scale so we're back at target zoom
                final float delta = targetZoom / currentScale;
                suppMatrix.postScale(delta, delta, focalX, focalY);
                checkAndDisplayMatrix();
            }
        }
    }

    // @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void postOnAnimation(View view, Runnable runnable) {
        // if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
        // view.postOnAnimation(runnable);
        // } else {
        view.postDelayed(runnable, 16);
        // }
    }

    /**
     * Returns the current scale value
     *
     * @return float - current scale value
     */
    public final float getScale() {
        // suppMatrix.getValues(matrixValues);
        // return matrixValues[Matrix.MSCALE_X];
        final RectF rect = getDisplayRect(getDisplayMatrix());
        final float curDrawableHeight = rect.bottom - rect.top;
        int height = getDrawable().getIntrinsicHeight();
        if (height > 0) {
            return curDrawableHeight / height;
        }
        return 1.0f;
    }

    @Override
    public void onGlobalLayout() {
        if (isJusted) {
            return;
        }
        // 调整视图位置
        configPosition();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * Helper method that simply checks the Matrix, and then displays the result
     */
    private void checkAndDisplayMatrix() {
        checkMatrixBounds();
        setImageMatrix(getDisplayMatrix());
    }

    private void checkMatrixBounds() {
        final RectF rect = getDisplayRect(getDisplayMatrix());
        if (null == rect) {
            return;
        }

        float deltaX = 0, deltaY = 0;
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();
        // 判断移动或缩放后，图片显示是否超出裁剪框边界
        if (rect.top > (viewHeight - borderHeightLength) / 2) {
            deltaY = (viewHeight - borderHeightLength) / 2 - rect.top;
        }
        if (rect.bottom < (viewHeight + borderHeightLength) / 2) {
            deltaY = (viewHeight + borderHeightLength) / 2 - rect.bottom;
        }
        if (rect.left > (viewWidth - borderWidthLength) / 2) {
            deltaX = (viewWidth - borderWidthLength) / 2 - rect.left;
        }
        if (rect.right < (viewWidth + borderWidthLength) / 2) {
            deltaX = (viewWidth + borderWidthLength) / 2 - rect.right;
        }
        // Finally actually translate the matrix
        suppMatrix.postTranslate(deltaX, deltaY);

        // 回弹动画
        TranslateAnimation trans = new TranslateAnimation(-deltaX, 0, -deltaY, 0);
        trans.setDuration(10);
        startAnimation(trans);

    }

    /**
     * Helper method that maps the supplied Matrix to the current Drawable
     *
     * @param matrix
     *            - Matrix to map Drawable against
     * @return RectF - Displayed Rectangle
     */
    private RectF getDisplayRect(Matrix matrix) {
        Drawable d = getDrawable();
        if (null != d) {
            displayRect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(displayRect);
            return displayRect;
        }

        return displayRect;
    }

    /**
     * Resets the Matrix back to FIT_CENTER, and then displays it.s
     */
    private void resetMatrix() {
        if (suppMatrix == null) {
            return;
        }
        suppMatrix.reset();
        setImageMatrix(getDisplayMatrix());
    }

    protected Matrix getDisplayMatrix() {
        drawMatrix.set(baseMatrix);
        drawMatrix.postConcat(suppMatrix);
        return drawMatrix;
    }

    /**
     * 剪切图片，返回剪切后的bitmap对象
     *
     * @param outputX
     *            - 输出图片的宽度.
     * @param outputY
     *            - 输出图片的高度.
     *
     * @return bitmap
     */
    public Bitmap crop(float outputX, float outputY) {

        // 获取ImageView自身的宽、高
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();

        // 获取当前图片的高度
        final RectF rect = getDisplayRect(getDisplayMatrix());
        final float curDrawableHeight = rect.bottom - rect.top;

        // 获取原始图片的高度，d.getIntrinsicHeight()返回的不是原始高度
        final BitmapDrawable d = (BitmapDrawable) getDrawable();
        final int srcHeight = d.getBitmap().getHeight();
        final int srcWidth = d.getBitmap().getWidth();

        // 当前图片相对原始图片的缩放比例
        float curScale = curDrawableHeight / srcHeight;

        // 获取截取位置的左上角相对坐标
        float cropDrawableTop = (viewHeight - borderHeightLength) / 2;
        float cropDrawableLeft = (viewWidth - borderWidthLength) / 2;
        final float relativeTop = cropDrawableTop - rect.top;
        final float relativeLeft = cropDrawableLeft - rect.left;

        // 缩放、截取原图
        Matrix m = new Matrix();
        float preScale = Math.max(outputX / srcWidth, outputY / srcHeight);
        m.setScale(preScale, preScale);

        Bitmap bitmap;
        try {
            bitmap = Bitmap.createBitmap(
                    d.getBitmap(), (int) (relativeLeft / curScale), (int) (relativeTop / curScale), (int) (borderWidthLength / curScale),
                    (int) (borderHeightLength / curScale), m, true);
        } catch (Exception e) {
            return null;
        }

        return bitmap;
    }

    /**
     * 剪切图片，返回剪切后的bitmap对象
     *
     * @param outputX
     *            - 输出图片的宽度.
     * @param outputY
     *            - 输出图片的高度.
     *
     * @return bitmap
     */
    public Bitmap cropUpPercentTen(float outputX, float outputY) {

        // 获取ImageView自身的宽、高
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();


        // 获取当前图片的高度
        final RectF rect = getDisplayRect(getDisplayMatrix());
        final float curDrawableHeight = rect.bottom - rect.top;

        // 获取原始图片的高度，d.getIntrinsicHeight()返回的不是原始高度
        final BitmapDrawable d = (BitmapDrawable) getDrawable();
        final int srcHeight = d.getBitmap().getHeight();
        final int srcWidth = d.getBitmap().getWidth();
        float preScale = Math.max(outputX / srcWidth, outputY / srcHeight);
        // 当前图片相对原始图片的缩放比例
        float curScale = curDrawableHeight / srcHeight;

        // 获取截取位置的左上角相对坐标,如果左上角可以刻外扩图片，默认扩大图片的10%
        float widthTenPercent= (float) ((outputX*0.1)/curScale);
        float heightTenPercent= (float) ((outputY*0.1)/curScale);
        float cropDrawableTop = (viewHeight - borderHeightLength) / 2;
        float relativeTop=cropDrawableTop - rect.top;
        if(Math.round(cropDrawableTop-rect.top)>heightTenPercent){
            relativeTop-=heightTenPercent;
        }
        float cropDrawableLeft = (viewWidth - borderWidthLength) / 2;
        float relativeLeft = cropDrawableLeft - rect.left;
        if(Math.round(cropDrawableLeft-rect.left)>widthTenPercent){
            relativeLeft -=widthTenPercent;
        }
        float cropDrawableRight=((viewWidth - borderWidthLength) / 2)+borderWidthLength;
        float relativeRight=borderWidthLength;
        if(Math.round(rect.right-cropDrawableRight)>widthTenPercent){
            relativeRight+=widthTenPercent;
        }
        float cropDrawableBottom=((viewHeight - borderHeightLength) / 2)+borderHeightLength;
        float relativeBottom=borderHeightLength;
        if(Math.round(rect.bottom-cropDrawableBottom)>heightTenPercent){
            relativeBottom+=heightTenPercent;
        }

        // 缩放、截取原图
        Matrix m = new Matrix();
        m.setScale(preScale, preScale);

        Bitmap bitmap;
        try {
            bitmap = Bitmap.createBitmap(
                    d.getBitmap(), (int)(relativeLeft/curScale), (int)(relativeTop/curScale), (int) (relativeRight/curScale),
                    (int) (relativeBottom/curScale), m, true);
        } catch (Exception e) {
            return null;
        }

        return bitmap;
    }
}

