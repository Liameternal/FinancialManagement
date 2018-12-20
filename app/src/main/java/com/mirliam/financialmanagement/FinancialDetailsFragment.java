package com.mirliam.financialmanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FinancialDetailsFragment extends Fragment implements View.OnClickListener {

    private String[] TITLE = {"食品", "交通", "娱乐", "购物", "通讯", "学习", "网游", "数码", "医疗", "其他"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FINANCIALDETAILSID = "financial_details_id";
    private static final String ARG_FINANCIALDETAILSISADD = "financial_details_is_add";


    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static int RESULT_LOAD_IMAGE = 1;

    private FinancialDetails mFinancialDetails;
    private FinancialDetailsLab mFinancialDetailsLab;
    private TextView mTitleEditText;
    private ListView mTypeLv;

    /**
     * popup窗口
     */
    private PopupWindow typeSelectPopup;

    /**
     * 模拟的假数据
     */
    private List<String> testData;

    /**
     * 数据适配器
     */
    private ArrayAdapter<String> testDataAdapter;

    private RadioGroup mInAndOutRadioGroup;
    private RadioButton mInRadioButton;
    private RadioButton mOutRadioButton;

    private Button mDataButton;
    private EditText mMoneyEditText;
    private EditText mRemarksEditText;

    private ImageView mImageView;

    private Button mEnsureButton;
    private Button mCancelButton;
    private Button mOneMoreButton;

    private int isAdd;

    private SQLiteDatabase mDatabaseWriter;
    private SQLiteDatabase mDatabaseRead;

    public static FinancialDetailsFragment newInstance(UUID uuid, int isAdd) {
        FinancialDetailsFragment fragment = new FinancialDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FINANCIALDETAILSID, uuid);
        args.putInt(ARG_FINANCIALDETAILSISADD, isAdd);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFinancialDetailsLab = FinancialDetailsLab.get(getActivity());
        if (getArguments() != null) {

            UUID financialDetailsId = (UUID) getArguments().getSerializable(ARG_FINANCIALDETAILSID);

            isAdd = getArguments().getInt(ARG_FINANCIALDETAILSISADD);
            if (isAdd != -1)
                mFinancialDetails = mFinancialDetailsLab.getFinancialDetails(financialDetailsId);
        }

        mDatabaseRead = new FinancialBashHelper(getContext()).getReadableDatabase();
        mDatabaseWriter = new FinancialBashHelper(getContext()).getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_financial_details, container, false);

        if (isAdd == 0) {
            getActivity().setTitle(R.string.check_financial_details_info);
        } else if (isAdd == 1) {
            getActivity().setTitle(R.string.modify_financial_details_info);
        } else {
            getActivity().setTitle(R.string.add_financial_details_info);
            mFinancialDetails = new FinancialDetails();
        }

        initUI(v);

        initListener(v);

        mInAndOutRadioGroup = v.findViewById(R.id.in_and_out);
        mInRadioButton = v.findViewById(R.id.in);
        mOutRadioButton = v.findViewById(R.id.out);
        if (mFinancialDetails.isInOrOut()) {
            mInRadioButton.setChecked(true);
        } else {
            mOutRadioButton.setChecked(true);
        }
        mInAndOutRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean ischecked = true;
                switch (checkedId) {
                    case R.id.in:
                        ischecked = true;
                        break;
                    case R.id.out:
                        ischecked = false;
                        break;
                    default:
                        break;
                }
                mFinancialDetails.setInOrOut(ischecked);
            }
        });

        mDataButton = v.findViewById(R.id.date_of_make_det);
        updateDate();
        mDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mFinancialDetails.getDate());
                dialog.setTargetFragment(FinancialDetailsFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mMoneyEditText = v.findViewById(R.id.money_of_make_det);
        mMoneyEditText.setText(String.valueOf(mFinancialDetails.getMoney()));
        mMoneyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(""))
                    mFinancialDetails.setMoney(Float.valueOf(s.toString()));
            }
        });

        mRemarksEditText = v.findViewById(R.id.remarks_of_make_det);
        mRemarksEditText.setText(mFinancialDetails.getRemark());
        mRemarksEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFinancialDetails.setRemark(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mImageView = v.findViewById(R.id.image_of_make_det);
        mImageView.setImageBitmap(mFinancialDetails.getPicture());
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //打开系统相册
                    openAlbum();
                }
            }
        });

        mEnsureButton = v.findViewById(R.id.ensure_button);
        mEnsureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check data
                try {
                    if (checkData()) {
                        if (isAdd == 1) {
                            mFinancialDetailsLab.updateFinancialDetails(mFinancialDetails);
                            getActivity().finish();
                        } else {
                            mFinancialDetailsLab.addFinancialDetails(mFinancialDetails);
                            getActivity().finish();
                        }
                        Toast.makeText(getActivity(), R.string.success_tip, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.failure_tip, Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        mCancelButton = v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mOneMoreButton = v.findViewById(R.id.one_more_button);
        mOneMoreButton.setVisibility(View.GONE);
        mOneMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.add_more_tip, Toast.LENGTH_SHORT).show();
                getActivity().recreate();
            }
        });

        if (isAdd == 0) {
            hideButton();
        } else if (isAdd == 1) {
            mOneMoreButton.setVisibility(View.GONE);
        }

        return v;
    }

    private void hideButton() {
        mEnsureButton.setVisibility(View.GONE);
        mCancelButton.setVisibility(View.GONE);
        mOneMoreButton.setVisibility(View.GONE);
    }

    private boolean checkData() throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = sdf.parse(mDataButton.getText().toString());
        if (date.getTime() > (new Date()).getTime()) {
            Toast.makeText(getActivity(), R.string.invalid_date, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);//打开系统相册

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mFinancialDetails.setDate(date);
            updateDate();
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (Build.VERSION.SDK_INT >= 19) {
                handleImageOnKitkat(data);
            } else {
                handleImageBeforeKitkat(data);
            }
        }

    }

    private void updateDate() {
        String date = (String) DateFormat.format("yyyy年MM月dd日", mFinancialDetails.getDate());
        mDataButton.setText(date);
    }

    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getContext(), uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片

    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mImageView.setImageBitmap(bitmap);
            /**
             * 保存图片
             */
            mFinancialDetails.setPicture(bitmap);
        } else {
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化UI
     */
    private void initUI(View v) {
        mTitleEditText = v.findViewById(R.id.title_of_make_det);
        mTitleEditText.setText(mFinancialDetails.getTitle());
    }

    /**
     * 初始化监听
     */
    private void initListener(View v) {
        mTitleEditText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_of_make_det:
                // 点击控件后显示popup窗口
                initSelectPopup();
                // 使用isShowing()检查popup窗口是否在显示状态
                if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
                    typeSelectPopup.showAsDropDown(mTitleEditText, 0, 10);
                }
                break;
        }
    }

    /**
     * 初始化popup窗口
     */
    private void initSelectPopup() {
        mTypeLv = new ListView(getContext());
        TestData();
        // 设置适配器
        testDataAdapter = new ArrayAdapter<String>(getContext(), R.layout.popup_text_item, testData);
        mTypeLv.setAdapter(testDataAdapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 在这里获取item数据
                String value = testData.get(position);
                // 把选择的数据展示对应的TextView上
                mTitleEditText.setText(value);

                /**
                 * 保存title
                 */
                mFinancialDetails.setTitle(value);
                // 选择完后关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
        typeSelectPopup = new PopupWindow(mTypeLv, mTitleEditText.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_corner);
        typeSelectPopup.setBackgroundDrawable(drawable);
        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
    }

    /**
     * 模拟假数据
     */
    private void TestData() {
        testData = new ArrayList<>();
        for (int i = 0; i < TITLE.length; i++) {
            testData.add(TITLE[i]);
        }
    }

}
