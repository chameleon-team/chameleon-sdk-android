package com.didi.chameleon.sdk.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 裁剪边框.
 *
 * @author soloslxu
 */
public class CmlCropView extends View {

    /**
     * 边框距左右边界距离，用于调整边框长度
     */
    public static final int BORDERDISTANCE = 50;

    private Paint mPaint;

    public CmlCropView(Context context) {
        this(context, null);
    }

    public CmlCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CmlCropView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint();
    }
    private int widthScale;
    private int heightScale;

    public void setWidthScale(int widthScale) {
        this.widthScale = widthScale;
    }

    public void setHeightScale(int heightScale) {
        this.heightScale = heightScale;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        // 边框长度，据屏幕左右边缘50px
        int borderWidthLength=width - BORDERDISTANCE * 2;
        int borderHeightLength = (int) (borderWidthLength*heightScale/widthScale);

        mPaint.setColor(0xaa000000);

        // 以下绘制透明暗色区域
        // top
        canvas.drawRect(0, 0, width, (height - borderHeightLength) / 2, mPaint);
        // bottom
        canvas.drawRect(0, (height + borderHeightLength) / 2, width, height, mPaint);
        // left
        canvas.drawRect(0, (height - borderHeightLength) / 2, BORDERDISTANCE,
                (height + borderHeightLength) / 2, mPaint);
        // right
        canvas.drawRect(width -BORDERDISTANCE,
                (height - borderHeightLength) / 2, width,
                (height + borderHeightLength) / 2, mPaint);

        // 以下绘制边框线
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(2.0f);
        // top
        canvas.drawLine(BORDERDISTANCE, (height - borderHeightLength) / 2, width
                - BORDERDISTANCE, (height - borderHeightLength) / 2, mPaint);
        // bottom
        canvas.drawLine(BORDERDISTANCE, (height + borderHeightLength) / 2, width
                - BORDERDISTANCE, (height + borderHeightLength) / 2, mPaint);
        // left
        canvas.drawLine(BORDERDISTANCE, (height - borderHeightLength) / 2,
                BORDERDISTANCE, (height + borderHeightLength) / 2, mPaint);
        // right
        canvas.drawLine(width - BORDERDISTANCE, (height - borderHeightLength) / 2,
                width - BORDERDISTANCE, (height + borderHeightLength) / 2, mPaint);
    }

}

