package com.example.administrator.vpindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/3/16.
 */
public class ColorTextView extends View {


    private int mTextStratX;

    public enum Direction {
        Left, Right;
    }

    private static final int DIRECTION_LEFT = 0;
    private static final int DIRECTION_RIGHT = 1;

    private int mDirection = DIRECTION_LEFT;

    public void setDirection(int direction) {
        this.mDirection = direction;
    }

    private String text = "xxxxxxxx";
    private Paint mPaint;

    private int textSize = sp2px(30);

    private int textOriginColor = 0xff000000;
    private int textChangeColor = 0xffff0000;

    private Rect bound = new Rect();

    private int mTextWidth;

    private int mRealWidth;

    private float progress;


    public ColorTextView(Context context) {
        super(context);
    }

    public ColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorTextView);

        text = a.getString(a.getIndex(R.styleable.ColorTextView_text));
        mDirection = a.getInt(a.getIndex(R.styleable.ColorTextView_direction), DIRECTION_LEFT);
        textSize = a.getDimensionPixelSize(a.getIndex(R.styleable.ColorTextView_text_size), sp2px(14));
        textOriginColor = a.getColor(a.getIndex(R.styleable.ColorTextView_text_origin_color), Color.BLACK);
        textChangeColor = a.getColor(a.getIndex(R.styleable.ColorTextView_text_change_color), Color.YELLOW);
        progress = a.getFloat(a.getIndex(R.styleable.ColorTextView_progress), 0);

        a.recycle();

        mPaint.setTextSize(textSize);
        measureText();

    }

    private void measureText() {
        //获取text的宽度
        mTextWidth = (int) mPaint.measureText(text);
        // 获取text的界限，并保存在bound矩形中
        mPaint.getTextBounds(text, 0, text.length(), bound);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mTextStratX = mRealWidth / 2 - mTextWidth / 2;
    }


    private int measureHeight(int heightMeasureSpec) {
        int model = MeasureSpec.getMode(heightMeasureSpec);
        int val = MeasureSpec.getSize(heightMeasureSpec);

        int result = 0;
        switch (model) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = Math.min(val, bound.height());
                break;
        }
        return result + getPaddingBottom() + getPaddingTop();
    }

    private int measureWidth(int widthMeasureSpec) {
        int model = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        int result = 0;
        switch (model) {
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = Math.min(size, mTextWidth);
                break;
        }
        return result + getPaddingLeft() + getPaddingRight();
    }

    public int sp2px(int value) {
        if (value < 0) {
            value = 14;
        }
        return (int) (getResources().getDisplayMetrics().density * value + 0.5f);
    }


}
