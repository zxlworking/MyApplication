package com.zxl.river.chief;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxl.river.chief.fragmetn.CountFragment;
import com.zxl.river.chief.fragmetn.MeFragment;
import com.zxl.river.chief.fragmetn.RiverFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private static final int RIVER_INDEX = 0;
    private static final int COUNT_INDEX = 1;
    private static final int ME_INDEX = 2;

    private static final String[] TITLE_ARRAY = new String[]{"巡河","统计分析","我的待办"};

    private ViewPager mViewPager;

    private CustomAdapter mCustomAdapter;

    private List<Fragment> mFragments = new ArrayList<>();

    private LinearLayout mRiverLl;
    private LinearLayout mCountLl;
    private LinearLayout mMeLl;

    private ImageView mRiverImg;
    private ImageView mCountImg;
    private ImageView mMeImg;

    private TextView mRiverTv;
    private TextView mCountTv;
    private TextView mMeTv;

    private int mCurrentIndex = RIVER_INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //addPreferencesFromResource(R.xml.category);

        //setTitle(TITLE_ARRAY[RIVER_INDEX]);

        mFragments.add(new RiverFragment());
        mFragments.add(new CountFragment());
        mFragments.add(new MeFragment());


        mRiverLl = (LinearLayout) findViewById(R.id.river_ll);
        mCountLl = (LinearLayout) findViewById(R.id.count_ll);
        mMeLl = (LinearLayout) findViewById(R.id.me_ll);

        mRiverImg = (ImageView) findViewById(R.id.river_img);
        mCountImg = (ImageView) findViewById(R.id.count_img);
        mMeImg = (ImageView) findViewById(R.id.me_img);

        mRiverTv = (TextView) findViewById(R.id.river_tv);
        mCountTv = (TextView) findViewById(R.id.count_tv);
        mMeTv = (TextView) findViewById(R.id.me_tv);

        mRiverLl.setOnClickListener(mOnClickListener);
        mCountLl.setOnClickListener(mOnClickListener);
        mMeLl.setOnClickListener(mOnClickListener);

        /*
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mCustomAdapter = new CustomAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mCustomAdapter);

        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        */

        setTab(RIVER_INDEX);

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.river_ll:
                    //mViewPager.setCurrentItem(RIVER_INDEX);
                    setTab(RIVER_INDEX);
                    break;
                case R.id.count_ll:
                    //mViewPager.setCurrentItem(COUNT_INDEX);
                    setTab(COUNT_INDEX);
                    break;
                case R.id.me_ll:
                    //mViewPager.setCurrentItem(DEAL_INDEX);
                    setTab(ME_INDEX);
                    break;
            }
        }
    };

    private void setTab(int index){

        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        for(int i = 0; i < mFragments.size(); i++){
            Fragment mFragment = mFragments.get(i);
            if(i != index){
                if (mFragment.isAdded()){
                    mFragmentTransaction.hide(mFragment);
                }
            }else{
                if(mFragment.isAdded()){
                    mFragmentTransaction.show(mFragment);
                }else{
                    mFragmentTransaction.add(R.id.content_fl,mFragment);
                }
            }

        }
        mFragmentTransaction.commit();


        //setTitle(TITLE_ARRAY[index]);
        switch (index){
            case RIVER_INDEX:
                mRiverImg.setImageResource(R.mipmap.ic_river_selected);
                mRiverTv.setTextColor(Color.parseColor("#199bff"));
                mCountImg.setImageResource(R.mipmap.ic_count_normal);
                mCountTv.setTextColor(Color.parseColor("#909090"));
                mMeImg.setImageResource(R.mipmap.ic_me_normal);
                mMeTv.setTextColor(Color.parseColor("#909090"));
                break;
            case COUNT_INDEX:
                mRiverImg.setImageResource(R.mipmap.ic_river_normal);
                mRiverTv.setTextColor(Color.parseColor("#909090"));
                mCountImg.setImageResource(R.mipmap.ic_count_selected);
                mCountTv.setTextColor(Color.parseColor("#199bff"));
                mMeImg.setImageResource(R.mipmap.ic_me_normal);
                mMeTv.setTextColor(Color.parseColor("#909090"));
                break;
            case ME_INDEX:
                mRiverImg.setImageResource(R.mipmap.ic_river_normal);
                mRiverTv.setTextColor(Color.parseColor("#909090"));
                mCountImg.setImageResource(R.mipmap.ic_count_normal);
                mCountTv.setTextColor(Color.parseColor("#909090"));
                mMeImg.setImageResource(R.mipmap.ic_me_selected);
                mMeTv.setTextColor(Color.parseColor("#199bff"));
                break;
        }
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    class CustomAdapter extends FragmentPagerAdapter{

        public CustomAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

    }

}
