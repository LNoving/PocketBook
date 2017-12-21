package cn.edu.zju.accountbook.accountbookdemo;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/12/19.
 */

public class ExpenditureRecord {
    private UUID mOutcomeId;
    private String mPurpose;
    private String mAmount;
    private String mDateTime;
    private String mLocation;
    private String mBarCodeInfo;
    private String mRemark;
    private String mPhoto;

    public ExpenditureRecord() {
        mOutcomeId = UUID.randomUUID();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        mDateTime = dateFormat.format(new Date());
    }

    public ExpenditureRecord(UUID id) {
        mOutcomeId = id;
    }

    public ExpenditureRecord(boolean type, String purpose, String amount, String dateTime, String location
            , String barCodeInfo, String remark, String photo) {
        mOutcomeId = UUID.randomUUID();
        mPurpose = purpose;
        mAmount = amount;
        mDateTime = dateTime;
        mLocation = location;
        mBarCodeInfo = barCodeInfo;
        mRemark = remark;
        mPhoto = photo;
    }

    public UUID getId() {
        return mOutcomeId;
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

    public String getBarCodeInfo() { return mBarCodeInfo; }

    public String getRemark() { return mRemark; }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPurpose(String purpose) { mPurpose = purpose; }

    public void setAmount(String amount) { mAmount = amount; }

    public void setDatetime(String datetime) {
        mDateTime = datetime;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public void setBarCodeInfo(String barCodeInfo) { mBarCodeInfo = barCodeInfo; }

    public void setRemark(String Remark) { mRemark = Remark; }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }
}
