package com.luxg.course.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by LuXiaogang on 2016/1/31.
 */
public class TablePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentsList;
    private FragmentManager fm;

    public TablePagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public TablePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragmentsList = fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        if (this.fragmentsList != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragmentsList) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
        this.fragmentsList = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
}
