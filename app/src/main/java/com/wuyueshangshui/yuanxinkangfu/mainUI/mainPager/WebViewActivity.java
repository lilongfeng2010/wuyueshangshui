package com.wuyueshangshui.yuanxinkangfu.mainUI.mainPager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.BaseActivity;

/**
 * 首页跳转的webview的详情页面
 */
public class WebViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mainpager);

        findViewById(R.id.all_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.finish();
                //activity消失动画，新activity从左侧进入，旧activity从右侧退出
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        String url=getIntent().getStringExtra(Constant.webview_url);
        WebView webView= (WebView) findViewById(R.id.mainpagerWebView);
        webView.loadUrl(url);
    }
}
