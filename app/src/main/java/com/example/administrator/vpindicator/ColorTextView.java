package com.example.administrator.vpindicator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/3/16.
 */
public class ColorTextView extends View {

    private static final String TAG = "ColorTextView";


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

    private int textOriginColor = Color.BLACK;
    private int textChangeColor = Color.RED;

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

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ColorTextView);

        text = a.getString(R.styleable.ColorTextView_text);
        mDirection = a.getInt(R.styleable.ColorTextView_direction, DIRECTION_LEFT);
        textSize = a.getDimensionPixelSize(R.styleable.ColorTextView_text_size, sp2px(30));
        textOriginColor = a.getColor(R.styleable.ColorTextView_text_origin_color, Color.BLACK);
        textChangeColor = a.getColor(R.styleable.ColorTextView_text_change_color, Color.YELLOW);
        progress = a.getFloat(R.styleable.ColorTextView_progress, 0);

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDirection == DIRECTION_LEFT) {
            drawChangeLeft(canvas);
            drawOriginLeft(canvas);
        } else {
            drawChangeRight(canvas);
            drawOriginRight(canvas);
        }
    }

    public void drawChangeLeft(Canvas canvas) {
        drawText(canvas, mTextStratX, (int) (mTextStratX + progress * mTextWidth), textChangeColor);
    }

    public void drawOriginLeft(Canvas canvas) {
        drawText(canvas, (int) (mTextStratX + progress * mTextWidth), mTextStratX + mTextWidth, textOriginColor);
    }

    public void drawChangeRight(Canvas canvas) {
        drawText(canvas, (int) (mTextStratX + (1 - progress) * mTextWidth), mTextStratX + mTextWidth, textChangeColor);
    }

    public void drawOriginRight(Canvas canvas) {
        drawText(canvas, mTextStratX, (int) (mTextStratX + (1 - progress) * mTextWidth), textOriginColor);
    }

    public void drawText(Canvas canvas, int start, int end, int color) {
        mPaint.setColor(color);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(start, 0, end, getMeasuredHeight());
        canvas.drawText(text, mTextStratX, getMeasuredHeight() / 2
                + bound.height() / 2, mPaint);
        canvas.restore();
    }

    private int measureHeight(int heightMeasureSpec) {
        int model = MeasureSpec.getMode(heightMeasureSpec);
        int val = MeasureSpec.getSize(heightMeasureSpec);

        int result = 0;
        switch (model) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST: // 求出自己测绘出view的大小以及系统测绘出view大小之间最小值
                result = Math.min(val, bound.height());
                break;
            case MeasureSpec.UNSPECIFIED:
                result = bound.height();
                break;
        }
        Log.d(TAG, "height " + result);
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
                result = Math.min(size, mTextWidth);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = mTextWidth;
                break;
        }
        Log.d(TAG, "width " + result);
        return result + getPaddingLeft() + getPaddingRight();
    }

    public int sp2px(int value) {
        if (value < 0) {
            value = 14;
        }
        return (int) (getResources().getDisplayMetrics().density * value + 0.5f);
    }


    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate(); // 通过属性动画调用，要及时重绘view
    }

    public void beginLeft() {
        this.setDirection(DIRECTION_LEFT);
        ObjectAnimator.ofFloat(this, "progress", 0, 1).setDuration(2000).start();
    }

    public void beginRight() {
        this.setDirection(DIRECTION_RIGHT);
        ObjectAnimator.ofFloat(this, "progress", 0, 1).setDuration(2000).start();
    }


}
