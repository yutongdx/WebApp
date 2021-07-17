package com.hailing.webapp.ui.browse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.hailing.webapp.R;
import com.hailing.webapp.logic.dao.BookMarkDao;
import com.hailing.webapp.logic.dao.HistoryDao;
import com.hailing.webapp.logic.model.History;
import com.hailing.webapp.util.Base64Util;
import com.hailing.webapp.util.GetTimeUtil;

import java.util.Objects;

public class BrowseActivity extends AppCompatActivity {

    private Intent intent;
    private String fromTag;
    private String icon;
    private String url;

    private BookMarkDao bookMarkDao;
    private HistoryDao historyDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
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
                } else if (!Objects.equals(url, newUrl) && !Objects.equals(webHistoryItem.getTitle(), null)){
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
}