package com.mrw.wzmrecyclerview.LayoutManager;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/11/2.
 */
public class WZMGridLayoutManager extends StaggeredGridLayoutManager {

    public WZMGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setGapStrategy(GAP_HANDLING_NONE );
    }

    private WZMGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    public WZMGridLayoutManager(int spanCount) {
        super(spanCount,VERTICAL);
        setGapStrategy(GAP_HANDLING_NONE);
    }

    public WZMGridLayoutManager(int spanCount, int orientation,
                             boolean reverseLayout) {
        super(spanCount, orientation);
        setReverseLayout(reverseLayout);
        setGapStrategy(GAP_HANDLING_NONE );
    }

}
