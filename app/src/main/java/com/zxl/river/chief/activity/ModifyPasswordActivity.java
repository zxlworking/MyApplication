package com.zxl.river.chief.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxl.river.chief.R;
import com.zxl.river.chief.preference.Preference;
import com.zxl.river.chief.utils.CommonUtils;

/**
 * Created by mac on 17-12-3.
 */

public class ModifyPasswordActivity extends BaseActivity {

    private EditText mOriginalPasswordEt;
    private EditText mNewPasswordEt;
    private EditText mNewAgainPasswordEt;

    private TextView mModifyPasswordTv;

    @Override
    public int getContentView() {
        return R.layout.modify_password_view;
    }

    @Override
    public void initView() {
        super.initView();
        setRightImgVisibility(View.GONE);
        setTitle("修改密码");

        mOriginalPasswordEt = (EditText) findViewById(R.id.original_password_et);
        mNewPasswordEt = (EditText) findViewById(R.id.new_password_et);
        mNewAgainPasswordEt = (EditText) findViewById(R.id.new_again_password_et);

        mModifyPasswordTv = (TextView) findViewById(R.id.modify_password_tv);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.title_bar_left_img:
                        finish();
                        break;
                    case R.id.modify_password_tv:
                        modifyPassword();
                        break;
                }
            }
        };

        mModifyPasswordTv.setOnClickListener(mOnClickListener);
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void modifyPassword(){
        String mOriginalPasswordStr = mOriginalPasswordEt.getText().toString();
        String mNewPasswordStr = mNewPasswordEt.getText().toString();
        String mNewAgainPasswordStr = mNewAgainPasswordEt.getText().toString();

        if(TextUtils.isEmpty(mOriginalPasswordStr)){
            CommonUtils.showMessage(mContext,"原密码不能为空!");
            return;
        }
        if(TextUtils.isEmpty(mNewPasswordStr) || TextUtils.isEmpty(mNewAgainPasswordStr)){
            CommonUtils.showMessage(mContext,"新密码不能为空!");
            return;
        }
        if(!TextUtils.equals(mOriginalPasswordStr, Preference.getInstance(mContext).getPassWord())){
            CommonUtils.showMessage(mContext,"原密码错误!");
            return;
        }
        if(!TextUtils.equals(mNewPasswordStr, mNewAgainPasswordStr)){
            CommonUtils.showMessage(mContext,"两次新密码不一致!");
            return;
        }
        Preference.getInstance(mContext).setPassWord(mNewPasswordStr);
        CommonUtils.showMessage(mContext,"密码修改完成!");
        finish();
    }
}
