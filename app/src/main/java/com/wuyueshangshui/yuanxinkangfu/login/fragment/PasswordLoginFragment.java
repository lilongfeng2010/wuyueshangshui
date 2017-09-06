package com.wuyueshangshui.yuanxinkangfu.login.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendBodyBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendRootBean;
import com.wuyueshangshui.yuanxinkangfu.bean.UserBean;
import com.wuyueshangshui.yuanxinkangfu.bean.WXUserBean;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.common.GlobalURL;
import com.wuyueshangshui.yuanxinkangfu.login.RegisterActivity;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.MainActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.CommentMethod;
import com.wuyueshangshui.yuanxinkangfu.utils.InputCheck;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.CheckNet;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.MyCallBack;
import com.wuyueshangshui.yuanxinkangfu.utils.SPUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.DialogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.encrypt.DES;
import com.wuyueshangshui.yuanxinkangfu.wigdet.ActivityCollector;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by lilfi on 2017/8/22.
 * 密码登录页面
 */

public class PasswordLoginFragment extends Fragment {

    private EditText phoneEdit;
    private EditText passEdit;
    private ImageView deletephone;
    private ImageView deletepassword;
    private TextView forgetText;
    private TextView register;
    private LinearLayout login_linear;
    private Gson gson;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.passwordlogin,null);
        gson=new Gson();
        initView(view);
        initListener();
        initWatcher();
        return view;
    }

    private void initWatcher() {
        //对手机号输入框进行监听，有文字输入就让小叉号显示
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    deletephone.setVisibility(View.VISIBLE);
                }else{
                    deletephone.setVisibility(View.INVISIBLE);
                }
            }
        });
        //对密码输入框进行监听
        passEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    deletepassword.setVisibility(View.VISIBLE);
                }else{
                    deletepassword.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void initListener() {
        //点击清空输入的手机号
        deletephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneEdit.setText("");
            }
        });
        //点击清空输入的密码
        deletepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passEdit.setText("");
            }
        });
        //忘记密码
        forgetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetIntent=new Intent(getActivity(), RegisterActivity.class);
                forgetIntent.putExtra(Constant.registerTag,Constant.forgetPassword);
                getActivity().startActivity(forgetIntent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //登录
        login_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phoneEdit.getText().toString())||TextUtils.isEmpty(passEdit.getText().toString())){
                    ToastUtils.Toast(getActivity(),getActivity().getResources().getString(R.string.please_input_phone_or_password));
                    return;
                }
                boolean isPhoneNum=InputCheck.checkPhone(getActivity(),phoneEdit.getText().toString());
                if (!isPhoneNum) return;//判断手机号格式是否正确
                //判断网络是否连接
                boolean netIsConnect= CheckNet.netIsOk(getActivity());
                if(!netIsConnect){
                    ToastUtils.Toast(getActivity(),getResources().getString(R.string.no_network));
                }else{
                    String encryptData=DES.encrypt(gson.toJson(getBean()));
                    //旋转条，让用户等待
                    ToastUtils.showLoadingToast(getActivity());

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
                                    jsonMethod(decryptData);
                                }
                            });
                }

            }
        });
        //立即注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent=new Intent(getActivity(), RegisterActivity.class);
                registerIntent.putExtra(Constant.registerTag,Constant.register);
                getActivity().startActivity(registerIntent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void jsonMethod(String decryptData) {
        try {
            JSONObject origin = new JSONObject(decryptData);
            JSONObject root=origin.optJSONObject(Constant.root);
            JSONObject head=root.optJSONObject(Constant.head);
            ToastUtils.dismissLoadingToast();
            if (head.optInt(Constant.code)==0){
                CommentMethod.loginSPUtils(root,gson,getActivity());
                Intent toMainActivity=new Intent(getActivity(), MainActivity.class);
                startActivity(toMainActivity);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                ActivityCollector.finishAll();


                ToastUtils.Toast(getActivity(),"登录成功");
            }else{
                DialogUtils.onePosition(getActivity(),head.optString(Constant.text));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private SendOriginBean getBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(102);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setAccount(phoneEdit.getText().toString());
        body.setPass(passEdit.getText().toString());
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }

    private void initView(View view) {
        phoneEdit = (EditText) view.findViewById(R.id.password_login_phonenum);
        passEdit = (EditText) view.findViewById(R.id.password_login_password);
        deletephone = (ImageView) view.findViewById(R.id.password_login_delete_1);
        deletepassword = (ImageView) view.findViewById(R.id.password_login_delete_2);
        forgetText = (TextView) view.findViewById(R.id.password_login_forget);
        login_linear = (LinearLayout) view.findViewById(R.id.password_login_btn);
        register = (TextView) view.findViewById(R.id.password_login_register);
    }
}
