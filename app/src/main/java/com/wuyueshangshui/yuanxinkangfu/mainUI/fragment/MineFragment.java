package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuyueshangshui.yuanxinkangfu.EventBus.MineHeadImageBus;
import com.wuyueshangshui.yuanxinkangfu.EventBus.MineNameBus;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.mine.HealthyDocumentActivity;
import com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.mine.MyFamilyActivity;
import com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.mine.PersonInformationActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.SPUtils;
import com.wuyueshangshui.yuanxinkangfu.wigdet.ListItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lilfi on 2017/8/24.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private CircleImageView person_center_imageview;
    private TextView person_center_name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.showe("===当前的fragment的初始化=three==");
    }

    @Override
    protected View initView() {
        EventBus.getDefault().register(this);

        view = View.inflate(mContext, R.layout.fragment_mine,null);
        view.findViewById(R.id.all_back).setVisibility(View.INVISIBLE);
        ((TextView)view.findViewById(R.id.all_title_text)).setText(mActivity.getResources().getString(R.string.person_center));
        //圆形头像控件
        person_center_imageview = (CircleImageView) view.findViewById(R.id.person_center_imageview);
        person_center_name = (TextView) view.findViewById(R.id.person_center_name);
        //个人信息
        ListItem person_information= (ListItem) view.findViewById(R.id.person_information);
        person_information.setOnClickListener(this);
        //我的家庭
        ListItem my_family= (ListItem) view.findViewById(R.id.my_family);
        my_family.setOnClickListener(this);
        //健康档案
        ListItem healthy_document= (ListItem) view.findViewById(R.id.healthy_document);
        healthy_document.setOnClickListener(this);
        return view;
    }

    @Override
    protected void initData() {
        //显示微信的头像图片
        Picasso.with(mContext)
                .load(SPUtils.getString(mContext, Constant.wechat_headimg_url,null))
                .placeholder(R.color.graybackground)
                .error(R.color.graybackground)
                .into(person_center_imageview);
        //显示名字
        person_center_name.setText(SPUtils.getString(mContext,Constant.real_name,null));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.person_information:
                mActivity.startActivity(new Intent(mActivity, PersonInformationActivity.class));
                break;
            case R.id.my_family:
                mActivity.startActivity(new Intent(mActivity, MyFamilyActivity.class));
                break;
            case R.id.healthy_document:
                mActivity.startActivity(new Intent(mActivity, HealthyDocumentActivity.class));
                break;
        }
    }

    //@Subscribe(threadMode = ThreadMode.MAIN)这一行必须写，否则不能运行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MineHeadImageBus event) {
        //及时修改头像
        person_center_imageview.setImageURI(Uri.fromFile(new File(event.getHeadImagePath())));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MineNameBus event) {
        //及时修改头像
        person_center_name.setText(event.getMineName());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
