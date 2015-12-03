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
    private Rect rectOri, rectDst, rectOpenOri, rectOpenDst, rectCloseOri, rectCloseDst;

    private Bitmap bitmapdefault;
    private Bitmap openDefault, closeDefault, openButton, closeButton, openButtonClicked,
            closeButtonClicked;
    private String name;
    private int state = 0;
    private String stringState;

    private byte[] openCmd = new byte[7];
    private byte[] closeCmd = new byte[7];

    public void setName(byte type) {

        switch (type) {
            case NodeInfo.TYPE_WARM:
                name = NodeInfo.WARM;
                openCmd = NodeInfo.OPEN_WARM;
                closeCmd =NodeInfo.CLOSE_WARM;
                break;
            case NodeInfo.TYPE_HUMIFY:
                name = NodeInfo.HUMIFY;
                openCmd = NodeInfo.OPEN_HUMIFY;
                closeCmd =NodeInfo.CLOSE_HUMIFY;
                break;
            case NodeInfo.TYPE_FAN:
                name = NodeInfo.FAN;
                openCmd = NodeInfo.OPEN_FAN;
                closeCmd =NodeInfo.CLOSE_FAN;
                break;
            case NodeInfo.TYPE_LAMP:
                name = NodeInfo.LAMP;
                openCmd = NodeInfo.OPEN_LAMP;
                closeCmd =NodeInfo.CLOSE_LAMP;
                break;
            case NodeInfo.TYPE_DRENCHING:
                name = NodeInfo.DRENCHING;
                openCmd = NodeInfo.OPEN_DRENCHING;
                closeCmd =NodeInfo.CLOSE_DRENCHING;
                break;
            case NodeInfo.TYPE_SHADE:
                name = NodeInfo.SHADE;
                openCmd = NodeInfo.OPEN_SHADE;
                closeCmd =NodeInfo.CLOSE_SHADE;
                break;
            case NodeInfo.TYPE_ALARM:
                name = NodeInfo.ALARM;
                openCmd = NodeInfo.OPEN_ALARM;
                closeCmd =NodeInfo.CLOSE_ALARM;
                break;
        }
    }

    public void setState(int state) {
        this.state = state;
        if (state == 1) {
            setStringState("\"开\"" );
        } else if (state == 0) {
            setStringState("\"关\"");
        } else if(state == -1){
            setStringState("断开连接");
        } else {
            setStringState("未连接");
        }
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

        name = " ";

        int widthMetrics = context.getResources().getDisplayMetrics().widthPixels;

        mPaint = new Paint();
        mPaint.setTextSize(18);
        mPaint.setStrokeWidth(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        bitmapdefault = BitmapFactory.decodeResource(getResources(), R.drawable.cnode);

        int oX, oY, dX, dY;
        oX = bitmapdefault.getWidth();
        oY = bitmapdefault.getHeight();
        rectOri = new Rect(0, 0, oX, oY);
        dX = widthMetrics / 6;
        dY = oY * dX / oX;
        rectDst = new Rect(0, 0, dX, dY);

        openButton = BitmapFactory.decodeResource(getResources(), R.drawable.open);
        openButtonClicked = BitmapFactory.decodeResource(getResources(), R.drawable.open_clicked);
        openDefault = openButton;
        rectOpenOri = new Rect(0, 0, openDefault.getWidth(), openDefault.getHeight());
        rectOpenDst = new Rect(1, rectDst.height() / 2, rectDst.width() / 2 - 1, rectDst.height() - 1);

        closeButton = BitmapFactory.decodeResource(getResources(), R.drawable.close);
        closeButtonClicked = BitmapFactory.decodeResource(getResources(), R.drawable.close_clicked);
        closeDefault = closeButton;
        rectCloseOri = new Rect(0, 0, closeDefault.getWidth(), closeDefault.getHeight());
        rectCloseDst = new Rect(rectDst.width() / 2 + 1, rectDst.height() / 2, rectDst.width() - 1, rectDst.height()
                - 1);

        openCmd = NodeInfo.OPEN;
        closeCmd = NodeInfo.CLOSE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmapdefault, rectOri, rectDst, mPaint);
        canvas.drawBitmap(openDefault, rectOpenOri, rectOpenDst, mPaint);
        canvas.drawBitmap(closeDefault, rectCloseOri, rectCloseDst, mPaint);
        if (name != null) {
            canvas.drawText(name, rectDst.left + 1, rectDst.height() / 4, mPaint);
        }
        if (stringState != null) {
            canvas.drawText(stringState, rectDst.left + 1, rectDst.height() / 2, mPaint);
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
                if (x > rectOpenDst.left && x < rectOpenDst.right && y > rectOpenDst.top && y < rectOpenDst.bottom) {
                    openDefault = openButtonClicked;
                } else if (x > rectCloseDst.left && x < rectCloseDst.right && y > rectCloseDst.top && y < rectCloseDst
                        .bottom) {
                    closeDefault = closeButtonClicked;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (x > rectOpenDst.left && x < rectOpenDst.right && y > rectOpenDst.top && y < rectOpenDst.bottom) {
                    openDefault = openButton;
                    if (state == 0) {
                        try {
                            TotalDatas.qSend.put(openCmd);
                            Log.d("put", StringTool.hexToString(openCmd) + name);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (x > rectCloseDst.left && x < rectCloseDst.right && y > rectCloseDst.top && y < rectCloseDst
                        .bottom) {
                    closeDefault = closeButton;
                    if (state == 1) {
                        try {
                            TotalDatas.qSend.put(closeCmd);
                            Log.d("put", StringTool.hexToString(closeCmd) + name);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
        invalidate();
        return true;
    }

}
