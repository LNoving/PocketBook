package cn.edu.zju.accountbook.accountbookdemo.view.myViewPager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import cn.edu.zju.accountbook.accountbookdemo.view.ListFragment;
import cn.edu.zju.accountbook.accountbookdemo.view.PieChartFragment;
import cn.edu.zju.accountbook.accountbookdemo.view.BarChartFragment;
import cn.edu.zju.accountbook.accountbookdemo.view.LineChartFragment;
import cn.edu.zju.accountbook.accountbookdemo.view.ViewActivity;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

//    private final int PAGER_COUNT = 4;
    private int size;
    private ListFragment mMyListFragment = null;
    private PieChartFragment mMyPieChartFragment = null;
    private BarChartFragment myBarChartFragment = null;
    private LineChartFragment myLineChartFragment = null;

    public MyViewPagerAdapter(FragmentManager fm,int size) {
        super(fm);
        mMyListFragment = new ListFragment();
        mMyPieChartFragment = new PieChartFragment();
        myBarChartFragment = new BarChartFragment();
        myLineChartFragment = new LineChartFragment();
        this.size = size;
    }


    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case ViewActivity.PAGE_ONE:
                fragment = mMyListFragment;
                break;
            case ViewActivity.PAGE_TWO:
                fragment = mMyPieChartFragment;
                break;
            case ViewActivity.PAGE_THREE:
                fragment = myBarChartFragment;
                break;
            case ViewActivity.PAGE_FOUR:
                fragment = myLineChartFragment;
                break;
        }
        return fragment;
    }

}
