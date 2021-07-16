package com.hailing.webapp.ui.browse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.hailing.webapp.R;

public class BrowseActivity extends AppCompatActivity {

    Intent intent;
    String fromTag;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        WebView webView = findViewById(R.id.browse_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);   //设定webView允许使用JavaScript
        webSettings.setSupportZoom(true);      //设定webView允许使用缩放手势
        webSettings.setLoadWithOverviewMode(true);  //设定webView以概述模式加载页面

        intent = getIntent();
        fromTag = intent.getStringExtra("fromTag");
        url = intent.getStringExtra("url");
        webView.loadUrl(url);  //打开首页点击的网页
        System.out.println("网址："+webView.getUrl());
        System.out.println("标题："+webView.getTitle());


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url == null) return false;

                try{
                    //不是打开网页则调用系统浏览器打开
                    if(!url.startsWith("http://") && !url.startsWith("https://")){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                }catch (Exception e){//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                // TODO Auto-generated method stub
                //跳转去浏览页面打开
                webView.loadUrl(url);
                System.out.println("网址："+webView.getUrl());
                System.out.println("标题："+webView.getTitle());
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