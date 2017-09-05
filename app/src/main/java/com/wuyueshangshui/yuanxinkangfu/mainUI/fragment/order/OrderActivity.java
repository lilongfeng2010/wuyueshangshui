package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.order;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wuyueshangshui.yuanxinkangfu.EventBus.OrderEventBus;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.bean.Get.GetOrigin;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendOrder.SendOrderBody;
import com.wuyueshangshui.yuanxinkangfu.bean.SendOrder.SendOrderOrigin;
import com.wuyueshangshui.yuanxinkangfu.bean.SendOrder.SendOrderRoot;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.common.GlobalURL;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.BaseActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.DateUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.CheckNet;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.MyCallBack;
import com.wuyueshangshui.yuanxinkangfu.utils.SPUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.DialogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.encrypt.DES;
import com.wuyueshangshui.yuanxinkangfu.wigdet.ListItem;
import com.wuyueshangshui.yuanxinkangfu.wigdet.ListItem1;
import com.zhy.http.okhttp.OkHttpUtils;


import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 预约的页面
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener {

    private TextView service_object_content;
    private TextView service_object_time;
    private TextView service_center_name;
    private int people_id;//接收回传的peopleid
    private int family_id;//接收回传的family_id
    private String service_id;//接收回传的service_id
    private String real_name;//接收回传的真名
    private String selectTime;//存储选择的时间
    private long currentSecond;
    private ListItem1 service_objectItem;
    private TextView my_phone;
    private ListItem1 order_timeItem;
    private ListItem1 service_centerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();

        initListener();
        //先有的数据直接填入其中
        initData();

    }


    //初始化控件
    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.all_title_text)).setText("新预约");
        ImageView call=(ImageView)findViewById(R.id.all_right_image);
        call.setImageResource(R.drawable.yuyue_icon_2);
        call.setVisibility(View.VISIBLE);
        call.setOnClickListener(this);
        //服务对象的条目
        service_objectItem = (ListItem1) findViewById(R.id.order_service_object);
        service_object_content = (TextView) service_objectItem.findViewById(R.id.list_item1_content);

        //我的电话的条目
        ListItem1 my_phoneItem= (ListItem1) findViewById(R.id.order_myphone);
        my_phone = (TextView) my_phoneItem.findViewById(R.id.list_item1_content);

        //预约时间的条目
        order_timeItem = (ListItem1) findViewById(R.id.order_time);
        service_object_time = (TextView) order_timeItem.findViewById(R.id.list_item1_content);

        //服务中心的条目
        service_centerItem = (ListItem1) findViewById(R.id.service_center);
        service_center_name = (TextView) service_centerItem.findViewById(R.id.list_item1_content);

        //完成的条目
        LinearLayout order_complete= (LinearLayout) findViewById(R.id.order_complete);
        order_complete.setOnClickListener(this);
    }


    private void initListener() {
        service_objectItem.setOnClickListener(this);
        order_timeItem.setOnClickListener(this);
        service_centerItem.setOnClickListener(this);
    }


    private void initData() {
        service_object_content.setText(SPUtils.getString(this,Constant.real_name,null));
        my_phone.setText(SPUtils.getString(this,Constant.mobile,null));
        real_name=SPUtils.getString(this,Constant.real_name,null);
        people_id=Integer.parseInt( SPUtils.getString(this,Constant.user_id,null));
        family_id=Integer.parseInt(SPUtils.getString(this,Constant.family_id,null));
        //如果姓名为空，则不能提交新的预约，需要先去个人中心填写个人姓名后才能预约。
        if (real_name==null){
            DialogUtils.twoButton(this, "您需要先填写姓名才能进行预约", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    //所有的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.all_right_image:

                break;
            case R.id.order_service_object://进入服务对象页面
                Intent toServiceObjectIntent=new Intent(this,ServiceObjectActivity.class);
                startActivityForResult(toServiceObjectIntent,Constant.ORDER_TO_SERVICEOBJECT);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.order_time://预约时间的点击事件
                initCalendar();
                break;
            case R.id.service_center://服务中心的列表
                Intent toServiceCenterIntent=new Intent(this,ServiceCenterActivity.class);
                startActivityForResult(toServiceCenterIntent,Constant.ORDER_TO_SERVICECENTER);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.order_complete:
                completeSubmit();
                break;
        }
    }
    //提交事件
    private void completeSubmit() {
        //检查需要填写的项填写完毕没有
        if (TextUtils.isEmpty(service_object_content.getText().toString())){
            DialogUtils.onePosition(this,"请先选择服务对象(=^ ^=)");
            return;
        }
        if(TextUtils.isEmpty(service_object_time.getText().toString())){
            DialogUtils.onePosition(this,"请先选择预约时间(=^ ^=)");
            return;
        }
        if(TextUtils.isEmpty(service_center_name.getText().toString())){
            DialogUtils.onePosition(this,"请先选择服务中心(=^ ^=)");
            return;
        }
        //检查网络是否连接
        boolean netIsConnect= CheckNet.netIsOk(this);
        if(!netIsConnect){
            ToastUtils.Toast(this,getResources().getString(R.string.no_network));
        }else{
            String encryptData=DES.encrypt(gson.toJson(getOrderBean()));
            OkHttpUtils.postString().url(GlobalURL.BaseURL)
                    .content(encryptData)
                    .mediaType(MediaType.parse("application/json;charset=utf-8"))
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.showe("===提交订单fail=="+e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String decryptData=DES.decrypt(response);
                            LogUtils.showe("===提交订单success=="+decryptData);

                            GetOrigin origin=gson.fromJson(decryptData,GetOrigin.class);
                            //订单提交成功
                            if (origin.getRoot().getHead().getCode()==0){
                                EventBus.getDefault().post(new OrderEventBus());
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                        }
             });
        }
    }

    private SendOrderOrigin getOrderBean() {
        SendOrderOrigin origin=new SendOrderOrigin();
        SendOrderRoot root=new SendOrderRoot();
        SendHeadBean head=new SendHeadBean();
        SendOrderBody body=new SendOrderBody();
        head.setClientid(Constant.clientid);
        head.setAction(115);
        head.setTimestamp(System.currentTimeMillis()+"");
        //===提交的所有参数=0::10000430::0::1006::张强::1504022400
        //10000081::10000430::1000080::1006::李帅::1504607400
        LogUtils.showe("===提交的所有参数="+people_id+"::"+Integer.parseInt(SPUtils.getString(this,Constant.user_id,null))+"::"+family_id
                    +"::"+service_id+"::"+real_name+"::"+currentSecond);
        body.setPeople_id(people_id);
        body.setUser_id(Integer.parseInt(SPUtils.getString(this,Constant.user_id,null)));
        body.setFamily_id(family_id);
        body.setService_id(service_id);
        body.setPeople_name(real_name);
        body.setDate(currentSecond+"");
        root.setHead(head);
        root.setBody(body);
        origin.setRoot(root);
        return origin;
    }
    //调用系统日历
    private void initCalendar() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectTime=year+"-"+(month+1)+"-"+dayOfMonth+" ";
                initTime();
            }
        }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    //调用系统钟表
    private void initTime() {
        new TimePickerDialog(OrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectTime=selectTime+hourOfDay+":"+minute;
                service_object_time.setText(selectTime);
                currentSecond = DateUtils.getSecond(selectTime)/1000;
                LogUtils.showe("========当前的秒秒=="+ currentSecond);
            }
        },0,0,false).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Constant.ORDER_TO_SERVICEOBJECT && resultCode==Constant.SERVICEOBJECT_TO_ORDER){
            LogUtils.showe("========接受的数据======"+data.getStringExtra(Constant.real_name));
            if (data!=null){
                if (!TextUtils.isEmpty(data.getStringExtra(Constant.real_name))
                        && !TextUtils.isEmpty(data.getIntExtra(Constant.people_id,0)+"")
                        && !TextUtils.isEmpty(data.getIntExtra(Constant.family_id,0)+"")){
                    real_name = data.getStringExtra(Constant.real_name);
                    people_id=data.getIntExtra(Constant.people_id,0);
                    family_id=data.getIntExtra(Constant.family_id,0);
                    service_object_content.setText(real_name);
                    LogUtils.showe("========接受的数据======"+data.getStringExtra(Constant.service_center)+"==family_id=="+family_id);
                }
            }
        }else if(requestCode==Constant.ORDER_TO_SERVICECENTER && resultCode==Constant.SERVICECENTER_TO_ORDER){
            if (data!=null){
                if (!TextUtils.isEmpty(data.getStringExtra(Constant.service_center))){
                    service_center_name.setText(data.getStringExtra(Constant.service_center));
                    service_id=data.getStringExtra(Constant.service_id);
                }
            }
        }
    }
}
