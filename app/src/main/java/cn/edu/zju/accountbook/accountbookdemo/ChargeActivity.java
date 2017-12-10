package cn.edu.zju.accountbook.accountbookdemo;

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

import cn.edu.zju.accountbook.accountbookdemo.barcode.BarCodeAnalyse;


public class ChargeActivity extends Activity {

    private static final String EXTRA_RECORD_ID = "cn.edu.zju.accountbook.accountbookdemo.record_id";
    private static final String ARG_RECORD_ID = "record_id";
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
    private Record mRecord = new Record();
    private Uri imageUri;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

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

        /***
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
                    Toast.makeText(getApplicationContext(), "插入成功", Toast.LENGTH_SHORT).show();
                    editAmount.setText("");
                    /***
                     * 等待动画效果结束，退出
                     */
                }
            }
        });

        /***
         * 扫描条码按钮
         */
        scanBarCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivityForResult(new Intent(ChargeActivity.this, CaptureActivity.class),BARCODE_REQUEST_CODE);
            }
        });


        /***
         * 拍照按钮
         */
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建file对象，用于存储拍照后的图片
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
                startActivityForResult(intent,TAKE_PHOTO);//    启动相机程序
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
                info = "{\"showapi_res_code\":0,\"showapi_res_error\":\"\",\"showapi_res_body\":{\"spec\":\"\",\"manuName\":\"新乡市和丝露饮品有限公司\",\"ret_code\":0,\"price\":\"3.00\",\"flag\":true,\"trademark\":\"\",\"img\":\"http://app2.showapi.com/img/barCode_img/20160404/9a615820-985b-4e8f-acc7-a324c90bd393.jpg\",\"code\":\"6938166920785\",\"goodsName\":\"苹果醋\",\"zzjb\":\"\",\"note\":" +
                        "\"\"}}";
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
                            price,img));
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
     *
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

    /***
     * Stop location service
     */
    //@Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                StringBuffer locationInformation = new StringBuffer(256);

                /***
                 * 以下是输出的位置信息  张昊
                 */
                locationInformation.append("\naddr : ");// 地址信息
                locationInformation.append(location.getAddrStr());
                //data.setAddress(location.getAddrStr());
                mRecord.setAddress(location.getAddrStr());
                locationInformation.append("\nDescription :");// 描述
                locationInformation.append(location.getLocationDescribe());// 位置语义化信息
                //data.setLocationDescribe(location.getLocationDescribe());
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
                //logMsg(sb.toString());
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

                /***
                 * 以下是原方法
                 */


                 /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                 /*
                sb.append("time : ");
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于" +
                            "飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                //logMsg(sb.toString());
                */
            }
        }
    };
}
