package com.jl.customs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import hrb.jl.pinai.R;

/**
 * 类名称：AnswerDialog
 * 类描述：接受邀请
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-30 下午5:18:54
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AnswerDialog extends Dialog {
    /**
     * @param context
     */
    public AnswerDialog(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param theme
     */
    public AnswerDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * Class Name: QuestionDialog.java Function: 内部类 创建弹出框 Modifications:
     *
     * @author 徐志国 DateTime 2014-12-30 下午7:07:42
     * @version 1.0
     */
    public static class Builder {
        private Context context;
        private String answer;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener nevitiveButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置对话框的问题资源
         *
         * @param answer
         * @return Builder
         */
        public Builder setAnswer(String answer) {
            this.answer = answer;
            return this;
        }

        public Builder setPositiveButton(OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNevitiveButton(OnClickListener listener) {
            this.nevitiveButtonClickListener = listener;
            return this;
        }

        /**
         * Function: 创建CustomDialog实例
         *
         * @return
         */
        public AnswerDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 实例化自定义主题的对话框
            final AnswerDialog dialog = new AnswerDialog(context,
                    R.style.Dialog);
            final View layout = inflater.inflate(R.layout.answer_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            if (answer != null) {
                ((TextView) layout.findViewById(R.id.answer)).setText(answer);
            }
            if (positiveButtonClickListener != null) {
                layout.findViewById(R.id.yes)
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                positiveButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_POSITIVE);
                            }
                        });
            }
            if (nevitiveButtonClickListener != null) {
                layout.findViewById(R.id.no)
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                nevitiveButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_POSITIVE);
                            }
                        });
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
