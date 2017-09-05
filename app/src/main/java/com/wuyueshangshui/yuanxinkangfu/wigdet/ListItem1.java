package com.wuyueshangshui.yuanxinkangfu.wigdet;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.R;

/**
 * Created by lilfi on 2017/8/25.
 * 我的页面的条目的自定义布局
 */

public class ListItem1 extends FrameLayout {

    public ListItem1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.list_item1,this);
        View mIcon = findViewById(R.id.list_item1_icon);
        TextView mText = (TextView) findViewById(R.id.list_item1_title);
        TextView mContext= (TextView) findViewById(R.id.list_item1_content);
        View mArrow= findViewById(R.id.list_item1_arrow);

        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.ListItem1);
        int icon=array.getResourceId(R.styleable.ListItem1_itemIcon1,R.drawable.uc_1);
        String title=array.getString(R.styleable.ListItem1_itemTitle1);
        String content=array.getString(R.styleable.ListItem1_itemContent1);
        boolean isShowArrow=array.getBoolean(R.styleable.ListItem1_itemArrowShow,true);
        array.recycle();

        mIcon.setBackgroundResource(icon);
        if (title==null) title="";
        mText.setText(title);
        mContext.setText(content);
        if (isShowArrow){
            mArrow.setVisibility(VISIBLE);
        }else{
            mArrow.setVisibility(INVISIBLE);
        }

    }
}
