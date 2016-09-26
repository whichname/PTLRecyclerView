package com.mrw.wzmrecyclerview.AutoLoad;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.mrw.wzmrecyclerview.PullToLoad.OnLoadListener;
import com.mrw.wzmrecyclerview.PullToRefresh.PullToRefreshRecyclerView;

/**
 * Created by Administrator on 2016/9/26.
 */
public class AutoLoadRecyclerView extends PullToRefreshRecyclerView {

    public AutoLoadRecyclerView(Context context) {
        super(context);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private View mLoadView;

    //    加载监听
    private OnLoadListener mOnLoadListener = new OnLoadListener() {
        @Override
        public void onStartLoading() {

        }
    };

    private AutoLoadAdapter mAdapter;
    private Adapter mRealAdapter;

//    是否正在加载
    private boolean isLoadingData = false;
//    是否还有更多
    private boolean hasMore = true;

    @Override
    public void setAdapter(Adapter adapter) {
        mRealAdapter = adapter;
        if (adapter instanceof AutoLoadAdapter) {
            mAdapter = (AutoLoadAdapter) adapter;
        } else {
            mAdapter = new AutoLoadAdapter(getContext(), adapter);
        }
        super.setAdapter(mAdapter);
        if (mLoadView != null) {
            mAdapter.setLoadView(mLoadView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //        若数据不满一屏 || 没有更多
        if (getChildCount() >= getAdapter().getItemCount() || !hasMore) {
            mAdapter.setLoadView(null);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.setLoadView(mLoadView);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE
                && mOnLoadListener != null
                && !isLoadingData
                && mLoadView != null
                && hasMore) {
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
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 2
                    && layoutManager.getItemCount() > layoutManager.getChildCount()) {
                isLoadingData = true;
                mOnLoadListener.onStartLoading();
            }
        }
    }

    private int findMax(int[] v) {
        int max = v[0];
        for (int a : v) {
            if (a > max)
                max = a;
        }
        return max;
    }


    /**
     * 结束刷新
     */
    public void completeLoad() {
        if (mOnLoadListener != null)
            mOnLoadListener.onStopLoad();
        isLoadingData = false;
    }

    /**设置没有更多*/
    public void hasNoMore(boolean hasMore) {
        this.hasMore = hasMore;
        //        若数据不满一屏 || 没有更多
        if (getChildCount() >= getAdapter().getItemCount() || !hasMore) {
            mAdapter.setLoadView(null);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.setLoadView(mLoadView);
            mAdapter.notifyDataSetChanged();
        }
    }



    /**
     * 设置监听
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

    /**
     * 设置自定义的加载尾部
     */
    public View setLoadView(int res) {
        mLoadView = LayoutInflater.from(getContext()).inflate(res, this, false);
        if (mAdapter != null) {
            mAdapter.setLoadView(mLoadView);
        }
        return mLoadView;
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


}
