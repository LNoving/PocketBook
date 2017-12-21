package cn.edu.zju.accountbook.accountbookdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.edu.zju.accountbook.accountbookdemo.view.ListFragment;

/**
 * @author
 * Created by 张昊 on 2017/12/21.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 3;

    private MyFragment1 myFragment1;
    private ListFragment myFragment2;
    private MyFragment3 myFragment3 ;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        myFragment1 = new MyFragment1();
        myFragment2 = new ListFragment();
        myFragment3 = new MyFragment3();
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
                fragment = myFragment3;
                break;
        }
        return fragment;
    }
}
