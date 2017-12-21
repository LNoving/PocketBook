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

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import cn.edu.zju.accountbook.accountbookdemo.charge.ChargeActivity;
import cn.edu.zju.accountbook.accountbookdemo.view.ViewActivity;


public class MainActivity extends Activity{

    private final static int REQUEST_CODE_ACCESS = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HTextView hTextView = (HTextView) findViewById(R.id.htext);
        hTextView.setAnimateType(HTextViewType.LINE);
        hTextView.animateText("MyAccountBook"); // animate
        hTextView.reset("Welcome");

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
                Intent intent = new Intent(MainActivity.this,ShowActivity.class);
                startActivity(intent);
            }
        });

        requestPermissions();
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
