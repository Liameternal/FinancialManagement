package com.mirliam.financialmanagement;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
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

    public FinancialDetails(UUID id){
        mUUID =id;
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

    public Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public String bitmapToString(Bitmap bitmap){
        //将Bitmap转换成字符串
        String string=null;
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        string=Base64.encodeToString(bytes,Base64.DEFAULT);
        return string;
    }

}
