package com.mrw.wzmrecyclerview.Divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mrw.wzmrecyclerview.HeaderAndFooter.HeaderAndFooterAdapter;

/**
 * Created by Administrator on 2016/9/25.
 */
public class LinearItemDecorationHelper extends BaseItemDecorationHelper {

    @Override
    public void onDraw(Canvas c, RecyclerView parent, Drawable divider, int height, int width) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
            drawVertical(c, parent, divider, width);
        } else if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            drawHorizontal(c, parent, divider, height);
        }
    }

    /**
     * 画水平分割线
     */
    private void drawHorizontal(Canvas c, RecyclerView parent, Drawable divider, int height) {
        final int childCount = parent.getChildCount();
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
//            若是头部 || 尾部 || 最后一项，不绘制
            if (isHeaderView(parent,child) || isFooterView(parent,child) ||isLastItem(parent,child))
                continue;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + height;
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    /**
     * 画垂直分割线
     */
    private void drawVertical(Canvas c, RecyclerView parent, Drawable divider, int width) {
        final int childCount = parent.getChildCount();
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
//            若是头部 || 尾部 || 最后一项，不绘制
            if (isHeaderView(parent,child) || isFooterView(parent,child) ||isLastItem(parent,child))
                continue;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + width;
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, int height, int width) {
//        若是头部 || 尾部 || 最后一项，不偏移
        if (isHeaderView(parent,view) || isFooterView(parent,view) ||isLastItem(parent,view))
            return;
//        若是水平布局
        if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL)
            outRect.set(0, 0, width, 0);
        else
            outRect.set(0, 0, 0, height);
    }

    private static boolean isLastItem(RecyclerView parent, View view) {
        if (parent.getAdapter() == null)
            return false;
        int position = parent.getChildAdapterPosition(view);
//        若是头尾部适配器
        if (parent.getAdapter() instanceof HeaderAndFooterAdapter) {
            HeaderAndFooterAdapter adapter = (HeaderAndFooterAdapter) parent.getAdapter();
            return position >= adapter.getHeadersCount() + adapter.getRealItemCount()-1;
        }
        return position >= parent.getAdapter().getItemCount() - 1;
    }

}
