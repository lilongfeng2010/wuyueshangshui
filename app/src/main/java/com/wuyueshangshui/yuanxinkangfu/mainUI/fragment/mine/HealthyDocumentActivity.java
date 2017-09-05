package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.BaseActivity;

public class HealthyDocumentActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_document);

        initView();
    }

    private void initView() {
        findViewById(R.id.all_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.all_title_text)).setText("健康档案");
        findViewById(R.id.healthy_document_item).setOnClickListener(this);
        findViewById(R.id.healthy_document_recure_report_item).setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.healthy_document_item:

                break;
            case R.id.healthy_document_recure_report_item:
                startActivity(new Intent(this,RecureReportActivity.class));
                break;
        }
    }
}
