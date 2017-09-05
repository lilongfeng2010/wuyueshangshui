package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;
import com.wuyueshangshui.yuanxinkangfu.mainUI.mainPager.WebViewActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;

/**
 * Created by lilfi on 2017/8/24.
 * 首页fragment
 */

public class MainPagerFragment extends BaseFragment {
    private View view;
    private WebView webView;
    private ContentLoadingProgressBar progressBar;

    @Override
    protected View initView() {
        view = View.inflate(mContext, R.layout.fragment_mainpager,null);
        webView = (WebView) view.findViewById(R.id.mainpagerWebView);
        view.findViewById(R.id.all_back).setVisibility(View.INVISIBLE);
        ((TextView)view.findViewById(R.id.all_title_text)).setText("元新康复");
        progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.mainpagerProgress);
//        WebSettings settings=webView.getSettings();
//        //支持获取手势焦点，输入用户名、密码或其他
//        webView.requestFocusFromTouch();
//        settings.setJavaScriptEnabled(true);//支持js
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setSupportZoom(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setDisplayZoomControls(false);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setSupportMultipleWindows(true);
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        settings.setAllowFileAccess(true);
//        settings.setNeedInitialFocus(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setLoadsImagesAutomatically(true);
//        settings.setDefaultTextEncodingName("utf-8");
//        WebViewClient mWebViewClient=new WebViewClient();
//        mWebViewClient.shouldOverrideUrlLoading()

        return view;
    }

    @Override
    protected void initData() {
        webView.loadUrl("file:///android_asset/index/fuwu.html");
        webView.setWebViewClient(mWebViewClient);
    }

    WebViewClient mWebViewClient=new WebViewClient(){
        //对webview 的点击事件进行拦截，并且对拦截到的url进行判断过滤
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.showe("====拦截的url:"+url);
            Intent toWebActivity=new Intent(getActivity(), WebViewActivity.class);
            toWebActivity.putExtra(Constant.webview_url,url);
            startActivity(toWebActivity);
            //activity消失动画，新activity从右侧进入，旧activity从左侧退出
            getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            return true;
        }
    };


}
