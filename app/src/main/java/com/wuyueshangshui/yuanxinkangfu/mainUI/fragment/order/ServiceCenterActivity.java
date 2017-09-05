package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.order;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.adapter.mainUI.ServiceCenterAdapter;
import com.wuyueshangshui.yuanxinkangfu.bean.GetServiceCenter.GetCenterOrigin;
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

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 服务中心
 */
public class ServiceCenterActivity extends BaseActivity implements View.OnClickListener {
    private int page=0;
    private RecyclerView recyclerView;
    private List<String> service_names;
    private List<String> address;
    private ServiceCenterAdapter adapter;
    private List<String> service_ids;//服务中心的service_id集合
    private SmartRefreshLayout refreshLayout;
    boolean firstEnterRefresh=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);

        service_names=new ArrayList<>();
        address=new ArrayList<>();
        service_ids=new ArrayList<>();
        //加载布局
        initView();
        //加载数据
        initData();
    }

    private void initData() {
        String encryptData= DES.encrypt(gson.toJson(getServiceCenterBean()));
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
                        String decryptData=DES.decrypt(response);
                        LogUtils.showe("=====获取门店列表===:"+decryptData);
                        GetCenterOrigin origin=gson.fromJson(decryptData, GetCenterOrigin.class);
                        if (origin.getRoot().getHead().getCode()==0){
                            if (origin.getRoot().getBody().getServices().size()==0){
                                //当数据为0条时候，提示用户
                                ToastUtils.Toast(ServiceCenterActivity.this,"没有数据了，别扯了");
                            }else{
                                //当有数据时候，判断此时的page数增加了没有，增加了说明是上拉加载，没有增加说明是下拉刷新.
                                if (page==0){
                                    service_names.clear();
                                    address.clear();
                                    service_ids.clear();
                                    addData(origin);
                                }else{
                                    addData(origin);
                                }

                            }

                        }
                       /* for (int i = 0; i < 10; i++) {
                            service_names.add("测试数据name"+i);
                            address.add("测试数据地址"+i);
                        }*/
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void addData(GetCenterOrigin origin) {
        for (int i = 0; i < origin.getRoot().getBody().getServices().size(); i++) {
            service_names.add(origin.getRoot().getBody().getServices().get(i).getService_name());
            address.add(origin.getRoot().getBody().getServices().get(i).getAddress());
            service_ids.add(origin.getRoot().getBody().getServices().get(i).getId());
        }
    }

    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.all_title_text)).setText("选择服务中心");
        recyclerView = (RecyclerView) findViewById(R.id.service_center_recycler);
        //下拉刷新页面
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.service_center_smartRefresh);
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
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(getRecyclerViewDivider(R.drawable.recycler_divider));//设置分割线
        adapter = new ServiceCenterAdapter(this,service_names,address);
        recyclerView.setAdapter(adapter);
        //通过点击事件把数据回传给上一个activity
        adapter.setOnItemClickListener(new ServiceCenterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent serviceCenter_to_orderIntent=new Intent();
                LogUtils.showe("========回传的的数据======"+service_names.get(position));
                serviceCenter_to_orderIntent.putExtra(Constant.service_center,service_names.get(position));
                serviceCenter_to_orderIntent.putExtra(Constant.service_id,service_ids.get(position));
                setResult(Constant.SERVICECENTER_TO_ORDER,serviceCenter_to_orderIntent);
                ServiceCenterActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }
    //获取分割线
    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId){
        DividerItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(drawableId));
        return itemDecoration;
    }

    private SendOriginBean getServiceCenterBean() {
        SendOriginBean origin=new SendOriginBean();
        SendRootBean root=new SendRootBean();
        SendHeadBean head=new SendHeadBean();
        SendBodyBean body=new SendBodyBean();
        head.setClientid(Constant.clientid);
        head.setAction(114);//获取门店列表
        head.setTimestamp(System.currentTimeMillis()+"");
        body.setPage(page+"");
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
                //activity消失动画，新activity从左侧进入，旧activity从右侧退出
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }


    }
}
