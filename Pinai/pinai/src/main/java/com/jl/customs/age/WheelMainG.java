package com.jl.customs.age;


import android.view.View;

import com.jl.customs.timer.NumericWheelAdapter;

import hrb.jl.pinai.R;


public class WheelMainG {

    public final static int IS_XING_ZUO = -11233;
    public final static int IS_PROVINCE = -1231233;
    public final static int IS_HIGH = -123123;
    public final static int IS_SALARY = -122213123;

    public int screenheight;
    private View view;
    private WheelViewG wv_year;
    private int start, end;

    public WheelMainG(View view, int start, int end) {
        this.view = view;
        this.start = start;
        this.end = end;
        setView(view);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * 弹出日期时间选择器
     * initDateTimePicker
     *
     * @param year
     * @param name 设置名
     *             void
     * @since 1.0.0
     */
    public void initDateTimePicker(int year, String name, int isWhat) {
        // 年
        wv_year = (WheelViewG) view.findViewById(R.id.year);
        switch (isWhat) {
            case IS_XING_ZUO:
                wv_year.setAdapter(new NumericWheelXZAdapter(start, end));
                break;
            case IS_PROVINCE:
                wv_year.setAdapter(new NumericWheelProAdpter(start, end));
                break;
            case IS_SALARY:
                wv_year.setAdapter(new NumericWheelSalaryAdapter(start, end));
                break;
            default:
                wv_year.setAdapter(new NumericWheelAdapter(start, end));
                break;

        }

        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel(name);// 添加文字
        wv_year.setCurrentItem(year - start);// 初始化时显示的数据
        // 添加"年"监听
        OnWheelChangedGListener wheelListener_year = new OnWheelChangedGListener() {
            @Override
            public void onChanged(WheelViewG wheel, int oldValue, int newValue) {

            }
        };
        wv_year.addChangingListener(wheelListener_year);
        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)字体大小设置
        int textSize = 0;
        textSize = (screenheight / 100) * 4;
        wv_year.TEXT_SIZE = textSize;
    }

    public int getYear() {
        return wv_year.getCurrentItem() + start;
    }
}
