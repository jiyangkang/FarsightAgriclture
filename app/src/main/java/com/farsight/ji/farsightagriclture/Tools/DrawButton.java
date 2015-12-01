package com.farsight.ji.farsightagriclture.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.farsight.ji.farsightagriclture.R;

/**
 * 绘制按钮
 * Created by jiyan on 2015/11/28.
 */
public class DrawButton extends View{

    private Context mContext;

    private Rect orRect, dstRect;

    private Paint mPaint;

    private Bitmap bitmapDefault, bitmapClicked, bitmapStand;

    public void setBitmapDefault(int bitmapDefault){
        this.bitmapDefault = BitmapFactory.decodeResource(getResources(), bitmapDefault);
    }

    public void setBitmapStand(int bitmapStand){
        this.bitmapStand = BitmapFactory.decodeResource(getResources(), bitmapStand);
    }

    public void setBitmapClicked(int bitmapClicked){
        this.bitmapClicked = BitmapFactory.decodeResource(getResources(), bitmapClicked);
    }

    public DrawButton(Context context){
        this(context, null);
    }

    public DrawButton(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public DrawButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        int widthMetrics = context.getResources().getDisplayMetrics().widthPixels;
        setBitmapDefault(R.drawable.button);

        mPaint = new Paint();
        mPaint.setTextSize(18);
        mPaint.setStrokeWidth(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);


        int oX = bitmapDefault.getWidth();
        int oY = bitmapDefault.getHeight();

        int rX = widthMetrics/5;
        int rY = rX * oY / oX;

        orRect = new Rect(0, 0, oX, oY);
        dstRect = new Rect(0, 0, rX, rY);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmapDefault,orRect,dstRect,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x, y;
        x = event.getX();
        y = event.getY();
        if (x > dstRect.left && x < dstRect.right && dstRect.top < y && dstRect.bottom > y){


            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    bitmapDefault = bitmapClicked;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    bitmapDefault = bitmapStand;
                    invalidate();
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
                int desired = (int) (getPaddingLeft() + getPaddingRight() + dstRect.width());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + dstRect.height() + getPaddingBottom());
            height = desired;
        }
        setMeasuredDimension(width, height);
    }
}
