package com.mrw.wzmrecyclerview.LayoutManager;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/11/2.
 */
public class WZMLinearLayoutManager extends StaggeredGridLayoutManager {


    public WZMLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setGapStrategy(GAP_HANDLING_NONE);
    }

    private WZMLinearLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    public WZMLinearLayoutManager() {
        super(1,VERTICAL);
        setGapStrategy(GAP_HANDLING_NONE);
    }

    public WZMLinearLayoutManager(int orientation) {
        super(1,orientation);
        setGapStrategy(GAP_HANDLING_NONE);
    }

    public WZMLinearLayoutManager(int orientation, boolean reverseLayout) {
        super(1,orientation);
        setReverseLayout(reverseLayout);
        setGapStrategy(GAP_HANDLING_NONE );
    }



}
