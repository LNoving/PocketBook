package cn.edu.zju.accountbook.accountbookdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.google.zxing.client.android.CaptureActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.edu.zju.accountbook.accountbookdemo.charge.BarCodeAnalyse;
import cn.edu.zju.accountbook.accountbookdemo.charge.ChargeActivity;
import cn.edu.zju.accountbook.accountbookdemo.charge.LocationService;
import cn.edu.zju.accountbook.accountbookdemo.charge.Query;
import cn.edu.zju.accountbook.accountbookdemo.data.*;
import cn.edu.zju.accountbook.accountbookdemo.data.Record;
import cn.edu.zju.accountbook.accountbookdemo.exception.NullException;

import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.BARCODE_REQUEST_CODE;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.CLOTHES;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.CROP_PHOTO;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.EXPENDITURE;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.FOOD;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.GIFT;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.HEALTH;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.OTHER;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.SHOPPING;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.TAKE_PHOTO;

public class ExpenditureRecordActivity extends AppCompatActivity {

    private ImageView mPurposeImageView;
    private Spinner mPurposeSpinner;
    private EditText mAmountEditText;
    private TextView mDatetimeTextView;
    private ImageView mDatetimeImageView;
    private TextView mLocationTextView;
    private ImageView mLocationImageView;
    private EditText mRemarkEditText;
    private ImageView mBarcodeImageView;
    private TextView mBarcodeTextView;
    private ImageView mPhoto;
    private String mPhotoAddress;
    private Button mCancelButton;
    private Button mSubmitButton;
    private Uri imageUri;
    private LocationService locationService;



    private ArrayAdapter<String> adapter;
    private ArrayList<String> purposesList;
    int purposeId;

