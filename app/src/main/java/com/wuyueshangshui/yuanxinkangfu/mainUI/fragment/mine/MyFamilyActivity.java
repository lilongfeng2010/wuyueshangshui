package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.wuyueshangshui.yuanxinkangfu.wigdet.ListItem2;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 我的家庭
 */
public class MyFamilyActivity extends BaseActivity implements View.OnClickListener {
    private static final int GridLayoutNumber=5;//recyclerview中每一行的条目个数
    private List<String> mAvatarLists=new ArrayList<>();//家庭成员的头像url
    private List<String> mRealNameLists=new ArrayList<>();//家庭成员的真名
    private RecyclerView recyclerView;
    private ListItem2 familyNameItem;
    private ListItem2 familyAddressItem;
    private ListItem2 otherFamilyItem;
    private TextView familyName;
    private TextView familyAddress;
    private TextView otherFamily;
    private FamilyRecyclerAdapter familyAdapter;
    private int family_id;
    private String family_name;
    private String address;
    private String community_name;
    private int family_use_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_family);

        initView();

        initListener();

        initData();
    }

    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.all_title_text)).setText("我的家庭");
        recyclerView = (RecyclerView) findViewById(R.id.my_family_recyclerView);
        GridLayoutManager manager=new GridLayoutManager(this,GridLayoutNumber);
        recyclerView.setLayoutManager(manager);

        familyAdapter = new FamilyRecyclerAdapter(this,mAvatarLists,mRealNameLists);
        recyclerView.setAdapter(familyAdapter);

        familyNameItem = (ListItem2) findViewById(R.id.my_family_familyName);
        familyName = (TextView) familyNameItem.findViewById(R.id.list_item2_content);

        familyAddressItem = (ListItem2) findViewById(R.id.my_family_address);
        familyAddress = (TextView) familyAddressItem.findViewById(R.id.list_item2_content);

        otherFamilyItem = (ListItem2) findViewById(R.id.my_family_otherFamily);
        otherFamily = (TextView) otherFamilyItem.findViewById(R.id.list_item2_content);
    }
    private void initListener() {
        familyNameItem.setOnClickListener(this);
        familyAddressItem.setOnClickListener(this);
        otherFamilyItem.setOnClickListener(this);
    }


    private void initData() {
        ToastUtils.showLoadingToast(this);
        String encryptData= DES.encrypt(gson.toJson(getMyFamilyBean()));
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
                            mAvatarLists.add(pair.getValue().getAvatar());
                            mRealNameLists.add(pair.getValue().getRealname());
                        }
                        family_id=origin.getRoot().getBody().getFamily().getId();
                        family_use_id = origin.getRoot().getBody().getFamily().getUser_id();
                        family_name = origin.getRoot().getBody().getFamily().getFamily_name();
                        community_name = origin.getRoot().getBody().getFamily().getCommunity_name();
                        address = origin.getRoot().getBody().getFamily().getAddress();
                        familyAdapter.notifyDataSetChanged();
                        familyName.setText(family_name);
                        familyAddress.setText(community_name+address);
                    }
                });
    }

    private Object getMyFamilyBean() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.my_family_familyName:
                Intent myFamilyToSettingName=new Intent(this,SettingFamilyNameActivity.class);
                myFamilyToSettingName.putExtra(Constant.family_id,family_id);
                myFamilyToSettingName.putExtra(Constant.family_name,family_name);
                myFamilyToSettingName.putExtra(Constant.family_user_id,family_use_id);
                startActivityForResult(myFamilyToSettingName,Constant.FAMILY_TO_SETTTING_FAMILY_NAME_TAG);
                break;
            case R.id.my_family_address:
                Intent myFamilyToSettingAddress=new Intent(this,SettingFamilyAddressActivity.class);
                myFamilyToSettingAddress.putExtra(Constant.family_id,family_id);
                myFamilyToSettingAddress.putExtra(Constant.community_name,community_name);
                myFamilyToSettingAddress.putExtra(Constant.address,address);
                startActivityForResult(myFamilyToSettingAddress,Constant.FAMILY_TO_SETTTING_FAMILY_ADDRESS_TAG);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Constant.FAMILY_TO_SETTTING_FAMILY_NAME_TAG && resultCode==Constant.SETTTING_FAMILY_NAME_TO_FAMILY_TAG){
            if (data!=null){
                familyName.setText(data.getStringExtra(Constant.family_name));
            }
        }else if (requestCode==Constant.FAMILY_TO_SETTTING_FAMILY_ADDRESS_TAG && resultCode==Constant.SETTTING_FAMILY_ADDRESS_TO_FAMILY_TAG){
            if (data!=null){
                familyAddress.setText(data.getStringExtra(Constant.community_name)+data.getStringExtra(Constant.address));
            }
        }
    }
}
