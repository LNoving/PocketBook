package cn.edu.zju.accountbook.accountbookdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class ShowActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener{
    // UI Objects
    private NavigationTabBar mNavigationTabBar;

    private FloatingActionsMenu mFloatingActionsMenu;
    FloatingActionButton mIncomeFloatingAction;
    FloatingActionButton mOutcomeFloatingAction;
    ArrayList<NavigationTabBar.Model> models;

    private ViewPager vpagerOne;
    private MainViewPagerAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;



    private TextView mThisMonthTextView;
    private TextView mOutcomeAmountTextView;
    private TextView mIncomeAmountTextView;
    private TextView mSurplusAmountTextView;
    private ListView mRecordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // get UI object
        mNavigationTabBar = (NavigationTabBar) findViewById(R.id.navigationTabBar);
        mFloatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.floatingActionsMenu);
        mIncomeFloatingAction = (FloatingActionButton) findViewById(R.id.incomeFloatingButton);
        mOutcomeFloatingAction = (FloatingActionButton) findViewById(R.id.outcomeFloatingButton);

        mThisMonthTextView = (TextView) findViewById(R.id.thisMonthTextView);
        mOutcomeAmountTextView = (TextView) findViewById(R.id.outcomeAmountTextView);
        mIncomeAmountTextView = (TextView) findViewById(R.id.incomeAmountTextView);
        mSurplusAmountTextView = (TextView) findViewById(R.id.surplusAmountTextView);


        // set nav tab bar
        models = new ArrayList<NavigationTabBar.Model>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.notebook),
                        Color.parseColor("#edc008")
                ).title("Book").badgeTitle("aaaa").build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.list),
                        Color.parseColor("#edc008")
                ).title("Details").badgeTitle("bbbb").build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.chart),
                        Color.parseColor("#edc008")
                ).title("StatisticsView").badgeTitle("cccc").build()
        );

        mNavigationTabBar.setModels(models);

        mNavigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        mNavigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        mNavigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        mNavigationTabBar.setIsBadged(true);
        mNavigationTabBar.setIsTitled(true);
        mNavigationTabBar.setIsTinted(true);
        mNavigationTabBar.setIsBadgeUseTypeface(true);
        mNavigationTabBar.setBadgeBgColor(Color.RED);
        mNavigationTabBar.setBadgeTitleColor(Color.WHITE);
        mNavigationTabBar.setIsSwiped(true);
        mNavigationTabBar.setBgColor(Color.BLACK);
        mNavigationTabBar.setBadgeSize(10);
        mNavigationTabBar.setTitleSize(10);
        mNavigationTabBar.setIconSizeFraction((float) 0.5);
        mNavigationTabBar.setBehaviorEnabled(true);

        // set floating button
        mIncomeFloatingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowActivity.this, IncomeRecordActivity.class);
                startActivity(intent);
            }
        });

        mOutcomeFloatingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowActivity.this, ExpenditureRecordActivity.class);
                startActivity(intent);
            }
        });

        mNavigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                switch (index){
                    case 0:vpagerOne.setCurrentItem(PAGE_ONE);break;
                    case 1:vpagerOne.setCurrentItem(PAGE_TWO);break;
                    case 2:vpagerOne.setCurrentItem(PAGE_THREE);break;
                    default:
                }
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {
            }
        });

        vpagerOne = (ViewPager) findViewById(R.id.vpager_one);
        mAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        vpagerOne.setCurrentItem(0);
        vpagerOne.setAdapter(mAdapter);
        vpagerOne.setOffscreenPageLimit(2);
        vpagerOne.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
