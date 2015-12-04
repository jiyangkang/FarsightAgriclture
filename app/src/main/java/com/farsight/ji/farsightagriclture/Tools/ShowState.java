package com.farsight.ji.farsightagriclture.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.farsight.ji.farsightagriclture.R;

/**
 * 监控节点
 * Created by jiyan on 2015/11/29.
 */
public class ShowState extends View {

    private Paint mPaint;
    private Context mContext;

    private Bitmap bitmapNode;
    //    private Bitmap bitmapBaseLine;
    private String name;
    private String valueNode1;
    private String valueNode2;

    private Rect rectOri;
    private Rect rectDst;

    public void setBitmapNode(int id) {
        bitmapNode = BitmapFactory.decodeResource(getResources(), id);
    }


    public void setName(String name) {
        this.name = name;
    }

    public ShowState(Context context) {
        this(context, null);
    }

    public ShowState(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowState(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        int widthMetrics = context.getResources().getDisplayMetrics().widthPixels;

        mPaint = new Paint();
        mPaint.setTextSize(16);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        bitmapNode = BitmapFactory.decodeResource(getResources(), R.drawable.node);

        valueNode1 = "节点未连接";

        int oX = bitmapNode.getWidth();
        int oY = bitmapNode.getHeight();
        int rX;
        if (oX < widthMetrics / 3) {
            rX = widthMetrics / 3;
        } else {
            rX = oX;
        }
        int rY = rX * oY / oX;
        rectOri = new Rect(0, 0, oX, oY);
        rectDst = new Rect(0, 0, rX, rY);

    }

    public void setStrValue(String valueNode1, String valueNode2) {
        this.valueNode1 = valueNode1;
        this.valueNode2 = valueNode2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmapNode, rectOri, rectDst, mPaint);
        canvas.drawText(valueNode1, rectDst.width() / 6, rectDst.bottom - 3, mPaint);
        if (valueNode2 != null) {
            canvas.drawText(valueNode2, rectDst.width() / 2 + rectDst.width() / 12, rectDst.bottom - 3, mPaint);
        } else {
        }

        if (name != null) {
            canvas.drawText(name, rectDst.width() / 6, rectDst.top + rectDst.height() / 2, mPaint);
        }

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
            int desired = (int) (getPaddingLeft() + getPaddingRight() + rectDst.width());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired;

            desired = (int) (getPaddingTop() + getPaddingBottom() + rectDst.height());
            height = desired;
        }
        setMeasuredDimension(width, height);
    }
}