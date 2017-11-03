package cn.edu.zju.accountbook.accountbookdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity{

    private final static int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button chargeActivity = findViewById(R.id.charge_activity);  //记账
        chargeActivity.setOnClickListener(new View.OnClickListener() {              //Button
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChargeActivity.class);
                startActivity(intent);
            }
        });
        Button viewActivity = findViewById(R.id.view_activity);//查看账本
        viewActivity.setOnClickListener(new View.OnClickListener() {              //Button
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ViewActivity.class);
                startActivity(intent);
            }
        });
        requestContactPermission();
    }
    /***
     * 获取定位权限
     */


    private void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                },REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }
        }
    }


}
