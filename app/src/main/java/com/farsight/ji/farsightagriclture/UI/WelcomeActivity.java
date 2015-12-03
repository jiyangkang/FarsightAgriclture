package com.farsight.ji.farsightagriclture.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.farsight.ji.farsightagriclture.R;

/**
 * 欢迎页面，选择内外网
 * Created by jiyan on 2015/12/2.
 */
public class WelcomeActivity extends Activity {
    private TextView textView;
    private String host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置全屏和无标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_activity);

    }

}
