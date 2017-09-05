package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.wuyueshangshui.yuanxinkangfu.utils.SPUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.encrypt.DES;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 设置家庭地址
 */
public class SettingFamilyAddressActivity extends BaseActivity implements View.OnClickListener {

    private EditText community;
    private EditText address;
    private ImageView delete_1;
    private ImageView delete_2;
    private LinearLayout submit;
    private int family_id;
    private String community_intent;
    private String address_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_family_address);

        initView();

        initListener();

        initWatcher();

        initGetIntentData();

        initData();
    }



    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.all_title_text)).setText("设置家庭地址");
        community = (EditText) findViewById(R.id.setting_family_community_name);
        address = (EditText) findViewById(R.id.setting_family_address_edit);
        delete_1 = (ImageView) findViewById(R.id.setting_family_address_delete_1);
        delete_2 = (ImageView) findViewById(R.id.setting_family_address_delete_2);
        submit = (LinearLayout) findViewById(R.id.setting_family_address_submit);
    }

    private void initListener() {
        delete_1.setOnClickListener(this);
        delete_2.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void initWatcher() {
        community.addTextChangedListener(new TextWatcher() {
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
        address.addTextChangedListener(new TextWatcher() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.setting_family_address_delete_1:
                community.setText("");
                break;
            case R.id.setting_family_address_delete_2:
                address.setText("");
                break;
            case R.id.setting_family_address_submit:
                submitData();
                break;
        }
    }

    private void submitData() {
        if (TextUtils.isEmpty(community.getText().toString())||TextUtils.isEmpty(address.getText().toString())){
            ToastUtils.Toast(this,"填写内容不能为空");
            return;
        }
        String encryptData= DES.encrypt(gson.toJson(getChangeAddressBean()));
//        ToastUtils.showLoadingToast(this);
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

    private SendOriginBean getChangeAddressBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(121);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setFamily_id(Integer.parseInt(family_id+""));
        body.setCommunity_name(community_intent);
        body.setAddress(address_intent);
        body.setUser_id(SPUtils.getString(this,Constant.user_id,null));
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }

    private void returnData(String decryptData) {
        try {
            JSONObject origin = new JSONObject(decryptData);
            JSONObject root=origin.optJSONObject(Constant.root);
            JSONObject head=root.optJSONObject(Constant.head);
            ToastUtils.dismissLoadingToast();
            if (head.optInt(Constant.code)==0){
                Intent change_familyaddress_to_person=new Intent();
                change_familyaddress_to_person.putExtra(Constant.community_name,community.getText().toString());
                change_familyaddress_to_person.putExtra(Constant.address,address.getText().toString());
                setResult(Constant.SETTTING_FAMILY_ADDRESS_TO_FAMILY_TAG,change_familyaddress_to_person);
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


    private void initGetIntentData() {
        family_id = getIntent().getIntExtra(Constant.family_id,0);
        community_intent = getIntent().getStringExtra(Constant.community_name);
        address_intent = getIntent().getStringExtra(Constant.address);
    }


    private void initData() {
        community.setText(community_intent);
        address.setText(address_intent);
    }
}
