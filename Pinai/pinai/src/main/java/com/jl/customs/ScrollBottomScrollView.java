package com.jl.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2015/5/19.
 */

public class ScrollBottomScrollView extends ScrollView {
    public ScrollBottomScrollView(Context context) {
        super(context);
      //  Log.e("Range", computeVerticalScrollRange() + "..1.." + getHeight());
    }

    public ScrollBottomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
       // Log.e("Range", computeVerticalScrollRange() + "..2.." + getHeight());
    }

    public ScrollBottomScrollView(Context context, AttributeSet attrs,int defStyle) {
        super(context, attrs, defStyle);
      //  Log.e("Range", computeVerticalScrollRange() + "..3.."+getHeight());
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
       // Log.e("Range", computeVerticalScrollRange() + "..onOverScrolled.." + getHeight());
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
       // Log.e("Range", computeVerticalScrollRange() + "..onWindowFocusChanged.." + getHeight());
    }

//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt){
//        Log.e("Range", computeVerticalScrollRange() + "");
//        if(t + getHeight() >=  computeVerticalScrollRange()){
//    //滑动到底部，doSomething();
//        }
//    }

}
