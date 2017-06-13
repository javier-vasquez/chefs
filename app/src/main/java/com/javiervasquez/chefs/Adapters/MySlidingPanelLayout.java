package com.javiervasquez.chefs.Adapters;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by JavierVasquez on 17/11/2016.
 */

public class MySlidingPanelLayout extends SlidingPaneLayout {

    public MySlidingPanelLayout(Context context) {
        super(context);
    }

    public MySlidingPanelLayout(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public MySlidingPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (this.isOpen() && event.getX() > (2 * getWidth() / 3)) {
            this.closePane();
        }
        if (this.isOpen() && event.getX() < (2 * getWidth() / 3)) {
            return super.onInterceptTouchEvent(event);
        }
        if ((!this.isOpen()) && event.getX() < (getWidth() / 5)) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
//        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.i("MySlidingPanelLayout", "onTouch:");
//        if (this.isOpen()) {
//            this.closePane();
//        }else{
//            this.closePane();
//        }
//        return false; // here it returns false so that another event's listener
//        // should be called, in your case the MapFragment
//        // listener
        return super.onTouchEvent(event);
    }
}