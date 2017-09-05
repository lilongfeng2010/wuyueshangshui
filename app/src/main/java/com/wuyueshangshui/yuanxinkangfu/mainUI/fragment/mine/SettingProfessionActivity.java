package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.mine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendBodyBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendRootBean;
import com.wuyueshangshui.yuanxinkangfu.bean.UserBean;
import com.wuyueshangshui.yuanxinkangfu.bean.WXUserBean;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.common.GlobalURL;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.BaseActivity;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.MainActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.MyCallBack;
import com.wuyueshangshui.yuanxinkangfu.utils.SPUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.encrypt.DES;
import com.wuyueshangshui.yuanxinkangfu.wigdet.ActivityCollector;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 设置职业页面
 */
public class SettingProfessionActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    private RadioButton radio_1;
    private RadioButton radio_2;
    private RadioButton radio_3;
    private RadioButton radio_4;
    private RadioButton radio_5;
    private RadioButton radio_6;
    private RadioButton radio_7;
    private RadioButton radio_8;
    private String selectJob;//选择好的职业

    //这个标记是为了判断如果有职业，进入这个页面，直接显示勾选过的该职业，不跳回原页面。如果不理解，可以把这个注释掉看看效果。
    private boolean tag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profession);
        //初始化控件
        initView();
        //如果已经选择了职业，就让该职业选中
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constant.job))){
            initSelect();
        }
        //设置监听
        initListener();

    }

    private void initSelect() {
        tag=false;
        selectJob=getIntent().getStringExtra(Constant.job);
        switch (selectJob){
            case "政府机关公务员":
                radio_1.setChecked(true);
                break;
            case "学校教师":
                radio_2.setChecked(true);
                break;
            case "企业白领":
                radio_3.setChecked(true);
                break;
            case "服务业从业者":
                radio_4.setChecked(true);
                break;
            case "农、林、牧、渔从业者":
                radio_5.setChecked(true);
                break;
            case "生产、运输设备操作者":
                radio_6.setChecked(true);
                break;
            case "军人":
                radio_7.setChecked(true);
                break;
            case "其他":
                radio_8.setChecked(true);
                break;
        }
    }

    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.all_title_text)).setText("设置职业");
        radioGroup = (RadioGroup) findViewById(R.id.profession_RadioGroup);
        radio_1 = (RadioButton) findViewById(R.id.radio_01);
        radio_2 = (RadioButton) findViewById(R.id.radio_02);
        radio_3 = (RadioButton) findViewById(R.id.radio_03);
        radio_4 = (RadioButton) findViewById(R.id.radio_04);
        radio_5 = (RadioButton) findViewById(R.id.radio_05);
        radio_6 = (RadioButton) findViewById(R.id.radio_06);
        radio_7 = (RadioButton) findViewById(R.id.radio_07);
        radio_8 = (RadioButton) findViewById(R.id.radio_08);
    }

    private void initListener() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        tag=true;
        switch (checkedId){
            case R.id.radio_01:
                selectJob=radio_1.getText().toString();
                break;
            case R.id.radio_02:
                selectJob=radio_2.getText().toString();
                break;
            case R.id.radio_03:
                selectJob=radio_3.getText().toString();
                break;
            case R.id.radio_04:
                selectJob=radio_4.getText().toString();
                break;
            case R.id.radio_05:
                selectJob=radio_5.getText().toString();
                break;
            case R.id.radio_06:
                selectJob=radio_6.getText().toString();
                break;
            case R.id.radio_07:
                selectJob=radio_7.getText().toString();
                break;
            case R.id.radio_08:
                selectJob=radio_8.getText().toString();
                break;
        }
        if (tag){
            submitInformation();
        }
    }

    private void submitInformation() {
        String encryptData= DES.encrypt(gson.toJson(getProfessionBean()));
        ToastUtils.showLoadingToast(this);
        OkHttpUtils.postString().url(GlobalURL.BaseURL)
                .content(encryptData)
                .mediaType(MediaType.parse("application/json;charset=utf-8"))
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.dismissLoadingToast();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        String decryptData=DES.decrypt(response);
                        LogUtils.showe("====修改职业的返回数据success:"+decryptData+":id:"+id+"职业："+selectJob);
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
                Intent profession_to_person=new Intent();
                profession_to_person.putExtra(Constant.job,selectJob);
                setResult(Constant.PROFESSION_TO_PERSON_TAG,profession_to_person);
                ToastUtils.Toast(this,"修改成功");
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                SPUtils.setString(this,Constant.job,selectJob);
            }else{
                ToastUtils.Toast(this,head.optString(Constant.text));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private SendOriginBean getProfessionBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(117);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setUser_id(SPUtils.getString(this,Constant.user_id,null));
        body.setType(3);
        body.setJob(selectJob);
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;

    }
}
