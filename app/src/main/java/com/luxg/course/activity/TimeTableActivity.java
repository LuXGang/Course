package com.luxg.course.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.luxg.course.R;
import com.luxg.course.adapter.TablePagerAdapter;
import com.luxg.course.fragment.WeekCourseFragment;
import com.luxg.course.widget.basewise.BaseWiseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuXiaogang on 2016/1/31.
 */
public class TimeTableActivity extends BaseWiseActivity {
    /**
     * TabLayout tabLayout; ViewPager vPager;
     */
    private TabLayout tabLayout;
    private ViewPager vPager;
    /**
     * channelList 要搭配实体类 --就是的具体实体类
     * fragmentsList 是fragments 的 List
     * 以及两个适配器的申明 ，其中一个是内部适配器ViolationFragmentPagerAdapter
     */
    private List<Fragment> fragmentsList = new ArrayList<>();
    private TablePagerAdapter mPagerAdapter;
    private TimeTableFragmentPagerAdapter mTabPagerAdapter;
    private String tabTitles[] = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        setTitleView("课程表");
        initView();
        initViewPager();
        initTabLayout();
    }

    private void initView() {
        /**
         * TabLayout tabLayout    ViewPager vPager
         */
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        vPager = (ViewPager) findViewById(R.id.vPager);
    }

    private void initViewPager() {
        vPager.setOffscreenPageLimit(1);
        fragmentsList.clear();

        /**
         * "1","2"代表周一，周二 以此类推
         */
        Fragment fragment1 = WeekCourseFragment.newInstance("1");
        Fragment fragment2 = WeekCourseFragment.newInstance("2");
        Fragment fragment3 = WeekCourseFragment.newInstance("3");
        Fragment fragment4 = WeekCourseFragment.newInstance("4");
        Fragment fragment5 = WeekCourseFragment.newInstance("5");
        Fragment fragment6 = WeekCourseFragment.newInstance("6");
        Fragment fragment7 = WeekCourseFragment.newInstance("7");
        fragmentsList.add(fragment1);
        fragmentsList.add(fragment2);
        fragmentsList.add(fragment3);
        fragmentsList.add(fragment4);
        fragmentsList.add(fragment5);
        fragmentsList.add(fragment6);
        fragmentsList.add(fragment7);


        mPagerAdapter = new TablePagerAdapter(getSupportFragmentManager(), fragmentsList);
        vPager.setAdapter(mPagerAdapter);
        vPager.setCurrentItem(0, false);
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void initTabLayout() {
        mTabPagerAdapter = new TimeTableFragmentPagerAdapter(TimeTableActivity.this.getSupportFragmentManager(), getContext());
        vPager.setAdapter(mTabPagerAdapter);

        tabLayout.setupWithViewPager(vPager);
    }


    public class TimeTableFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT;
        private Context context;

        public TimeTableFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            PAGE_COUNT = tabTitles.length;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position].toString();
        }
    }

    // 页卡切换监听
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
