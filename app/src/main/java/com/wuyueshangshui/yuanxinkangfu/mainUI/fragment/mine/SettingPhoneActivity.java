package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendBodyBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendRootBean;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.common.GlobalURL;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.BaseActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.InputCheck;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.CheckNet;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.MyCallBack;
import com.wuyueshangshui.yuanxinkangfu.utils.SPUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.DialogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.encrypt.DES;
import com.wuyueshangshui.yuanxinkangfu.wigdet.TimeCount;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 设置手机号页面
 */
public class SettingPhoneActivity extends BaseActivity implements View.OnClickListener {

    private EditText phoneNum;
    private EditText checknumEdit;
    private ImageView delete_1;
    private ImageView delete_2;
    private TextView getChecknum;
    private TimeCount time;
    private LinearLayout submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_setting);
        //初始化控件
        initView();
        //初始化数据
        initData();
        //添加监听
        initListener();
        //添加观察者
        initWatcher();
    }


    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.all_title_text)).setText("设置手机号");
        phoneNum = (EditText) findViewById(R.id.phoneNum_setting_edit);
        checknumEdit = (EditText) findViewById(R.id.phoneNum_setting_checknum_edit);
        delete_1 = (ImageView) findViewById(R.id.phoneNum_setting_delete_1);
        delete_2 = (ImageView) findViewById(R.id.phoneNum_setting_delete_2);
        getChecknum = (TextView) findViewById(R.id.phoneNum_setting_checknum);
        submit = (LinearLayout) findViewById(R.id.phoneNum_setting_submit);
    }
    private void initData() {

    }
    private void initListener() {
        delete_1.setOnClickListener(this);
        delete_2.setOnClickListener(this);
        getChecknum.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.phoneNum_setting_delete_1:
                phoneNum.setText("");
                break;
            case R.id.phoneNum_setting_delete_2:
                checknumEdit.setText("");
                break;
            case R.id.phoneNum_setting_checknum:
                //发送验证码按钮
                startSendCheckNum();
                break;
            case R.id.phoneNum_setting_submit:
                //设置手机号的提交按钮
                submitData();
                break;
        }
    }
    private void submitData() {
        String encryptData=DES.encrypt(gson.toJson(getChangePhoneBean()));
        ToastUtils.showLoadingToast(this);
        OkHttpUtils.postString().url(GlobalURL.BaseURL)
                .content(encryptData)
                .mediaType(MediaType.parse("application/json;charset=utf-8"))
                .build().execute(new MyCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.dismissLoadingToast();
                LogUtils.showe("====修改手机号的返回数据fail:"+e+":id:"+id);
            }

            @Override
            public void onResponse(String response, int id) {
                String decryptData=DES.decrypt(response);
                LogUtils.showe("====修改手机号的返回数据success:"+decryptData+":id:"+id);
                returnData(decryptData);
            }
        });
    }

    private void returnData(String decryptData) {
        try {
            JSONObject origin = new JSONObject(decryptData);
            JSONObject root=origin.optJSONObject(Constant.root);
            JSONObject head=root.optJSONObject(Constant.head);
            ToastUtils.dismissLoadingToast();
            if (head.optInt(Constant.code)==0){
                ToastUtils.Toast(SettingPhoneActivity.this,"修改成功");
                Intent toPerson=new Intent();
                toPerson.putExtra(Constant.phone,phoneNum.getText().toString());
                setResult(Constant.SETPHONENUM_TO_PERSON_TAG,toPerson);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }else{
                ToastUtils.Toast(SettingPhoneActivity.this,head.optString(Constant.text));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private SendOriginBean getChangePhoneBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(117);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setUser_id(SPUtils.getString(this,Constant.user_id,null));
        body.setType(6);
        body.setMobile(phoneNum.getText().toString());
        body.setCode(checknumEdit.getText().toString());
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;

    }

    private void startSendCheckNum() {
        time = new TimeCount(60000,1000,this,getChecknum);
        //手机号不能为空
        if (TextUtils.isEmpty(phoneNum.getText().toString())){
            DialogUtils.onePosition(this,getResources().getString(R.string.phone_is_not_empty));
            return;
        }
        //校验手机号
        boolean isOk= InputCheck.checkPhone(this,phoneNum.getText().toString());
        if (!isOk) return;
        //判断网络是否连接
        boolean netIsConnect= CheckNet.netIsOk(this);
        if (!netIsConnect){
            ToastUtils.Toast(this,getResources().getString(R.string.no_network));
        }else{
            //计时器开始计时
            time.start();
            String encryptContent= DES.encrypt(gson.toJson(getCheckNumBean()));
            OkHttpUtils.postString().url(GlobalURL.BaseURL)
                    .content(encryptContent)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.showe("====验证码的返回数据fail:"+e+":id:"+id);
                        }
                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.showe("====验证码的返回数据success:"+response+":id:"+id);
                            String decryptData=DES.decrypt(response);
                            LogUtils.showe("====验证码的返回数据解密后的success:"+decryptData+":id:"+id);
                            try{
                                if (!TextUtils.isEmpty(decryptData)){
                                    jsonCheckMethod(decryptData);
                                }
                            }catch (IllegalStateException e){
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }

    public SendOriginBean getCheckNumBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(117);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setUser_id(SPUtils.getString(this,Constant.user_id,null));
        body.setType(5);
        body.setMobile(phoneNum.getText().toString());
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }

    private void jsonCheckMethod(String decryptData) {
        try {
            JSONObject origin = new JSONObject(decryptData);
            JSONObject root=origin.optJSONObject(Constant.root);
            JSONObject head=root.optJSONObject(Constant.head);
            ToastUtils.Toast(this,head.optString(Constant.text));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initWatcher() {
        phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    delete_1.setVisibility(View.VISIBLE);
                }else{
                    delete_1.setVisibility(View.INVISIBLE);
                }
            }
        });
        checknumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    delete_2.setVisibility(View.VISIBLE);
                }else{
                    delete_2.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
