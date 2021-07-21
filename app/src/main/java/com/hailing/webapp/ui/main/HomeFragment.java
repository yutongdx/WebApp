package com.hailing.webapp.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hailing.webapp.R;
import com.hailing.webapp.ui.RefreshWebView;
import com.hailing.webapp.ui.browse.BrowseActivity;
import com.hailing.webapp.util.UrlUtil;

public class HomeFragment extends Fragment {

    private SwipeRefreshLayout swipeRefresh;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //设置主页搜索页面和跳转处理
        RefreshWebView webView = view.findViewById(R.id.home_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);   //设定webView允许使用JavaScript
        webSettings.setSupportZoom(true);      //设定webView允许使用缩放手势
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadWithOverviewMode(true);  //设定webView以概述模式加载页面
        webView.loadUrl("https://cn.bing.com/");  //主页加载必应首页
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url == null) return false;

                try {
                    //不是打开网页则调用系统浏览器打开
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        getActivity().finish();
                        return false;
                    }
                } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                if (url.contains("FORM=BEHPTB")) {
                    webView.loadUrl(url);
                } else { //跳转去浏览页面打开
                    BrowseActivity.actionStart(getActivity(), "homeFragment", url);
                }
                return true;
            }
        });

        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.home_swipeRefresh);
        swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl("https://cn.bing.com/");
                swipeRefresh.setRefreshing(false);
            }
        });
        swipeRefresh.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                return webView.getScrollY()>0;
            }
        });


        //顶部搜索
        EditText search = (EditText)view.findViewById(R.id.home_search);
        Button homeGoto = (Button)view.findViewById(R.id.home_goto);
        homeGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = UrlUtil.converKeywordLoadOrSearch(search.getText().toString());
                BrowseActivity.actionStart(getActivity(), "homeFragment", url);
            }
        });

        return view;
    }

}