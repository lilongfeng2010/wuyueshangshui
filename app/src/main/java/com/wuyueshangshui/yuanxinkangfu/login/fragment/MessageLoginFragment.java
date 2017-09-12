package com.wuyueshangshui.yuanxinkangfu.login.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.bean.GetBean.GetOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendBodyBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendRootBean;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.common.GlobalURL;
import com.wuyueshangshui.yuanxinkangfu.login.RegisterActivity;
import com.wuyueshangshui.yuanxinkangfu.login.WeChatLoginActivity;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.MainActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.CommentMethod;
import com.wuyueshangshui.yuanxinkangfu.utils.InputCheck;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.CheckNet;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.MyCallBack;
import com.wuyueshangshui.yuanxinkangfu.utils.NiceDialog.ConfirmDialog;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.DialogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.encrypt.DES;
import com.wuyueshangshui.yuanxinkangfu.wigdet.ActivityCollector;
import com.wuyueshangshui.yuanxinkangfu.wigdet.TimeCount;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by lilfi on 2017/8/22.
 * 短信登录页面
 */

public class MessageLoginFragment extends Fragment {

    private EditText phone;
    private EditText checknum;
    private ImageView delete_1;
    private ImageView delete_2;
    private TextView getCheckNum;
    private LinearLayout login;
    private TextView register;
    private TimeCount time;
    private Gson gson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.messagelogin,null);
        initView(view);
        time = new TimeCount(60000,1000,getActivity(),getCheckNum);
        gson = new Gson();
        initListener();
        initWatcher();
        return view;
    }

    private void initView(View view) {
        phone = (EditText) view.findViewById(R.id.messagelogin_phonenum);
        checknum = (EditText) view.findViewById(R.id.messagelogin_checknum);
        delete_1 = (ImageView) view.findViewById(R.id.messagelogin_delete_1);
        delete_2 = (ImageView) view.findViewById(R.id.messagelogin_delete_2);
        getCheckNum = (TextView) view.findViewById(R.id.messagelogin_getchecknum);
        login = (LinearLayout) view.findViewById(R.id.messagelogin_login);
        register = (TextView) view.findViewById(R.id.messagelogin_register);
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

    private void getLoginMethod() {
        if (TextUtils.isEmpty(phone.getText().toString())||TextUtils.isEmpty(checknum.getText().toString())){
            ToastUtils.Toast(getActivity(),getActivity().getResources().getString(R.string.please_input_phone_or_checknum));
            return;
        }
        //校验手机号
        boolean isOk= InputCheck.checkPhone(getActivity(),phone.getText().toString());
        if (!isOk) return;
        //判断网络是否连接
        boolean netIsConnect= CheckNet.netIsOk(getActivity());
        if (!netIsConnect) {
            ToastUtils.Toast(getActivity(), getResources().getString(R.string.no_network));
        }else{
            String encryptData=DES.encrypt(gson.toJson(getLoginBean()));
            ToastUtils.showLoadingToast(getActivity());
            OkHttpUtils.postString().url(GlobalURL.BaseURL)
                    .content(encryptData)
                    .mediaType(MediaType.parse("application/json;charset=utf-8"))
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.showe("===短信登录的返回信息fail===="+e);
                            ToastUtils.dismissLoadingToast();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String decryptData=DES.decrypt(response);
                            LogUtils.showe("===短信登录的返回信息success:"+decryptData);
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
                                    ToastUtils.Toast(getActivity(),head.optString(Constant.text));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }
    }

    private void getCheckNumMethod() {
        //手机号不能为空
        if (TextUtils.isEmpty(phone.getText().toString())){
//            DialogUtils.onePosition(getActivity(),getResources().getString(R.string.phone_is_not_empty));
            ConfirmDialog.newInstance()
                    .setMessage(getResources().getString(R.string.phone_is_not_empty))
                    .setMargin(60)
                    .setOutCancel(false)
                    .show(getActivity().getSupportFragmentManager());
            return;
        }
        //校验手机号
        boolean isOk= InputCheck.checkPhone(getActivity(),phone.getText().toString());
        if (!isOk) return;
        //判断网络是否连接
        boolean netIsConnect= CheckNet.netIsOk(getActivity());
        if (!netIsConnect){
            ToastUtils.Toast(getActivity(),getResources().getString(R.string.no_network));
        }else{
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
                                        ToastUtils.Toast(getActivity(),head.optString(Constant.text));
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

    private SendOriginBean getLoginBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(105);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setMobile(phone.getText().toString());
        body.setCode(checknum.getText().toString());
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }

    private SendOriginBean getBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(104);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setMobile(phone.getText().toString());
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }


}
