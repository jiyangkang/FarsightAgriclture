package com.farsight.ji.farsightagriclture.Tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.farsight.ji.farsightagriclture.Datas.NodeInfo;
import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.R;

/**
 * Created by jiyan on 2015/12/1.
 */
public class CtrlableState extends View {

    private Context mContext;

    private Paint mPaint;
    private Rect rectOri, rectDst, rectScrollOri, rectScrollDst, rectScrollDstReight, rectScrollDSTLeft,
            rectScrollNo;

    private Bitmap bitmapdefault;
    private Bitmap bitmapScroll, bitmapScrollNoclicked, bitmapScrollClicked;
    private int state = -9999;
    private String stringState;
    private boolean moving = false;
    private boolean isClicked = false;


    private byte[] openCmd = new byte[7];
    private byte[] closeCmd = new byte[7];

    public void setName(byte type) {

        switch (type) {
            case NodeInfo.TYPE_WARM:
                openCmd = NodeInfo.OPEN_WARM;
                closeCmd = NodeInfo.CLOSE_WARM;
                break;
            case NodeInfo.TYPE_HUMIFY:
                openCmd = NodeInfo.OPEN_HUMIFY;
                closeCmd = NodeInfo.CLOSE_HUMIFY;
                break;
            case NodeInfo.TYPE_FAN:
                openCmd = NodeInfo.OPEN_FAN;
                closeCmd = NodeInfo.CLOSE_FAN;
                break;
            case NodeInfo.TYPE_LAMP:
                openCmd = NodeInfo.OPEN_LAMP;
                closeCmd = NodeInfo.CLOSE_LAMP;
                break;
            case NodeInfo.TYPE_DRENCHING:
                openCmd = NodeInfo.OPEN_DRENCHING;
                closeCmd = NodeInfo.CLOSE_DRENCHING;
                break;
            case NodeInfo.TYPE_SHADE:
                openCmd = NodeInfo.OPEN_SHADE;
                closeCmd = NodeInfo.CLOSE_SHADE;
                break;
            case NodeInfo.TYPE_ALARM:
                openCmd = NodeInfo.OPEN_ALARM;
                closeCmd = NodeInfo.CLOSE_ALARM;
                break;
            case NodeInfo.TYPE_CAMERA:

                break;
        }
    }

    public void setState(int state) {
        this.state = state;
        if (state == 0){
            setStringState("\"关\"");
            if (!moving){
                rectScrollDst = rectScrollDSTLeft;
                moving = true;
            }
        }else if (state == 1){
            setStringState("\"开\"");
            if (!moving){
                rectScrollDst = rectScrollDstReight;
                moving = true;
            }
        }else if (state == -1){
            setStringState("\"断开连接\"");
            rectScrollDst = rectScrollNo;
            moving = false;
        }else if(state == 2){
            setStringState("视频监控");
            rectScrollDst = rectScrollNo;
        }else if (state < -1){
            setStringState("\"未连接\"");
            rectScrollDst = rectScrollNo;
            moving = false;
        }
    }

    public void setBitmapdefault(int bitmapdefault) {
        this.bitmapdefault = BitmapFactory.decodeResource(mContext.getResources(), bitmapdefault);
    }

    public void setStringState(String state) {
        this.stringState = state;
    }

    public CtrlableState(Context context) {
        this(context, null);
    }

    public CtrlableState(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CtrlableState(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        int widthMetrics = context.getResources().getDisplayMetrics().widthPixels;

        mPaint = new Paint();
        mPaint.setTextSize(12);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        bitmapdefault = BitmapFactory.decodeResource(getResources(), R.drawable.cnode);
        int oX, oY, dX, dY;
        oX = bitmapdefault.getWidth();
        oY = bitmapdefault.getHeight();
        rectOri = new Rect(0, 0, oX, oY);
        dX = widthMetrics / 9;
        dY = oY * dX / oX;
        rectDst = new Rect(0, 0, dX, dY);

        bitmapScrollNoclicked = BitmapFactory.decodeResource(getResources(), R.drawable.scroll);
        bitmapScrollClicked = BitmapFactory.decodeResource(getResources(), R.drawable.scrollclicked);
        bitmapScroll = bitmapScrollNoclicked;
        int sOX, sOY, sDY, sDX;
        sOX = bitmapScroll.getWidth();
        sOY = bitmapScroll.getHeight();
        rectScrollOri = new Rect(0, 0, sOX, sOY);

        sDX = sOX * dX / oX;
        sDY = sOY * dY / oY;
        rectScrollDSTLeft = new Rect(rectDst.left + 2, rectDst.bottom - sDY - 2, rectDst.left + sDX, rectDst.bottom -
                2);
        rectScrollDstReight = new Rect(rectDst.right - sDX - 2, rectDst.bottom - sDY - 2, rectDst.right - 2,
                rectDst.bottom - 2);
        rectScrollNo = new Rect(rectDst.left + 2, rectDst.bottom - sDY - 2, rectDst.right - 2, rectDst.bottom - 2);
        rectScrollDst = new Rect();
        rectScrollDst = rectScrollDSTLeft;
        openCmd = NodeInfo.OPEN;
        closeCmd = NodeInfo.CLOSE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmapdefault, rectOri, rectDst, mPaint);
        canvas.drawBitmap(bitmapScroll, rectScrollOri, rectScrollDst, mPaint);
        if (stringState != null) {
            if (state == 0 || state == 1) {
                canvas.drawText(stringState, rectDst.width() * 15 / 20, rectDst.height() * 22 / 30, mPaint);
            } else {
                canvas.drawText(stringState, rectDst.width() / 4, rectDst.height() * 19 / 20, mPaint);
            }
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
            int desired = getPaddingLeft() + getPaddingRight() + rectDst.width();
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = getPaddingTop() + getPaddingBottom() + rectDst.height();
            height = desired;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x > rectScrollDst.left && x < rectScrollDst.right && y > rectScrollDst.top &&
                        y < rectScrollDst.bottom) {
                    bitmapScroll = bitmapScrollClicked;
                    isClicked = true;
                }
                break;
            case MotionEvent.ACTION_UP:

                    isClicked = false;
                    bitmapScroll = bitmapScrollNoclicked;
                    if (state == 0) {
                        rectScrollDst = rectScrollDstReight;
                        try {
                            TotalDatas.qSend.put(openCmd);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (state == 1) {
                        rectScrollDst = rectScrollDSTLeft;
                        try {
                            TotalDatas.qSend.put(closeCmd);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (state == 2){
                        ComponentName componentName = new ComponentName("com.mobile.myeye", "com.mobile.myeye" +
                                ".activity.WelcomeActivity");
                        Intent intent = new Intent();
                        intent.setComponent(componentName);
                        intent.setAction("android.intent.action.MAIN");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                break;
        }
        invalidate();
        return true;
    }

}
