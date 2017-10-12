package com.wuyueshangshui.yuanxinkangfu.mainactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.mainUI.mainPager.WebViewActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.wuyueshangshui.yuanxinkangfu.wigdet.ActivityCollector;

public class BaseActivity extends FragmentActivity {
    protected Gson gson;
//    private NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson=new Gson();
        ActivityCollector.addActivity(this);

        //监听网络变化
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        if (networkChangeReceiver==null){
//            networkChangeReceiver=new NetworkChangeReceiver();
//        }
//        registerReceiver(networkChangeReceiver,intentFilter);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_BACK://监听实体键的返回键
                finish();
                //activity消失动画，新activity从左侧进入，旧activity从右侧退出
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);

//        unregisterReceiver(networkChangeReceiver);
    }

    /*class NetworkChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();
            if (networkInfo!=null && networkInfo.isAvailable()){
                ToastUtils.Toast(BaseActivity.this,getResources().getString(R.string.network_info_on));
            }else{
                ToastUtils.Toast(BaseActivity.this,getResources().getString(R.string.network_info_off));
            }
        }
    }*/

}
