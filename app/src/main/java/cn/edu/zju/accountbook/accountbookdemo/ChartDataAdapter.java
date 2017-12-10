package cn.edu.zju.accountbook.accountbookdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.zxing.client.android.Contents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 张昊 on 2017/12/10.
 */

public class ChartDataAdapter  {

    private Typeface tf;
    private List<Record> records;
    private Context context;

    public ChartDataAdapter(Context context){
        this.context = context;
        records = RecordLab.get(context).getInvertedRecords();
    }


    protected PieData setPieData() {

        int count = 4;

        ArrayList<PieEntry> entries1 = new ArrayList<PieEntry>();

        for(int i = 0; i < count; i++) {
            entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "Quarter " + (i+1)));
        }

        PieDataSet ds1 = new PieDataSet(entries1, "Quarterly Revenues 2015");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(ds1);
        d.setValueTypeface(tf);

        return d;
    }

    public BarData setBarData(int dataSets, float range, int count) {

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();

        for(int i = 0; i < dataSets; i++) {

            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

//            entries = FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "stacked_bars.txt");

            /***
             * 获取最近count天的花费总额
             */
            for(int j = 0; j < count; j++) {
                entries.add(new BarEntry(j, (float)Float.parseFloat(records.get(j).getAmount())));
            }

            BarDataSet ds = new BarDataSet(entries, getLabel(i));
            ds.setColors(ColorTemplate.VORDIPLOM_COLORS);
            sets.add(ds);
        }

        BarData d = new BarData(sets);
        d.setValueTypeface(tf);
        return d;
    }

    /***
     * 获得折现图的数据。
     * @param count
     * @param context
     * @return
     */
    public ArrayList<Entry>  setLineData(int count,Context context){
        ArrayList<Entry> values = new ArrayList<Entry>();

        //Iterator<Record> iterator = records.iterator();
        String dateTime = records.get(0).getDateTime().substring(0,10);
        float sum = Float.parseFloat(records.get(0).getAmount());
        int cnt = 0;
        for (int i = 1; cnt < count&&i<records.size(); i++) {
            try{
                Log.v("日期:",records.get(i).getDateTime().substring(0,10));
                if(dateTime.equals(records.get(i).getDateTime().substring(0,10))){
                    sum+=Float.parseFloat(records.get(i).getAmount());
                    continue;
                }
                else {
                    values.add(new Entry(cnt++, sum, context.getResources().getDrawable(R.drawable.star)));
                    dateTime = records.get(i).getDateTime().substring(0,10);
                    Log.v("数字",String.valueOf(sum));
                    sum = Float.parseFloat(records.get(0).getAmount());
                }
            }catch (Exception e){
                continue;
            }

        }
        return values;
    }




    private String[] mLabels = new String[] { "Company A", "Company B", "Company C", "Company D", "Company E", "Company F" };
//    private String[] mXVals = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };

    private String getLabel(int i) {
        return mLabels[i];
    }
}
