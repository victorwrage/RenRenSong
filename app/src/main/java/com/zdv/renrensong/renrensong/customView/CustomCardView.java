package com.zdv.renrensong.renrensong.customView;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/6/8 11:20
 */

public class CustomCardView extends CardView {
    float mLastMotionX,mLastMotionY;
    public CustomCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                mLastMotionX = event.getX();
                mLastMotionY = event.getY();
            case MotionEvent.ACTION_MOVE:



                float deltaX = mLastMotionX ;
                float oldScrollX = getScrollX();
                float scrollX = oldScrollX + deltaX;
                mLastMotionX += scrollX - (int) scrollX;
                scrollTo((int) scrollX, getScrollY());

                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEventCompat.ACTION_POINTER_DOWN:
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
