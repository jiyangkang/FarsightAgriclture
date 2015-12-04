package com.farsight.ji.farsightagriclture.Tools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 *
 * Created by Jaque_Ji on 2015/12/5 0005.
 */
public class DrawEditText extends EditText{

    Context mContext;

    public DrawEditText(Context context){
        this(context, null);
    }

    public DrawEditText(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public DrawEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }


}
