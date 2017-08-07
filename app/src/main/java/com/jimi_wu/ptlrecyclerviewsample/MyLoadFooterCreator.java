package com.jimi_wu.ptlrecyclerviewsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jimi_wu.ptlrecyclerview.DefaultHeaderAndFooterCreator.DefaultLoadFooterCreator;


/**
 * Created by wzm on 2017/8/4.
 */

public class MyLoadFooterCreator extends DefaultLoadFooterCreator {

    private View mNoMoreView;

    @Override
    protected View getNoMoreView(Context context, RecyclerView recyclerView) {
        return null;
    }


}
