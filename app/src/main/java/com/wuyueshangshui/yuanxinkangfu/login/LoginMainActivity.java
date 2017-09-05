package com.wuyueshangshui.yuanxinkangfu.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.BaseActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.MyApplication;

/**
 * 登录注册页面
 */
public class LoginMainActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout wechat_login_btn;
    private LinearLayout phonenum_login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        initView();

        initListener();

        MyApplication.api.registerApp(MyApplication.APP_ID);
    }



    private void initView() {
        wechat_login_btn = (LinearLayout) findViewById(R.id.wechat_login_btn);
        phonenum_login_btn = (LinearLayout) findViewById(R.id.phonenum_login_btn);
    }

    private void initListener() {
        wechat_login_btn.setOnClickListener(this);
        phonenum_login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wechat_login_btn://微信登录按钮
                if (!MyApplication.api.isWXAppInstalled()){
                    Toast.makeText(this, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
                    return;
                }
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wuyueshangshui";
                MyApplication.api.sendReq(req);
                break;
            case R.id.phonenum_login_btn://手机号或者档案号登录
                Intent toLoginIntent=new Intent(this,LoginActivity.class);
                startActivity(toLoginIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }
}
