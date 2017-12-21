package cn.edu.zju.accountbook.accountbookdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.edu.zju.accountbook.accountbookdemo.view.BarChartFragment;
import cn.edu.zju.accountbook.accountbookdemo.view.LineChartFragment;
import cn.edu.zju.accountbook.accountbookdemo.view.ListFragment;
import cn.edu.zju.accountbook.accountbookdemo.view.PieChartFragment;

/**
 * Created by 张昊 on 2017/12/21.
 */

public class ChartViewPagerAdapter extends FragmentPagerAdapter{

    private final int PAGER_COUNT = 3;

    private BarChartFragment barChartFragment ;
    private PieChartFragment pieChartFragment;
    private LineChartFragment lineChartFragment;

    public ChartViewPagerAdapter(FragmentManager fm) {
        super(fm);
        barChartFragment = new BarChartFragment();
        pieChartFragment = new PieChartFragment();
        lineChartFragment = new LineChartFragment();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case ShowActivity.PAGE_ONE:
                fragment = barChartFragment;
                break;
            case ShowActivity.PAGE_TWO:
                fragment = pieChartFragment;
                break;
            case ShowActivity.PAGE_THREE:
                fragment = lineChartFragment;
                break;
        }
        return fragment;
    }
}
