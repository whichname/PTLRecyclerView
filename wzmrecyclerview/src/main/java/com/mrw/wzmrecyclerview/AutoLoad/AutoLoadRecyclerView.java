package com.mrw.wzmrecyclerview.AutoLoad;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.mrw.wzmrecyclerview.DefaultHeaderAndFooterCreator.DefaultAutoLoadFooterCreator;
import com.mrw.wzmrecyclerview.PullToLoad.OnLoadListener;
import com.mrw.wzmrecyclerview.PullToRefresh.PullToRefreshRecyclerView;

/**
 * Created by Administrator on 2016/9/26.
 */
public class AutoLoadRecyclerView extends PullToRefreshRecyclerView {

    public AutoLoadRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private View mLoadView;
    private AutoLoadAdapter mAdapter;
    private Adapter mRealAdapter;

    private boolean mIsLoading = false;
    private boolean mLoadMoreEnable = true;
    private boolean mNoMore = false;
    private View mNoMoreView;

    private AutoLoadFooterCreator mAutoLoadFooterCreator;
    private OnLoadListener mOnLoadListener;

    private void init(Context context) {
        mAutoLoadFooterCreator = new DefaultAutoLoadFooterCreator();
        mLoadView = mAutoLoadFooterCreator.getLoadView(context,this);
        mNoMoreView = mAutoLoadFooterCreator.getNoMoreView(context,this);
    }


    @Override
    public void setAdapter(Adapter adapter) {
        mRealAdapter = adapter;
        if (adapter instanceof AutoLoadAdapter) {
            mAdapter = (AutoLoadAdapter) adapter;
        } else{
            mAdapter = new AutoLoadAdapter(getContext(),adapter);
        }
        super.setAdapter(mAdapter);
        if (mLoadView != null) {
            mAdapter.setLoadView(mLoadView);
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE  && !mIsLoading && mLoadMoreEnable && mLoadView != null) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 && layoutManager.getItemCount() > layoutManager.getChildCount() && !mNoMore ) {
                mIsLoading = true;
                if (mOnLoadListener != null)
                    mOnLoadListener.onStartLoading(mRealAdapter.getItemCount());
            }
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**完成加载*/
    public void completeLoad() {
        mIsLoading = false;
        setLoadingViewGone();
        mRealAdapter.notifyDataSetChanged();
    }

    /**没有更多*/
    public void setNoMore(boolean noMore) {
        mIsLoading = false;
        mNoMore = noMore;
        if (mNoMore) {
            if (mNoMoreView != null)
                mAdapter.setLoadView(mNoMoreView);
            else
                mAdapter.setLoadView(null);
        }
        else if (mLoadView != null)
            mAdapter.setLoadView(mLoadView);
        mRealAdapter.notifyDataSetChanged();
    }

    /**设置加载监听*/
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

    /**获得加载中View和底部填充view的个数，用于绘制分割线*/
    public int getLoadViewCount() {
        if (mLoadView != null)
            return 1;
        return 0;
    }

    /**获得真正的adapter*/
    @Override
    public Adapter getRealAdapter() {
        return mRealAdapter;
    }

    /**
     * 设置自定义的加载尾部
     */
    public void setAutoLoadViewCreator(AutoLoadFooterCreator autoLoadFooterCreator) {
        if (autoLoadFooterCreator == null) {
            this.mAutoLoadFooterCreator = null;
            mLoadView = null;
            mNoMoreView = null;
            mAdapter.setLoadView(mLoadView);
            mRealAdapter.notifyDataSetChanged();
            return;
        }
        this.mAutoLoadFooterCreator = autoLoadFooterCreator;
        mLoadView = autoLoadFooterCreator.getLoadView(getContext(),this);
        mAdapter.setLoadView(mLoadView);
        mNoMoreView = autoLoadFooterCreator.getNoMoreView(getContext(),this);
        mRealAdapter.notifyDataSetChanged();
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading = isLoading;
    }


}
