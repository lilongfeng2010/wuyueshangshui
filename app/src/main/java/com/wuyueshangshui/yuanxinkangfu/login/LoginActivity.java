
package com.wuyueshangshui.yuanxinkangfu.login;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.login.fragment.MessageLoginFragment;
import com.wuyueshangshui.yuanxinkangfu.login.fragment.PasswordLoginFragment;
import com.wuyueshangshui.yuanxinkangfu.mainactivity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录页面，包括1，密码登录 ；2，短信登录
 */
public class LoginActivity extends BaseActivity {

    private TabLayout tab;
    private ViewPager viewpager;
    private List<Fragment> fragments=new ArrayList<>();
    private String[] titles={"密码登录","短信登录"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((TextView) findViewById(R.id.all_title_text)).setText("登录");
        findViewById(R.id.all_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        viewpager = (ViewPager) findViewById(R.id.login_viewpager);
        tab = (TabLayout) findViewById(R.id.login_tablayout);

        PasswordLoginFragment fragment1=new PasswordLoginFragment();
        MessageLoginFragment fragment2=new MessageLoginFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);

        FragmentManager fm=getSupportFragmentManager();
        PagerAdapter adapter=new PagerAdapter(fm);
        viewpager.setAdapter(adapter);

        tab.setupWithViewPager(viewpager);//tab和viewpager绑定

    }
    class PagerAdapter extends FragmentPagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }




}
