package com.mrw.wzmrecyclerview.Divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Administrator on 2016/9/26.
 */
public class StaggeredItemDecorationHelper extends BaseItemDecorationHelper {

    @Override
    public void onDraw(Canvas c, RecyclerView parent, Drawable divider, int height, int width) {

        drawVertical(c, parent, divider, width);
        drawHorizontal(c, parent, divider, height, width);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, int height, int width) {

//        若是头部 || 尾部，不偏移
        if (isHeaderView(parent, view) || isFooterView(parent, view)) {
            outRect.set(0,0,0,0);
            return;
        }
//      若是第一行且最后一列，不偏移
        if (isLastColumn(parent, view) && isFirstRaw(parent, view)) {
            outRect.set(0,0,0,0);
            return;
        }
//        若是最后一列，只向下偏移
        if (isLastColumn(parent, view)) {
            outRect.set(0,height,0,0);
            return;
        }
//        若是最后一行，只向右偏移
        if (isFirstRaw(parent, view)) {
            outRect.set(0, 0, width, 0);
            return;
        }

        outRect.set(0, height, width, 0);
    }

    /**
     * 绘制垂直分割线
     */
    private void drawVertical(Canvas canvas, RecyclerView parent, Drawable divider, int width) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
//            若是头部 || 尾部 || 最后一列，不绘制
            if (isHeaderView(parent, child) || isFooterView(parent, child) || isLastColumn(parent, child))
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
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
//            若是头部 || 尾部 || 第一行，不绘制
            if (isHeaderView(parent, child) || isFooterView(parent, child) || isFirstRaw(parent, child))
                continue;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin
                    + width;
            int top = child.getTop() - params.topMargin - height;
            int bottom = top + height;
            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
    }


    /**是否是最后一列*/
    private boolean isLastColumn(RecyclerView parent,View view) {
        StaggeredGridLayoutManager.LayoutParams childLP = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        return getSpanCount(parent) - 1 == childLP.getSpanIndex();
    }

    /**是不是第一行*/
    private boolean isFirstRaw(RecyclerView parent,View view) {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
        int position = parent.getChildAdapterPosition(view);
        position -= getHeadersCount(parent);
        return position < layoutManager.getSpanCount();
    }

    private int getSpanCount(RecyclerView parent) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
        return staggeredGridLayoutManager.getSpanCount();
    }


}
