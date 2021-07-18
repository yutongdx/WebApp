package com.hailing.webapp.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hailing.webapp.R;
import com.hailing.webapp.logic.adapter.BookmarkAdapter;
import com.hailing.webapp.logic.dao.BookMarkDao;
import com.hailing.webapp.logic.model.BookMark;

import java.util.ArrayList;
import java.util.List;

// 此碎片实现书签功能
public class BookmarkFragment extends Fragment {

    public BookMarkDao bookMarkDao;

    public void setBookMarkDao(){
        this.bookMarkDao = new BookMarkDao(getActivity());
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        RecyclerView bookmark_list = (RecyclerView) view.findViewById(R.id.bookmark_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        bookmark_list.setLayoutManager(layoutManager);

        setBookMarkDao();
        BookmarkAdapter adapter = new BookmarkAdapter(bookMarkDao.queryAll(),
                                    getActivity(), bookMarkDao );
        bookmark_list.setAdapter(adapter);

        return view;
    }


    //固定数据测试
    private List<BookMark> getBookmarks(){
        List<BookMark> bookMarkList = new ArrayList<>();
        // 网易新闻
        BookMark bookMark1 = new BookMark();
        bookMark1.setId(1);
        bookMark1.setIcon("icon1");
        bookMark1.setTitle("网易新闻");
        bookMark1.setUrl("https://news.163.com/");
        bookMarkList.add(bookMark1);

        // 百度知道
        BookMark bookMark2 = new BookMark();
        bookMark2.setId(2);
        bookMark2.setIcon("icon2");
        bookMark2.setTitle("百度知道");
        bookMark2.setUrl("https://www.baidu.com/");
        bookMarkList.add(bookMark2);

        return bookMarkList;
    }
}
