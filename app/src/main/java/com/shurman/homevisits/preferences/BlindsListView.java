package com.shurman.homevisits.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class BlindsListView extends ListView {
    private boolean mIsTouchIntercepted = false;

    public BlindsListView(Context context, AttributeSet attrs) { super(context, attrs); }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsTouchIntercepted) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                mIsTouchIntercepted = false;
            }
            onTouchEvent(ev);
        } else {
            mIsTouchIntercepted = super.onInterceptTouchEvent(ev);
        }
        return false;
    }
}
