package com.hailing.webapp.logic.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hailing.webapp.R;
import com.hailing.webapp.logic.dao.HistoryDao;
import com.hailing.webapp.logic.model.History;
import com.hailing.webapp.ui.main.HistoryFragment;
import com.hailing.webapp.util.Base64Util;

import java.util.List;
import java.util.Objects;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<History> mHistoryList;
    private LocalBroadcastManager localBroadcastManager;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View historyView;
        ImageView historyIcon;
        TextView historyTitle;
        TextView historyUrl;
        TextView historyTime;
        ImageButton historyDelete;

        public ViewHolder(View view) {
            super(view);
            historyView = view;
            historyIcon = view.findViewById(R.id.history_icon);
            historyTitle = view.findViewById(R.id.history_title);
            historyUrl = view.findViewById(R.id.history_url);
            historyTime = view.findViewById(R.id.history_time);
            historyDelete = view.findViewById(R.id.history_delete);
        }
    }

    public HistoryAdapter(List<History> historyList) {
        mHistoryList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        HistoryDao historyDao = new HistoryDao(view.getContext());

        holder.historyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                History history = mHistoryList.get(position);
                Toast.makeText(v.getContext(), "跳转网页:" + history.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.historyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                History history = mHistoryList.get(position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("删除提醒");
                dialog.setMessage("确认删除该历史记录？");
                dialog.setCancelable(true);
                localBroadcastManager = LocalBroadcastManager.getInstance(viewGroup.getContext());
                final Intent intent = new Intent(HistoryFragment.LOCAL_BROADCAST);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        historyDao.deleteHistoryById(history.getId());
                        intent.putExtra("refresh_history", true);   //通知historyFragment,让它去调用refresh()方法
                        localBroadcastManager.sendBroadcast(intent);   //发送本地广播,通知fragment该刷新了
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        History history = mHistoryList.get(i);
        String icon = history.getIcon();
        if (Objects.equals(icon, "defaultIcon")) {
            viewHolder.historyIcon.setImageResource(R.drawable.icon_default);
        } else {
            viewHolder.historyIcon.setImageBitmap(Base64Util.base64ToBitmap(history.getIcon()));
        }
        viewHolder.historyTitle.setText(history.getTitle());
        viewHolder.historyUrl.setText(history.getUrl());
        viewHolder.historyTime.setText(history.getTime());
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }

}
