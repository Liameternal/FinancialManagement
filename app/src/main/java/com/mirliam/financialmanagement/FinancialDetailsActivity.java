package com.mirliam.financialmanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class FinancialDetailsActivity extends SingleFragmentActivity {

    private static final String EXTRA_FINANCIAL_DETAILS_ID = "com.mirliam.financialmanagement.financial_details_id";
    private static final String EXTRA_FINANCIAL_DETAILS_IS_ADD = "com.mirliam.financialmanagement.financial_details_is_add";

    public static Intent newIntent(Context packageContext, UUID financialDetailsId, int isAdd) {

        Intent intent = new Intent(packageContext, FinancialDetailsActivity.class);
        intent.putExtra(EXTRA_FINANCIAL_DETAILS_ID, financialDetailsId);
        intent.putExtra(EXTRA_FINANCIAL_DETAILS_IS_ADD, isAdd);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID financialDetailsId = (UUID) getIntent().getSerializableExtra(EXTRA_FINANCIAL_DETAILS_ID);
        int isAdd = getIntent().getIntExtra(EXTRA_FINANCIAL_DETAILS_IS_ADD, -1);
        return FinancialDetailsFragment.newInstance(financialDetailsId, isAdd);
    }
}
