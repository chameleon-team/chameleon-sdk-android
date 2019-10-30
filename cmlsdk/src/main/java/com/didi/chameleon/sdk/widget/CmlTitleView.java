package com.didi.chameleon.sdk.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.didi.chameleon.sdk.R;

/**
 * CmlWeexactivity的标题栏
 *
 * @author chensi
 * @since 2015-9-11
 */
public class CmlTitleView extends RelativeLayout implements View.OnClickListener {

    private TextView mTitleTextView;
    private TextView mLeftTextView;
    private TextView mRightTextView;
    private ImageView mLeftImageView;
    private ImageView mRightImageView;
    private View mLeftBtn;


    private OnClickListener mLeftOnClickListener;
    private OnClickListener mRightOnClickListener;

    public CmlTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public CmlTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CmlTitleView(Context context) {
        this(context, null, 0);
    }

    private void initView() {
        View.inflate(getContext(), R.layout.cml_title_view, this);
        mTitleTextView = (TextView) findViewById(R.id.cml_header_title);
        mLeftTextView = (TextView) findViewById(R.id.cml_header_left_btn);
        mRightTextView = (TextView) findViewById(R.id.cml_header_right_btn);
        mLeftImageView = (ImageView) findViewById(R.id.cml_header_left_image);
        mRightImageView = (ImageView) findViewById(R.id.cml_header_right_image);
        mLeftBtn = findViewById(R.id.cml_title_view_left_btn);
        mLeftBtn.setOnClickListener(this);
        mRightImageView.setOnClickListener(this);
        mRightTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final int vid = view.getId();
        if (vid == R.id.cml_title_view_left_btn) {
            if (mLeftOnClickListener == null) {
                return;
            }
            mLeftOnClickListener.onClick(view);
        } else if (vid == R.id.cml_header_right_btn || vid == R.id.cml_header_right_image) {
            if (mRightOnClickListener == null) {
                return;
            }
            mRightOnClickListener.onClick(view);
        }
    }


    /**
     * 显示左侧返回按钮，并为其添加点击监听
     *
     * @param listener
     */
    public void showLeftBackDrawable(OnClickListener listener) {
        setLeftBtnRes(R.drawable.cml_title_view_btn_back_selector, 0, listener);
    }

    /**
     * 设置左按钮的图片及点击监听
     *
     * @param drawableId 图片资源id
     * @param textId     按钮的文案id
     * @param listener
     */
    public void setLeftBtnRes(@DrawableRes int drawableId, @StringRes int textId, OnClickListener listener) {
        if (drawableId != 0) {
            mLeftImageView.setVisibility(View.VISIBLE);
            mLeftImageView.setImageResource(drawableId);
        } else {
            mLeftImageView.setVisibility(View.GONE);
        }
        if (textId != 0) {
            mLeftTextView.setText(textId);
            mLeftTextView.setVisibility(View.VISIBLE);
        } else {
            mLeftTextView.setVisibility(View.GONE);
        }
        mLeftOnClickListener = listener;
    }

    /**
     * 设置左边图片是否显示
     */
    public void setLeftDrawableVisibility(int visible) {
        mLeftImageView.setVisibility(visible);
    }

    /**
     * 更新左边图片并控制是否显示
     **/
    public void setLeftDrawableVisibility(int resId, int visible) {
        mLeftImageView.setImageResource(resId);
        mLeftTextView.setVisibility(View.GONE);
        mLeftImageView.setVisibility(visible);
    }

    /**
     * 设置左按钮的文本及点击监听
     *
     * @param resId 文本资源id
     */
    public void setLeftText(@StringRes int resId) {
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setText(resId);
    }


    /**
     * 设置右按钮的图片及点击监听
     *
     * @param resId    图片资源id
     * @param listener
     */
    public void setRightDrawable(int resId, OnClickListener listener) {
        mRightImageView.setVisibility(View.VISIBLE);
        mRightImageView.setImageResource(resId);
        mRightTextView.setVisibility(View.GONE);
        mRightOnClickListener = listener;
    }

    /**
     * 设置右按钮的文本及点击监听
     *
     * @param resId    文本资源id
     * @param listener
     */
    public void setRightText(@StringRes int resId, OnClickListener listener) {
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setText(resId);
        mRightImageView.setVisibility(View.GONE);
        mRightOnClickListener = listener;
    }

    public void hideRightImg() {
        mRightImageView.setVisibility(View.GONE);
    }

    /**
     * 设置右按钮的文本颜色
     *
     * @param color
     */
    public void setRightTextColor(int color) {
        mRightTextView.setTextColor(color);
    }

    /**
     * 设置标题
     *
     * @param title 中间标题的文本
     */
    public void setTitle(String title) {
        mTitleTextView.setText(title);
        mTitleTextView.setVisibility(View.VISIBLE);
    }

    public String getTitleString() {
        return mTitleTextView.getText().toString();
    }
}
