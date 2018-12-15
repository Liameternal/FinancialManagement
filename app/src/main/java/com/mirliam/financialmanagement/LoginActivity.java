package com.mirliam.financialmanagement;

import android.content.ContentValues;
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

import static com.mirliam.financialmanagement.FinancialDbSchema.UserTable;

public class LoginActivity extends AppCompatActivity {
    private EditText mAccountEditText;
    private EditText mPasswdEditText;
    private Button mSignButton;
    private Button mLoginButton;
    private SQLiteDatabase mDatabaseWriter;
    private SQLiteDatabase mDatabaseRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                mDatabaseRead = new FinancialBashHelper(getApplicationContext()).getReadableDatabase();
                Cursor c = mDatabaseRead.query(UserTable.NAME, null,
                        null,
                        null, null, null, null);
                try {
                    c.moveToFirst();
                    while (!c.isAfterLast()){
                        String account = c.getString(c.getColumnIndex(UserTable.Cols.ACCOUNT));
                        String passwd = c.getString(c.getColumnIndex(UserTable.Cols.PASSWD));
                        if (mAccountEditText.getText().toString().equals(account) && mPasswdEditText.getText().toString().equals(passwd)) {
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        }
                        c.moveToNext();
                    }
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                }finally {
                    c.close();
                }
            }
        });

        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseWriter = new FinancialBashHelper(getApplicationContext()).getWritableDatabase();
                User user = new User(getApplicationContext(), mAccountEditText.getText().toString(),
                        mPasswdEditText.getText().toString());
                if (addUser(user)){
                    Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                } else{
                    Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean addUser(User user) {
        ContentValues values = User.getContentValues(user);
        mDatabaseWriter.insert(UserTable.NAME, null, values);
        mDatabaseWriter.close();

        return true;
    }

}
