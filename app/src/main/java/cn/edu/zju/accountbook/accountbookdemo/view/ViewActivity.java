package cn.edu.zju.accountbook.accountbookdemo.view;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.edu.zju.accountbook.accountbookdemo.R;
import cn.edu.zju.accountbook.accountbookdemo.data.RecordLab;
import cn.edu.zju.accountbook.accountbookdemo.view.myViewPager.MyViewPagerAdapter;
import me.majiajie.pagerbottomtabstrip.MaterialMode;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class ViewActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener
{

    NormalItemView item1;
    NormalItemView item2;
    NormalItemView item3;
    NormalItemView item4;

    private MyViewPagerAdapter mAdapter;

    ViewPager viewPager;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;


    int[] testColors = {0xFF455A64, 0xFF00796B, 0xFF795548, 0xFF5B4947, 0xFFF57C00};
 //   int[] testColors = {0xFF009688, 0xFF009688, 0xFF009688, 0xFF009688, 0xFF009688};

    NavigationController mNavigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        PageNavigationView pageBottomTabLayout = (PageNavigationView) findViewById(R.id.tab);

        mNavigationController = pageBottomTabLayout.material()
                .addItem(R.drawable.ic_format_list_bulleted_black_24dp,"Detail",testColors[0])
                .addItem(R.drawable.ic_pie_chart_black_24dp, "Pie Chart",testColors[1])
                .addItem(R.drawable.ic_equalizer_black_24dp, "Bar Chart",testColors[2])
                .addItem(R.drawable.ic_show_chart_black_24dp, "Line Chart",testColors[3])
                .setDefaultColor(0x89FFFFFF)//未选中状态的颜色
                .setMode(MaterialMode.CHANGE_BACKGROUND_COLOR | MaterialMode.HIDE_TEXT)//这里可以设置样式模式，总共可以组合出4种效果
                .build();

        mAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),mNavigationController.getItemCount());
        /***
         * viewpager的切换？？？
         */


        /***
         * 原来的方法
         */

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        /***/
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);/***/

        /***
         * 设置ViewPager预加载页面的个数为4
         */
        viewPager.setOffscreenPageLimit(3);

        //自动适配ViewPager页面切换
        mNavigationController.setupWithViewPager(viewPager);

        //也可以设置Item选中事件的监听
        mNavigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                Log.i("asd","selected: " + index + " old: " + old);
            }

            @Override
            public void onRepeat(int index) {
                Log.i("asd","onRepeat selected: " + index);
            }
        });
        //设置消息圆点
        /***
//        mNavigationController.setMessageNumber(0,12);
//        mNavigationController.setHasMessage(3,true);
         */
    }







    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (viewPager.getCurrentItem()) {
                case PAGE_ONE:
                    item1.setChecked(true);
                    break;
                case PAGE_TWO:
                    item2.setChecked(true);
                    break;
                case PAGE_THREE:
                    item3.setChecked(true);
                    break;
                case PAGE_FOUR:
                    item4.setChecked(true);
                    break;
            }
        }
    }


    /***
     * 初始化一个item  可以更改选中后的图表  设计UI时最好考虑
     */

    private NormalItemView newItem(int drawable, int checkedDrawable, String text){
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable,checkedDrawable,text);
        normalItemView.setTextDefaultColor(Color.GRAY);
        normalItemView.setTextCheckedColor(0xFF009688);
        return normalItemView;
    }

}
