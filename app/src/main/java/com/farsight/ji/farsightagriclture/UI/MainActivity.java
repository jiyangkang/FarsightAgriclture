package com.farsight.ji.farsightagriclture.UI;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.R;
import com.farsight.ji.farsightagriclture.Service.DueDatasService;
import com.farsight.ji.farsightagriclture.Service.SoapService;
import com.farsight.ji.farsightagriclture.Service.UDPService;
import com.farsight.ji.farsightagriclture.Tools.DrawButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnTouchListener {

    private DrawButton btnVoir, btnCtrl, btnChangeNet;
    private ImageView imageTab;
    private VoirFragment voirFragment;
    private CtrlFragment ctrlFragment;
    private ChangeFragement changeFragement;


    private ViewPager mViewPager;

    private List<Fragment> fragmentList;

    private int screenWidth;
    private int currentTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置全屏和无标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_main);

        initShow();


        btnVoir.setOnTouchListener(this);
        btnCtrl.setOnTouchListener(this);
        btnChangeNet.setOnTouchListener(this);
        //设置ImageView的属性
        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(screenWidth / 3,
                btnVoir.getMeasuredHeight() / 10);
        imgParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        imageTab.setLayoutParams(imgParams);

        mViewPager.setAdapter(new SwitchFragmentAdapter(getSupportFragmentManager()));
        if (TotalDatas.isUDP) {
            Intent intent = new Intent(MainActivity.this, UDPService.class);
            startService(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, SoapService.class);
            startService(intent);
        }
        Intent intent1 = new Intent(MainActivity.this, DueDatasService.class);
        startService(intent1);

    }

    private void initShow() {

        btnVoir = (DrawButton) findViewById(R.id.btn_voir);
        btnVoir.setBitmapDefault(R.drawable.part_voir_noclicked);
        btnVoir.invalidate();

        btnCtrl = (DrawButton) findViewById(R.id.btn_ctrl);
        btnCtrl.setBitmapDefault(R.drawable.part_ctrl_noclicked);
        btnCtrl.invalidate();

        btnChangeNet = (DrawButton) findViewById(R.id.btn_change);
        btnChangeNet.setBitmapDefault(R.drawable.part_net_noclicked);
        btnChangeNet.invalidate();

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        voirFragment = new VoirFragment();
        ctrlFragment = new CtrlFragment();
        changeFragement = new ChangeFragement();

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(voirFragment);
        fragmentList.add(ctrlFragment);
        fragmentList.add(changeFragement);

        screenWidth = getResources().getDisplayMetrics().widthPixels;

        btnVoir.measure(0, 0);

        imageTab = (ImageView) findViewById(R.id.img_tab);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.btn_voir:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnVoir.setBitmapDefault(R.drawable.part_voir_clicked);
                    btnVoir.invalidate();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnVoir.setBitmapDefault(R.drawable.part_voir_noclicked);
                    changeView(0);
                    btnVoir.invalidate();
                }
                break;
            case R.id.btn_ctrl:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnCtrl.setBitmapDefault(R.drawable.part_ctrl_clicked);
                    btnCtrl.invalidate();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnCtrl.setBitmapDefault(R.drawable.part_ctrl_noclicked);
                    changeView(1);
                    btnCtrl.invalidate();
                }
                break;

            case R.id.btn_change:
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    btnChangeNet.setBitmapDefault(R.drawable.part_net_clicked);
                    btnChangeNet.invalidate();
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    btnChangeNet.setBitmapDefault(R.drawable.part_net_noclicked);
                    changeView(2);
                    btnChangeNet.invalidate();
                }
                break;
        }
        return true;//自定义的绝对是恶心人的---啊啊啊啊

    }

    private class SwitchFragmentAdapter extends FragmentPagerAdapter {

        public SwitchFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            int currentItem = mViewPager.getCurrentItem();
            if (currentTab == currentItem) {
                return;
            }
            imageMove(mViewPager.getCurrentItem());
            currentTab = mViewPager.getCurrentItem();
        }
    }

    private void imageMove(int currentItem) {
        int startPositon = 0;
        int moveToPosition = 0;

        startPositon = currentTab * (screenWidth / 3);
        moveToPosition = currentItem * (screenWidth / 3);

        TranslateAnimation translateAnimation = new TranslateAnimation(startPositon, moveToPosition, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(200);
        imageTab.startAnimation(translateAnimation);
    }


    private void changeView(int desTab) {
        mViewPager.setCurrentItem(desTab, true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, DueDatasService.class);
        stopService(intent);
        Intent intent1 = new Intent(this, SoapService.class);
        stopService(intent1);
        Intent intent2 = new Intent(this, UDPService.class);
        stopService(intent2);

    }
}
