package com.mirliam.financialmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;


public class PersonalSpaceFragment extends Fragment {
    private TextView mInComeTodayTextView;
    private TextView mOutComeTodayTextView;
    private TextView mInComeMonthTextView;
    private TextView mOutComeMonthTextView;
    private TextView mInComeYearTextView;
    private TextView mOutComeYearTextView;

    private List<FinancialDetails> mFinancialDetailsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal_space, container, false);

        mInComeTodayTextView = v.findViewById(R.id.income_today);
        mInComeTodayTextView.setText(getInCome(0));

        mOutComeTodayTextView = v.findViewById(R.id.outcome_today);
        mOutComeTodayTextView.setText(getOutCome(0));

        mInComeMonthTextView = v.findViewById(R.id.income_month);
        mInComeMonthTextView.setText(getInCome(1));

        mOutComeMonthTextView = v.findViewById(R.id.outcome_month);
        mOutComeMonthTextView.setText(getOutCome(1));

        mInComeYearTextView = v.findViewById(R.id.income_year);
        mInComeYearTextView.setText(getInCome(2));

        mOutComeYearTextView = v.findViewById(R.id.outcome_year);
        mOutComeYearTextView.setText(getOutCome(2));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    private void updateUI() {
        mInComeTodayTextView.setText(getInCome(0));

        mOutComeTodayTextView.setText(getOutCome(0));

        mInComeMonthTextView.setText(getInCome(1));

        mOutComeMonthTextView.setText(getOutCome(1));

        mInComeYearTextView.setText(getInCome(2));

        mOutComeYearTextView.setText(getOutCome(2));
    }

    private String getInCome(int flag) {  //flag == 0 表示当日, flag == 1 表示当月, flag == 2 表示当年
        getFinancialDetailsList();
        float dayInCome = 0;
        float monthInCome = 0;
        float yearInCome = 0;

        Calendar c = Calendar.getInstance();//
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前日期

        for (FinancialDetails financialDetails : mFinancialDetailsList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(financialDetails.getDate());

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            if (year == mYear) {
                if (financialDetails.isInOrOut()) {
                    yearInCome = yearInCome + financialDetails.getMoney();
                }
            }
            if (month == mMonth) {
                if (financialDetails.isInOrOut()) {
                    monthInCome = monthInCome + financialDetails.getMoney();
                }
            }
            if (day == mDay) {
                if (financialDetails.isInOrOut()) {
                    dayInCome = dayInCome + financialDetails.getMoney();
                }
            }

        }

        switch (flag) {
            case 0:
                return String.valueOf(dayInCome);
            case 1:
                return String.valueOf(monthInCome);
            case 2:
                return String.valueOf(yearInCome);
        }

        return null;
    }

    private String getOutCome(int flag) {  //flag == 0 表示当日, flag == 1 表示当月, flag == 2 表示当年
        getFinancialDetailsList();
        float dayOutCome = 0;
        float monthOutCome = 0;
        float yearOutCome = 0;

        Calendar c = Calendar.getInstance();//
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前日期

        for (FinancialDetails financialDetails : mFinancialDetailsList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(financialDetails.getDate());

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            if (year == mYear) {
                if (!financialDetails.isInOrOut()) {
                    yearOutCome = yearOutCome + financialDetails.getMoney();
                }
            }
            if (month == mMonth) {
                if (!financialDetails.isInOrOut()) {
                    monthOutCome = monthOutCome + financialDetails.getMoney();
                }
            }
            if (day == mDay) {
                if (!financialDetails.isInOrOut()) {
                    dayOutCome = dayOutCome + financialDetails.getMoney();
                }
            }

        }

        switch (flag) {
            case 0:
                return String.valueOf(dayOutCome);
            case 1:
                return String.valueOf(monthOutCome);
            case 2:
                return String.valueOf(yearOutCome);
        }

        return null;
    }

    private void getFinancialDetailsList() {
        mFinancialDetailsList = FinancialDetailsLab.get(getActivity()).getFinancialDetailsList();
    }
}
