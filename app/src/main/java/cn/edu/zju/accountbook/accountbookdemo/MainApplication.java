package cn.edu.zju.accountbook.accountbookdemo;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import cn.edu.zju.accountbook.accountbookdemo.charge.LocationService;

public class MainApplication extends Application {
	public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());  
       
    }
}
