package com.mirliam.financialmanagement;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FinancialDetailsLab {
    private static FinancialDetailsLab sFinancialDetailsLab;

    private List<FinancialDetails> mFinancialDetailsList;

    public static FinancialDetailsLab get(Context context) {
        if (sFinancialDetailsLab == null) {
            sFinancialDetailsLab = new FinancialDetailsLab(context);
        }
        return sFinancialDetailsLab;
    }

    private FinancialDetailsLab(Context context) {
        mFinancialDetailsList = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            FinancialDetails financialDetails = new FinancialDetails();
//            mFinancialDetailsList.add(financialDetails);
//        }

    }

    public List<FinancialDetails> getFinancialDetailsList() {
        return mFinancialDetailsList;
    }

    public FinancialDetails getFinancialDetails(UUID id) {
        for (FinancialDetails financialDetails : mFinancialDetailsList) {
            if (financialDetails.getId().equals(id)) {
                return financialDetails;
            }
        }

        return null;
    }

    public void addFinancialDetails(FinancialDetails financialDetails) {
        mFinancialDetailsList.add(financialDetails);
    }

    public boolean deleteFinancialDetails(FinancialDetails financialDetails) {
        for (FinancialDetails financial : mFinancialDetailsList) {
            if (financial.getId().equals(financialDetails.getId())) {
                mFinancialDetailsList.remove(financial);
                return true;
            }
        }
        return false;
    }
}
