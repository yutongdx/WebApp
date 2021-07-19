package com.hailing.webapp.logic.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.hailing.webapp.R;
import com.hailing.webapp.logic.dao.BookMarkDao;
import com.hailing.webapp.logic.model.BookMark;
import com.hailing.webapp.ui.browse.BrowseActivity;
import com.hailing.webapp.util.Base64Util;

import java.util.List;


public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {
    private List<BookMark> mBookmarkList;
    private Context mcontext;
    private BookMarkDao mbookMarkDao;

    public BookmarkAdapter(List<BookMark> bookMarkList, Context context, BookMarkDao bookMarkDao){
        this.mBookmarkList = bookMarkList;
        this.mcontext = context;
        this.mbookMarkDao = bookMarkDao;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView title;
        TextView url;
        ImageButton bookmark_edit;
        ImageButton bookmark_delete;

        public ViewHolder(View view){
            super(view);
            icon = (ImageView) view.findViewById(R.id.icon);
            title = (TextView) view.findViewById(R.id.title);
            url = (TextView) view.findViewById(R.id.url);
            bookmark_edit = (ImageButton) view.findViewById(R.id.bookmark_edit);
            bookmark_delete = (ImageButton) view.findViewById(R.id.bookmark_delete);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_bookmark_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookMark bookMark = mBookmarkList.get(holder.getAdapterPosition());
                BrowseActivity.actionStart(mcontext, "bookmarkFragment", bookMark.getUrl());
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookMark bookMark = mBookmarkList.get(position);
        if (bookMark.getIcon().equals("defaultIcon")){
            holder.icon.setImageResource(R.drawable.icon_default);
        } else {
            holder.icon.setImageBitmap(Base64Util.base64ToBitmap(bookMark.getIcon()));
        }
        holder.title.setText(bookMark.getTitle());
        holder.url.setText(bookMark.getUrl());
        holder.bookmark_edit.setImageResource(R.drawable.item_edit);
        holder.bookmark_delete.setImageResource(R.drawable.item_delete);

        // 修改按钮监听
        holder.bookmark_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(mcontext);
                View editView = factory.inflate(R.layout.recyclerview_edit_alterdialog, null);
                EditText editNewTitle = (EditText) editView.findViewById(R.id.editNewTitle);
                EditText editNewUrl = (EditText) editView.findViewById(R.id.editNewUrl);
                AlertDialog.Builder dialog = new AlertDialog.Builder(mcontext);

                //设置默认值
                BookMark bookMark = mBookmarkList.get(holder.getAdapterPosition());
                editNewTitle.setText(bookMark.getTitle());
                editNewUrl.setText(bookMark.getUrl());

                //设置属性
                dialog.setTitle("修改书签");
                dialog.setView(editView);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取修改内容
                        bookMark.setTitle(editNewTitle.getText().toString());
                        bookMark.setUrl(editNewUrl.getText().toString());
                        // 修改数据库数据
                        mbookMarkDao.updateBookmark(bookMark);
                        // 刷新页面
                        mBookmarkList.get(holder.getAdapterPosition()).setTitle(bookMark.getTitle());
                        mBookmarkList.get(holder.getAdapterPosition()).setUrl(bookMark.getUrl());
                        notifyItemChanged(holder.getAdapterPosition());
                        Toast.makeText(mcontext, "修改成功", Toast.LENGTH_SHORT).show();
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

        // 删除按钮监听
        holder.bookmark_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookMark bookMark = mBookmarkList.get(holder.getAdapterPosition());
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("删除提醒");
                dialog.setMessage("确认删除该书签？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 修改数据库数据
                        mbookMarkDao.deleteById(bookMark.getId());
                        // 刷新页面
                        mBookmarkList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        Toast.makeText(mcontext, "删除成功", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public int getItemCount(){
        return mBookmarkList.size();
    }
}
