package cn.edu.zju.accountbook.accountbookdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.client.android.result.SMSResultHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.zju.accountbook.accountbookdemo.data.RecordLab;

/**
 * Created by 张昊 on 2017/12/21.
 */
public class MyFragment1 extends Fragment {

    private TextView mThisMonthTextView;
    private TextView mExpenditureAmountTextView;
    private TextView mIncomeAmountTextView;
    private TextView mSurplusAmountTextView;
    private ListView mRecordsList;

    public MyFragment1() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_one, container, false);
        mThisMonthTextView = (TextView) view.findViewById(R.id.thisMonthTextView);
        mExpenditureAmountTextView = (TextView) view.findViewById(R.id.outcomeAmountTextView);
        mIncomeAmountTextView = (TextView) view.findViewById(R.id.incomeAmountTextView);
        mSurplusAmountTextView = (TextView) view.findViewById(R.id.surplusAmountTextView);

        Date date = new Date();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String thisMonth = monthFormat.format(date);
        mThisMonthTextView.setText(thisMonth);

        RecordLab recordLab = RecordLab.get(getActivity());
        mExpenditureAmountTextView.setText(String.valueOf(recordLab.getMonthExpenditure(thisMonth)));
        mIncomeAmountTextView.setText(String.valueOf(recordLab.getMonthIncome(thisMonth)));
        mSurplusAmountTextView.setText(String.valueOf(recordLab.getMonthSurplus(thisMonth)));

        return view;
    }

}