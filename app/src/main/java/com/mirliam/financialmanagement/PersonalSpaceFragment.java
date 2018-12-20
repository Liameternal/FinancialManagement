package com.mirliam.financialmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class PersonalSpaceFragment extends Fragment {
    private TextView mBalanceTextView;
    private TextView mInComeTextView;
    private TextView mOutComeTextView;

    private List<FinancialDetails> mFinancialDetailsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal_space, container, false);

        mBalanceTextView = v.findViewById(R.id.balance);
        mBalanceTextView.setText(getBalance());

        mInComeTextView = v.findViewById(R.id.income_of_month);
        mInComeTextView.setText(getInCome());

        mOutComeTextView = v.findViewById(R.id.outcome_of_month);
        mOutComeTextView.setText(getOutCome());

        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        updateUI();
        Log.d("tttt","onStart() is called");
    }

    @Override
    public void onResume(){
        super.onResume();

        Log.d("tttt","onResume() is called");
    }

    @Override
    public void onPause(){
        super.onPause();

        Log.d("tttt","onPause() is called");
    }

    @Override
    public void onStop(){
        super.onStop();

        Log.d("tttt","onStop() is called");
    }

    private void updateUI() {
        mBalanceTextView.setText(getBalance());
        mInComeTextView.setText(getInCome());
        mOutComeTextView.setText(getOutCome());
    }

    private String getBalance() {
        getFinancialDetailsList();
        float balance = 0;

        for (FinancialDetails financialDetails : mFinancialDetailsList) {
            if(financialDetails.isInOrOut()){
                balance = balance + financialDetails.getMoney();
            }else {
                balance = balance - financialDetails.getMoney();
            }
        }

        return String.valueOf(balance);
    }

    private String getInCome() {
        getFinancialDetailsList();
        float inCome = 0;

        for (FinancialDetails financialDetails : mFinancialDetailsList) {
            if (financialDetails.isInOrOut()) {
                inCome = inCome + financialDetails.getMoney();
            }
        }

        return String.valueOf(inCome);
    }

    private String getOutCome() {
        getFinancialDetailsList();
        float outCome = 0;

        for (FinancialDetails financialDetails : mFinancialDetailsList) {
            if (!financialDetails.isInOrOut()) {
                outCome = outCome + financialDetails.getMoney();
            }
        }

        return String.valueOf(outCome);
    }

    private void getFinancialDetailsList() {
        mFinancialDetailsList = FinancialDetailsLab.get(getActivity()).getFinancialDetailsList();
    }
}
