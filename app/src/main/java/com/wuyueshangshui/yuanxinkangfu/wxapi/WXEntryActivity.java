package com.wuyueshangshui.yuanxinkangfu.wxapi;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.bean.GetBean.GetOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendBodyBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendRootBean;
import com.wuyueshangshui.yuanxinkangfu.bean.WXUserBean;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.common.GlobalURL;
import com.wuyueshangshui.yuanxinkangfu.login.WeChatLoginActivity;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.BaseActivity;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.MainActivity;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.MyApplication;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 微信登录回调信息
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler{
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private JSONObject getWXUserBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果没回调onResp，八成是这句没有写
        MyApplication.api.handleIntent(getIntent(),this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MyApplication.api.handleIntent(intent,this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {


    }
    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(final BaseResp baseResp) {
        LogUtils.showi("==错误码:=="+baseResp.errStr+"=错误码:=="+baseResp.errCode+"::"+baseResp.openId);
        switch (baseResp.errCode){
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                if (baseResp.getType()==RETURN_MSG_TYPE_LOGIN){
                    ToastUtils.Toast(this,"登录失败");
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_OK://用户同意
                if (baseResp.getType()==RETURN_MSG_TYPE_LOGIN){
                    //拿到了微信返回的code
                    String code=((SendAuth.Resp)baseResp).code;
                    LogUtils.showe("========code:"+code+":=state==:"+((SendAuth.Resp)baseResp).state+":country:"+((SendAuth.Resp)baseResp).country+":lang:"+((SendAuth.Resp)baseResp).lang);
                    final SendOriginBean origin=new SendOriginBean();
                    SendRootBean root=new SendRootBean();
                    SendHeadBean head=new SendHeadBean();
                    head.setClientid(Constant.clientid);
                    head.setAction(103);
                    head.setTimestamp(System.currentTimeMillis()+"");

                    SendBodyBean body=new SendBodyBean();
                    body.setCode(code);

                    root.setHead(head);
                    root.setBody(body);
                    origin.setRoot(root);
                    String jsonContent=gson.toJson(origin);
                    LogUtils.showe("===传入的json数据=="+jsonContent);
                    String encryptContent=DES.encrypt(jsonContent);
                    LogUtils.showe("===加密后的json数据=="+encryptContent);
                    OkHttpUtils.postString().url(GlobalURL.BaseURL)
                            .content(encryptContent)
                            .mediaType(MediaType.parse("application/json; charset=utf-8"))
                            .build().execute(new MyCallBack() {

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String decryptData=DES.decrypt(response);
                            LogUtils.showe("====微信授权后的返回的数据==:"+decryptData);
//                            ToastUtils.Toast(WXEntryActivity.this,"登录成功");
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
                                            SPUtils.setString(WXEntryActivity.this,Constant.wechat_headimg_url,headImgUrl);
                                        }
                                    }
                                    //保存用户微信的unionid
                                    if (body.optJSONObject(Constant.wx_user).optString(Constant.unionid)!=null){
                                        String unionid=body.optJSONObject(Constant.wx_user).optString(Constant.unionid);
                                        if (!TextUtils.isEmpty(unionid)){
                                            SPUtils.setString(WXEntryActivity.this,Constant.wechat_unionid,unionid);
                                        }
                                    }
                                    getWXUserBean = body.optJSONObject(Constant.wx_user);

                                    if (body.optJSONObject(Constant.user)==null){
                                        //如果没有绑定过，就进入绑定微信页面
                                        Intent toWeChatLoginIntent=new Intent(WXEntryActivity.this,WeChatLoginActivity.class);
                                        SPUtils.setString(WXEntryActivity.this,Constant.wx_user,getWXUserBean.toString());
                                        startActivity(toWeChatLoginIntent);
                                    }else{
                                        //如果绑定过了直接进入主页面
                                        CommentMethod.loginSPUtils(root,gson,WXEntryActivity.this);
                                        Intent toMainIntent=new Intent(WXEntryActivity.this,MainActivity.class);
                                        startActivity(toMainIntent);
                                        ActivityCollector.finishAll();
                                    }

                                }else{
                                    ToastUtils.Toast(WXEntryActivity.this,head.optString(Constant.text));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
        }

    }

}
