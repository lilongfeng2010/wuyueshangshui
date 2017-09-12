package com.wuyueshangshui.yuanxinkangfu.login;

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
import com.wuyueshangshui.yuanxinkangfu.mainactivity.MainActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.CommentMethod;
import com.wuyueshangshui.yuanxinkangfu.utils.InputCheck;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.CheckNet;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.MyCallBack;
import com.wuyueshangshui.yuanxinkangfu.utils.NiceDialog.ConfirmDialog;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.encrypt.DES;
import com.wuyueshangshui.yuanxinkangfu.wigdet.ActivityCollector;
import com.wuyueshangshui.yuanxinkangfu.wigdet.TimeCount;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 注册页面,和忘记密码页面相同，公用一个页面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText phone;//注册页面的手机号输入框
    private EditText checknum;//验证码输入框
    private EditText password;//密码输入框
    private ImageView delete_1;//删除  手机号输入框  文字的小叉号
    private ImageView delete_2;//删除  验证码输入框  文字的小叉号
    private ImageView delete_3;//删除  密码输入框  文字的小叉号
    private LinearLayout complete;//注册的按钮
    private TextView getChecknum;//获取验证码的按钮
    private TimeCount time;//倒计时器
    private String registerTag;//通过这个标记，来判断是“注册页面”还是“忘记密码”页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerTag = getIntent().getStringExtra(Constant.registerTag);
        initView();
        //按钮的监听
        initListener();
        //输入框的监听
        initWatcher();
        //倒计时的对象
        time = new TimeCount(60000,1000,this,getChecknum);
    }

    private void initView() {
        if (registerTag.equals(Constant.register)){
            ((TextView)findViewById(R.id.all_title_text)).setText("注册");
            ((TextView) findViewById(R.id.forgetpassword_complete_text)).setText("注册");
        }else{
            ((TextView)findViewById(R.id.all_title_text)).setText("忘记密码");
            ((TextView) findViewById(R.id.forgetpassword_complete_text)).setText("完成");
        }
        findViewById(R.id.all_back).setOnClickListener(this);
        phone = (EditText) findViewById(R.id.forgetpassword_phonenum);
        checknum = (EditText) findViewById(R.id.forgetpassword_mess_checknum);
        password = (EditText) findViewById(R.id.forgetpassword_password);
        delete_1 = (ImageView) findViewById(R.id.forgetpassword_delete_1);
        delete_2 = (ImageView) findViewById(R.id.forgetpassword_delete_2);
        delete_3 = (ImageView) findViewById(R.id.forgetpassword_delete_3);
        getChecknum = (TextView) findViewById(R.id.forgetpassword_get_checknum);
        complete = (LinearLayout) findViewById(R.id.forgetpassword_complete);
    }

    private void initListener() {
        //清空手机号输入框
        delete_1.setOnClickListener(this);
        //清空短信验证码输入框
        delete_2.setOnClickListener(this);
        //清空密码输入框
        delete_3.setOnClickListener(this);
        //点击获取验证码
        getChecknum.setOnClickListener(this);
        //点击注册
        complete.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.forgetpassword_delete_1:
                phone.setText("");
                break;
            case R.id.forgetpassword_delete_2:
                checknum.setText("");
                break;
            case R.id.forgetpassword_delete_3:
                password.setText("");
                break;
            case R.id.forgetpassword_get_checknum:
                getCheckNumberMethod();
                break;
            case R.id.forgetpassword_complete:
                completeMethod();
                break;
        }
    }

    private void completeMethod() {
        String encryptData=DES.encrypt(gson.toJson(getRegisterBean()));
        OkHttpUtils.postString().url(GlobalURL.BaseURL)
                .content(encryptData)
                .mediaType(MediaType.parse("application/json;charset=utf-8"))
                .build().execute(new MyCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.showe("====注册的返回数据fail:"+e+":id:"+id);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.showe("====注册的返回数据success:"+response+":id:"+id);
                String decryptData=DES.decrypt(response);
                jsonMethod(decryptData);

            }
        });
    }

    private void getCheckNumberMethod() {
        //手机号不能为空
        if (TextUtils.isEmpty(phone.getText().toString())){
//            DialogUtils.onePosition(RegisterActivity.this,getResources().getString(R.string.phone_is_not_empty));
            ConfirmDialog.newInstance()
                    .setMessage(getResources().getString(R.string.phone_is_not_empty))
                    .setMargin(60)
                    .setOutCancel(false)
                    .show(getSupportFragmentManager());

            return;
        }
        //校验手机号
        boolean isOk=InputCheck.checkPhone(RegisterActivity.this,phone.getText().toString());
        if (!isOk) return;

        //判断网络是否连接
        boolean netIsConnect=CheckNet.netIsOk(RegisterActivity.this);
        if (!netIsConnect){
            ToastUtils.Toast(RegisterActivity.this,getResources().getString(R.string.no_network));
        }else{
            time.start();
            ToastUtils.showLoadingToast(this);
            String encryptContent=DES.encrypt(gson.toJson(getCheckNumBean()));
            OkHttpUtils.postString().url(GlobalURL.BaseURL)
                    .content(encryptContent)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.showe("====验证码的返回数据fail:"+e+":id:"+id);
                            ToastUtils.dismissLoadingToast();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String decryptData=DES.decrypt(response);
                            ToastUtils.dismissLoadingToast();
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

    private void jsonMethod(String decryptData) {
        try {
            JSONObject origin = new JSONObject(decryptData);
            JSONObject root=origin.optJSONObject(Constant.root);
            JSONObject head=root.optJSONObject(Constant.head);
            if (head.optInt(Constant.code)==0){
                CommentMethod.loginSPUtils(root,gson,this);
                Intent toMainActivity=new Intent(RegisterActivity.this, MainActivity.class);
                ToastUtils.Toast(this,"操作成功");
                startActivity(toMainActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                ActivityCollector.finishAll();
            }else{
                ToastUtils.Toast(this,head.optString(Constant.text));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private SendOriginBean getRegisterBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        if (registerTag.equals(Constant.register)){
            head.setAction(109);
        }else if(registerTag.equals(Constant.forgetPassword)){
            head.setAction(107);
        }
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setMobile(phone.getText().toString());
        body.setCode(checknum.getText().toString());
        body.setPass(password.getText().toString());
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }

    private void initWatcher() {
        phone.addTextChangedListener(new TextWatcher() {
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
        checknum.addTextChangedListener(new TextWatcher() {
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
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    delete_3.setVisibility(View.VISIBLE);
                }else{
                    delete_3.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public SendOriginBean getCheckNumBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        if (registerTag.equals(Constant.register)){
            head.setAction(108);
        }else if(registerTag.equals(Constant.forgetPassword)){
            head.setAction(106);
        }
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setMobile(phone.getText().toString());
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }

}
