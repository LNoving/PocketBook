package cn.edu.zju.accountbook.mypocketbook.view.charts;

/**
 * Created by 张昊 on 2017/12/21.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.zju.accountbook.mypocketbook.R;
import github.chenupt.springindicator.SpringIndicator;

public class ChartsFragment extends Fragment {

    private ChartViewPagerAdapter mAdapter;
    private ViewPager viewPager;
    private SpringIndicator springIndicator;


    public ChartsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts, container, false);
        mAdapter = new ChartViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager = view.findViewById(R.id.charts);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        springIndicator = view.findViewById(R.id.indicator);
        springIndicator.setViewPager(viewPager);
        return view;
    }

}