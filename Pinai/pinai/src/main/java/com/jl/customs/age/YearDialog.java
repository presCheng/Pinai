package com.jl.customs.age;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.jl.basic.Config;
import com.jl.customs.timer.ScreenInfo;

import hrb.jl.pinai.R;


/**
 * 类名称：TimerDialog 类描述：选择器对话框
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-27 上午10:07:29
 * 修改备注：修改字体大小问题
 *
 * @version 1.0.0
 */
public class YearDialog extends Dialog {
    /**
     * @param context
     */
    public YearDialog(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param theme
     */
    public YearDialog(Context context, int theme) {
        super(context, theme);
    }

    public interface PositiveListener {
        /**
         * onClick
         *
         * @param dialog
         * @param time   时间
         *               void
         * @since 1.0.0
         */
        void onClick(DialogInterface dialog, String time);
    }

    public static class Builder {
        private Context context;
        private Activity activity;
        private PositiveListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private WheelMainG wheelMainG;

        public Builder(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
        }

        /**
         * 设置资源和确定的按钮侦听器
         *
         * @param listener
         * @return Builder
         */
        public Builder setPositiveButton(PositiveListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(
                OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Function: 创建CustomDialog实例
         *
         * @return
         */
        public YearDialog create(int start, int end) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 实例化自定义主题的对话框
            final YearDialog dialog = new YearDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.year_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            setButton(dialog, layout);
            setTime(dialog, layout, start, end);
            dialog.setContentView(layout);
            return dialog;
        }

        private void setTime(final YearDialog dialog, View layout, int start,
                             int end) {
            ScreenInfo screenInfo = new ScreenInfo(activity);
            View timepickerview = layout.findViewById(R.id.year_timePicker);
            wheelMainG = new WheelMainG(timepickerview, start, end);
            wheelMainG.screenheight = screenInfo.getHeight();
            select(start, layout);
        }

        /**
         * 区分星座，年龄，身高 select
         *
         * @param start void
         * @since 1.0.0
         */
        private void select(int start, View layout) {
            TextView tittle = (TextView) layout
                    .findViewById(R.id.year_dialog_tittle);
            if (start == Config.MIN_CON) {
                tittle.setText("选择星座");
                //星座
                int year = 5;
                wheelMainG.initDateTimePicker(year, "座", WheelMainG.IS_XING_ZUO);
            } else if (start == Config.MIN_BIRTH) {
                //出生年
                int year = 1990;
                tittle.setText("选择出生年");
                wheelMainG.initDateTimePicker(year, "年", WheelMainG.IS_HIGH);
            } else if (start == Config.MIN_GRADE) {
                //入学年
                tittle.setText("选择入学年");
                int year = 2010;
                wheelMainG.initDateTimePicker(year, "年", WheelMainG.IS_HIGH);
            }
            else if (start == Config.MIN_SALARY) {
                //薪资
                tittle.setText("选择薪资范围");
                int year = 8;
                wheelMainG.initDateTimePicker(year, " ", WheelMainG.IS_SALARY);
            }
            else if (start == Config.MIN_PROVINCE) {
                //省份
                tittle.setText("选择所在地");
                int year = 1;
                wheelMainG.initDateTimePicker(year, "省", WheelMainG.IS_PROVINCE);
            }
        }
        private void setButton(final YearDialog dialog, View layout) {
            if (positiveButtonClickListener != null) {
                layout.findViewById(R.id.timer_positiveButton)
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                int year = wheelMainG.getYear();
                                String time = year + "";
                                positiveButtonClickListener.onClick(dialog,
                                        time);
                            }
                        });
            }
            if (negativeButtonClickListener != null) {
                (layout.findViewById(R.id.timer_negativeButton))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                negativeButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_NEGATIVE);
                                dialog.cancel();
                            }
                        });
            }
        }
    }
}
