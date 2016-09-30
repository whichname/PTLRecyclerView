package com.mrw.wzmrecyclerview.SimpleAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mrw.wzmrecyclerview.HeaderAndFooter.OnItemClickListener;
import com.mrw.wzmrecyclerview.HeaderAndFooter.OnItemLongClickListener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/27.
 */
public abstract class MultiTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;
    protected ArrayList<T> mDatas;

    public MultiTypeAdapter(Context mContext, ArrayList<T> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutIdByType(viewType);
        return ViewHolder.get(mContext,parent,layoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBindViewHolder(holder,mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    protected abstract int getLayoutIdByType(int viewType);

    protected abstract void onBindViewHolder(ViewHolder holder,T data);

}
