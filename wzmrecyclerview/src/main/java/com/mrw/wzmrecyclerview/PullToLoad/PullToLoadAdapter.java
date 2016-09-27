package com.mrw.wzmrecyclerview.PullToLoad;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.mrw.wzmrecyclerview.HeaderAndFooter.HeaderAndFooterAdapter;

/**
 * Created by Administrator on 2016/9/23.
 */
public class PullToLoadAdapter<T extends RecyclerView.Adapter> extends HeaderAndFooterAdapter {

    private static final int ITEM_TYPE_LOAD = 30000000;
    private static final int ITEM_TYPE_BOTTOM = ITEM_TYPE_LOAD+1;

    private View mLoadView;
    private View mBottomView;

    protected T mRealAdapter;

    public PullToLoadAdapter(Context mContext, T mRealAdapter) {
        super(mContext, mRealAdapter);
        this.mRealAdapter = mRealAdapter;
    }

    public T getRealAdapter() {
        return mRealAdapter;
    }

    @Override
    public int getItemCount() {
        if (mLoadView == null && mBottomView == null)
            return super.getItemCount();
        if (mLoadView == null || mBottomView == null)
            return super.getItemCount()+1;
        return super.getItemCount()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadPosition(position)) {
            return ITEM_TYPE_LOAD;
        }
        if (isBottomPosition(position)) {
            return ITEM_TYPE_BOTTOM;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD)
            return new RecyclerView.ViewHolder(mLoadView) {};
        if (viewType == ITEM_TYPE_BOTTOM)
            return new RecyclerView.ViewHolder(mBottomView) {};
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isLoadPosition(position) || isBottomPosition(position)) return;
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
                    if (isHeaderPosition(position) || isFooterPosition(position) || isLoadPosition(position) || isBottomPosition(position))
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
        if (isHeaderPosition(position) || isFooterPosition(position) || isLoadPosition(position) || isBottomPosition(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) lp;
                layoutParams.setFullSpan(true);
            }
        }
    }


    private boolean isLoadPosition(int position) {
        if (mLoadView == null) return false;
        return position == getItemCount()-2;
    }

    private boolean isBottomPosition(int position) {
        if (mBottomView == null) return false;
        return position == getItemCount()-1;
    }

    public void setLoadView(View loadView) {
        this.mLoadView = loadView;
    }

    public void setBottomView(View bottomView) {
        this.mBottomView = bottomView;
    }

}
