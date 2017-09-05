package com.wuyueshangshui.yuanxinkangfu.adapter.mainUI;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.order.ServiceObjectActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.order.ServiceObjectActivity.*;

/**
 * Created by lilfi on 2017/8/29.
 */

public class FamilyRecyclerAdapter extends RecyclerView.Adapter<FamilyRecyclerAdapter.ViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<String> mAvatarLists;
    private List<String> mRealNameLists;
    private OnItemClickListener mOnItemClickListener=null;
    public FamilyRecyclerAdapter(Context context ,List<String> avatarLists,List<String> realNameLists){
        mContext=context;
        mAvatarLists=avatarLists;
        mRealNameLists=realNameLists;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener!=null){
            LogUtils.showe("====v.getTag()==="+v.getTag());
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        View allView;
        CircleImageView imageview;
        TextView name;
        public ViewHolder(View view) {
            super(view);
            allView=view;
            imageview= (CircleImageView) view.findViewById(R.id.family_recycler_item_image);
            name = (TextView) view.findViewById(R.id.family_recycler_item_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.family_recycler_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        holder.imageview.setOnClickListener(this);
        holder.name.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //毕加索加载头像图片
        Picasso.with(mContext).load(mAvatarLists.get(position)).placeholder(R.color.graybackground).error(R.color.graybackground).into(holder.imageview);
        holder.name.setText(mRealNameLists.get(position));
        //这两行方法是为了把点击事件传到外面去
        holder.imageview.setTag(position);
        holder.name.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mAvatarLists.size();
    }
    //定义接口，并且把点击事件通过adapter传到外面去
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }


    public static interface OnItemClickListener{
        void onItemClick(View view,int position);
    }


}
