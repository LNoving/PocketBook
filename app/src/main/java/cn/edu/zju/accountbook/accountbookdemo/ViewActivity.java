package cn.edu.zju.accountbook.accountbookdemo;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.RadioGroup;
import android.widget.TextView;

import cn.edu.zju.accountbook.accountbookdemo.other.MyViewPagerAdapter;
import me.majiajie.pagerbottomtabstrip.MaterialMode;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class ViewActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener,
        Fragment1.OnFragmentInteractionListener,
        Fragment2.OnFragmentInteractionListener,
        Fragment3.OnFragmentInteractionListener,
        Fragment4.OnFragmentInteractionListener
{

    //UI Objects
    private TextView txt_topbar;
    /*
    private RadioGroup rg_tab_bar;

    private RadioButton rb_channel;
    private RadioButton rb_message;
    private RadioButton rb_better;
    private RadioButton rb_setting;
    */

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
//    int[] testColors = {0xFF009688, 0xFF009688, 0xFF009688, 0xFF009688, 0xFF009688};

    NavigationController mNavigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Data data = new Data(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        mAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),/*mNavigationController.getItemCount()*/4);
//        bindViews();                 //来自菜鸟
//        rb_channel.setChecked(true);  //来自菜鸟
        /***
         * 把整个表打印在log中
         */
        data.printTable();

        PageNavigationView pageBottomTabLayout = (PageNavigationView) findViewById(R.id.tab);

        mNavigationController = pageBottomTabLayout.material()
                .addItem(R.drawable.ic_ondemand_video_black_24dp,"Movies & TV",testColors[0])
                .addItem(R.drawable.ic_audiotrack_black_24dp, "Music",testColors[1])
                .addItem(R.drawable.ic_book_black_24dp, "Books",testColors[2])
                .addItem(R.drawable.ic_news_black_24dp, "Newsstand",testColors[3])
                .setDefaultColor(0x89FFFFFF)//未选中状态的颜色
                .setMode(MaterialMode.CHANGE_BACKGROUND_COLOR | MaterialMode.HIDE_TEXT)//这里可以设置样式模式，总共可以组合出4种效果
                .build();


        /***
         * viewpager的切换？？？
         */


        /***
         * 原来的方法
         */

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(),mNavigationController.getItemCount()));
        /***/
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);/***/

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


        item1 = newItem(R.drawable.ic_ondemand_video_black_24dp,R.drawable.ic_ondemand_video_black_24dp,"项目1");
        item2 = newItem(R.drawable.ic_audiotrack_black_24dp,R.drawable.ic_audiotrack_black_24dp,"项目2");
        item3 = newItem(R.drawable.ic_book_black_24dp,R.drawable.ic_book_black_24dp,"项目3");
        item4 = newItem(R.drawable.ic_news_black_24dp,R.drawable.ic_news_black_24dp,"项目4");


        //设置消息圆点
//        mNavigationController.setMessageNumber(0,12);
//        mNavigationController.setHasMessage(3,true);

    }







    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {Log.v("我在鼓楼的夜色中，为你唱花香自来","onPageScrolled");
    }

    @Override
    public void onPageSelected(int position) {           Log.v("我在鼓楼的夜色中，为你唱花香自来","onPageSelected");
    }

    @Override
    public void onPageScrollStateChanged(int state) {    Log.v("我在鼓楼的夜色中，为你唱花香自来","onPageScrollStateChanged");
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /***
     * 初始化一个item  可以更改选中后的图表  设计UI时最好考虑
     */

    private NormalItemView newItem(int drawable, int checkedDrawable, String text){
        Log.v("我在鼓楼的夜色中，为你唱花香自来","在别处，沉默，相遇，和期待");
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable,checkedDrawable,text);
        normalItemView.setTextDefaultColor(Color.GRAY);
        normalItemView.setTextCheckedColor(0xFF009688);
        return normalItemView;
    }

}
