package com.hailing.webapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.hailing.webapp.ui.main.BookmarkFragment;
import com.hailing.webapp.ui.main.HistoryFragment;
import com.hailing.webapp.ui.main.HomeFragment;

// 加载主页布局，主页逻辑代码实现
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String fragmentTag; // 碎片标签，用以实现3个Fragment之间的跳转
    private RadioButton rb_home;
    private RadioButton rb_history;
    private RadioButton rb_bookmark;
    private static boolean isExit = false; // 判断是否退出软件

    //用于判断进入主页跳转去哪个Fragment
    public String fromTag;
    public String toTag;

    // 新建主界面三个模块的碎片对象
    private Fragment homeFragment = new HomeFragment();
    private Fragment historyFragment = new HistoryFragment();
    private Fragment bookmarkFragment = new BookmarkFragment();

    // 添加碎片，初始化布局
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 将三个模块的碎片添加到主界面中，并设定进入主界面显示的第一个界面是首页
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_content, homeFragment, "homeFragment")
                .add(R.id.main_content, historyFragment, "historyFragment")
                .add(R.id.main_content, bookmarkFragment, "bookmarkFragment")
                .hide(historyFragment)
                .hide(bookmarkFragment)
                .commit();
        this.fragmentTag = "homeFragment";
        toTag = "homeFragment";
        initView();
    }

    // 碎片选择标记
    public void switchFragment(String toTag) {
        Drawable drawable1;
        Drawable drawable2;
        Drawable drawable3;
        //先将导航栏图标及文字全部变灰色
        rb_home.setTextColor(Color.parseColor("#000000"));
        rb_history.setTextColor(Color.parseColor("#000000"));
        rb_bookmark.setTextColor(Color.parseColor("#000000"));
        drawable1 = this.getResources().getDrawable(R.drawable.bottom_home_normal);
        drawable2 = this.getResources().getDrawable(R.drawable.bottom_history_normal);
        drawable3 = this.getResources().getDrawable(R.drawable.bottom_bookmark_normal);

        switch (toTag) {
            case "homeFragment":
                rb_home.setTextColor(Color.parseColor("#228B22"));
                drawable1 = this.getResources().getDrawable(R.drawable.bottom_home_pressed);
                break;
            case "historyFragment":
                rb_history.setTextColor(Color.parseColor("#228B22"));
                drawable2 = this.getResources().getDrawable(R.drawable.bottom_history_pressed);
                break;
            case "bookmarkFragment":
                rb_bookmark.setTextColor(Color.parseColor("#228B22"));
                drawable3 = this.getResources().getDrawable(R.drawable.bottom_bookmark_pressed);
                break;
        }
        rb_home.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable1, null, null);
        rb_history.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable2, null, null);
        rb_bookmark.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable3, null, null);
        setRadioButtonStyle(rb_home, rb_history, rb_bookmark);

        MyFragmentActivity mfa = new MyFragmentActivity();
        mfa.switchFragment(getSupportFragmentManager(), toTag, this.fragmentTag);
        this.fragmentTag = toTag;
    }

    // 实现碎片之间的跳转
    class MyFragmentActivity extends FragmentActivity {
        public void switchFragment(FragmentManager fm, String toTag, String foTag) {
            Fragment fo = fm.findFragmentByTag(foTag);
            Fragment to = fm.findFragmentByTag(toTag);
            if (fo != to) {
                fm.beginTransaction().hide(fo).show(to).commit();
            }
        }
    }

    // 初始化布局
    public void initView() {
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_history = (RadioButton) findViewById(R.id.rb_history);
        rb_bookmark = (RadioButton) findViewById(R.id.rb_bookmark);

        rb_home.setOnClickListener(this);
        rb_history.setOnClickListener(this);
        rb_bookmark.setOnClickListener(this);

        // 初始化导航栏颜色和图案
        rb_home.setTextColor(Color.parseColor("#228B22"));
        rb_history.setTextColor(Color.parseColor("#000000"));
        rb_bookmark.setTextColor(Color.parseColor("#000000"));
        Drawable drawable = this.getResources().getDrawable(R.drawable.bottom_home_pressed);
        rb_home.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
        setRadioButtonStyle(rb_home, rb_history, rb_bookmark);
    }

    // 选择跳转碎片
    public void onClick(View v) {
        fromTag = toTag;
        switch (v.getId()) {
            case R.id.rb_history:
                toTag = "historyFragment";
                break;
            case R.id.rb_bookmark:
                toTag = "bookmarkFragment";
                break;
            default: //默认为主页
                toTag = "homeFragment";
                break;
        }
        this.switchFragment(toTag);
    }

    // 设置RadioButton样式、大小等
    public void setRadioButtonStyle(RadioButton rb1, RadioButton rb2, RadioButton rb3) {
        // 设置导航栏图片大小
        RadioButton[] radioButtons = new RadioButton[]{rb1, rb2, rb3};
        for (RadioButton rb : radioButtons) {
            // 挨着给每个RadioButton加入drawable限制边距以控制显示大小
            Drawable[] drawables = rb.getCompoundDrawables();
            // 获取drawables
            Rect r = new Rect(0, 0, drawables[1].getMinimumWidth() * 1 / 3, drawables[1].getMinimumHeight() * 1 / 3);
            // 定义一个Rect边界
            drawables[1].setBounds(r);
            // 添加限制给控件
            rb.setCompoundDrawables(null, drawables[1], null, null);
        }
    }

    //连续点击两次返回按钮即退出程序
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出浏览器", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    //用于传入指定的参数来启动MainActivity
    public static void actionStart(Context context, String fromTag, String toTag) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("fromTag", fromTag);
        intent.putExtra("toTag", toTag);
        context.startActivity(intent);
    }

    //浏览页面打开MainActivity时更新Intent
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    //重新进入MainActivity时，跳转去相应的Fragment
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        fromTag = intent.getStringExtra("fromTag");
        if ( fromTag != null) {  //说明非应用首次打开该活动
            toTag = intent.getStringExtra("toTag");
            this.switchFragment(toTag);
        }
        Log.d("jump", "from:" + fromTag);
        Log.d("jump","to:" + toTag);
    }

}
