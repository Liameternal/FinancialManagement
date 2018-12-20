package com.mirliam.financialmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class StreamFragment extends Fragment{
    private RecyclerView mFinancialDetailsRecycleListView;
    private FinancialDetailsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stream, container, false);

        mFinancialDetailsRecycleListView = v.findViewById(R.id.financial_details_recycler_view);
        mFinancialDetailsRecycleListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        FinancialDetailsLab financialDetailsLab = FinancialDetailsLab.get(getActivity());
        List<FinancialDetails> financialDetailsList = financialDetailsLab.getFinancialDetailsList();

        if(mAdapter ==null){
            mAdapter = new FinancialDetailsAdapter(financialDetailsList);
            mFinancialDetailsRecycleListView.setAdapter(mAdapter);
        }else {
            mAdapter.setFinancialDetailsList(financialDetailsList);
            mAdapter.notifyDataSetChanged();
        }
    }


    public class FinancialDetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mStreamTitleTextView;
        private TextView mStreamMoneyTextview;
        private TextView mStreamDataTextview;

        private FinancialDetails mFinancialDetails;

        public FinancialDetailsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_financial_details, parent, false));
            itemView.setOnClickListener(this);

            mStreamTitleTextView = itemView.findViewById(R.id.stream_title);
            mStreamMoneyTextview = itemView.findViewById(R.id.stream_money);
            mStreamDataTextview = itemView.findViewById(R.id.stream_date);

            registerForContextMenu(itemView);
        }

        public void bind(FinancialDetails financialDetails) {
            mFinancialDetails = financialDetails;
            mStreamTitleTextView.setText(mFinancialDetails.getTitle());
            if (financialDetails.isInOrOut()) {
                mStreamMoneyTextview.setText("+" + String.valueOf(mFinancialDetails.getMoney()));
            } else {
                mStreamMoneyTextview.setText("-" + String.valueOf(mFinancialDetails.getMoney()));
            }
            String date = (String) DateFormat.format("yyyy年MM月dd日", mFinancialDetails.getDate());
            mStreamDataTextview.setText(date);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(getActivity(),mFinancialDetails.getTitle()+" clicked",Toast.LENGTH_SHORT).show();
            Intent intent = FinancialDetailsActivity.newIntent(getActivity(),mFinancialDetails.getId(),0);
            startActivity(intent);
        }
    }

    public class FinancialDetailsAdapter extends RecyclerView.Adapter<FinancialDetailsHolder> {
        private List<FinancialDetails> mFinancialDetailsList;
        private FinancialDetails mFinancialDetails;
        private int mPosition;

        public FinancialDetailsAdapter(List<FinancialDetails> financialDetailsList) {
            mFinancialDetailsList = financialDetailsList;
        }

        @NonNull
        @Override
        public FinancialDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new FinancialDetailsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull final FinancialDetailsHolder holder, int position) {
            FinancialDetails financialDetails = mFinancialDetailsList.get(position);
            holder.bind(financialDetails);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mPosition = holder.getAdapterPosition();
                    mFinancialDetails = mFinancialDetailsList.get(mPosition);
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mFinancialDetailsList.size();
        }

        public void setFinancialDetailsList(List<FinancialDetails> financialDetailsList){
            mFinancialDetailsList = financialDetailsList;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,1,0,"修改");
        menu.add(0,2,0,"删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 1:
                Intent intent = FinancialDetailsActivity.newIntent(getActivity(), FinancialDetailsLab.get(getActivity()).getFinancialDetailsList().get(mAdapter.mPosition).getId(),1);
                startActivity(intent);
                break;
            case 2:
                FinancialDetailsLab.get(getActivity()).deleteFinancialDetails(mAdapter.mFinancialDetails);
                mAdapter.notifyItemRemoved(mAdapter.mPosition);
                updateUI();
                break;
        }

        return super.onContextItemSelected(item);
    }
}
