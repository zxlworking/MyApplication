package com.zxl.river.chief.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.zxl.river.chief.R;
import com.zxl.river.chief.preference.Preference;
import com.zxl.river.chief.utils.CommonUtils;

/**
 * Created by mac on 17-11-24.
 */

public class LoginActivity extends BaseActivity {

    private EditText mUserNameEt;
    private EditText mPassWordEt;

    private TextView mLoginTv;

    private CheckBox mRememberMeCb;

    @Override
    public int getContentView() {
        return R.layout.login_activity;
    }

    @Override
    public void initView() {
        super.initView();
        mUserNameEt = (EditText) findViewById(R.id.user_name_et);
        mPassWordEt = (EditText) findViewById(R.id.pass_word_et);

        mLoginTv = (TextView) findViewById(R.id.login_tv);

        mRememberMeCb = (CheckBox) findViewById(R.id.remember_me_cb);

        mLoginTv.setOnClickListener(mOnClickListener);

        if(Preference.getInstance(mContext).getRememberMe()){
            mRememberMeCb.setChecked(true);
            login(Preference.getInstance(mContext).getUserName(),Preference.getInstance(mContext).getPassWord());
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_tv:
                    String mUserName = mUserNameEt.getText().toString();
                    String mPassWord = mPassWordEt.getText().toString();


                    login(mUserName, mPassWord);

                    break;
            }
        }
    };

    private void login(String mUserName, String mPassWord) {
        if(TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPassWord)){
            CommonUtils.showMessage(mContext,"用户名或密码不能为空!");
            return;
        }

        if(TextUtils.equals(mUserName,"admin") && TextUtils.equals(mPassWord,"123456")){

            if(mRememberMeCb.isChecked()){
                Preference.getInstance(mContext).setRememberMe(true);
                Preference.getInstance(mContext).setUserName(mUserName);
                Preference.getInstance(mContext).setPassWord(mPassWord);
            }

            Intent mSettingsIntent = new Intent(mContext,MainActivity.class);
            mSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mSettingsIntent);
            finish();
        }else {
            CommonUtils.showMessage(mContext,"用户名或密码错误!");
        }
    }
}
