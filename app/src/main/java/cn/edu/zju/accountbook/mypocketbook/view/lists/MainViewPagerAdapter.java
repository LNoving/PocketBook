package cn.edu.zju.accountbook.mypocketbook.view.lists;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.edu.zju.accountbook.mypocketbook.view.ShowActivity;
import cn.edu.zju.accountbook.mypocketbook.view.charts.ChartsFragment;

/**
 * @author
 * Created by 张昊 on 2017/12/21.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 3;

    private MainListFragment myFragment1;
    private ListFragment myFragment2;
    private ChartsFragment mChartsFragment;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        myFragment1 = new MainListFragment();
        myFragment2 = new ListFragment();
        mChartsFragment = new ChartsFragment();
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
                fragment = myFragment1;
                break;
            case ShowActivity.PAGE_TWO:
                fragment = myFragment2;
                break;
            case ShowActivity.PAGE_THREE:
                fragment = mChartsFragment;
                break;
        }
        return fragment;
    }
}
