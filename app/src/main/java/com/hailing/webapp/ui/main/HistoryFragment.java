package com.hailing.webapp.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hailing.webapp.R;
import com.hailing.webapp.logic.adapter.HistoryAdapter;
import com.hailing.webapp.logic.dao.HistoryDao;
import com.hailing.webapp.logic.model.History;

import java.util.Collections;
import java.util.List;

// 此碎片实现历史记录功能
public class HistoryFragment extends Fragment implements View.OnClickListener {

    private SwipeRefreshLayout swipeRefresh;
    private HistoryDao historyDao;
    private RecyclerView recyclerView;
    private List<History> historyList;

    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;    //本地广播接收者
    private LocalBroadcastManager localBroadcastManager;   //本地广播管理者,用于注册广播

    /**
     * 发送本地广播的action
     */
    public static final String LOCAL_BROADCAST = "com.hailing.webapp.LOCAL_BROADCAST";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        historyDao = new HistoryDao(view.getContext());
        ImageButton historyGoBack = (ImageButton)view.findViewById(R.id.history_go_back);
        Button historyClear = (Button)view.findViewById(R.id.history_clear);
        historyGoBack.setOnClickListener(this);
        historyClear.setOnClickListener(this);

        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.history_swipeRefresh);
        swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        recyclerView = view.findViewById(R.id.history_list);
        LinearLayoutManager layoutManage = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManage);

        //获取LocalBroadcastManager   本地广播管理者实例
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localReceiver = new LocalReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(LOCAL_BROADCAST);   //添加action
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);   //注册本地广播监听器

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_go_back:
                //TODO 待添加页面跳转相关功能
                break;
            case R.id.history_clear:
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("清空提醒");
                dialog.setMessage("确认清空所有历史记录？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        historyDao.deleteAllHistory();
                        refresh();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        refresh();
    }

    public void refresh() {
        if (historyList != null) {
            historyList.clear();
        }
        historyList = historyDao.queryAll();
        Collections.reverse(historyList);
        HistoryAdapter adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);
        swipeRefresh.setRefreshing(false);
    }

    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(LOCAL_BROADCAST)){
                boolean refreshHistory = intent.getBooleanExtra("refresh_history",false);

                //如果是接收到需要更新历史记录的广播，则去执行该方法
                if(refreshHistory){
                    refresh();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);    //取消广播的注册
    }

}
