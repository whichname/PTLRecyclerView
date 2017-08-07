package com.jimi_wu.ptlrecyclerview.HeaderAndFooter;

import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.jimi_wu.ptlrecyclerview.PullToRefresh.PullToRefreshRecyclerView;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by wzm on 2017/8/3.
 */

public class HeaderAndFooterObserver<T extends HeaderAndFooterAdapter> extends RecyclerView.AdapterDataObserver {

    protected RecyclerView mRecyclerView;
    protected View mEmptyView;
    protected RecyclerView.Adapter mRealAdapter;
    protected T mAdapter;

    //   当有header和footer的时候，是否显示emptyview
    protected boolean showEmptyViewHasHF = false;

    public HeaderAndFooterObserver(RecyclerView mRecyclerView, View mEmptyView, RecyclerView.Adapter mRealAdapter, T mAdapter, boolean showEmptyViewHasHF) {
        this.mRecyclerView = mRecyclerView;
        this.mEmptyView = mEmptyView;
        this.mRealAdapter = mRealAdapter;
        this.mAdapter = mAdapter;
        this.showEmptyViewHasHF = showEmptyViewHasHF;
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    public void setShowEmptyViewHasHF(boolean showEmptyViewHasHF) {
        this.showEmptyViewHasHF = showEmptyViewHasHF;
    }

    public void setAdapter(T adapter) {
        this.mAdapter = adapter;
    }

    public void setRealAdapter(RecyclerView.Adapter realAdapter) {
        this.mRealAdapter = realAdapter;
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        setEmptyViewVisible();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        super.onItemRangeChanged(positionStart, itemCount, payload);
        setEmptyViewVisible();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        setEmptyViewVisible();
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        setEmptyViewVisible();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        setEmptyViewVisible();
    }

    @Override
    public void onChanged() {
        setEmptyViewVisible();
    }

    protected void setEmptyViewVisible() {
        if (mAdapter == null) return;
        if (mRealAdapter != mAdapter)
            mRealAdapter.notifyDataSetChanged();
        if (mEmptyView != null) {
            int itemCount = getItemCount();
            if (itemCount == 0) {
                mEmptyView.setVisibility(VISIBLE);
                if (mRecyclerView.getVisibility() != INVISIBLE)
                    mRecyclerView.setVisibility(INVISIBLE);
            } else {
                mEmptyView.setVisibility(GONE);
                if (mRecyclerView.getVisibility() != VISIBLE)
                    mRecyclerView.setVisibility(VISIBLE);
            }
        }
    }

    protected int getItemCount() {
        int itemCount = 0;
        if (!showEmptyViewHasHF) {
            if (mAdapter instanceof HeaderAndFooterAdapter)
                itemCount += mAdapter.getHeadersCount() + mAdapter.getFootersCount();
//                若是下拉刷新，需减掉下拉头
            if (mRecyclerView instanceof PullToRefreshRecyclerView)
                itemCount -= ((PullToRefreshRecyclerView) mRecyclerView).getRefreshViewCount();
        }
        itemCount += mRealAdapter.getItemCount();
        return itemCount;
    }


}
