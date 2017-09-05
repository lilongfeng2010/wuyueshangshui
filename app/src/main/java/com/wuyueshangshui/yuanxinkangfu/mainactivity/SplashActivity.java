package com.wuyueshangshui.yuanxinkangfu.mainactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.wuyueshangshui.yuanxinkangfu.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splash_img= (ImageView) findViewById(R.id.splash_img);

    }
}
