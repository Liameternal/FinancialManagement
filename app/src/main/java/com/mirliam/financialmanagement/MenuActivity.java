package com.mirliam.financialmanagement;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity  implements View.OnClickListener{

    /**
     * 用于展示个人中心的Fragment
     */
    private PersonalSpaceFragment mPersonalSpaceFragment;

//    /**
//     * 用于展示记账的Fragment
//     */
//    private FinancialDetailsFragment mFinancialDetailsFragment;

    /**
     * 用于展示流水的Fragment
     */
    private StreamFragment mStreamFragment;

    /**
     * 个人中心界面布局
     */
    private View mPersonalSpaceLayout;

//    /**
//     * 记账界面布局
//     */
//    private View mMakeDetLayout;

    /**
     * 流水界面布局
     */
    private View mStreamLayout;

    /**
     * 在Tab布局上显示个人中心图标的控件
     */
    private ImageView mPersonalSpaceImage;

    /**
     * 在Tab布局上显示记账图标的控件
     */
    private ImageView mMakeDetImage;

    /**
     * 在Tab布局上显示流水图标的控件
     */
    private ImageView mStreamImage;

    /**
     * 在Tab布局上显示个人中心标题的控件
     */
    private TextView mPersonalSpaceText;

//    /**
//     * 在Tab布局上显示记账标题的控件
//     */
//    private TextView mMakeDetText;

    /**
     * 在Tab布局上显示流水标题的控件
     */
    private TextView mStreamText;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    private SQLiteDatabase mDatabaseWriter;
    private SQLiteDatabase mDatabaseRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);

        mDatabaseRead = new FinancialBashHelper(getApplicationContext()).getReadableDatabase();
        mDatabaseWriter = new FinancialBashHelper(getApplicationContext()).getWritableDatabase();

        // 初始化布局元素
        initViews();

        fragmentManager = getSupportFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);

        mMakeDetImage = findViewById(R.id.make_det_image);
        mMakeDetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FinancialDetails financialDetails = new FinancialDetails();
//                FinancialDetailsLab.get(getApplicationContext()).addFinancialDetails(financialDetails);
//                Intent intent = FinancialDetailsActivity.newInstance(getApplicationContext(),financialDetails.getId());
                Intent intent = new Intent(getApplicationContext(),FinancialDetailsActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
        mPersonalSpaceLayout = findViewById(R.id.personal_space_layout);
//        mMakeDetLayout = findViewById(R.id.make_det_layout);
        mStreamLayout = findViewById(R.id.stream_layout);

        mPersonalSpaceImage = (ImageView) findViewById(R.id.personal_space_image);
//        mMakeDetImage = (ImageView) findViewById(R.id.make_det_image);
        mStreamImage = (ImageView) findViewById(R.id.stream_image);

        mPersonalSpaceText = (TextView) findViewById(R.id.personal_space_text);
//        mMakeDetText = (TextView) findViewById(R.id.make_det_text);
        mStreamText = (TextView) findViewById(R.id.stream_text);




        mPersonalSpaceLayout.setOnClickListener(this);
//        mMakeDetLayout.setOnClickListener(this);
        mStreamLayout.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_space_layout:
                // 当点击了个人中心tab时，选中第1个tab
                setTabSelection(0);
                break;
//            case R.id.make_det_layout:
//                // 当点击了记账tab时，选中第2个tab
//                setTabSelection(1);
//                break;
            case R.id.stream_layout:
                // 当点击了流水tab时，选中第2个tab
                setTabSelection(1);
                break;
            default:
                break;
        }
    }




    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *            每个tab页对应的下标。0表示个人中心，1表示记账，2表示流水。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        switch (index){
            case 0:
                // 当点击了个人中心tab时，改变控件的图片和文字颜色
//                mPersonalSpaceImage.setImageResource(R.drawable.first_page);
                mPersonalSpaceText.setTextColor(Color.RED);
                if (mPersonalSpaceFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mPersonalSpaceFragment = new PersonalSpaceFragment();
                    transaction.add(R.id.content, mPersonalSpaceFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mPersonalSpaceFragment);
                }
                break;
//            case 1:
//                // 当点击了记账tab时，改变控件的图片和文字颜色
////                mMakeDetImage.setImageResource(R.drawable.telephone);
//                mMakeDetText.setTextColor(Color.RED);
//                if (mFinancialDetailsFragment == null) {
//                    // 如果ContactsFragment为空，则创建一个并添加到界面上
//                    mFinancialDetailsFragment = new FinancialDetailsFragment();
//                    transaction.add(R.id.content, mFinancialDetailsFragment);
//                } else {
//                    // 如果ContactsFragment不为空，则直接将它显示出来
//                    transaction.show(mFinancialDetailsFragment);
//                }
//                break;
            case 1:
                // 当点击了流水tab时，改变控件的图片和文字颜色
//                mStreamImage.setImageResource(R.drawable.notice);
                mStreamText.setTextColor(Color.RED);
                if (mStreamFragment == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    mStreamFragment = new StreamFragment();
                    transaction.add(R.id.content, mStreamFragment);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(mStreamFragment);
                }
                break;
            default:
                break;
        }


        transaction.commit();

    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
//        mPersonalSpaceImage.setImageResource(R.drawable.first_page);
        mPersonalSpaceText.setTextColor(Color.parseColor("#82858b"));
//        mMakeDetImage.setImageResource(R.drawable.telephone);
//        mMakeDetText.setTextColor(Color.parseColor("#82858b"));
//        mStreamImage.setImageResource(R.drawable.notice);
        mStreamText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction){
        if (mPersonalSpaceFragment != null) {
            transaction.hide(mPersonalSpaceFragment);
        }
//        if (mFinancialDetailsFragment != null) {
//            transaction.hide(mFinancialDetailsFragment);
//        }
        if (mStreamFragment != null) {
            transaction.hide(mStreamFragment);
        }
    }

}

