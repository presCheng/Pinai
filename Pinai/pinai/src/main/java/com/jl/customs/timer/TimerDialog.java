package com.jl.customs.timer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import java.util.Calendar;

import hrb.jl.pinai.R;


/**
 * 类名称：TimerDialog 类描述：时间选择器对话框 创建人：徐志国 修改人：徐志国 修改时间：2014-11-27 上午10:07:29 修改备注：
 *
 * @version 1.0.0
 */
public class TimerDialog extends Dialog {
    /**
     * @param context
     */
    public TimerDialog(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param theme
     */
    public TimerDialog(Context context, int theme) {
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
        private WheelMain wheelMain;

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
        public TimerDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 实例化自定义主题的对话框
            final TimerDialog dialog = new TimerDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.timer_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            setButton(dialog, layout);
            setTime(dialog, layout);
            dialog.setContentView(layout);
            return dialog;
        }

        private void setTime(final TimerDialog dialog, View layout) {
            ScreenInfo screenInfo = new ScreenInfo(activity);
            View timepickerview = layout.findViewById(R.id.timer_timePicker);
            wheelMain = new WheelMain(timepickerview);
            wheelMain.screenheight = screenInfo.getHeight();
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            wheelMain.initDateTimePicker(year, month, day);
        }

        private void setButton(final TimerDialog dialog, View layout) {
            if (positiveButtonClickListener != null) {
                layout.findViewById(R.id.timer_positiveButton)
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                int year = wheelMain.getYear();
                                int month = wheelMain.getMonth();
                                int day = wheelMain.getDay();
                                String time = year + "-" + month + "-" + day;
                                positiveButtonClickListener.onClick(dialog,
                                        time);
                            }
                        });
            }
            if (negativeButtonClickListener != null) {
                layout.findViewById(R.id.timer_negativeButton)
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
