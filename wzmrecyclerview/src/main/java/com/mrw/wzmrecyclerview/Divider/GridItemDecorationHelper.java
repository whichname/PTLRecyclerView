package com.mrw.wzmrecyclerview.Divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/9/25.
 */
public class GridItemDecorationHelper extends BaseItemDecorationHelper {


    @Override
    public void onDraw(Canvas c, RecyclerView parent, Drawable divider, int height, int width) {
        drawVertical(c, parent, divider, width);
        drawHorizontal(c, parent, divider, height, width);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, int height, int width) {
//        若是头部 || 尾部，不偏移
        if (isHeaderView(parent, view) || isFooterView(parent, view))
            return;
//      若是最后一行且最后一列，不偏移
        if (isLastColumn(parent, view, getSpanCount(parent)) && isLastRaw(parent,view,getSpanCount(parent),parent.getAdapter().getItemCount()))
            return;
//        若是最后一列，只向下偏移
        if (isLastColumn(parent, view, getSpanCount(parent))) {
            outRect.set(0,0,0,height);
            return;
        }
//        若是最后一行，只向右偏移
        if (isLastRaw(parent, view, getSpanCount(parent), parent.getAdapter().getItemCount())) {
            outRect.set(0, 0, width, 0);
            return;
        }
        outRect.set(0, 0, width, height);
    }

    /**
     * 绘制垂直分割线
     */
    private void drawVertical(Canvas canvas, RecyclerView parent, Drawable divider, int width) {
        final int childCount = parent.getChildCount();
        final int spanCount = getSpanCount(parent);
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
//            若是头部 || 尾部 || 最后一列，不绘制
            if (isHeaderView(parent, child) || isFooterView(parent, child) || isLastColumn(parent, child, spanCount))
                continue;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            int left = child.getRight() + params.rightMargin;
            int right = left + width;
            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
    }

    /**
     * 绘制水平分割线
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent, Drawable divider, int height, int width) {
        final int childCount = parent.getChildCount();
        final int spanCount = getSpanCount(parent);
        final int itemCount = parent.getAdapter().getItemCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
//            若是头部 || 尾部 || 最后一行，不绘制
            if (isHeaderView(parent, child) || isFooterView(parent, child) || isLastRaw(parent, child, spanCount, itemCount))
                continue;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin
                    + width;
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + height;
            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
    }


    /**
     * 是不是最后一列
     */
    private boolean isLastColumn(RecyclerView parent, View view, int spanCount) {
        int position = parent.getChildAdapterPosition(view);
        position -= getHeadersCount(parent);
        if ((position + 1) % spanCount == 0) {
            return true;
        }
        return false;
    }

    /**
     * 是不是最后一行
     */
    private boolean isLastRaw(RecyclerView parent, View view, int spanCount, int itemCount) {
        int position = parent.getChildAdapterPosition(view);
        itemCount = itemCount - getHeadersCount(parent) - getFootersCount(parent) - getLoadViewCount(parent);
        position -= getHeadersCount(parent);
//        如果刚好最后一行是满的
        if (itemCount % spanCount == 0) {
            position += spanCount;
        }
        itemCount = itemCount - itemCount % spanCount;
        if (position >= itemCount)
            return true;
        return false;
    }

    /**
     * 获得列数
     */
    private int getSpanCount(RecyclerView parent) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        return layoutManager.getSpanCount();
    }

}
