package com.jl.customs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import hrb.jl.pinai.R;

/**
 * 类名称：QuestionDialog
 * 类描述：回答问题
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-30 下午5:18:54
 * 修改备注：
 *
 * @version 1.0.0
 */
public class QuestionDialog extends Dialog {
    /**
     * @param context
     */
    public QuestionDialog(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param theme
     */
    public QuestionDialog(Context context, int theme) {
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
        private String question;
        private String answer;
        private String positiveButtonText;
        private OnClickListener positiveButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置对话框的问题资源
         *
         * @param question
         * @return Builder
         */
        public Builder setQuestion(int question) {
            this.question = (String) context.getText(question);
            return this;
        }

        /**
         * 设置对话框的问题资源
         *
         * @param question
         * @return Builder
         */
        public Builder setQuestion(String question) {
            this.question = question;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        /**
         * Function: 创建CustomDialog实例
         *
         * @return
         */
        public QuestionDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 实例化自定义主题的对话框
            final QuestionDialog dialog = new QuestionDialog(context,
                    R.style.Dialog);
            final View layout = inflater.inflate(R.layout.question_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            if (question != null) {
                ((TextView) layout.findViewById(R.id.dialog_question)).setText(question);
            }
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.dialog_submit))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    layout.findViewById(R.id.dialog_submit)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    EditText et = (EditText) layout.findViewById(R.id.dialog_answer);
                                    setAnswer(et.getText().toString());
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
