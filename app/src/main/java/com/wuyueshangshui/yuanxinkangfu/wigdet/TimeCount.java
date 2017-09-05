package com.wuyueshangshui.yuanxinkangfu.wigdet;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.R;

/**
 * Created by lilfi on 2017/8/23.
 * 倒计时
 */

public class TimeCount extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private TextView getChecknum;
    private Context context;
    public TimeCount(long millisInFuture, long countDownInterval, Context context, TextView getChecknum) {
        super(millisInFuture, countDownInterval);
        this.getChecknum=getChecknum;
        this.context=context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        getChecknum.setClickable(false);//防止重复点击
        getChecknum.setText(millisUntilFinished/1000+"s");
    }

    @Override
    public void onFinish() {
        getChecknum.setText(context.getResources().getString(R.string.get_checknum));
        getChecknum.setClickable(true);
    }
}
