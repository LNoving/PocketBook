package cn.edu.zju.accountbook.accountbookdemo.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.accountbook.accountbookdemo.R;
import cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants;
import cn.edu.zju.accountbook.accountbookdemo.data.Record;
import cn.edu.zju.accountbook.accountbookdemo.data.RecordLab;

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

    /***
     * 获取扇形图的数据
     * @return d
     */
    protected PieData setPieData() {

        int count = 6;
        int size = records.size();
        float [] sum = new float[count];

        ArrayList<PieEntry> entries1 = new ArrayList<PieEntry>();

        for(int i = 0; i < size; i++) {
            sum[records.get(i).getType()]+= Float.parseFloat(records.get(i).getAmount());
        }

        for(int i =0;i<count;i++){
            entries1.add(new PieEntry(sum[i], CommonConstants.getTypeString(i)));
        }

        PieDataSet ds1 = new PieDataSet(entries1, "支出类别统计");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(ds1);
        d.setValueTypeface(tf);

        return d;
    }

    /***
     * 条形图获取数据
     * @param dataSets
     * @param count
     * @return
     */
    public BarData setBarData(int dataSets, int count) {

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();

        for(int i = 0; i < dataSets; i++) {

            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
//            entries = FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "stacked_bars.txt");
            /***
             * 获取最近几笔花费
             */
            int size = count < records.size()?count:records.size();
            for(int j = 0; j < size; j++) {
                entries.add(new BarEntry(j,Float.parseFloat(records.get(size -1 -j).getAmount())));
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
     * 获得折现图的数据。得到倒序记录，按日期统计，再倒序输出
     * @param count
     * @param context
     * @return
     */
    public ArrayList<Entry>  setLineData(int count,Context context){
        ArrayList<Entry> values = new ArrayList<Entry>();

        //Iterator<Record> iterator = records.iterator();
        String dateTime = records.get(0).getDateTime().substring(0,10);
        float sum;
        int size ;
        /***
         * if中按日期加载数据，else中按条目加载数据
         */
        if(false)
            for (int i = 0; size < count&&i<records.size(); i++) {
                try{
                    Log.v("日期:",records.get(i).getDateTime().substring(0,10));
                    if(dateTime.equals(records.get(i).getDateTime().substring(0,10))){
                        sum+=Float.parseFloat(records.get(i).getAmount());
                        continue;
                    }
                    else {
                        values.add(new Entry(size++, sum, context.getResources().getDrawable(R.drawable.star)));
                        dateTime = records.get(i).getDateTime().substring(0,10);
                        Log.v("数字",String.valueOf(sum));
                        sum = Float.parseFloat(records.get(0).getAmount());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        else{
            size = count<records.size()?count:records.size();
            for (int i = 0; i < size; i++) {
                try{
                    sum = Float.parseFloat(records.get(size -1 -i).getAmount());
                    values.add(new Entry(i, sum, context.getResources().getDrawable(R.drawable.star)));
                    Log.v("值",String.valueOf(size)+"     "+String.valueOf(i));
                }catch (Exception e){
                    e.printStackTrace();
                }
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
