package com.farsight.ji.farsightagriclture.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.farsight.ji.farsightagriclture.R;
import com.farsight.ji.farsightagriclture.Tools.DrawButton;

import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private DrawButton btnVoir, btnCtrl;
    private ImageView imageTab;
    private VoirFragment voirFragment;
    private CtrlFragment ctrlFragment;

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

        btnCtrl.setOnClickListener(this);
        btnVoir.setOnClickListener(this);

        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(screenWidth/2,btnVoir.getMeasuredHeight());
        imgParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        imageTab.setLayoutParams(imgParams);

        mViewPager.setAdapter(new SwitchFragmentAdapter(getSupportFragmentManager()));
    }

    private void initShow() {
        btnVoir = (DrawButton) findViewById(R.id.btn_voir);
        btnVoir.setBitmapDefault(R.drawable.part_voir_noclicked);
        btnVoir.setBitmapClicked(R.drawable.part_voir_clicked);
        btnVoir.setBitmapStand(R.drawable.part_voir_noclicked);
        btnVoir.invalidate();

        btnCtrl = (DrawButton) findViewById(R.id.btn_ctrl);
        btnCtrl.setBitmapDefault(R.drawable.part_ctrl_noclicked);
        btnCtrl.setBitmapStand(R.drawable.part_ctrl_noclicked);
        btnCtrl.setBitmapClicked(R.drawable.part_ctrl_clicked);
        btnCtrl.invalidate();

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        voirFragment = new VoirFragment();
        ctrlFragment = new CtrlFragment();
        fragmentList.add(voirFragment);
        fragmentList.add(ctrlFragment);

        screenWidth = getResources().getDisplayMetrics().widthPixels;

        btnVoir.measure(0, 0);

        imageTab = (ImageView) findViewById(R.id.img_tab);

    }

    private class SwitchFragmentAdapter extends FragmentPagerAdapter{

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
            if (currentTab == currentItem){
                return;
            }
            imageMove(mViewPager.getCurrentItem());
            currentTab = mViewPager.getCurrentItem();
        }
    }

    private void imageMove(int currentItem) {
        int startPositon = 0;
        int moveToPosition = 0;

        startPositon = currentTab * (screenWidth/2);
        moveToPosition = currentItem * (screenWidth/2);

        TranslateAnimation translateAnimation = new TranslateAnimation(startPositon,moveToPosition, 0,0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(200);
        imageTab.startAnimation(translateAnimation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_voir:
                changeView(0);
                break;
            case R.id.btn_ctrl:
                changeView(1);
                break;
        }
    }

    private void changeView(int desTab)
    {
        mViewPager.setCurrentItem(desTab, true);
    }
}
