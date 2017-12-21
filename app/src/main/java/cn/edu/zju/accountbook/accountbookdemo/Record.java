package cn.edu.zju.accountbook.accountbookdemo;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/12/19.
 */

public class Record {
    private UUID mId;
    private boolean mType;
    private String mPurpose;
    private String mAmount;
    private String mDateTime;
    private String mLocation;
    private String mAddress;
    private String mPhoto;

    private final static String OUTCOME_STRING = "支出";
    private final static String INCOME_STRING = "收入";

    public Record() {
        mId = UUID.randomUUID();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        mDateTime = dateFormat.format(new Date());
    }

    public Record(UUID id) {
        mId = id;
    }

    public Record(boolean type, String purpose, String amount, String dateTime, String location
            ,String address,String photo) {
        mId = UUID.randomUUID();
        mType = type;
        mPurpose = purpose;
        mAmount = amount;
        mDateTime = dateTime;
        mLocation = location;
        mAddress = address;
        mPhoto = photo;
    }

    public UUID getId() {
        return mId;
    }

    public boolean getType() {
        return mType;
    }

    public String getTypeString(){
        return mType ? INCOME_STRING : OUTCOME_STRING;
    }

    public String getPurpose() {
        return mPurpose;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setType(boolean type) {
        mType = type;
    }

    public void setPurpose(String purpose) { mPurpose = purpose; }

    public void setAmount(String amount) { mAmount = amount; }

    public void setDatetime(String datetime) {
        mDateTime = datetime;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }
}