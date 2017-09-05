package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.mine;

import android.content.Intent;
import android.support.constraint.solver.widgets.ConstraintAnchor;
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
 * 身份证号设置
 */
public class SettingIdentifyCardActivity extends BaseActivity implements View.OnClickListener {

    private EditText card_number;
    private ImageView delete;
    private LinearLayout complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_card);
        //初始化控件
        initView();
        //如果有身份证号，就显示
        initIdentifyNum();
        //添加监听
        initListener();
        //添加观察者
        initWatcher();
    }



    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.all_title_text)).setText("身份证号设置");
        card_number = (EditText) findViewById(R.id.identify_card_edit);
        delete = (ImageView) findViewById(R.id.identify_card_delete_1);
        complete = (LinearLayout) findViewById(R.id.identify_card_complete);
    }

    private void initIdentifyNum() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constant.identify_card_num))){
            card_number.setText(getIntent().getStringExtra(Constant.identify_card_num));
        }
    }

    private void initListener() {
        delete.setOnClickListener(this);
        complete.setOnClickListener(this);
    }

    private void initWatcher() {
        card_number.addTextChangedListener(new TextWatcher() {
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
            case R.id.identify_card_delete_1:
                card_number.setText("");
                break;
            case R.id.identify_card_complete:
                submitIdentifyCardNum();
                break;
        }
    }

    private void submitIdentifyCardNum() {
        if (TextUtils.isEmpty(card_number.getText().toString())||card_number.getText().toString().length()!=18){
            ToastUtils.Toast(this,"请输入您的完整的身份证号");
            return;
        }
        String encryptData= DES.encrypt(gson.toJson(getChangeIdentityNumberBean()));
        ToastUtils.showLoadingToast(this);
        OkHttpUtils.postString().url(GlobalURL.BaseURL)
                .content(encryptData)
                .mediaType(MediaType.parse("application/json;charset=utf-8"))
                .build().execute(new MyCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.dismissLoadingToast();
                LogUtils.showe("====修改身份证号的返回数据fail:"+e+":id:"+id);
            }

            @Override
            public void onResponse(String response, int id) {
                String decryptData=DES.decrypt(response);
                LogUtils.showe("====修改身份证号的返回数据success:"+decryptData+":id:"+id);
                returnData(decryptData);
            }
        });
    }
    // 用户性别：0未知 1男 2女
    private void returnData(String decryptData) {
        try {
            JSONObject origin = new JSONObject(decryptData);
            JSONObject root=origin.optJSONObject(Constant.root);
            JSONObject head=root.optJSONObject(Constant.head);
            ToastUtils.dismissLoadingToast();
            if (head.optInt(Constant.code)==0){
                Intent change_identifynum_to_person=new Intent();
                change_identifynum_to_person.putExtra(Constant.identify_card_num,card_number.getText().toString());

                if (!TextUtils.isEmpty(root.optJSONObject(Constant.body).optString(Constant.birthday))){
                    SPUtils.setString(this,Constant.birthday,root.optJSONObject(Constant.body).optString(Constant.birthday));
//                    change_identifynum_to_person.putExtra(Constant.birthday,root.optJSONObject(Constant.body).optString(Constant.birthday));
                }
                if (!TextUtils.isEmpty(root.optJSONObject(Constant.body).optString(Constant.gender))){
                    SPUtils.setString(this,Constant.gender,root.optJSONObject(Constant.body).optString(Constant.gender));
//                    change_identifynum_to_person.putExtra(Constant.gender,root.optJSONObject(Constant.body).optString(Constant.gender));
                }
                LogUtils.showe("====回传的数据==="+card_number.getText().toString());
                setResult(Constant.CHANGE_IDENTIFYNUM_TO_PERSON_TAG,change_identifynum_to_person);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                ToastUtils.Toast(this,"修改成功");
                SPUtils.setString(this,Constant.identify_card_num,card_number.getText().toString());
            }else{
                ToastUtils.Toast(SettingIdentifyCardActivity.this,head.optString(Constant.text));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private SendOriginBean getChangeIdentityNumberBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(117);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setUser_id(SPUtils.getString(this,Constant.user_id,null));
        body.setType(2);
        body.setId_card_no(card_number.getText().toString());
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }
}
