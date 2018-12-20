package com.mirliam.financialmanagement;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.CursorWrapper;

import com.mirliam.financialmanagement.FinancialDbSchema.FinancialDetailsTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FinancialDetailsCursorWrapper extends CursorWrapper {

    public FinancialDetailsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public FinancialDetails getFinancialDetails() throws ParseException {
        String uuidString = getString(getColumnIndex(FinancialDetailsTable.Cols.UUID));
        String title = getString(getColumnIndex(FinancialDetailsTable.Cols.TITLE));
        String date = getString(getColumnIndex(FinancialDetailsTable.Cols.DATE));
        String type = getString(getColumnIndex(FinancialDetailsTable.Cols.TYPE));
        String money = getString(getColumnIndex(FinancialDetailsTable.Cols.MONEY));
        String remark = getString(getColumnIndex(FinancialDetailsTable.Cols.REMARK));
        String picture = getString(getColumnIndex(FinancialDetailsTable.Cols.PICTURE));

        FinancialDetails financialDetails = new FinancialDetails(UUID.fromString(uuidString));
        financialDetails.setTitle(title);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date dt = sdf.parse(date);
        financialDetails.setDate(dt);
        if (Boolean.parseBoolean(type))
            financialDetails.setInOrOut(Boolean.TRUE);
        else
            financialDetails.setInOrOut(Boolean.FALSE);

        financialDetails.setMoney(Float.valueOf(money));
        financialDetails.setRemark(remark);
        if (picture != null)
            if (!picture.equals(""))
                financialDetails.setPicture(financialDetails.stringToBitmap(picture));

        return financialDetails;
    }
}
