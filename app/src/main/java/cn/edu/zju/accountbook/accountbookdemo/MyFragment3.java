package cn.edu.zju.accountbook.accountbookdemo;

/**
 * Created by 张昊 on 2017/12/21.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.chenupt.springindicator.SpringIndicator;

public class MyFragment3 extends Fragment {

    private ChartViewPagerAdapter mAdapter;
    private ViewPager viewPager;
    private SpringIndicator springIndicator;


    public MyFragment3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        mAdapter = new ChartViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager = (ViewPager)view.findViewById(R.id.charts);
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
        springIndicator =(SpringIndicator)view.findViewById(R.id.indicator);
        springIndicator.setViewPager(viewPager);

        return view;
    }
}