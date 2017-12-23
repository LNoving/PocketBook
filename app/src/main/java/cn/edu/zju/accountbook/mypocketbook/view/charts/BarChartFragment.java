package cn.edu.zju.accountbook.mypocketbook.view.charts;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import cn.edu.zju.accountbook.mypocketbook.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BarChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BarChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarChartFragment extends Fragment implements OnChartGestureListener {


    protected boolean isCreate = false;
    private BarChart mChart;

    private OnFragmentInteractionListener mListener;

    public BarChartFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new BarChartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreate=true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bar_chart, container, false);

        // create a new chart object
        mChart = v.findViewById(R.id.chart1);

        /***
         * 原先图形加载的位置，现在换成createView()
         */
        createView();
        return v;
    }

    public void createV(){
        createView();
    }

    private void createView(){
        mChart.getDescription().setEnabled(false);
        mChart.setOnChartGestureListener(this);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);

        mChart.getAxisLeft().setDrawGridLines(false);


        // add a nice and smooth animation
        mChart.animateY(2500);

        mChart.getLegend().setEnabled(false);


        //Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Light.ttf");
        ChartDataAdapter chartDataAdapter = new ChartDataAdapter(getActivity());

        try {
            mChart.setData(chartDataAdapter.setBarData(1, 10));
        }catch (Exception e){
            e.printStackTrace();
        }

        Legend l = mChart.getLegend();
        //l.setTypeface(tf);

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(tf);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
/*
        // programatically add the chart
        FrameLayout parent = (FrameLayout) v.findViewById(R.id.);
        parent.addView(mChart);
*/
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START");
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END");
        mChart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreate) {
            createView();
        } else {

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
