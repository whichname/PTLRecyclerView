package com.mrw.wzmrecyclerview.AutoLoad;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.mrw.wzmrecyclerview.HeaderAndFooter.HeaderAndFooterAdapter;

/**
 * Created by Administrator on 2016/9/26.
 */
public class AutoLoadAdapter<T extends RecyclerView.Adapter> extends HeaderAndFooterAdapter {

    private static int ITEM_TYPE_LOAD = 30000000;

    private View mLoadView;

    protected T mRealAdapter;

    public AutoLoadAdapter(Context mContext, T mRealAdapter) {
        super(mContext, mRealAdapter);
        this.mRealAdapter = mRealAdapter;
    }

    public T getRealAdapter() {
        return mRealAdapter;
    }

    @Override
    public int getItemCount() {
        if (mLoadView != null)
            return super.getItemCount() + 1;
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadPosition(position)) {
            return ITEM_TYPE_LOAD;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD)
            return new RecyclerView.ViewHolder(mLoadView) {};
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isLoadPosition(position)) return;
        super.onBindViewHolder(holder, position);
    }

    /**解决GridLayoutManager问题*/
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRealAdapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeaderPosition(position) || isFooterPosition(position) || isLoadPosition(position))
                        return gridLayoutManager.getSpanCount();
                    return 1;
                }
            });
        }

    }

    /**解决瀑布流布局问题*/
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mRealAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderPosition(position) || isFooterPosition(position) || isLoadPosition(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) lp;
                layoutParams.setFullSpan(true);
            }
        }
    }


    private boolean isLoadPosition(int position) {
        if (mLoadView == null) return false;
        return position == getItemCount()-1;
    }

    public void setLoadView(View loadView) {
        ITEM_TYPE_LOAD++;
        this.mLoadView = loadView;
        notifyDataSetChanged();
    }

    public View getLoadView() {
        return this.mLoadView;
    }

}
