package com.mirliam.financialmanagement;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.UUID;

public class FinancialDetails {
    private UUID mUUID;
    private boolean mInOrOut;
    private String mTitle;
    private Date mDate;
    private float mMoney;
    private String mRemark;
    private Bitmap mPicture;

    public FinancialDetails(){
        mUUID = UUID.randomUUID();
        mInOrOut = true;
        mTitle = "医疗";
        mMoney = 0;
        mDate = new Date();
        mRemark = "";
    }

    public UUID getId() {
        return mUUID;
    }

    public boolean isInOrOut() {
        return mInOrOut;
    }

    public void setInOrOut(boolean inOrOut) {
        mInOrOut = inOrOut;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public float getMoney() {
        return mMoney;
    }

    public void setMoney(float money) {
        mMoney = money;
    }

    public String getRemark() {
        return mRemark;
    }

    public void setRemark(String remark) {
        mRemark = remark;
    }

    public Bitmap getPicture() {
        return mPicture;
    }

    public void setPicture(Bitmap picture) {
        mPicture = picture;
    }
}
