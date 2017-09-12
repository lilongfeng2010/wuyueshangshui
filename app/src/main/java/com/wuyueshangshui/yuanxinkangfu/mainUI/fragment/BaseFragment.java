package com.wuyueshangshui.yuanxinkangfu.mainUI.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;

/**
 * Created by lilfi on 2017/8/24.
 */

public abstract class BaseFragment extends Fragment {
    public Context mContext;
    protected Gson gson;
    protected Activity mActivity;
    /**
     *这里调用onAttach的原因是，
     * 可能你遇到过getActivity()返回null，或者平时运行完好的代码，
     * 在“内存重启”之后，调用getActivity()的地方却返回null，报了空指针异常。
     * 具体详解请看  http://www.jianshu.com/p/d9143a92ad94
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mActivity= (Activity) context;
    }

    /**
     * 初始化上下文
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson=new Gson();
        mContext=getActivity();
    }
    /**
     * Fragment视图创建
     * 子类必须实现该抽象方法 用于加载自己的视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return initView();
    }
    /**
     * 用于强制子类实现加载自己的视图
     *
     * @return
     */
    protected abstract View initView();
    /**
     * 当Activity的onCreate方法返回时调用
     * 用于初始化数据 相当于Activity的OnCreate方法调用
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }




    protected abstract void initData();

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
