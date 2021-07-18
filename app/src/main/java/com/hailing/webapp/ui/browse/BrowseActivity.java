package com.hailing.webapp.ui.browse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.hailing.webapp.MainActivity;
import com.hailing.webapp.R;
import com.hailing.webapp.logic.dao.BookMarkDao;
import com.hailing.webapp.logic.dao.HistoryDao;
import com.hailing.webapp.logic.model.History;
import com.hailing.webapp.util.Base64Util;
import com.hailing.webapp.util.GetTimeUtil;

import java.util.Objects;

// 浏览页功能
public class BrowseActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private String fromTag;
    private String icon;
    private String url;

    private BookMarkDao bookMarkDao;
    private HistoryDao historyDao;


    private RadioButton rb_go_back;
    private RadioButton rb_go;
    private RadioButton rb_menu;
    private RadioButton rb_home;

    private PopupWindow popupWindow;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        // 初始化布局
        initView();

        //隐藏主题顶部标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //获取数据库引用
        bookMarkDao = new BookMarkDao(this);
        historyDao = new HistoryDao(this);

        WebView webView = findViewById(R.id.browse_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);   //设定webView允许使用JavaScript
        webSettings.setSupportZoom(true);      //设定webView允许使用缩放手势
        webSettings.setLoadWithOverviewMode(true);  //设定webView以概述模式加载页面

        intent = getIntent();
        fromTag = intent.getStringExtra("fromTag");
        url = intent.getStringExtra("url");
        webView.loadUrl(url);  //打开首页点击的网页

        //监听网页图标更新即访问新页面时，增加历史记录
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedIcon(WebView view, Bitmap bitmap) {
                WebBackForwardList webBackForwardList = webView.copyBackForwardList();
                WebHistoryItem webHistoryItem = webBackForwardList.getCurrentItem();

                String newIcon = Base64Util.bitmapToBase64(webHistoryItem.getFavicon());
                String newUrl = webHistoryItem.getOriginalUrl();

                if (!Objects.equals(newIcon, null)) {   //新加载的页面有图标时
                    if (!Objects.equals(icon, newIcon)) {  //避免在渲染页面过程中重复加载相同图标
                        History history = new History();
                        history.setIcon(newIcon);
                        history.setTitle(webHistoryItem.getTitle());
                        history.setUrl(webHistoryItem.getOriginalUrl());
                        history.setTime(GetTimeUtil.getNowTimeString());
                        //如果该网址有当天的访问记录，则先删除再添加，使历史记录排在后面
                        int queryId = historyDao.queryId(history.getUrl(), history.getTime());
                        if (queryId != -1) {
                            historyDao.deleteHistoryById(queryId);
                        }
                        historyDao.addHistory(history);
                        //更新判断条件
                        icon = newIcon;
                        url = newUrl;

                    }
                } else if (!Objects.equals(url, newUrl) && !Objects.equals(webHistoryItem.getTitle(), null)) {
                    //新加载的图片无图标，则当url发生变化和标题加载完成时，增加历史记录
                    History history = new History();
                    history.setIcon("defaultIcon");
                    history.setTitle(webHistoryItem.getTitle());
                    history.setUrl(webHistoryItem.getOriginalUrl());
                    history.setTime(GetTimeUtil.getNowTimeString());
                    //如果该网址有当天的访问记录，则先删除再添加，使历史记录排在后面
                    int queryId = historyDao.queryId(history.getUrl(), history.getTime());
                    if (queryId != -1) {
                        historyDao.deleteHistoryById(queryId);
                    }
                    historyDao.addHistory(history);
                    //更新判断条件
                    icon = "defaultIcon";
                    url = newUrl;
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url == null) return false;

                try {
                    //不是打开网页则调用系统浏览器打开
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                // TODO Auto-generated method stub
                //跳转去浏览页面打开
                webView.loadUrl(url);
                return true;
            }
        });
    }

    //用于传入指定的参数来启动BrowseActivity
    public static void actionStart(Context context, String fromTag, String url) {
        Intent intent = new Intent(context, BrowseActivity.class);
        intent.putExtra("fromTag", fromTag);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    // 初始化底部导航栏按钮
    public void initView() {
        rb_go_back = (RadioButton) findViewById(R.id.rb_go_back);
        rb_go = (RadioButton) findViewById(R.id.rb_go);
        rb_menu = (RadioButton) findViewById(R.id.rb_menu);
        rb_home = (RadioButton) findViewById(R.id.rb_home);

        rb_go_back.setOnClickListener(this);
        rb_go.setOnClickListener(this);
        rb_menu.setOnClickListener(this);
        rb_home.setOnClickListener(this);

        // 设置导航栏按钮大小
        setRadioButtonStyle(rb_go_back, rb_go, rb_menu, rb_home);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 此处添加跳转到上一个页面的代码
            case R.id.rb_go_back:

                break;
            // 此处添加跳转到下一个页面的代码
            case R.id.rb_go:

                break;
            // 此处添加显示菜单栏代码
            case R.id.rb_menu:
                showPopWindow();
                break;
            // 此处添加回到主页代码
            case R.id.rb_home:
                Intent i = new Intent(BrowseActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
            // 此处为添加书签代码
            case R.id.rb_add_bookmark:

                break;
            // 此处为显示书签列表代码
            case R.id.rb_bookmark:

                break;
            // 此处为显示历史记录代码
            case R.id.rb_history:

                break;
        }
    }

    // 显示弹出菜单栏
    public void showPopWindow() {
        // 初始化界面
        view = LayoutInflater.from(BrowseActivity.this).inflate(R.layout.menubar, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(view);

        // 初始化弹出菜单栏按钮
        RadioButton rb_add_bookmark = (RadioButton) view.findViewById(R.id.rb_add_bookmark);
        RadioButton rb_bookmark = (RadioButton) view.findViewById(R.id.rb_bookmark);
        RadioButton rb_history = (RadioButton) view.findViewById(R.id.rb_history);

        // 设置popupWindow按钮样式
        RadioButton[] radioButtons = new RadioButton[]{rb_add_bookmark, rb_bookmark, rb_history};

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

        rb_add_bookmark.setOnClickListener(this);
        rb_bookmark.setOnClickListener(this);
        rb_history.setOnClickListener(this);

        // 显示popupWindow布局
        View popView = LayoutInflater.from(BrowseActivity.this).inflate(R.layout.activity_browse, null);

        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
    }

    // 设置RadioButton样式、大小等
    public void setRadioButtonStyle(RadioButton rb1, RadioButton rb2, RadioButton rb3, RadioButton rb4) {
        // 设置导航栏图片大小
        RadioButton[] radioButtons1 = new RadioButton[]{rb1, rb2};
        RadioButton[] radioButtons2 = new RadioButton[]{rb3, rb4};

        for (RadioButton rb : radioButtons1) {
            // 挨着给每个RadioButton加入drawable限制边距以控制显示大小
            Drawable[] drawables = rb.getCompoundDrawables();
            // 获取drawables
            Rect r = new Rect(0, 0, drawables[1].getMinimumWidth() * 6 / 5, drawables[1].getMinimumHeight() * 6 / 5);
            // 定义一个Rect边界
            drawables[1].setBounds(r);
            // 添加限制给控件
            rb.setCompoundDrawables(null, drawables[1], null, null);
        }

        for (RadioButton rb : radioButtons2) {
            // 挨着给每个RadioButton加入drawable限制边距以控制显示大小
            Drawable[] drawables = rb.getCompoundDrawables();
            // 获取drawables
            Rect r = new Rect(0, 0, drawables[1].getMinimumWidth() * 3 / 5, drawables[1].getMinimumHeight() * 3 / 5);
            // 定义一个Rect边界
            drawables[1].setBounds(r);
            // 添加限制给控件
            rb.setCompoundDrawables(null, drawables[1], null, null);
        }
    }
}