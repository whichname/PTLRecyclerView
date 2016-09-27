package com.mrw.wzmrecyclerview.SimpleAdapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/27.
 */
public abstract class SimpleAdapter<T> extends MultiTypeAdapter<T> {

    protected int mLayoutId;

    public SimpleAdapter(Context context,ArrayList<T> datas,int layoutId) {
        super(context,datas);
        this.mLayoutId = layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    protected int getLayoutIdByType(int viewType) {
        return mLayoutId;
    }



}
