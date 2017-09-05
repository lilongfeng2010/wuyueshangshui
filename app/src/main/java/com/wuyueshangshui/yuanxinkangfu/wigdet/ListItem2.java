package com.wuyueshangshui.yuanxinkangfu.wigdet;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.R;

/**
 * Created by lilfi on 2017/8/25.
 * 我的页面的条目的自定义布局
 */

public class ListItem2 extends FrameLayout {

    public ListItem2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.list_item2,this);
        TextView mText = (TextView) findViewById(R.id.list_item2_title);
        TextView mContent= (TextView) findViewById(R.id.list_item2_content);


        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.ListItem2);
        String title=array.getString(R.styleable.ListItem2_itemTitle2);
        String content=array.getString(R.styleable.ListItem2_itemContent2);
        array.recycle();

        if (title==null) title="";
        mText.setText(title);
        mContent.setText(content);

    }
}
