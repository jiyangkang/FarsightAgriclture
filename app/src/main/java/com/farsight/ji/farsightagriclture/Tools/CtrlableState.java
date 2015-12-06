package com.farsight.ji.farsightagriclture.Tools;

import android.content.Context;
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
    private Rect rectOri, rectDst, rectScrollOri, rectScrollDst, rectScrollDstReight, rectScrollDSTLeft;

    private Bitmap bitmapdefault;
    private Bitmap bitmapScroll;
    private int state = -9999;
    private String stringState;

    private byte[] openCmd = new byte[7];
    private byte[] closeCmd = new byte[7];

    public void setName(byte type) {

        switch (type) {
            case NodeInfo.TYPE_WARM:
//                name = NodeInfo.WARM;
                openCmd = NodeInfo.OPEN_WARM;
                closeCmd = NodeInfo.CLOSE_WARM;
                break;
            case NodeInfo.TYPE_HUMIFY:
//                name = NodeInfo.HUMIFY;
                openCmd = NodeInfo.OPEN_HUMIFY;
                closeCmd = NodeInfo.CLOSE_HUMIFY;
                break;
            case NodeInfo.TYPE_FAN:
//                name = NodeInfo.FAN;
                openCmd = NodeInfo.OPEN_FAN;
                closeCmd = NodeInfo.CLOSE_FAN;
                break;
            case NodeInfo.TYPE_LAMP:
//                name = NodeInfo.LAMP;
                openCmd = NodeInfo.OPEN_LAMP;
                closeCmd = NodeInfo.CLOSE_LAMP;
                break;
            case NodeInfo.TYPE_DRENCHING:
//                name = NodeInfo.DRENCHING;
                openCmd = NodeInfo.OPEN_DRENCHING;
                closeCmd = NodeInfo.CLOSE_DRENCHING;
                break;
            case NodeInfo.TYPE_SHADE:
//                name = NodeInfo.SHADE;
                openCmd = NodeInfo.OPEN_SHADE;
                closeCmd = NodeInfo.CLOSE_SHADE;
                break;
            case NodeInfo.TYPE_ALARM:
//                name = NodeInfo.ALARM;
                openCmd = NodeInfo.OPEN_ALARM;
                closeCmd = NodeInfo.CLOSE_ALARM;
                break;
        }
    }

    public void setState(int state) {
        this.state = state;
        if (state == 1) {
            rectScrollDst = rectScrollDstReight;
            setStringState("\"开\"");
        } else if (state == 0) {
            rectScrollDst = rectScrollDSTLeft;
            setStringState("\"关\"");
        } else if (state == -1) {
            rectScrollDst = rectScrollDSTLeft;
            setStringState("断开连接");
        } else {
            rectScrollDst = rectScrollDSTLeft;
            setStringState("未连接");
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

//        name = " ";
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
        dX = widthMetrics / 7;
        dY = oY * dX / oX;
        rectDst = new Rect(0, 0, dX, dY);

        bitmapScroll = BitmapFactory.decodeResource(getResources(), R.drawable.scroll);
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
        rectScrollDst = new Rect();
        rectScrollDst = rectScrollDSTLeft;
        openCmd = NodeInfo.OPEN;
        closeCmd = NodeInfo.CLOSE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmapdefault, rectOri, rectDst, mPaint);
//        canvas.drawBitmap(openDefault, rectOpenOri, rectOpenDst, mPaint);
//        canvas.drawBitmap(closeDefault, rectCloseOri, rectCloseDst, mPaint);

//        if (name != null) {
//            canvas.drawText(name, rectOpenDst.left, rectDst.height() / 3, mPaint);
//        }
//        if (state > -1) {
//            canvas.drawBitmap(bitmapScroll, rectScrollOri, rectScrollDst, mPaint);
//
//        } else if(state == 0){
//
//        }
        canvas.drawBitmap(bitmapScroll, rectScrollOri, rectScrollDst, mPaint);
        if (stringState != null) {
            canvas.drawText(stringState, rectDst.width() * 8 / 10, rectDst.height() * 22 / 30, mPaint);
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
            int desired = (int) (getPaddingTop() + getPaddingBottom() + rectDst.height());
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
                    if (state == 0) {
//                        openDefault = openButtonClicked;
                    }
                } else if (x > rectScrollDst.left && x < rectScrollDst.right && y > rectScrollDst.top && y <
                        rectScrollDst.bottom) {
                    if (state == 1) {
//                        closeDefault = closeButtonClicked;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        invalidate();
        return true;
    }

}
