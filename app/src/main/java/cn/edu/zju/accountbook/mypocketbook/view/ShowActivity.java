package cn.edu.zju.accountbook.mypocketbook.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import cn.edu.zju.accountbook.mypocketbook.R;
import cn.edu.zju.accountbook.mypocketbook.charge.ExpenditureChargeActivity;
import cn.edu.zju.accountbook.mypocketbook.charge.IncomeChargeActivity;
import cn.edu.zju.accountbook.mypocketbook.view.lists.MainViewPagerAdapter;
import devlight.io.library.ntb.NavigationTabBar;

import static cn.edu.zju.accountbook.mypocketbook.cons.CommonConstants.REQUEST_CODE_ACCESS;

public class ShowActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener{
    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    FloatingActionButton mIncomeFloatingAction;
    FloatingActionButton mOutcomeFloatingAction;
    ArrayList<NavigationTabBar.Model> models;
    // UI Objects
    private NavigationTabBar mNavigationTabBar;
    private FloatingActionsMenu mFloatingActionsMenu;
    private ViewPager vpagerOne;
    private MainViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // get UI object
        mNavigationTabBar = findViewById(R.id.navigationTabBar);
        mFloatingActionsMenu = findViewById(R.id.floatingActionsMenu);
        mIncomeFloatingAction = findViewById(R.id.incomeFloatingButton);
        mOutcomeFloatingAction = findViewById(R.id.outcomeFloatingButton);


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
                Intent intent = new Intent(ShowActivity.this, IncomeChargeActivity.class);
                startActivity(intent);
            }
        });

        mOutcomeFloatingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowActivity.this, ExpenditureChargeActivity.class);
                startActivity(intent);
            }
        });


        vpagerOne = findViewById(R.id.vpager_one);
        mAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        vpagerOne.setCurrentItem(0);
        vpagerOne.setAdapter(mAdapter);
        vpagerOne.setOffscreenPageLimit(2);
        vpagerOne.addOnPageChangeListener(this);

        mNavigationTabBar.setViewPager(vpagerOne,PAGE_ONE);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        requestPermissions();
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

    /**
     * 请求定位、相机、存储权限
     */
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },REQUEST_CODE_ACCESS);
            }
        }
    }

}
