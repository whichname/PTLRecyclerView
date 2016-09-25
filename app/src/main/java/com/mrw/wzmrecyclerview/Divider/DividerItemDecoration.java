package com.mrw.wzmrecyclerview.Divider;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.View;

import com.mrw.wzmrecyclerview.PullToLoad.PullToLoadRecyclerView;
import com.mrw.wzmrecyclerview.PullToRefresh.PullToRefreshRecyclerView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/25.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;

    private Drawable mDivider;
    private int mHeight;
    private int mWidth;


    public DividerItemDecoration(Context mContext, int res) {
        this.mContext = mContext;
        this.mDivider = getDivider(res);
        mHeight = dp2px(5);
        mWidth = dp2px(5);
    }

    public DividerItemDecoration(Context mContext, Drawable mDivider, int mHeight) {
        this.mContext = mContext;
        this.mDivider = mDivider;
        this.mHeight = mHeight;
        mWidth = dp2px(5);
    }

    public DividerItemDecoration(int mWidth, Drawable mDivider, Context mContext) {
        this.mWidth = mWidth;
        this.mDivider = mDivider;
        this.mContext = mContext;
        mHeight = dp2px(5);
    }

    public DividerItemDecoration(Context mContext, Drawable mDivider, int mHeight, int mWidth) {
        this.mContext = mContext;
        this.mDivider = mDivider;
        this.mHeight = mHeight;
        this.mWidth = mWidth;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    /**画水平线*/
    private void drawHorizontal(Canvas canvas,RecyclerView parent) {
        final int childCount = parent.getChildCount();
        int refreshHeaderCount = getRefreshViewCount(parent);
        int loadFooterCount = getLoadViewCount(parent);
        int itemCount = parent.getAdapter().getItemCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(child) < refreshHeaderCount
                    || parent.getChildAdapterPosition(child) >= itemCount - loadFooterCount - 1)
                continue;

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mWidth;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    /**画垂直线*/
    private void drawVertical(Canvas c,RecyclerView parent) {
        final int childCount = parent.getChildCount();
        int refreshHeaderCount = getRefreshViewCount(parent);
        int loadFooterCount = getLoadViewCount(parent);
        int itemCount = parent.getAdapter().getItemCount();
        for (int i = 0; i < childCount; i++)
        {
            final View child = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(child) < refreshHeaderCount
                    || parent.getChildAdapterPosition(child) >= itemCount - loadFooterCount - 1)
                continue;

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mWidth;

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }



    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int itemPosition = parent.getChildAdapterPosition(view);
        int refreshHeaderCount = getRefreshViewCount(parent);
        int loadFooterCount = getLoadViewCount(parent);
        int itemCount = parent.getAdapter().getItemCount();

//            若是头部刷新View  || 底部加载View
        if (itemPosition < refreshHeaderCount
                || itemPosition >= itemCount - loadFooterCount-1) {
            outRect.set(0,0,0,0);
            return;
        }


//        如果是表格布局或者瀑布流
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            int spanCount = getSpanCount(parent);
            if (isLastRaw(parent, itemPosition - refreshHeaderCount, spanCount, itemCount - refreshHeaderCount - loadFooterCount))// 如果是最后一行，则不需要绘制底部
            {
                outRect.set(0, 0, mWidth, 0);
            } else if (isLastColum(parent, itemPosition - refreshHeaderCount, spanCount, itemCount - refreshHeaderCount - loadFooterCount))// 如果是最后一列，则不需要绘制右边
            {
                outRect.set(0, 0, 0, mHeight);
            } else
            {
                outRect.set(0, 0, mWidth,
                        mHeight);
            }
        }
//        线性布局
        else if (layoutManager instanceof LinearLayoutManager) {
//            若是水平布局
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL)
                    outRect.set(0,0,mWidth,0);
            else
                    outRect.set(0,0,0,mHeight);
        }
    }

    /**获得刷新头部数量*/
    private int getRefreshViewCount(RecyclerView recyclerView) {
        if (recyclerView instanceof PullToRefreshRecyclerView) {
            return ((PullToRefreshRecyclerView) recyclerView).getRefreshViewCount();
        }
        return 0;
    }

    /**获得加载尾部数量*/
    private int getLoadViewCount(RecyclerView recyclerView) {
        if (recyclerView instanceof PullToLoadRecyclerView) {
            return ((PullToLoadRecyclerView) recyclerView).getLoadViewCount();
        }
        return 0;
    }

    /**获得列数*/
    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager)
        {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    /**是否是最后一列*/
    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager)
        {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL)
            {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else
            {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    /**是否是最后一行*/
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager)
        {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL)
            {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0)
                {
                    return true;
                }
            }
        }
        return false;
    }


    private Drawable getDivider(int res) {
        Resources resources = mContext.getResources();
        return resources.getDrawable(res);
    }

    private int dp2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,mContext.getResources().getDisplayMetrics());
    }




}
