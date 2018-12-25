package com.mirliam.financialmanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.mirliam.financialmanagement.FinancialDbSchema.UserTable;

public class LoginActivity extends AppCompatActivity {
    private EditText mAccountEditText;
    private EditText mPasswdEditText;
    private Button mSignButton;
    private Button mLoginButton;
    private SQLiteDatabase mDatabaseWriter;
    private SQLiteDatabase mDatabaseRead;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUser = new User();

        mDatabaseRead = new FinancialBashHelper(getApplicationContext()).getReadableDatabase();
        mDatabaseWriter = new FinancialBashHelper(getApplicationContext()).getWritableDatabase();

        mAccountEditText = findViewById(R.id.account);
        mAccountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPasswdEditText = findViewById(R.id.passwd);

        mSignButton = findViewById(R.id.sign_button);
        mSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = mDatabaseRead.query(UserTable.NAME, null,
                        null,
                        null, null, null, null);
                try {
                    c.moveToFirst();
                    while (!c.isAfterLast()) {
                        String account = c.getString(c.getColumnIndex(UserTable.Cols.ACCOUNT));
                        String passwd = c.getString(c.getColumnIndex(UserTable.Cols.PASSWD));
                        String md5Account = md5(mAccountEditText.getText().toString());
                        String md5Passwd = md5(mPasswdEditText.getText().toString());
                        if (checkExist(mAccountEditText.getText().toString())) {
                            if (account.equals(md5Account) && passwd.equals(md5Passwd)) {
                                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                startActivity(intent);

                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                LoginActivity.this.finish();
                                return;
                            }
                        }
                        c.moveToNext();
                    }
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                } finally {
                    c.close();
                }
            }
        });

        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mAccountEditText.getText().toString().equals("") && !mPasswdEditText.getText().toString().equals("")) {
                    mUser.setAccount(md5(mAccountEditText.getText().toString()));
                    mUser.setPasswd(md5(mPasswdEditText.getText().toString()));
                }
                if (!mAccountEditText.getText().toString().equals("") && !mPasswdEditText.getText().toString().equals("")) {

                    if (!checkExist(mAccountEditText.getText().toString())) {
                        addUser(mUser);
                        Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean addUser(User user) {
        ContentValues values = User.getContentValues(user);
        mDatabaseWriter.insert(UserTable.NAME, null, values);

        return true;
    }

    public static String md5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    private boolean checkExist(String name) {
        Cursor c = mDatabaseRead.query(UserTable.NAME, null,
                null,
                null, null, null, null);
        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String account = c.getString(c.getColumnIndex(UserTable.Cols.ACCOUNT));

                boolean isequal = md5(name).equals(account);
                if (isequal) {
                    return true;
                }
                c.moveToNext();
            }
        } finally {
            c.close();
        }

        return false;
    }

}
