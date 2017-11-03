package cn.edu.zju.accountbook.accountbookdemo.other;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import cn.edu.zju.accountbook.accountbookdemo.Fragment1;
import cn.edu.zju.accountbook.accountbookdemo.Fragment2;
import cn.edu.zju.accountbook.accountbookdemo.Fragment3;
import cn.edu.zju.accountbook.accountbookdemo.Fragment4;
import cn.edu.zju.accountbook.accountbookdemo.ViewActivity;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

//    private final int PAGER_COUNT = 4;
    private int size;
    private Fragment1 myFragment1 = null;
    private Fragment2 myFragment2 = null;
    private Fragment3 myFragment3 = null;
    private Fragment4 myFragment4 = null;

    public MyViewPagerAdapter(FragmentManager fm,int size) {
        super(fm);
        myFragment1 = new Fragment1();
        myFragment2 = new Fragment2();
        myFragment3 = new Fragment3();
        myFragment4 = new Fragment4();
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
                fragment = myFragment1;
                break;
            case ViewActivity.PAGE_TWO:
                fragment = myFragment2;
                break;
            case ViewActivity.PAGE_THREE:
                fragment = myFragment3;
                break;
            case ViewActivity.PAGE_FOUR:
                fragment = myFragment4;
                break;
        }
        return fragment;
    }

}
