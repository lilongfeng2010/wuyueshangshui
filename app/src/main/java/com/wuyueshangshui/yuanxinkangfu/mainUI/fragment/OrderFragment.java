package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wuyueshangshui.yuanxinkangfu.EventBus.OrderAdapterBus;
import com.wuyueshangshui.yuanxinkangfu.EventBus.OrderEventBus;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.adapter.mainUI.OrderFragmentAdapter;
import com.wuyueshangshui.yuanxinkangfu.bean.GetOrder.GetOrderAppoints;
import com.wuyueshangshui.yuanxinkangfu.bean.GetOrder.GetOrderOrigin;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendBodyBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendOriginBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendRootBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendCancelOrder.SendCancelOrderOrigin;
import com.wuyueshangshui.yuanxinkangfu.bean.SendCancelOrder.SendCancelOrderRoot;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.common.GlobalURL;
import com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.order.OrderActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.MyCallBack;
import com.wuyueshangshui.yuanxinkangfu.utils.SPUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.DialogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.encrypt.DES;
import com.zhy.http.okhttp.OkHttpUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by lilfi on 2017/8/24.
 *
 *
 */

public class OrderFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private OrderFragmentAdapter mAdapter;
    private ImageView yuyue_add;
    private int page=0;//分页号
    private List<GetOrderAppoints> appointLists;//返回的数据的集合
    private boolean firstEnterRefresh=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.showe("===当前的fragment的初始化==two=");
    }

    @Override
    protected View initView() {
        EventBus.getDefault().register(this);
        view = View.inflate(mContext, R.layout.fragment_order,null);
        view.findViewById(R.id.all_back).setVisibility(View.INVISIBLE);
        ((TextView)view.findViewById(R.id.all_title_text)).setText("预约");
        yuyue_add = (ImageView) view.findViewById(R.id.all_right_image);
        yuyue_add.setImageResource(R.drawable.yuyue_add);
        yuyue_add.setVisibility(View.VISIBLE);
        yuyue_add.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.order_recycler);

        //recyclerview的使用
        LinearLayoutManager manager=new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(manager);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.smartRefresh);
        //数据载体
        appointLists=new ArrayList<>();
        mAdapter = new OrderFragmentAdapter(mActivity,appointLists);
        recyclerView.setAdapter(mAdapter);
        //是否自动刷新控件
        refreshLayout.setEnableAutoLoadmore(true);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshlayout.finishRefresh();
                        page=0;
                        initData();
                        refreshlayout.setLoadmoreFinished(false);
                    }
                },2000);
            }
        });
        //上拉加载更多
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshlayout.finishLoadmore();
                        page++;
                        initData();
                    }
                },2000);
            }
        });
        //进入页面直接刷新
        if (firstEnterRefresh){
            refreshLayout.autoRefresh();
            firstEnterRefresh=false;
        }
        return view;
    }
    @Override
    protected void initData() {
        String encryptData= DES.encrypt(gson.toJson(getOrderBean()));
        OkHttpUtils.postString().url(GlobalURL.BaseURL)
                .content(encryptData)
                .mediaType(MediaType.parse("application/json;charset=utf-8"))
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showe("=====服务对象的返回值fail======"+e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String decryptData= DES.decrypt(response);
                        LogUtils.showe("=====服务对象的返回值success======"+decryptData);
                        GetOrderOrigin origin=gson.fromJson(decryptData,GetOrderOrigin.class);
                        //返回的code是0，也就是返回数据成功
                        if (origin.getRoot().getHead().getCode()==0){
                            if (origin.getRoot().getBody().getAppoints().size()==0){
                                //当数据为0条时候，提示用户
                                ToastUtils.Toast(mActivity,"没有数据了，别扯了");
                            }else{
                                //当有数据时候，判断此时的page数增加了没有，增加了说明是上拉加载，没有增加说明是下拉刷新.
                                if (page==0){
                                    appointLists.clear();
                                    appointLists.addAll(origin.getRoot().getBody().getAppoints());
                                }else{
                                    appointLists.addAll(origin.getRoot().getBody().getAppoints());
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private SendOriginBean getOrderBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(112);
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setUser_id(SPUtils.getString(mActivity,Constant.user_id,null));
        body.setPage(page+"");
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_right_image:
                startActivity(new Intent(mActivity, OrderActivity.class));
                mActivity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
        }
    }


    //@Subscribe(threadMode = ThreadMode.MAIN)这一行必须写，否则不能运行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(OrderEventBus event) {
        refreshLayout.autoRefresh();
//        initData();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(OrderAdapterBus event) {
        refreshLayout.autoRefresh();
        cancelOrder(event.getMPosition());


    }
    //取消预约的请求  position表示取消的是第几个条目
    private void cancelOrder(int position) {
        String encryptData= DES.encrypt(gson.toJson(SendCancelOrderBean(position)));
        OkHttpUtils.postString().url(GlobalURL.BaseURL)
                .content(encryptData)
                .mediaType(MediaType.parse("application/json;charset=utf-8"))
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showe("======取消预约fail======="+e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String decryptData= DES.decrypt(response);
                        LogUtils.showe("======取消预约success======="+decryptData);
                        //取消成功后要刷新列表
                       initData();
                    }
                });
    }

    private SendCancelOrderOrigin SendCancelOrderBean(int position) {
        SendCancelOrderOrigin origin=new SendCancelOrderOrigin();
        SendCancelOrderRoot root=new SendCancelOrderRoot();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(116);//取消预约
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setAppoint_id(appointLists.get(position).getId());
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }


    public interface getAdapterPositionButton{
        void refresh();
    }

    public void getRefresh(getAdapterPositionButton button){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
