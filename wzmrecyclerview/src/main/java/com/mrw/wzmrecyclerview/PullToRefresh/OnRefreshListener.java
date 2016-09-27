package com.mrw.wzmrecyclerview.PullToRefresh;

/**
 * Created by Administrator on 2016/9/21.
 */
public abstract class OnRefreshListener {

    /**
     * 下拉
     * @param distance 距离
     * @return 是否继续下拉
     */
    public boolean onStartPull(float distance) {
        return true;
    }

    /**
     * 松手刷新
     * @param distance 距离
     * @return 是否继续下拉
     */
    public boolean onReleaseToRefresh(float distance) {
        return true;
    }

    /**开始刷新*/
    public abstract void onStartRefreshing();

    /**刷新结束*/
    public void onStopRefresh() {

    }


}
