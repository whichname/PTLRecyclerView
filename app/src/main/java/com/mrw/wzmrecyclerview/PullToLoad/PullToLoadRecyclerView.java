package com.mrw.wzmrecyclerview.PullToLoad;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.mrw.wzmrecyclerview.PullToRefresh.PullToRefreshRecyclerView;

/**
 * Created by Administrator on 2016/9/21.
 */
public class PullToLoadRecyclerView extends PullToRefreshRecyclerView {
    public PullToLoadRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public PullToLoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private int mState = STATE_DEFAULT;
    //    初始
    private final static int STATE_DEFAULT = 0;
    //    正在上拉
    private final static int STATE_PULLING = 1;
    //    松手加载
    private final static int STATE_RELEASE_TO_LOAD = 2;
    //    加载中
    private final static int STATE_LOADING = 3;


    //   位于加载View底部的view，通过改变其高度来上拉
    private View bottomView;

    private View mLoadView;
    //    用于测量高度的加载View
    private View loadView;
    private int mLoadViewHeight = 0;

    private float mFirstY = 0;
    private boolean mPulling = false;

    //    是否可以上拉加载
    private boolean isAbleToLoad = true;

    //    回弹动画
    private ValueAnimator valueAnimator;

    //    加载监听
    private OnLoadListener mOnLoadListener = new OnLoadListener() {
        @Override
        public void onStartLoading() {

        }
    };


    private PullToLoadAdapter mAdapter;
    private Adapter mRealAdapter;

    @Override
    public void setAdapter(Adapter adapter) {
        mRealAdapter = adapter;
        if (adapter instanceof PullToLoadAdapter) {
            mAdapter = (PullToLoadAdapter) adapter;
        } else {
            mAdapter = new PullToLoadAdapter(getContext(), adapter);
        }
        super.setAdapter(mAdapter);
        if (mLoadView != null) {
            addHeaderView(loadView);
            mAdapter.setLoadView(mLoadView);
            mAdapter.setBottomView(bottomView);
        }
    }

    private void init(Context context) {
        if (bottomView == null) {
            bottomView = new View(context);
//            该view的高度不能为0，否则将无法判断是否已滑动到底部
            bottomView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
        }
    }

    /**
     * 在layout的时候，隐藏加载尾部
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (loadView != null && mLoadViewHeight == 0) {
            mLoadViewHeight = loadView.getMeasuredHeight();
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) getLayoutParams();
            marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin - mLoadViewHeight - 1);
            setLayoutParams(marginLayoutParams);
//            高度测量之后将其从头部中去掉
            removeHeaderView(loadView);
        }
//        若数据不满一屏
        if (getChildCount() >= getAdapter().getItemCount()) {
            if (mLoadView.getVisibility() != GONE) {
                mLoadView.setVisibility(GONE);
                mState = STATE_DEFAULT;
                replyPull();
            }
        } else {
            if (mLoadView.getVisibility() != VISIBLE) {
                mLoadView.setVisibility(VISIBLE);
                mState = STATE_DEFAULT;
                replyPull();
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        若不允许拖动
        if (!isAbleToLoad) return super.onTouchEvent(e);
//        若加载尾部为空，不处理
        if (mLoadView == null)
            return super.onTouchEvent(e);
//        若当前加载尾部被隐藏(不足一屏)
        if (mLoadView.getVisibility() != VISIBLE)
            return super.onTouchEvent(e);

//        若回弹动画正在进行，不处理
        if (valueAnimator != null && valueAnimator.isRunning())
            return super.onTouchEvent(e);
        if (mLoadViewHeight == 0) {
            mLoadViewHeight = mLoadView.getMeasuredHeight();
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!mPulling) {
                    if (isBottom()) {
//                        当listview滑动到最低部时，记录当前y坐标
                        mFirstY = e.getRawY();
                    }
//                    若listview没有滑动到最低部，不处理
                    else
                        break;
                }
                float distance = (int) ((mFirstY - e.getRawY()) * 0.5);
//                若向上滑动(此时加载胃部已隐藏)，不处理
                if (distance < 0) break;
                mPulling = true;
//                若加载中，距离需加上尾部的高度
                if (mState == STATE_LOADING) {
                    distance += mLoadViewHeight;
                }
                setState(distance);
                return true;
            case MotionEvent.ACTION_UP:
                replyPull();
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 判断是否滑动到底部
     */
    private boolean isBottom() {
        return !ViewCompat.canScrollVertically(this, 1);
    }


