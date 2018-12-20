package com.mirliam.financialmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mirliam.financialmanagement.FinancialDbSchema.UserTable;
import static com.mirliam.financialmanagement.FinancialDbSchema.FinancialDetailsTable;

public class FinancialBashHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATEBASE_NAME = "financialBase.db";

    public FinancialBashHelper(Context context) {
        super(context, DATEBASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + UserTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                UserTable.Cols.ACCOUNT + "," +
                UserTable.Cols.PASSWD +
                ")"
        );

         db.execSQL("create table " + FinancialDetailsTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                FinancialDetailsTable.Cols.UUID + "," +
                FinancialDetailsTable.Cols.TITLE + "," +
                FinancialDetailsTable.Cols.TYPE + "," +
                FinancialDetailsTable.Cols.DATE + "," +
                FinancialDetailsTable.Cols.MONEY + "," +
                FinancialDetailsTable.Cols.REMARK + "," +
                FinancialDetailsTable.Cols.PICTURE +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
