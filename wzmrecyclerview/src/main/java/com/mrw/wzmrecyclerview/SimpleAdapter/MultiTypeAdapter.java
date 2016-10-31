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
public abstract class MultiTypeAdapter extends RecyclerView.Adapter<ViewHolder> {

    protected String TAG;
    protected Context mContext;
    protected ArrayList mDatas;

    public MultiTypeAdapter(Context mContext, ArrayList mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.TAG = getClass().getSimpleName();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutIdByType(viewType);
        return ViewHolder.get(mContext,parent,layoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBindViewHolder(holder,getItemViewType(position),mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    /**子类需实现以下三个方法*/

    protected abstract int getLayoutIdByType(int viewType);

    @Override
    public abstract int getItemViewType(int position);

    protected abstract void onBindViewHolder(ViewHolder holder,int type,Object data);

}
