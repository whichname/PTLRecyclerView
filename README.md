# WZMRecyclerView
the recyclerview which is able to pull to refresh and pull to load more

中文博客：http://blog.csdn.net/anyfive/article/details/53020321

## How to use

in android-studio :

compile 'com.mrw:wzmrecyclerview:1.0.4'

## add/remove HeaderView or FooterView

use HeaderAndFooterRecyclerView :
```Java
HeaderAndFooterRecyclerView.addHeaderView(HeaderView);//add header
HeaderAndFooterRecyclerView.removeHeaderView(HeaderView);//remove header

HeaderAndFooterRecyclerView.addFooterView(FooterView);//add footer
HeaderAndFooterRecyclerView.removeFooterView(FooterView);//remove footer

HeaderAndFooterRecyclerView.setOnItemClickListener(OnItemClickListener onItemClickListener);//add onItemClickListener
HeaderAndFooterRecyclerView.setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener);//add onItemLongClickListener
```

![头尾部](https://github.com/whichname/WZMRecyclerView/blob/master/screenshot/HF.gif?raw=true)


![Grid头尾部](https://github.com/whichname/WZMRecyclerView/blob/master/screenshot/HF_GRID.gif?raw=true)


![Staggred头尾部](https://github.com/whichname/WZMRecyclerView/blob/master/screenshot/HF_STAGGRED.gif?raw=true)


## PullToRefresh

use PullToRefreshRecyclerView :

```
PullToRefreshRecyclerView.setOnRefreshListener(OnRefreshListener onRefreshListener);//add OnRefreshListener

PullToRefreshRecyclerView.setRefreshViewCreator(RefreshHeaderCreator refreshHeaderCreator);//use your own RefreshHeaderView

PullToRefreshRecyclerView.setRefreshEnable(boolean refreshEnable);//set pull to refresh able/disable

PullToRefreshRecyclerView.setPullRatio(float pullRatio);

PullToRefreshRecyclerView.completeRefresh();//complete refresh
```

Generally,u just need to set OnRefreshListener ,and use '.completeRefresh()' when refresh completed.

## PullToLoad

use PullToLoadRecyclerView :

```
PullToLoadRecyclerView.setOnLoadListener(OnLoadListener onLoadListener);//add OnLoadListener

PullToLoadRecyclerView.setLoadViewCreator(LoadFooterCreator loadViewCreator);//use your own LoadFooterView

PullToLoadRecyclerView.setLoadEnable(boolean loadMoreEnable);//set pull to load more able/disable

PullToLoadRecyclerView.setPullLoadRatio(float loadRatio);

PullToLoadRecyclerView.completeLoad();//complete load
```

U can use it just like use PullToRefreshRecyclerView.

**Notice: if u want to use PullToLoadRecyclerView,use WZMLinearLayoutManager instead of LinearLayoutManager,use WZMGridLayoutManager instead of GridLayoutManager.**

 
![刷新加载](https://github.com/whichname/WZMRecyclerView/blob/master/screenshot/PTR_PTL.gif?raw=true)

![Grid刷新加载](https://github.com/whichname/WZMRecyclerView/blob/master/screenshot/PTR_PTL_GRID.gif?raw=true)

![Staggred刷新加载](https://github.com/whichname/WZMRecyclerView/blob/master/screenshot/PTR_PTL_STAGGRED.gif?raw=true)

## AutoLoad

use AutoLoadRecyclerView :

```
AutoLoadRecyclerView.setOnLoadListener(OnLoadListener onLoadListener);//add OnLoadListener

AutoLoadRecyclerView.setAutoLoadViewCreator(AutoLoadFooterCreator autoLoadFooterCreator);//use your own LoadFooterView

AutoLoadRecyclerView.completeLoad();//complete load

AutoLoadRecyclerView.setNoMore(boolean noMore);//set has no more data
```


![Grid自动加载](https://github.com/whichname/WZMRecyclerView/blob/master/screenshot/ATL_GRID.gif?raw=true)


## use your own Refresh-Header-View

1. New a class extends RefreshHeaderCreator
2. Override five fuctions:
```
/**
* @param distance:the distance of pull
* @param lastState
* @return able to continue pull?
*/
public abstract boolean onStartPull(float distance, int lastState);

/**
* @param distance:the distance of pull
* @param lastState
* @return able to continue release?
*/
public abstract boolean onReleaseToRefresh(float distance,int lastState);

/**
*call back when start refresh
*/
public abstract void onStartRefreshing();

/**
*callback when complete refresh
*/
public abstract void onStopRefresh();

/**
*return the refresh headerview
*/
public abstract View getRefreshView(Context context,RecyclerView recyclerView);

```
3. Call PullToRefreshRecyclerView.setOnRefreshViewCreator.
 
Example:
```
public class DefaultRefreshHeaderCreator extends RefreshHeaderCreator {

    private View mRefreshView;
    private ImageView iv;
    private TextView tv;

    private int rotationDuration = 200;

    private int loadingDuration = 1000;
    private ValueAnimator ivAnim;

    @Override
    public boolean onStartPull(float distance,int lastState) {
        if (lastState == PullToRefreshRecyclerView.STATE_DEFAULT ) {
            iv.setImageResource(R.drawable.arrow_down);
            iv.setRotation(0f);
            tv.setText("pull to refresh");
        } else if (lastState == PullToRefreshRecyclerView.STATE_RELEASE_TO_REFRESH) {
            startArrowAnim(0);
            tv.setText("pull to refresh");
        }
        return true;
    }

    @Override
    public void onStopRefresh() {
        if (ivAnim != null) {
            ivAnim.cancel();
        }
    }

    @Override
    public boolean onReleaseToRefresh(float distance,int lastState) {
        if (lastState == PullToRefreshRecyclerView.STATE_DEFAULT ) {
            iv.setImageResource(R.drawable.arrow_down);
            iv.setRotation(-180f);
            tv.setText("release to refresh");
        } else if (lastState == PullToRefreshRecyclerView.STATE_PULLING) {
            startArrowAnim(-180f);
            tv.setText("release to refresh");
        }
        return true;
    }

    @Override
    public void onStartRefreshing() {
        iv.setImageResource(R.drawable.loading);
        startLoadingAnim();
        tv.setText("Refreshing...");
    }

    @Override
    public View getRefreshView(Context context, RecyclerView recyclerView) {
        mRefreshView = LayoutInflater.from(context).inflate(R.layout.layout_ptr_ptl,recyclerView,false);
        iv = (ImageView) mRefreshView.findViewById(R.id.iv);
        tv = (TextView) mRefreshView.findViewById(R.id.tv);
        return mRefreshView;
    }

    private void startArrowAnim(float roration) {
        if (ivAnim != null) {
            ivAnim.cancel();
        }
        float startRotation = iv.getRotation();
        ivAnim = ObjectAnimator.ofFloat(startRotation,roration).setDuration(rotationDuration);
        ivAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv.setRotation((Float) animation.getAnimatedValue());
            }
        });
        ivAnim.start();
    }

    private void startLoadingAnim() {
        if (ivAnim != null) {
            ivAnim.cancel();
        }
        ivAnim = ObjectAnimator.ofFloat(0,360).setDuration(loadingDuration);
        ivAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv.setRotation((Float) animation.getAnimatedValue());
            }
        });
        ivAnim.setRepeatMode(ObjectAnimator.RESTART);
        ivAnim.setRepeatCount(ObjectAnimator.INFINITE);
        ivAnim.setInterpolator(new LinearInterpolator());
        ivAnim.start();
    }

}
```

## user your own Load-Footer-View:

1. New a class extends LoadFooterCreator;
2. Override five fuctions:
```
/**
* @param distance:the distance of pull
* @param lastState
* @return able to continue pull?
*/
public abstract boolean onStartPull(float distance, int lastState);

/**
* @param distance:the distance of pull
* @param lastState
* @return able to continue release?
*/
public abstract boolean onReleaseToRefresh(float distance,int lastState);

/**
*call back when start load
*/
protected abstract void onStartLoading();

/**
*callback when complete load
*/
public abstract void onStopRefresh();

/**
*return the load footerview
*/
protected abstract View getLoadView(Context context, RecyclerView recyclerView);

```
3. Call PullToLoadRecyclerView.setLoadViewCreator.
 
Example:
```
public class DefaultLoadFooterCreator extends LoadFooterCreator {

    private View mLoadView;
    private ImageView iv;
    private TextView tv;

    private int rotationDuration = 200;

    private int loadingDuration = 1000;
    private ValueAnimator ivAnim;

    @Override
    public boolean onStartPull(float distance, int lastState) {
        if (lastState == PullToLoadRecyclerView.STATE_DEFAULT) {
            iv.setImageResource(R.drawable.arrow_down);
            iv.setRotation(-180f);
            tv.setText("pull to load");
        } else if (lastState == PullToLoadRecyclerView.STATE_RELEASE_TO_LOAD) {
            startArrowAnim(-180f);
            tv.setText("pull to load");
        }
        return true;
    }

    @Override
    public boolean onReleaseToLoad(float distance, int lastState) {
        if (lastState == PullToLoadRecyclerView.STATE_DEFAULT ) {
            iv.setImageResource(R.drawable.arrow_down);
            iv.setRotation(0f);
            tv.setText("release to load more");
        } else if (lastState == PullToLoadRecyclerView.STATE_PULLING) {
            startArrowAnim(0f);
            tv.setText("release to load more");
        }
        return true;
    }

    @Override
    public void onStartLoading() {
        iv.setImageResource(R.drawable.loading);
        startLoadingAnim();
        tv.setText("Loading...");
    }

    @Override
    public void onStopLoad() {
        if (ivAnim != null) {
            ivAnim.cancel();
        }
    }

    @Override
    public View getLoadView(Context context, RecyclerView recyclerView) {
        mLoadView = LayoutInflater.from(context).inflate(R.layout.layout_ptr_ptl,recyclerView,false);
        iv = (ImageView) mLoadView.findViewById(R.id.iv);
        tv = (TextView) mLoadView.findViewById(R.id.tv);
        return mLoadView;
    }

    private void startArrowAnim(float roration) {
        if (ivAnim != null) {
            ivAnim.cancel();
        }
        float startRotation = iv.getRotation();
        ivAnim = ObjectAnimator.ofFloat(startRotation, roration).setDuration(rotationDuration);
        ivAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv.setRotation((Float) animation.getAnimatedValue());
            }
        });
        ivAnim.start();
    }

    private void startLoadingAnim() {
        if (ivAnim != null) {
            ivAnim.cancel();
        }
        ivAnim = ObjectAnimator.ofFloat(0,360).setDuration(loadingDuration);
        ivAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv.setRotation((Float) animation.getAnimatedValue());
            }
        });
        ivAnim.setRepeatMode(ObjectAnimator.RESTART);
        ivAnim.setRepeatCount(ObjectAnimator.INFINITE);
        ivAnim.setInterpolator(new LinearInterpolator());
        ivAnim.start();
    }

}

```

## use your own AutoLoad-Footer-View

1. New a class extends AutoLoadFooterCreator;
2. Override two fuctions:
```
/***
 * return the autoload footer view
 */
protected abstract View getLoadView(Context context, RecyclerView recyclerView);

/***
 * return the nomore footer view
 */
protected abstract View getNoMoreView(Context context,RecyclerView recyclerView);

```
3. Call AutoLoadRecyclerView.setAutoLoadViewCreator.

Example:
```
public class DefaultAutoLoadFooterCreator extends AutoLoadFooterCreator {

    protected View mAutoLoadFooter;
    protected ImageView iv;
    protected ValueAnimator ivAnim;
    private int loadingDuration = 1000;

    @Override
    protected View getLoadView(Context context, RecyclerView recyclerView) {
        if (mAutoLoadFooter == null) {
            mAutoLoadFooter = LayoutInflater.from(context).inflate(R.layout.layout_auto_load_footer,recyclerView,false);
            iv = (ImageView) mAutoLoadFooter.findViewById(R.id.iv);
            startLoadingAnim();
        }
        return mAutoLoadFooter;
    }

    @Override
    protected View getNoMoreView(Context context, RecyclerView recyclerView) {
        return null;
    }

    private void startLoadingAnim() {
        if (ivAnim != null) {
            ivAnim.cancel();
        }
        ivAnim = ObjectAnimator.ofFloat(0, 360).setDuration(loadingDuration);
        ivAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv.setRotation((Float) animation.getAnimatedValue());
            }
        });
        ivAnim.setRepeatMode(ObjectAnimator.RESTART);
        ivAnim.setRepeatCount(ObjectAnimator.INFINITE);
        ivAnim.setInterpolator(new LinearInterpolator());
        ivAnim.start();
    }

}
```


    