    cn.edu.zju.accountbook.accountbookdemo.data.Record mExpenditureRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_record);

        mPurposeImageView = (ImageView) findViewById(R.id.purposeImage);
        mPurposeSpinner = (Spinner) findViewById(R.id.purposeSpinner);
        mAmountEditText = (EditText) findViewById(R.id.amountEditText);
        mDatetimeTextView = findViewById(R.id.datetimeTextView);
        mDatetimeImageView = findViewById(R.id.datetimeIcon);
        mLocationTextView = findViewById(R.id.locationTextView);
        mLocationImageView = findViewById(R.id.locationIcon);
        mRemarkEditText = findViewById(R.id.remarkEditText);
        mBarcodeImageView = findViewById(R.id.barcodeIcon);
        mBarcodeTextView = findViewById(R.id.barcodeTextView);
        mPhoto = findViewById(R.id.photoImageView);
        mCancelButton = findViewById(R.id.cancelButton);
        mSubmitButton = findViewById(R.id.submitButton);

        locationService = ((MainApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.start();

        mExpenditureRecord = new Record();

        purposesList = new ArrayList<String>();
        purposesList.add("Shopping");
        purposesList.add("Clothes");
        purposesList.add("Food");
        purposesList.add("Gift");
        purposesList.add("Health");
        purposesList.add("Other");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, purposesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPurposeSpinner.setAdapter(adapter);

        mPurposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        purposeId = 0;
                        mPurposeImageView.setImageDrawable(getResources().getDrawable(R.mipmap.shopping));
                        break;
                    case 1:
                        purposeId = 1;
                        mPurposeImageView.setImageDrawable(getResources().getDrawable(R.mipmap.clothing));
                        break;
                    case 2:
                        purposeId = 2;
                        mPurposeImageView.setImageDrawable(getResources().getDrawable(R.mipmap.eating));
                        break;
                    case 3:
                        purposeId = 3;
                        mPurposeImageView.setImageDrawable(getResources().getDrawable(R.mipmap.gift));
                        break;
                    case 4:
                        purposeId = 4;
                        mPurposeImageView.setImageDrawable(getResources().getDrawable(R.mipmap.health));
                        break;
                    case 5:
                        purposeId = 5;
                        mPurposeImageView.setImageDrawable(getResources().getDrawable(R.mipmap.add));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ;
            }
        });

        // set Datetime UI
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date currentDate = new Date(System.currentTimeMillis());
        String currentDateString = format.format(currentDate);
        mDatetimeTextView.setText(currentDateString);

        // set location UI
        // get location

        // set bar code UI
        mBarcodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ExpenditureRecordActivity.this,
                        CaptureActivity.class),BARCODE_REQUEST_CODE);

            }
        });

        // set image view UI
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(Environment.
                        getExternalStorageDirectory(),mExpenditureRecord.getId()+".jpg");
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
                    startActivityForResult(intent,TAKE_PHOTO);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        // set buttons UI
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpenditureRecord.setAmount(mAmountEditText.getText().toString());
                mExpenditureRecord.setDatetime(mDatetimeTextView.getText().toString());
                switch (purposeId) {
                    case 0:
                        mExpenditureRecord.setType(SHOPPING);
                        break;
                    case 1:
                        mExpenditureRecord.setType(CLOTHES);
                        break;
                    case 2:
                        mExpenditureRecord.setType(FOOD);
                        break;
                    case 3:
                        mExpenditureRecord.setType(GIFT);
                        break;
                    case 4:
                        mExpenditureRecord.setType(HEALTH);
                        break;
                    case 5:
                        mExpenditureRecord.setType(OTHER);
                        break;
                }
                mExpenditureRecord.setLocation(mLocationTextView.getText().toString());
                mExpenditureRecord.setRemark(mRemarkEditText.getText().toString());
                mExpenditureRecord.setPhoto(mPhotoAddress);
                mExpenditureRecord.setCategory(EXPENDITURE);
                RecordLab.get(ExpenditureRecordActivity.this).addRecord(mExpenditureRecord);
                mExpenditureRecord = new cn.edu.zju.accountbook.accountbookdemo.data.Record();
                Toast.makeText(getApplicationContext(), "Charge Succeeded!", Toast.LENGTH_SHORT).show();
                mAmountEditText.setText("");
                mRemarkEditText.setText("");

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                        mPhoto.setMaxWidth(400);
                        mPhoto.setMaxHeight(500);
                        mPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        mPhoto.setAdjustViewBounds(true);
                        mPhoto.setImageBitmap(bitmap);
                        mExpenditureRecord.setPhoto(imageUri.toString());
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                        mExpenditureRecord.setPhoto(null);
                    }
                }
                break;
            case BARCODE_REQUEST_CODE:
                String result = null;//do not delete
                try{
                    String info;
                    result = data.getExtras().getString("result");
                    ExecutorService exec = Executors.newCachedThreadPool();
                    info = exec.submit(new Query(result)).get();
                    //info = "{\"showapi_res_code\":0,\"showapi_res_error\":\"\",\"showapi_res_body\":{\"spec\":\"\",\"manuName\":\"新乡市和丝露饮品有限公司\",\"ret_code\":0,\"price\":\"3.00\",\"flag\":true,\"trademark\":\"\",\"img\":\"http://app2.showapi.com/img/barCode_img/20160404/9a615820-985b-4e8f-acc7-a324c90bd393.jpg\",\"code\":\"6938166920785\",\"goodsName\":\"苹果醋\",\"zzjb\":\"\",\"note\":\"\"}}";
                    if(info.equals("")) {
                        throw new NullException();
                    }
                    else{
                        try{
                            BarCodeAnalyse BarCodeResult = new BarCodeAnalyse(info);
                            String name = BarCodeResult.getName();
                            String price = BarCodeResult.getPrice();
                            mBarcodeTextView.setText(String.format(this.getResources().
                                    getString(R.string.commodity_info),name,price));
                            mAmountEditText.setText(price);
                            Toast.makeText(getApplicationContext(), "Query succeeded", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            throw new Exception(e);
                        }
                    }
                    exec.shutdown();

                }catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Failed to scan", Toast.LENGTH_SHORT).show();
                    mBarcodeTextView.setText(R.string.failed_to_scan);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Failed to query", Toast.LENGTH_SHORT).show();
                    mBarcodeTextView.setText(String.format(this.getResources().
                            getString(R.string.commodity_information_invalid),result));
                }
            default:break;
        }
    }

    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer locationInformation = new StringBuffer(256);

                locationInformation.append(location.getAddrStr()+"\n");
                mExpenditureRecord.setAddress(location.getAddrStr());
                locationInformation.append(location.getLocationDescribe());
                mExpenditureRecord.setLocation(location.getLocationDescribe());
                if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    locationInformation.append("\ndescribe : ");
                    locationInformation.append("离线定位，无法获取地址信息");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    locationInformation.append("\ndescribe : ");
                    locationInformation.append("服务端网络定位失败");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    locationInformation.append("\ndescribe : ");
                    locationInformation.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    locationInformation.append("\ndescribe : ");
                    locationInformation.append("无法获取有效定位依据导致定位失败，可能的原因" +
                            "有：未开启定位权限、未开启GPS、飞行模式打开等");
                }
                if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                    Toast.makeText(getApplicationContext(), "定位成功", Toast.LENGTH_SHORT).show();
                    mLocationTextView.setText(mExpenditureRecord.getLocation());
                    locationService.unregisterListener(mListener);
                    locationService.stop();
                }
                else {
                    mExpenditureRecord.setLocation(null);
                    mLocationTextView.setText(locationInformation);
                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
