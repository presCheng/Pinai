package com.jl.customs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import hrb.jl.pinai.R;

/**
 * 类名称：ConfirmAlertDialog
 * 类描述：确认对话框
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-6-30
 * 下午5:18:54
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ConfirmAlertDialog extends Dialog {
    /**
     * @param context
     */
    public ConfirmAlertDialog(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param theme
     */
    public ConfirmAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * Class Name: CustomDialog.java Function: 内部类 创建弹出框 Modifications:
     *
     * @author 徐志国 DateTime 2013-12-11 下午7:07:42
     * @version 1.0
     */
    public static class Builder {
        private Context context;
        private String title;
        private String content;
        private String negativeButtonText;
        private String positiveButtonText;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }


        /**
         * 设置对话框的信息资源
         *
         * @param title
         * @return Builder
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * 设置对话框的信息资源
         *
         * @param title
         * @return Builder
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        /**
         * 设置对话框的信息资源
         *
         * @param content
         * @return Builder
         */
        public Builder setContent(int content) {
            this.content = (String) context.getText(content);
            return this;
        }

        /**
         * 设置对话框的信息资源
         *
         * @param content
         * @return Builder
         */

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        /**
         * 设置资源和确定的按钮侦听器
         *
         * @param positiveButtonText
         * @return Builder
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Function: 创建CustomDialog实例
         *
         * @return
         */
        public ConfirmAlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 实例化自定义主题的对话框
            final ConfirmAlertDialog dialog = new ConfirmAlertDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_confirm, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            ((TextView) layout.findViewById(R.id.content)).setText(content);
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    layout.findViewById(R.id.positiveButton)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    layout.findViewById(R.id.negativeButton)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }

            dialog.setContentView(layout);
            return dialog;
        }
    }
}
