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

import com.google.gson.JsonElement;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendBodyBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendRootBean;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.common.GlobalURL;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.BaseActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.MyCallBack;
import com.wuyueshangshui.yuanxinkangfu.utils.NiceDialog.ConfirmDialog;
import com.wuyueshangshui.yuanxinkangfu.utils.SPUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.DialogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.encrypt.DES;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

public class SettingFamilyNameActivity extends BaseActivity implements View.OnClickListener {


    private EditText familyName;
    private ImageView delete;
    private LinearLayout submit;
    private int family_id;
    private String family_name;
    private int family_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_family_name);

        initView();

        initListener();

        initWatcher();

        initIntentData();
    }



    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.all_title_text)).setText("设置名称");
        familyName = (EditText) findViewById(R.id.setting_family_name_edit);
        delete = (ImageView) findViewById(R.id.setting_family_name_delete_1);
        submit = (LinearLayout) findViewById(R.id.setting_family_name_submit);
    }


    private void initListener() {
        delete.setOnClickListener(this);
        submit.setOnClickListener(this);
    }


    private void initWatcher() {
        familyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    delete.setVisibility(View.VISIBLE);
                }else{
                    delete.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.setting_family_name_delete_1:
                familyName.setText("");
                break;
            case R.id.setting_family_name_submit:
                submitData();
                break;
        }
    }
    //提交信息
    private void submitData() {
        if (TextUtils.isEmpty(familyName.getText().toString())){
            ConfirmDialog.newInstance()
                    .setMessage("请输入新名称")
                    .setMargin(60)
                    .setOutCancel(false)
                    .show(getSupportFragmentManager());
            return;
        }
        if (familyName.getText().toString().equals(family_name)){
            ConfirmDialog.newInstance()
                    .setMessage("名称没有修改")
                    .setMargin(60)
                    .setOutCancel(false)
                    .show(getSupportFragmentManager());
            return;
        }
        String encryptData= DES.encrypt(gson.toJson(getChangeFamilyNameBean()));
        ToastUtils.showLoadingToast(this);
        OkHttpUtils.postString().url(GlobalURL.BaseURL)
                .content(encryptData)
                .mediaType(MediaType.parse("application/json;charset=utf-8"))
                .build().execute(new MyCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.dismissLoadingToast();
                LogUtils.showe("====修改姓名的返回数据fail:"+e+":id:"+id);
            }

            @Override
            public void onResponse(String response, int id) {
                String decryptData=DES.decrypt(response);
                LogUtils.showe("====修改姓名的返回数据success:"+decryptData+":id:"+id);
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
                Intent change_familyname_to_person=new Intent();
                change_familyname_to_person.putExtra(Constant.family_name,familyName.getText().toString());
                setResult(Constant.SETTTING_FAMILY_NAME_TO_FAMILY_TAG,change_familyname_to_person);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                ToastUtils.Toast(this,"修改成功");
            }else{
                ToastUtils.Toast(this,head.optString(Constant.text));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initIntentData() {
        family_id = getIntent().getIntExtra(Constant.family_id,0);
        family_name = getIntent().getStringExtra(Constant.family_name);
        family_user_id = getIntent().getIntExtra(Constant.family_user_id,0);

        familyName.setText(family_name);
    }


    private SendOriginBean getChangeFamilyNameBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(120);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setFamily_id(family_id);
        body.setFamily_name(familyName.getText().toString());
        body.setUser_id(family_user_id+"");
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }
}
