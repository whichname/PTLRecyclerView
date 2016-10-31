package com.mrw.wzmrecyclerview.SimpleAdapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/27.
 */
public abstract class SimpleAdapter<T> extends MultiTypeAdapter {

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
    protected int getLayoutIdByType(int viewType) {
        return mLayoutId;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int type, Object data) {
        onBindViewHolder(holder, (T)data);
    }

    /**子类需实现以下方法*/

    protected abstract void onBindViewHolder(ViewHolder holder,T data);


}
