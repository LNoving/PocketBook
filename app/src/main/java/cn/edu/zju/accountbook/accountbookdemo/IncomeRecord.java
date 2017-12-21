package cn.edu.zju.accountbook.accountbookdemo;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/12/19.
 */

public class IncomeRecord {
    private UUID mIncomeId;
    private String mSource;
    private String mAmount;
    private String mDateTime;
    private String mRemark;
    private String mPhoto;

    public IncomeRecord() {
        mIncomeId = UUID.randomUUID();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        mDateTime = dateFormat.format(new Date());
    }

    public IncomeRecord(UUID id) {
        mIncomeId = id;
    }

    public IncomeRecord(boolean type, String source, String amount, String dateTime, String remark, String photo) {
        mIncomeId = UUID.randomUUID();
        mSource = source;
        mAmount = amount;
        mDateTime = dateTime;
        mRemark = remark;
        mPhoto = photo;
    }

    public UUID getId() {
        return mIncomeId;
    }

    public String getSource() {
        return mSource;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public String getRemark() {
        return mRemark;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setSource(String source) { mSource = source; }

    public void setAmount(String amount) { mAmount = amount; }

    public void setDatetime(String datetime) {
        mDateTime = datetime;
    }

    public void setRemark(String remark) {
        mRemark = remark;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }
}
