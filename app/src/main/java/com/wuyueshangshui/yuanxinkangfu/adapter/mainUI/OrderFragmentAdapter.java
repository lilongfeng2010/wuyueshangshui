package com.wuyueshangshui.yuanxinkangfu.adapter.mainUI;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.EventBus.OrderAdapterBus;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.bean.GetOrder.GetOrderAppoints;
import com.wuyueshangshui.yuanxinkangfu.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by lilfi on 2017/8/29.
 */

public class OrderFragmentAdapter extends RecyclerView.Adapter<OrderFragmentAdapter.ViewHolder> {
    private Context context;
    private List<GetOrderAppoints> datas;
    public OrderFragmentAdapter(Context context, List<GetOrderAppoints> datas){
        this.context=context;
        this.datas=datas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView cardview_item_title;
        TextView cardview_item_status;
        TextView cardview_item_name;
        TextView cardview_item_time;
        TextView cardview_item_style;
        Button cardview_item_button;
        public ViewHolder(View view) {
            super(view);
            cardview_item_title= (TextView) view.findViewById(R.id.cardview_item_title);
            cardview_item_status= (TextView) view.findViewById(R.id.cardview_item_status);
            cardview_item_name= (TextView) view.findViewById(R.id.cardview_item_name);
            cardview_item_time= (TextView) view.findViewById(R.id.cardview_item_time);
            cardview_item_style= (TextView) view.findViewById(R.id.cardview_item_style);
            cardview_item_button= (Button) view.findViewById(R.id.cardview_item_button);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cardview_item2,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardview_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position=holder.getAdapterPosition();
                getDialog(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetOrderAppoints appoints=datas.get(position);
        holder.cardview_item_title.setText(appoints.getService_name());
        holder.cardview_item_status.setText(getServiceState(appoints.getState(),holder.cardview_item_button));
        holder.cardview_item_name.setText(appoints.getPeople_name());
        holder.cardview_item_time.setText(DateUtils.stampToDate(Long.parseLong(appoints.getDate())*1000+""));
        holder.cardview_item_style.setText(getServiceStyle(appoints.getWay()));
        holder.itemView.setTag(position);

    }
    @Override
    public int getItemCount() {
        return datas.size();
    }
    // 服务方式：1到店 2上门
    public String getServiceStyle(String style){
        String returnStyle=null;
        switch (style){
            case "1":
                returnStyle="到店";
                break;
            case "2":
                returnStyle="上门";
                break;
        }
        return returnStyle;
    }

    //预约状态：0无效订单，1待审核，2预约中，3已取消，4已完成
    //
    public String getServiceState(String status,Button button){
        String returnStatus = null;
        switch (status){
            case "0":
                returnStatus= "无效订单";
                button.setEnabled(false);
                break;
            case "1":
                returnStatus= "待审核";
                button.setEnabled(true);
                break;
            case "2":
                returnStatus= "预约中";
                button.setEnabled(true);
                break;
            case "3":
                returnStatus= "已取消";
                button.setEnabled(false);
                break;
            case "4":
                returnStatus= "已完成";
                button.setEnabled(false);
                break;
        }
        return returnStatus;
    }

    //取消预约的dialog
    public void getDialog(final int mPosition){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view=View.inflate(context, R.layout.twobutton,null);
        ((TextView)view.findViewById(R.id.twoButtonTV)).setText("确定要取消预约吗?");
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog mDialog=builder.create();
        mDialog.show();
        view.findViewById(R.id.TwoButtonNegative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        view.findViewById(R.id.TwoButtonPosition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                EventBus.getDefault().post(new OrderAdapterBus(mPosition));
            }
        });

    }

}
