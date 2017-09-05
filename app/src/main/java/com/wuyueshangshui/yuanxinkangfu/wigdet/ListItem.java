package com.wuyueshangshui.yuanxinkangfu.wigdet;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.R;

/**
 * Created by lilfi on 2017/8/25.
 * 我的页面的条目的自定义布局
 */

public class ListItem extends FrameLayout {

    public ListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.list_item,this);
        View mIcon = findViewById(R.id.list_item_icon);
        TextView mText = (TextView) findViewById(R.id.list_item_title);

        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.ListItem);
        int icon=array.getResourceId(R.styleable.ListItem_itemIcon,R.drawable.uc_1);
        String title=array.getString(R.styleable.ListItem_itemTitle);

        array.recycle();

        mIcon.setBackgroundResource(icon);
        if (title==null) title="";
        mText.setText(title);


    }
}
