package com.jl.atys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import com.jl.basic.AtySupport;

public abstract class AtyBaseGuide extends AtySupport {
	//1.定义一个手势识别器
	private GestureDetector detector;

	protected SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//2.实例化这个手势识别器
		detector = new GestureDetector(this, new SimpleOnGestureListener(){

			/**
			 * 当我们的手指在上面滑动的时候回调
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
								   float velocityX, float velocityY) {

				//屏蔽在X滑动很慢的情形

				if(Math.abs(velocityX)<50){
					//showToast("滑动得太慢了");
					return true;
				}

				//屏蔽斜滑这种情况
				if(Math.abs((e2.getRawY() - e1.getRawY())) > 300){
					//showToast("不能这样滑");

					return true;
				}

				if((e2.getRawX() - e1.getRawX())> 100 ){
					//显示上一个页面：从左往右滑动
					System.out.println("显示上一个页面：从左往右滑动");
					showPre();
					return true;

				}

				if((e1.getRawX()-e2.getRawX()) > 100 ){
					//显示下一个页面：从右往左滑动
					System.out.println("显示下一个页面：从右往左滑动");
					showNext();
					return true;
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}

		});
	}

	public abstract void showNext();
	public abstract void showPre();
	/**
	 * 下一步的点击事件
	 * @param view
	 */
	public void next(View view){
		showNext();
	}

	/**
	 *   上一步
	 * @param view
	 */
	public void pre(View view){
		showPre();
	}
	//3.使用手势识别器
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
