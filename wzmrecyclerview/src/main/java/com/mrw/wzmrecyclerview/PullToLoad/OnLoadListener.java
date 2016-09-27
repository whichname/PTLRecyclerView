package com.mrw.wzmrecyclerview.PullToLoad;

/**
 * Created by Administrator on 2016/9/21.
 */
public abstract class OnLoadListener {

    /**
     * 上拉
     * @param distance 距离
     * @return 是否继续上拉
     */
    public boolean onStartPull(float distance) {
        return true;
    }

    /**
     * 松手加载
     * @param distance 距离
     * @return 是否继续上拉
     */
    public boolean onReleaseToLoad(float distance) {
        return true;
    }

    /**开始加载*/
    public abstract void onStartLoading();

    /**加载结束*/
    public void onStopLoad() {

    }

}
