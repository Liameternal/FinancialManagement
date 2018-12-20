package com.mirliam.financialmanagement;

import android.content.ContentValues;
import android.content.Context;

import static com.mirliam.financialmanagement.FinancialDbSchema.UserTable;

public class User {
    private String account;
    private String passwd;
    private Context mContext;

    public User(){

    }

    public User(Context context, String account, String passwd) {
        mContext = context.getApplicationContext();

        this.account = account;
        this.passwd = passwd;
    }

    public static ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UserTable.Cols.ACCOUNT, user.account);
        values.put(UserTable.Cols.PASSWD, user.passwd);

        return values;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