    /**
     * 判断当前是拖动中还是松手刷新
     * 刷新中不在此处判断，在手指抬起时才判断
     */
    private void setState(float distance) {
//        刷新中，状态不变
        if (mState == STATE_LOADING) {

        } else if (distance == 0) {
            mState = STATE_DEFAULT;
        }
//        松手刷新
        else if (Math.abs(distance) >= mLoadViewHeight) {
            mState = STATE_RELEASE_TO_LOAD;
            if (mOnLoadListener != null)
//                若不能继续下拉
                if (!mOnLoadListener.onReleaseToLoad(distance))
                    return;
        }
//        正在拖动
        else if (Math.abs(distance) < mLoadViewHeight) {
            mState = STATE_PULLING;
            if (mOnLoadListener != null)
//                若不能继续下拉
                if (!mOnLoadListener.onStartPull(distance))
                    return;
        }
        startPull(distance);
        scrollToPosition(getLayoutManager().getItemCount() - 1);
    }

    /**
     * 拖动或回弹时，改变低部的margin
     */
    private void startPull(float distance) {
//            该view的高度不能为0，否则将无法判断是否已滑动到底部
        if (distance < 1)
            distance = 1;
        if (bottomView != null) {
            LayoutParams layoutParams = (LayoutParams) bottomView.getLayoutParams();
            layoutParams.height = (int) distance;
            bottomView.setLayoutParams(layoutParams);
        }
    }

    /**
     * 松手回弹
     */
    private void replyPull() {
        mPulling = false;
//        回弹位置
        float destinationY = 0;
//        判断当前状态
//        若是刷新中，回弹
        if (mState == STATE_LOADING) {
            destinationY = mLoadViewHeight;
        }
//        若是松手刷新，刷新，回弹
        else if (mState == STATE_RELEASE_TO_LOAD) {
//            改变状态
            mState = STATE_LOADING;
//            刷新
            if (mOnLoadListener != null)
                mOnLoadListener.onStartLoading();
//            若在onStartRefreshing中调用了completeRefresh方法，将不会滚回初始位置，因此这里需加个判断
            if (mState != STATE_LOADING) return;
            destinationY = mLoadViewHeight;
        } else if (mState == STATE_DEFAULT || mState == STATE_PULLING) {
            mState = STATE_DEFAULT;
        }

        LayoutParams layoutParams = (RecyclerView.LayoutParams) bottomView.getLayoutParams();
        float distance = layoutParams.height;
        if (distance <= 0) return;

        valueAnimator = ObjectAnimator.ofFloat(distance, destinationY).setDuration((long) (distance * 0.5));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float nowDistance = (float) animation.getAnimatedValue();
                startPull(nowDistance);
            }
        });
        valueAnimator.start();
    }

    /**
     * 结束刷新
     */
    public void completeLoad() {
        if (mOnLoadListener != null)
            mOnLoadListener.onStopLoad();
        mState = STATE_DEFAULT;
        replyPull();
    }

    /**
     * 设置监听
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

    /**
     * 设置自定义的加载尾部
     */
    public View setLoadView(int res) {
        mLoadView = LayoutInflater.from(getContext()).inflate(res, this, false);
        loadView = LayoutInflater.from(getContext()).inflate(res, this, false);
        if (mAdapter != null) {
//            为了测量高度
            addHeaderView(loadView);
            mAdapter.setLoadView(mLoadView);
            mAdapter.setBottomView(bottomView);
        }
        return mLoadView;
    }

    /**获得加载中View和底部填充view的个数，用于绘制分割线*/
    public int getLoadViewCount() {
        if (mLoadView != null)
            return 2;
        return 0;
    }

    public void setLoadEnable(boolean isAbleToLoad) {
        this.isAbleToLoad = isAbleToLoad;
    }

    /**获得真正的adapter*/
    @Override
    public Adapter getRealAdapter() {
        return mRealAdapter;
    }

}
