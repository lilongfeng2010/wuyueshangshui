package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.adapter.mainUI.FamilyRecyclerAdapter;
import com.wuyueshangshui.yuanxinkangfu.bean.GetServiceObject.GetServiceOrigin;
import com.wuyueshangshui.yuanxinkangfu.bean.GetServiceObject.GetServiceUserBean;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 服务对象页面,标题是“家庭”
 */
public class ServiceObjectActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private List<String> avatarLists;//家庭成员的头像url
    private List<String> realNameLists;//家庭成员的真名
    private List<Integer> peopleIds;//服务对象ID
    private int family_id;//服务对象所在的家庭ID
    private FamilyRecyclerAdapter familyAdapter;
    private String family_name;
    private TextView familyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_object);

        avatarLists=new ArrayList<>();
        realNameLists=new ArrayList<>();
        peopleIds=new ArrayList<>();
        initView();
        initData();


    }

    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.all_title_text)).setText("选择服务对象");
        recyclerView = (RecyclerView) findViewById(R.id.testItem).findViewById(R.id.family_listitem_recycler);
        familyName = (TextView) findViewById(R.id.testItem).findViewById(R.id.family_listitem_name);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(manager);
        familyAdapter = new FamilyRecyclerAdapter(ServiceObjectActivity.this,avatarLists,realNameLists);
        recyclerView.setAdapter(familyAdapter);
        //点击回传数据
        familyAdapter.setOnItemClickListener(new FamilyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent serviceobject_to_orderIntent=new Intent();
                LogUtils.showe("=========回传的数据==="+peopleIds.get(position)+":family_id:"+family_id);
                serviceobject_to_orderIntent.putExtra(Constant.real_name,realNameLists.get(position));
                serviceobject_to_orderIntent.putExtra(Constant.people_id,peopleIds.get(position));
                serviceobject_to_orderIntent.putExtra(Constant.family_id,family_id);
                setResult(Constant.SERVICEOBJECT_TO_ORDER,serviceobject_to_orderIntent);
                ServiceObjectActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    private void initData() {
        String encryptData= DES.encrypt(gson.toJson(getServiceObjectBean()));
        ToastUtils.showLoadingToast(this);
        OkHttpUtils.postString().url(GlobalURL.BaseURL)
                .content(encryptData)
                .mediaType(MediaType.parse("application/json;charset=utf-8"))
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showe("=====服务对象的返回值fail======"+e);
                        ToastUtils.dismissLoadingToast();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String decryptData=DES.decrypt(response);
                        LogUtils.showe("=====服务对象的返回值success======"+decryptData);
                        ToastUtils.dismissLoadingToast();
                        GetServiceOrigin origin=gson.fromJson(decryptData, GetServiceOrigin.class);
                        Map<String, GetServiceUserBean> user= origin.getRoot().getBody().getFamily().getUsers();
                        //动态获取key和value
                        for (Map.Entry<String,GetServiceUserBean> pair:user.entrySet()){
                            avatarLists.add(pair.getValue().getAvatar());
                            realNameLists.add(pair.getValue().getRealname());
                            peopleIds.add(pair.getValue().getId());
                        }
                        family_id=origin.getRoot().getBody().getFamily().getId();
                        family_name = origin.getRoot().getBody().getFamily().getFamily_name();
                        familyName.setText(family_name);
                        familyAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_back:
                finish();
                //activity退出的动画
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }
    private SendOriginBean getServiceObjectBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(119);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setUser_id(SPUtils.getString(this,Constant.user_id,null));
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }




}
