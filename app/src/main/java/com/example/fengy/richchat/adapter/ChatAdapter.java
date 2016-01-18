package com.example.fengy.richchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.fengy.richchat.R;
import com.example.fengy.richchat.widget.RichTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天页面适配器
 *
 * @author 小孩子xm 2016年1月18日 18:40:09
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final Context mActivity;
    private List<String> mDataSource;
    private OnItemClickListener mItemClickListener;

    public ChatAdapter(Context context, List<String> stringList) {
        this.mActivity = context;
        this.mDataSource = new ArrayList<>();
        mDataSource.addAll(stringList);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(mActivity);
        final View sView = mInflater.inflate(R.layout.listitem_chat, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String content = mDataSource.get(position);
        holder.richTextView.setRichText(content);

    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    @Override
    public long getItemId(int position) {
        return mDataSource.get(position).hashCode();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        RichTextView richTextView;

        public ViewHolder(View view) {
            super(view);
            richTextView = (RichTextView) view.findViewById(R.id.itemText);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, mDataSource.get(getLayoutPosition()));
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, String face);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    // Add
    public void addMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        mDataSource.add(msg);
        notifyDataSetChanged();
    }

}