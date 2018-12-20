package com.mirliam.financialmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mirliam.financialmanagement.FinancialDbSchema.FinancialDetailsTable;

public class FinancialDetailsLab {
    private static FinancialDetailsLab sFinancialDetailsLab;

    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static FinancialDetailsLab get(Context context) {
        if (sFinancialDetailsLab == null) {
            sFinancialDetailsLab = new FinancialDetailsLab(context);
        }
        return sFinancialDetailsLab;
    }

    private FinancialDetailsLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new FinancialBashHelper(mContext).getWritableDatabase();
    }

    public List<FinancialDetails> getFinancialDetailsList() {
        List<FinancialDetails> financialDetailsList = new ArrayList<>();

        FinancialDetailsCursorWrapper cursor = queryFinancialDetails(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                financialDetailsList.add(cursor.getFinancialDetails());
                cursor.moveToNext();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return financialDetailsList;
    }

    public FinancialDetails getFinancialDetails(UUID id) {
        FinancialDetailsCursorWrapper cursor = queryFinancialDetails(
                FinancialDetailsTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getFinancialDetails();
        } catch (ParseException e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return null;
    }

    public void addFinancialDetails(FinancialDetails financialDetails) {
        ContentValues contentValues = getContentValues(financialDetails);

        mDatabase.insert(FinancialDetailsTable.NAME, null, contentValues);
    }

    public void deleteFinancialDetails(FinancialDetails financialDetails) {
        mDatabase.delete(FinancialDetailsTable.NAME,
                FinancialDetailsTable.Cols.UUID+"= ?",
                new String[]{financialDetails.getId().toString()}
                );
    }

    public void updateFinancialDetails(FinancialDetails financialDetails) {
        String uuidString = financialDetails.getId().toString();
        ContentValues values = getContentValues(financialDetails);
        mDatabase.update(FinancialDetailsTable.NAME, values,
                FinancialDetailsTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private FinancialDetailsCursorWrapper queryFinancialDetails(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                FinancialDetailsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new FinancialDetailsCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(FinancialDetails financialDetails) {
        ContentValues values = new ContentValues();
        values.put(FinancialDetailsTable.Cols.UUID, financialDetails.getId().toString());
        values.put(FinancialDetailsTable.Cols.TITLE, financialDetails.getTitle());
        values.put(FinancialDetailsTable.Cols.TYPE, String.valueOf(financialDetails.isInOrOut()));
        String date = (String) DateFormat.format("yyyy年MM月dd日", financialDetails.getDate());
        values.put(FinancialDetailsTable.Cols.DATE, date);
        values.put(FinancialDetailsTable.Cols.MONEY, financialDetails.getMoney());
        values.put(FinancialDetailsTable.Cols.REMARK, financialDetails.getRemark());
        if (financialDetails.getPicture() != null)
            values.put(FinancialDetailsTable.Cols.PICTURE, financialDetails.bitmapToString(financialDetails.getPicture()));

        return values;
    }
}
