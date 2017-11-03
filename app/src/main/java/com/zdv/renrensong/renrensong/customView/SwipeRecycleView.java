package com.zdv.renrensong.renrensong.customView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.util.RecycleViewUtil;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/6/5 14:50
 */

public class SwipeRecycleView extends RelativeLayout {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView tvLoadingText;
    //x上次保存的
    private int mLastMotionX;
    //y上次保存的
    private int mLastMotionY;
    //滑动状态
    private int mPullState;
    //上滑
    private int PULL_UP_STATE = 2;
    private int PULL_FINISH_STATE = 0;
    //当前滑动的距离，偏移量
    private int curTransY;
    //尾部的高度
    private int footerHeight;
    //内容布局
    private View contentView;
    //尾布局
    private View footerView;
    private LinearLayout linearView;
    //是否上拉加载更多
    private boolean isLoadNext = false;
    //是否在加载中
    private boolean isLoading = false;
    private OnSwipeRecyclerViewListener onSwipeRecyclerViewListener;
    private boolean isCancelLoadNext = false;

    public SwipeRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            //手指触摸屏幕
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            //手指移动
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastMotionX;
                int deltaY = y - mLastMotionY;
                //这里是判断左右滑动和上下滑动，如果是上下滑动和滑动了一定的距离，被认为是上下滑动
                if (Math.abs(deltaX) < Math.abs(deltaY) && Math.abs(deltaY) > 10) {
                    //进入条件判断，如果isRreshViewScroll返回true，则事件被拦截
                    if (isRefreshViewScroll(deltaY)) {
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isRefreshViewScroll(int deltaY) {
        //条件1：deltaY<0，现在处于上下拉的状态；条件2：RecyclerView是否到达底部；
        //条件3：curTransY<=footerHeight是下拉的偏移量;条件4：是否处于上拉或者下拉状态
        if (deltaY < 0 && RecycleViewUtil.isBottom(recyclerView) && curTransY <= footerHeight && !isLoading && !isCancelLoadNext) {
            //处于下拉状态
            mPullState = PULL_UP_STATE;
            isLoading = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float deltaY = y - mLastMotionY;
                if (mPullState == PULL_UP_STATE) {
                    //算出偏移量
                    curTransY += deltaY;
                    //如果偏移量大于footerView的高度，把下拉的高度赋给偏移量
                    if (Math.abs(curTransY) > Math.abs(footerHeight)) {
                        curTransY = -footerHeight;
                    }
                    //把View整体向上滑动
                    linearView.setTranslationY(curTransY);
                    if (Math.abs(curTransY) == Math.abs(footerHeight)) {
                        isLoadNext = true;
                    }
                }
                mLastMotionY = y;
                return true;
            //在这里UP与CANCEL是一样的
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isLoadNext) {
                    //设置footerView的变化
                    changeFooterState(true);
                    mPullState = PULL_FINISH_STATE;
                    //设置上拉监听
                    if (onSwipeRecyclerViewListener != null) {
                        onSwipeRecyclerViewListener.onLoadNext();
                    } else {
                        //如果没有拉到底，则再次把尾部隐藏
                        hideTranslationY();
                        isLoading = false;
                    }
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void initView(Context context) {

        linearView = new LinearLayout(context);
        linearView.setOrientation(LinearLayout.VERTICAL);
        final LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(linearView, linearParams);
        contentView = LayoutInflater.from(context).inflate(R.layout.swiperecycle_lay, null);
        swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swiperefreshlayout);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerview);
        footerView = LayoutInflater.from(context).inflate(R.layout.recycle_footer_view, null);
        tvLoadingText = (TextView) footerView.findViewById(R.id.loading_more_text);
        //设置SwipeRefreshLayout的监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    isLoading = true;
                    swipeRefreshLayout.setRefreshing(true);
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            if (onSwipeRecyclerViewListener != null) {
                                onSwipeRecyclerViewListener.onRefresh();
                            }
                            isLoading = false;
                        }
                    }, 2000);
                }
            }
        });
        linearView.addView(contentView);
        linearView.addView(footerView);
        //测量并设置各个布局的高度
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = getHeight();
                if (height != 0) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ViewGroup.LayoutParams recycleParams = contentView.getLayoutParams();
                    recycleParams.height = height;
                    contentView.setLayoutParams(recycleParams);
                    ViewGroup.LayoutParams footerParams = tvLoadingText.getLayoutParams();
                    footerHeight = footerParams.height;
                    ViewGroup.LayoutParams contentParams = linearView.getLayoutParams();
                    contentParams.height = height + footerHeight;
                    linearView.setLayoutParams(contentParams);
                    // 设置偏移量为0
                    curTransY = 0;
                }
            }
        });

    }

    //数据改变完之后，调用这方法，重置各种数值
    public void onLoadFinish() {
        if (curTransY == 0) {
            return;
        }
        isLoading = false;
        hideTranslationY();
    }

    //用动画的方式把footerView隐藏
    private void hideTranslationY() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(linearView, "translationY", curTransY, 0).setDuration(1000);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                curTransY = 0;
                changeFooterState(false);
            }
        });
    }

    private void changeFooterState(boolean loading) {
        if (loading) {
            tvLoadingText.setText("正在努力的加载中...");
        } else {
            tvLoadingText.setText("加载更多");
        }
    }

    public void setOnSwipeRecyclerViewListener(OnSwipeRecyclerViewListener onSwipeRecyclerViewListener) {
        this.onSwipeRecyclerViewListener = onSwipeRecyclerViewListener;
    }

    public interface OnSwipeRecyclerViewListener {
        public void onRefresh();

        public void onLoadNext();
    }
}
