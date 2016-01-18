package com.example.fengy.richchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fengy.richchat.bean.Face;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.example.fengy.richchat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情适配器
 *
 * @author 小孩子xm 2016年1月18日 18:40:09
 */
public class FaceItemAdapter extends RecyclerView.Adapter<FaceItemAdapter.ViewHolder> {

    private final Context mActivity;
    private List<Face> mDataSource;
    private OnItemClickListener mItemClickListener;

    private DisplayImageOptions options;

    public FaceItemAdapter(Context context, List<Face> faces) {
        this.mActivity = context;
        this.mDataSource = new ArrayList<>();
        mDataSource.addAll(faces);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(mActivity);
        final View sView = mInflater.inflate(R.layout.listitem_face, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Face face = mDataSource.get(position);

        ImageLoader.getInstance().displayImage("assets://face/" + face.getId() + ".png", holder.vFace, options);
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

        ImageView vFace;

        public ViewHolder(View view) {
            super(view);
            vFace = (ImageView) view.findViewById(R.id.itemImage);

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
        void onItemClick(View view, Face face);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

}