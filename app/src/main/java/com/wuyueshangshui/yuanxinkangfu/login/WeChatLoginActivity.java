package com.wuyueshangshui.yuanxinkangfu.login;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.bean.GetBean.GetOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendBodyBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendRootBean;
import com.wuyueshangshui.yuanxinkangfu.bean.WXUserBean;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.common.GlobalURL;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.BaseActivity;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.MainActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.CommentMethod;
import com.wuyueshangshui.yuanxinkangfu.utils.InputCheck;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.CheckNet;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.MyCallBack;
import com.wuyueshangshui.yuanxinkangfu.utils.SPUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.DialogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.encrypt.DES;
import com.wuyueshangshui.yuanxinkangfu.wigdet.ActivityCollector;
import com.wuyueshangshui.yuanxinkangfu.wigdet.TimeCount;
import com.wuyueshangshui.yuanxinkangfu.wxapi.WXEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 微信授权后注册页面
 */
public class WeChatLoginActivity extends BaseActivity {

    private CircleImageView circleHead;
    private EditText phone;
    private EditText checknum;
    private ImageView delete_1;
    private ImageView delete_2;
    private TextView getCheckNum;
    private LinearLayout login;
    private TimeCount time;
    private WXUserBean wxUserBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat_login);

        initView();
        initHeadImage();
        time = new TimeCount(60000,1000,this,getCheckNum);
        initListener();
        initWatcher();
        initWXData();
    }

    private void initWXData() {
        String wx_userData=SPUtils.getString(this,Constant.wx_user,null);
        wxUserBean = gson.fromJson(wx_userData, WXUserBean.class);
    }


    private void initHeadImage() {
        if (SPUtils.getString(this, Constant.wechat_headimg_url,null)!=null){
            String headUrl= SPUtils.getString(this, Constant.wechat_headimg_url,null);
            Picasso.with(this).load(headUrl).placeholder(R.color.graybackground).error(R.color.graybackground).into(circleHead);
        }
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
                    delete_2.setVisibility(View.INVISIBLE);
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
    }

    private void initListener() {
        delete_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.setText("");
            }
        });
        delete_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checknum.setText("");
            }
        });
        getCheckNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCheckNumMethod();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoginMethod();
            }
        });
    }

    private void getLoginMethod() {
        if (TextUtils.isEmpty(phone.getText().toString())||TextUtils.isEmpty(checknum.getText().toString())){
            ToastUtils.Toast(WeChatLoginActivity.this,WeChatLoginActivity.this.getResources().getString(R.string.please_input_phone_or_password));
            return;
        }
        boolean isPhoneNum=InputCheck.checkPhone(WeChatLoginActivity.this,phone.getText().toString());
        if (!isPhoneNum) return;//判断手机号格式是否正确
        //判断网络是否连接
        boolean netIsConnect= CheckNet.netIsOk(WeChatLoginActivity.this);
        if(!netIsConnect){
            ToastUtils.Toast(WeChatLoginActivity.this,getResources().getString(R.string.no_network));
        }else{
            String encryptData=DES.encrypt(gson.toJson(getLoginBean()));
            OkHttpUtils.postString().url(GlobalURL.BaseURL)
                    .content(encryptData)
                    .mediaType(MediaType.parse("application/json;charset=utf-8"))
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.showe("===登录fail=="+e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String decryptData=DES.decrypt(response);
                            LogUtils.showe("===登录success=="+decryptData);
                            LoginReturnData(decryptData);

                        }
                    });

        }
    }

    private void LoginReturnData(String decryptData) {
        try {
            JSONObject origin=new JSONObject(decryptData);
            JSONObject root=origin.optJSONObject(Constant.root);
            JSONObject head=root.optJSONObject(Constant.head);
            JSONObject body=root.optJSONObject(Constant.body);
            if (head.optInt(Constant.code)==0){
                //保存用户微信头像url
                if (body.optJSONObject(Constant.wx_user).optString(Constant.headimgurl)!=null&& body.optJSONObject(Constant.wx_user).optString(Constant.headimgurl)!="/0"){
                    String headImgUrl=body.optJSONObject(Constant.wx_user).optString(Constant.headimgurl);
                    if (!TextUtils.isEmpty(headImgUrl)){
                        SPUtils.setString(this,Constant.wechat_headimg_url,headImgUrl);
                    }
                }
                //保存用户微信的unionid
                if (body.optJSONObject(Constant.wx_user).optString(Constant.unionid)!=null){
                    String unionid=body.optJSONObject(Constant.wx_user).optString(Constant.unionid);
                    if (!TextUtils.isEmpty(unionid)){
                        SPUtils.setString(this,Constant.wechat_unionid,unionid);
                    }
                }
                CommentMethod.loginSPUtils(root,gson,this);
                Intent toMainIntent=new Intent(this,MainActivity.class);
                startActivity(toMainIntent);
                ActivityCollector.finishAll();

            }else{
                ToastUtils.Toast(this,head.optString(Constant.text));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCheckNumMethod() {
        //手机号不能为空
        if (TextUtils.isEmpty(phone.getText().toString())){
            DialogUtils.onePosition(WeChatLoginActivity.this,getResources().getString(R.string.phone_is_not_empty));
            return;
        }
        //校验手机号
        boolean isOk= InputCheck.checkPhone(WeChatLoginActivity.this,phone.getText().toString());
        if (!isOk) return;
        //判断网络是否连接
        boolean netIsConnect= CheckNet.netIsOk(WeChatLoginActivity.this);
        if (!netIsConnect){
            ToastUtils.Toast(WeChatLoginActivity.this,getResources().getString(R.string.no_network));
        }else{
            //启动倒计时
            time.start();
            String encryptContent= DES.encrypt(gson.toJson(getBean()));
            OkHttpUtils.postString().url(GlobalURL.BaseURL)
                    .content(encryptContent)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.showe("====短信登录验证码的返回数据fail:"+e+":id:"+id);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.showe("====短信登录验证码的返回数据success:"+response+":id:"+id);
                            String decryptData=DES.decrypt(response);
                            LogUtils.showe("====验证码的返回数据解密后的success:"+decryptData+":id:"+id);
                            try{
                                if (!TextUtils.isEmpty(decryptData)){
                                    try {
                                        JSONObject origin=new JSONObject(decryptData);
                                        JSONObject root=origin.optJSONObject(Constant.root);
                                        JSONObject head=root.optJSONObject(Constant.head);
                                        ToastUtils.Toast(WeChatLoginActivity.this,head.optString(Constant.text));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }catch (IllegalStateException e){
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.all_title_text)).setText("微信绑定");
        circleHead = (CircleImageView) findViewById(R.id.wechatlogin_circle_head);
        phone = (EditText) findViewById(R.id.wechatlogin_phonenum);
        checknum = (EditText) findViewById(R.id.wechatlogin_checknum);
        delete_1 = (ImageView) findViewById(R.id.wechatlogin_delete_1);
        delete_2 = (ImageView) findViewById(R.id.wechatlogin_delete_2);
        getCheckNum = (TextView) findViewById(R.id.wechatlogin_getchecknum);
        login = (LinearLayout) findViewById(R.id.wechatlogin_login);

    }

    private SendOriginBean getBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(110);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setMobile(phone.getText().toString());
        body.setUnionid(SPUtils.getString(this,Constant.wechat_unionid,null));
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }

    private SendOriginBean getLoginBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(111);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setMobile(phone.getText().toString());
        body.setWx_user(wxUserBean);
        body.setCode(checknum.getText().toString());
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }
}
