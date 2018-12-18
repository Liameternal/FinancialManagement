package com.mirliam.financialmanagement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FinancialDetailsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FinancialDetailsFragment();
    }
}
