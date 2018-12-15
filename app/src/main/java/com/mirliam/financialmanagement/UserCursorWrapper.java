package com.mirliam.financialmanagement;

import android.database.Cursor;
import android.database.CursorWrapper;

import static com.mirliam.financialmanagement.FinancialDbSchema.UserTable;

public class UserCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser(){
        String ACCOUNT = getString(getColumnIndex(UserTable.Cols.ACCOUNT));
        String PASSWD = getString(getColumnIndex(UserTable.Cols.PASSWD));

        return null;
    }
}
