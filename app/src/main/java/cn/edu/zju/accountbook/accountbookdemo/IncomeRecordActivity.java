package cn.edu.zju.accountbook.accountbookdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.edu.zju.accountbook.accountbookdemo.data.Record;
import cn.edu.zju.accountbook.accountbookdemo.data.RecordLab;

import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.BONUS;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.CROP_PHOTO;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.INCOME;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.OTHER;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.SALARY;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.TAKE_PHOTO;
import static cn.edu.zju.accountbook.accountbookdemo.cons.CommonConstants.getTypeString;

public class IncomeRecordActivity extends AppCompatActivity {

    private ImageView mSourceImageView;
    private Spinner mSourceSpinner;
    private EditText mAmountEditText;
    private TextView mDatetimeTextView;
    private ImageView mDatetimeImageView;
    private EditText mRemarkEditText;
    private ImageView mPhoto;
    private Button mCancelButton;
    private Button mSubmitButton;
    private Uri imageUri;


    private ArrayAdapter<String> adapter;
    private ArrayList<String> sourcesList;

    cn.edu.zju.accountbook.accountbookdemo.data.Record mRecord;
    int sourceId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_record);

        // get UI Objects
        mSourceImageView = (ImageView) findViewById(R.id.sourceImage);
        mSourceSpinner = (Spinner) findViewById(R.id.sourceSpinner);
        mAmountEditText = (EditText) findViewById(R.id.amountEditText);
        mDatetimeTextView = (TextView) findViewById(R.id.datetimeTextView);
        mDatetimeImageView = (ImageView) findViewById(R.id.datetimeIcon);
        mRemarkEditText = (EditText) findViewById(R.id.remarkEditText);
        mPhoto = (ImageView) findViewById(R.id.photoImageView);
        mCancelButton = (Button) findViewById(R.id.cancelButton);
        mSubmitButton = (Button) findViewById(R.id.submitButton);
        mRecord = new Record();

        // set source UI
        sourcesList = new ArrayList<String>();
        sourcesList.add(getTypeString(SALARY));
        sourcesList.add(getTypeString(BONUS));
        sourcesList.add(getTypeString(OTHER));


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sourcesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSourceSpinner.setAdapter(adapter);

        mSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mSourceImageView.setImageDrawable(getResources().getDrawable(R.mipmap.rmb));
                        sourceId = 0;
                        break;
                    case 1:
                        mSourceImageView.setImageDrawable(getResources().getDrawable(R.mipmap.profit));
                        sourceId = 1;
                        break;
                    case 2:
                        mSourceImageView.setImageDrawable(getResources().getDrawable(R.mipmap.add));
                        sourceId = 2;
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

        // set image view UI
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 拍照，并显示，注意控制大小
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
                try{
                    float f = Float.valueOf(mAmountEditText.getText().toString());
                    mRecord.setAmount(String.valueOf(f));
                    mRecord.setDatetime(mDatetimeTextView.getText().toString());
                    switch (sourceId) {
                        case 0:
                            mRecord.setType(SALARY);
                            break;
                        case 1:
                            mRecord.setType(BONUS);
                            break;
                        case 2:
                            mRecord.setType(OTHER);
                            break;
                    }
                    mRecord.setRemark(mRemarkEditText.getText().toString());
                    mRecord.setCategory(INCOME);
                    RecordLab.get(IncomeRecordActivity.this).addRecord(mRecord);
                    mRecord = new Record();
                    Toast.makeText(getApplicationContext(), "Charge Succeeded!", Toast.LENGTH_SHORT).show();
                    mPhoto.setImageResource(R.mipmap.image);
                    mAmountEditText.setText("");
                    mRemarkEditText.setText("");
                }catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "Invalid number!", Toast.LENGTH_SHORT).show();
                }
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
                        mRecord.setPhoto(imageUri.toString());
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                        mRecord.setPhoto(null);
                    }
                }
                break;
            default:break;
        }
    }

}
