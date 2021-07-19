package com.hailing.webapp.ui.main;


import android.content.DialogInterface;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hailing.webapp.MainActivity;
import com.hailing.webapp.R;
import com.hailing.webapp.logic.adapter.BookmarkAdapter;
import com.hailing.webapp.logic.dao.BookMarkDao;
import com.hailing.webapp.logic.model.BookMark;
import com.hailing.webapp.ui.browse.BrowseActivity;

import java.util.Collections;
import java.util.List;

// 此碎片实现书签功能
public class BookmarkFragment extends Fragment implements View.OnClickListener {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private List<BookMark> bookMarkList;
    private BookMarkDao bookMarkDao;

    public void setBookMarkDao(){
        this.bookMarkDao = new BookMarkDao(getActivity());
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        setBookMarkDao();
        ImageButton bookmarkGoBack = (ImageButton)view.findViewById(R.id.bookmark_go_back);
        Button bookmarkClear = (Button)view.findViewById(R.id.bookmark_clear);
        bookmarkGoBack.setOnClickListener(this);
        bookmarkClear.setOnClickListener(this);
        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.bookmark_swipeRefresh);
        swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefresh.setRefreshing(false);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.bookmark_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        BookmarkAdapter adapter = new BookmarkAdapter(bookMarkDao.queryAll(),
                                    getActivity(), bookMarkDao );
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bookmark_go_back:
                MainActivity mainActivity = (MainActivity) getActivity();
                String from = mainActivity.fromTag;
                mainActivity.fromTag = mainActivity.toTag;
                switch (from) {
                    case "homeFragment":
                        mainActivity.toTag = "homeFragment";
                        mainActivity.switchFragment(mainActivity.toTag);
                        break;
                    case "historyFragment":
                        mainActivity.toTag = "historyFragment";
                        mainActivity.switchFragment(mainActivity.toTag);
                        break;
                    case "bookmarkFragment":
                        mainActivity.toTag = "bookmarkFragment";
                        mainActivity.switchFragment(mainActivity.toTag);
                        break;
                    default:  //返回网页
                        mainActivity.toTag = from;
                        BrowseActivity.actionStart(mainActivity, "bookmarkFragment", mainActivity.toTag);
                        break;
                }
                break;
            case R.id.bookmark_clear:
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("清空提醒");
                dialog.setMessage("确认清空所有书签？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bookMarkDao.deleteAllBookmark();
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
        if (bookMarkList != null) {
            bookMarkList.clear();
        }
        bookMarkList = bookMarkDao.queryAll();
        Collections.reverse(bookMarkList);
        BookmarkAdapter adapter = new BookmarkAdapter(bookMarkList, getActivity(), bookMarkDao);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

}
