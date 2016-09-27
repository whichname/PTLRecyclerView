package com.mrw.wzmrecyclerview.HeaderAndFooter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.mrw.wzmrecyclerview.AutoLoad.AutoLoadAdapter;
import com.mrw.wzmrecyclerview.AutoLoad.AutoLoadRecyclerView;
import com.mrw.wzmrecyclerview.PullToLoad.PullToLoadAdapter;
import com.mrw.wzmrecyclerview.PullToRefresh.PullToRefreshRecyclerView;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HeaderAndFooterRecyclerView extends RecyclerView {
    public HeaderAndFooterRecyclerView(Context context) {
        super(context);
    }

    public HeaderAndFooterRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderAndFooterRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected HeaderAndFooterAdapter mAdapter;
    protected Adapter mRealAdapter;

    private View mEmptyView;
//   当有header和footer的时候，是否显示emptyview
    private boolean showEmptyViewHasHF = false;
    private final DataObserver mDataObserver = new DataObserver();

    @Override
    public void setAdapter(Adapter adapter) {
        mRealAdapter = adapter;
        if (adapter instanceof HeaderAndFooterAdapter) {
            mAdapter = (HeaderAndFooterAdapter) adapter;
        }
        else {
            mAdapter = new HeaderAndFooterAdapter(getContext(),adapter);
        }
        super.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    public void addHeaderView(View view) {
        if (null == view) {
            throw new IllegalArgumentException("the view to add must not be null !");
        } else if (mAdapter == null) {
            throw new IllegalStateException("u must set a adapter first !");
        } else {
            mAdapter.addHeaderView(view);
        }
    }

    public void addFooterView(View view) {
        if (null == view) {
            throw new IllegalArgumentException("the view to add must not be null !");
        } else if (mAdapter == null) {
            throw new IllegalStateException("u must set a adapter first !");
        } else {
            mAdapter.addFooterView(view);
        }
    }

    public void removeHeaderView(View view) {
        if (null == view) {
            throw new IllegalArgumentException("the view to remove must not be null !");
        } else if (mAdapter == null) {
            throw new IllegalStateException("u must set a adapter first !");
        } else {
            mAdapter.removeHeaderView(view);
        }
    }
    public void removeFooterView(View view) {
        if (null == view) {
            throw new IllegalArgumentException("the view to remove must not be null !");
        } else if (mAdapter == null) {
            throw new IllegalStateException("u must set a adapter first !");
        } else {
            mAdapter.removeFooterView(view);
        }
    }

    /**获得真正的adapter*/
    public Adapter getRealAdapter() {
        return mRealAdapter;
    }

    /**设置空白view*/
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    /**设置空白view*/
    public void setEmptyView(View emptyView,boolean showEmptyViewHasHF) {
        this.mEmptyView = emptyView;
        this.showEmptyViewHasHF = showEmptyViewHasHF;
        mDataObserver.onChanged();
    }

    /**数据监听*/
    private class DataObserver extends AdapterDataObserver{

        @Override
        public void onChanged() {
            if (getAdapter() == null) return;
            Adapter realAdapter = ((HeaderAndFooterAdapter)getAdapter()).getRealAdapter();
            realAdapter.notifyDataSetChanged();
            if (mEmptyView == null) return;
            int itemCount = 0;
            if (!showEmptyViewHasHF) {
                Adapter adapter = getAdapter();
                if (adapter instanceof HeaderAndFooterAdapter)
                    itemCount += ((HeaderAndFooterAdapter) adapter).getHeadersCount() + ((HeaderAndFooterAdapter) adapter).getFootersCount();
//                若是下拉刷新，需减掉下拉头
                if (HeaderAndFooterRecyclerView.this instanceof PullToRefreshRecyclerView)
                    itemCount -= ((PullToRefreshRecyclerView) HeaderAndFooterRecyclerView.this).getRefreshViewCount();
            }
            itemCount += realAdapter.getItemCount();
            if (itemCount == 0) {
                mEmptyView.setVisibility(VISIBLE);
            } else {
                mEmptyView.setVisibility(GONE);
            }
        }

    }





}
