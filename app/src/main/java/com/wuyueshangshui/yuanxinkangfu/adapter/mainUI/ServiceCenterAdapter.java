package com.wuyueshangshui.yuanxinkangfu.adapter.mainUI;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;

import java.util.List;

/**
 * Created by lilfi on 2017/8/29.
 */

public class ServiceCenterAdapter extends RecyclerView.Adapter<ServiceCenterAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<String> mName;
    private List<String> mAddress;
    private OnItemClickListener mOnItemClicklistener=null;
    public ServiceCenterAdapter(Context context, List<String> name,List<String> address){
        mContext=context;
        mName=name;
        mAddress=address;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClicklistener!=null){
            LogUtils.showe("====v.getTag()==="+v.getTag());
            if (v.getTag()!=null){
                mOnItemClicklistener.onItemClick(v, (Integer) v.getTag());
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,address;
        View allView;
        public ViewHolder(View view) {
            super(view);
            allView=view;
            name= (TextView) view.findViewById(R.id.service_center_item_title);
            address= (TextView) view.findViewById(R.id.service_center_item_content);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.service_center_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        //将创建的View注册点击事件
        holder.name.setOnClickListener(this);
        holder.address.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(mName.get(position));
        holder.address.setText(mAddress.get(position));
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.name.setTag(position);
        holder.address.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mName.size();
    }
    //用接口使adapter的item的点击事件传递出去
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClicklistener=onItemClickListener;
    }

    public static interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

}
