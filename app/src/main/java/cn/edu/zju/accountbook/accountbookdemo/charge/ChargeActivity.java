package cn.edu.zju.accountbook.accountbookdemo.charge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.google.zxing.client.android.CaptureActivity;
import com.spark.submitbutton.SubmitButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.edu.zju.accountbook.accountbookdemo.MainApplication;
import cn.edu.zju.accountbook.accountbookdemo.R;
import cn.edu.zju.accountbook.accountbookdemo.data.Record;
import cn.edu.zju.accountbook.accountbookdemo.data.RecordLab;


public class ChargeActivity extends Activity {

    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private static final int BARCODE_REQUEST_CODE = 3;

    private LocationService locationService;
    private TextView locationResult;
    private TextView commodityInformation;
    private SubmitButton insert;
    private EditText editAmount;
    private Button scanBarCode;
    private Button takePhoto;
    private Record mRecord;
    private Uri imageUri;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        mRecord = new Record();

        locationResult = findViewById(R.id.location);
        locationResult.setMovementMethod(ScrollingMovementMethod.getInstance());

        commodityInformation = findViewById(R.id.commodity);

        insert = findViewById(R.id.insert);

        editAmount = findViewById(R.id.edit_amount);

        scanBarCode = findViewById(R.id.scan_bar_code);

        takePhoto = findViewById(R.id.take_photo);

        picture = findViewById(R.id.show_photo);

        locationService = ((MainApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.start();

        /**
         * 记账按钮
         */
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //double amount = Double.parseDouble(editAmount.getText().toString());
                if(editAmount.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "请输入金额！", Toast.LENGTH_SHORT).show();
                }
                else {
                    mRecord.setAmount(editAmount.getText().toString());
                    RecordLab.get(ChargeActivity.this).addRecord(mRecord);
                    mRecord = new Record();
                    Toast.makeText(getApplicationContext(), "插入成功", Toast.LENGTH_SHORT).show();
                    editAmount.setText("");
                    /***
                     * 等待动画效果结束，退出
                     */
                }
            }
        });

        /**
         * 扫描条码按钮
         */
        scanBarCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivityForResult(new Intent(ChargeActivity.this, CaptureActivity.class),BARCODE_REQUEST_CODE);
            }
        });


        /**
         * 拍照按钮
         */
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(Environment.
                        getExternalStorageDirectory(),mRecord.getId()+".jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                try{
                    startActivityForResult(intent,TAKE_PHOTO);//    启动相机程序
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /***
         * 处理拍照结果
         */
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,CROP_PHOTO);//启动裁剪程序
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream
                                (getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);//将裁剪后的照片显示出来
                        mRecord.setPhoto(imageUri.toString());
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                        mRecord.setPhoto(null);
                    }
                }
                break;
            default:break;
        }

        /***
         * 处理条码扫描结果
         */
        if (requestCode == BARCODE_REQUEST_CODE) {
            Toast.makeText(getApplicationContext(), "扫描成功！", Toast.LENGTH_SHORT).show();
            String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
            Log.i("扫描到的结果", result);
            try{
                String info = null;
                ExecutorService exec = Executors.newCachedThreadPool();
                /***
                 * 关键
                 */
                //info = exec.submit(new Query(result)).get();
                info = "{\"showapi_res_code\":0,\"showapi_res_error\":\"\",\"showapi_res_body\":{\"spec\":\"\",\"manuName\":\"新乡市和丝露饮品有限公司\",\"ret_code\":0,\"price\":\"3.00\",\"flag\":true,\"trademark\":\"\",\"img\":\"http://app2.showapi.com/img/barCode_img/20160404/9a615820-985b-4e8f-acc7-a324c90bd393.jpg\",\"code\":\"6938166920785\",\"goodsName\":\"苹果醋\",\"zzjb\":\"\",\"note\":\"\"}}";
                Log.i("查询到的结果", info);

                if(info.equals("")) {
                    throw new Exception();
                }
                else{
                    Toast.makeText(getApplicationContext(), "查询成功！", Toast.LENGTH_SHORT).show();//待修改
                    Log.v("j===",info);
                    BarCodeAnalyse BarCodeResult = new BarCodeAnalyse(info);
                    String name = BarCodeResult.getName();
                    String price = BarCodeResult.getPrice();
                    String img = BarCodeResult.getImg();
                    Log.v("jieguo===",name+price+img);
                    Log.v("j===",info);
                    commodityInformation.setText(String.format(this.getResources().
                            getString(R.string.commodity_info),name,
                            price));
                    editAmount.setText(price);
                }
                exec.shutdown();

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "查询失败！", Toast.LENGTH_SHORT).show();//待修改
                //规范化的setText如下 运用了String.format,其中String资源中的&1$s表示第1个参数，字符串
                commodityInformation.setText(String.format(this.getResources().
                        getString(R.string.commodity_information_invalid),result));
            }
        }
    }
    /**
     * 显示请求字符串
     * @param str
     */
    public void logMsg(String str) {
        final String s = str;
        try {
            if (locationResult != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        locationResult.post(new Runnable() {
                            @Override
                            public void run() {
                                locationResult.setText(s);
                            }
                        });

                    }
                }).start();
            }
            //LocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                StringBuffer locationInformation = new StringBuffer(256);

                locationInformation.append("\naddr : ");// 地址信息
                locationInformation.append(location.getAddrStr());
                mRecord.setAddress(location.getAddrStr());
                locationInformation.append("\nDescription :");// 描述
                locationInformation.append(location.getLocationDescribe());// 位置语义化信息
                mRecord.setLocation(location.getLocationDescribe());
                if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    locationInformation.append("\ndescribe : ");
                    locationInformation.append("离线定位，无法获取地址信息");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    locationInformation.append("\ndescribe : ");
                    locationInformation.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    locationInformation.append("\ndescribe : ");
                    locationInformation.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    locationInformation.append("\ndescribe : ");
                    locationInformation.append("无法获取有效定位依据导致定位失败，可能的原因" +
                            "有：未开启定位权限、未开启GPS、飞行模式打开等");
                }
                logMsg(locationInformation.toString());
                if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                    Toast.makeText(getApplicationContext(), "定位成功", Toast.LENGTH_SHORT).show();
                    locationService.unregisterListener(mListener); //注销掉监听
                    locationService.stop();
                }
                else {
                    mRecord.setLocation(null);
                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
