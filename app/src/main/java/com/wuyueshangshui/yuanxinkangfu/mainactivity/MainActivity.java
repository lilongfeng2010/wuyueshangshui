package com.wuyueshangshui.yuanxinkangfu.mainactivity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.BaseFragment;
import com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.MainPagerFragment;
import com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.MineFragment;
import com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.OrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {
    private List<BaseFragment> mBaseFragments;
    private RadioGroup mRadioGroup;
    private int position;//当前选中的位置
    private BaseFragment mFragment;//刚显示的Fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();

        setListener();
    }

    private void setListener() {
        mRadioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        mRadioGroup.check(R.id.main_icon_1);
    }

    private void initData() {
        mBaseFragments=new ArrayList<>();
        mBaseFragments.add(new MainPagerFragment());
        mBaseFragments.add(new OrderFragment());
        mBaseFragments.add(new MineFragment());
    }

    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.main_icon_1:
                    position = 0;
                    break;
                case R.id.main_icon_3:
                    position=1;
                    break;
                case R.id.main_icon_5:
                    position=2;
                    break;
                default:
                    position=0;
                    break;
            }
            //根据位置得到对应的Fragment
            BaseFragment currentFragment=getFragment();
            //替换fragment
//            replaceFragment(mFragment,currentFragment);
            replaceFragment(currentFragment);
        }
    }
    /**
     *
     * @param lastFragment
     * @param currentFragment
     */
   /* private void replaceFragment(BaseFragment lastFragment,BaseFragment currentFragment){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        //如果两个不相等,说明切换了Fragment
        if (lastFragment!=currentFragment){
            mFragment=currentFragment;
            //隐藏刚显示的Fragment
            if (lastFragment!=null){
                transaction.hide(lastFragment);
            }
            *//**
     * 显示 或者 添加当前要显示的Fragment
     *
     * 如果当前要显示的Fragment没添加过 则 添加
     * 如果当前要显示的Fragment被添加过 则 隐藏
     *//*
            if (!currentFragment.isAdded()){
                if (currentFragment!=null){
                    transaction.add(R.id.activity_main_container,currentFragment);
                }
            }else{
                if (currentFragment!=null){
                    transaction.show(currentFragment).commit();
                }
            }

        }



    }
*/
    public void replaceFragment(BaseFragment fragment){
        //1.得到FragmentManger
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        //3.替换
        transaction.replace(R.id.activity_main_container, fragment);
        //4.提交事务
        transaction.commit();

    }


    /**
     * 根据返回到对应的Fragment
     * @return
     */
    private BaseFragment getFragment(){
        return mBaseFragments.get(position);
    }



}
