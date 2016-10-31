package com.mrw.wzmrecyclerview.HeaderAndFooter;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HeaderAndFooterAdapter<T extends RecyclerView.Adapter> extends RecyclerView.Adapter {

    private static int BASE_ITEM_TYPE_HEADER = 10000000;
    private static int BASE_ITEM_TYPE_FOOTER = 20000000;

    protected SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    protected SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();

    protected T mRealAdapter;

    protected Context mContext;

    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;

    public HeaderAndFooterAdapter(Context mContext, T mRealAdapter) {
        super();
        this.mContext = mContext;
        this.mRealAdapter = mRealAdapter;
    }

    public T getRealAdapter() {
        return mRealAdapter;
    }

    @Override
    public int getItemCount() {
        return getRealItemCount()+getHeadersCount()+getFootersCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return mHeaderViews.keyAt(position);
        }
        if (isFooterPosition(position)) {
            return mFooterViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mRealAdapter.getItemViewType(position - getHeadersCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        如果是头部
        if (isHeaderType(viewType)) {
            int headerPosition = mHeaderViews.indexOfKey(viewType);
            View headerView = mHeaderViews.valueAt(headerPosition);
            return createHeaderAndFooterViewHolder(headerView);
        }
//        如果是尾部
        if (isFooterType(viewType)) {
            int footerPosition = mFooterViews.indexOfKey(viewType);
            View footerView = mFooterViews.valueAt(footerPosition);
            return createHeaderAndFooterViewHolder(footerView);
        }
        return mRealAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position) || isFooterPosition(position)) {

        } else {
            final int realPosition = position - getHeadersCount();
            mRealAdapter.onBindViewHolder(holder, realPosition);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.OnItemClick(realPosition);
                    }
                });
            }
            if (mOnItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mOnItemLongClickListener.onItemLongClick(realPosition);
                    }
                });
            }
        }
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
                    if (isHeaderPosition(position) || isFooterPosition(position))
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
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) lp;
                layoutParams.setFullSpan(true);
            }
        }
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(BASE_ITEM_TYPE_HEADER++, view);
        notifyDataSetChanged();
    }

    public void addFooterView(View view) {
        mFooterViews.put(BASE_ITEM_TYPE_FOOTER ++,view);
        notifyDataSetChanged();
    }

    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) return;
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index < 0) return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    private RecyclerView.ViewHolder createHeaderAndFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view){};
    }

    protected boolean isHeaderPosition(int position) {
        return position < getHeadersCount();
    }

    protected boolean isFooterPosition(int position) {
        return position >= getHeadersCount()+getRealItemCount();
    }

    protected boolean isHeaderType(int viewType) {
        return mHeaderViews.indexOfKey(viewType) >= 0;
    }

    protected boolean isFooterType(int viewType) {
        return mFooterViews.indexOfKey(viewType) >= 0;
    }

    public int getRealItemCount() {
        return mRealAdapter.getItemCount();
    }

    public  int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

}
