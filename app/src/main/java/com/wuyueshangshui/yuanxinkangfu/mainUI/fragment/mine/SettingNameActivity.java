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
import com.wuyueshangshui.yuanxinkangfu.EventBus.MineNameBus;
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

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 设置姓名页面
 */
public class SettingNameActivity extends BaseActivity implements View.OnClickListener {

    private EditText name;
    private ImageView delete;
    private LinearLayout submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_name);
        //初始化控件
        initView();
        //如果已经有姓名，就填入输入框
        initName();
        //设置监听
        initListener();
        //设置观察者
        initWatcher();
    }

    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.all_title_text)).setText("设置姓名");
        name = (EditText) findViewById(R.id.setting_name_edit);
        delete = (ImageView) findViewById(R.id.setting_name_edit_delete_1);
        submit = (LinearLayout) findViewById(R.id.setting_name_submit);
    }

    private void initName() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constant.name))){
            name.setText(getIntent().getStringExtra(Constant.name));
        }
    }

    private void initListener() {
        delete.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void initWatcher() {
        name.addTextChangedListener(new TextWatcher() {
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
            case R.id.setting_name_edit_delete_1:
                name.setText("");
                break;
            case R.id.setting_name_submit:
                submitName();
                break;
        }
    }

    private void submitName() {
        if (TextUtils.isEmpty(name.getText().toString())){
            ToastUtils.Toast(this,"请输入您的姓名");
            return;
        }
        String encryptData= DES.encrypt(gson.toJson(getChangeNameBean()));
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
                Intent change_name_to_person=new Intent();
                change_name_to_person.putExtra(Constant.name,name.getText().toString());
                setResult(Constant.CHANGE_NAME_TO_PERSON_TAG,change_name_to_person);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                EventBus.getDefault().post(new MineNameBus(name.getText().toString()));
                ToastUtils.Toast(this,"修改成功");
                SPUtils.setString(this,Constant.real_name,name.getText().toString());
            }else{
                ToastUtils.Toast(this,head.optString(Constant.text));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private SendOriginBean getChangeNameBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(117);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setUser_id(SPUtils.getString(this,Constant.user_id,null));
        body.setType(1);
        body.setName(name.getText().toString());
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }
}
