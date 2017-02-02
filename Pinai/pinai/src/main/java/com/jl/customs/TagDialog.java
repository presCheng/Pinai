package com.jl.customs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.jl.db.SqliteUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hrb.jl.pinai.R;

/**
 * 类名称：TagDialog
 * 类描述：选择标签
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-6-30 下午5:18:54
 * 修改备注：
 *
 * @version 1.0.0
 */
public class TagDialog extends Dialog {
    /**
     * @param context
     */
    public TagDialog(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param theme
     */
    public TagDialog(Context context, int theme) {
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
        private String positiveButtonText;
        private OnClickListener positiveButtonClickListener;
        private Set<String> selectTag;//选择的标签

        public Builder(Context context) {
            this.context = context;
        }

        public Set<String> getSelectTag() {
            return selectTag;
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
        /**
         * Function: 创建CustomDialog实例
         *
         * @return
         */
        public TagDialog create() {
            selectTag = new HashSet<>();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 实例化自定义主题的对话框
            final TagDialog dialog = new TagDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.customs_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            setTag(layout);
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ( layout.findViewById(R.id.positiveButton))
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
            dialog.setContentView(layout);
            return dialog;
        }

        /**
         * 设置标签 setTag
         * @param layout void
         * @since 1.0.0
         */
        private void setTag(View layout) {
            List<String> tag = SqliteUtils.getInstance().getTag(context);
            GridView gr = (GridView) layout.findViewById(R.id.gr);
            gr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((DrawCloseLinearLayout)view).getCloseCallBack();
                }
            });
            gr.setSelector(new ColorDrawable(Color.TRANSPARENT));
            MyAdapter adapter=new MyAdapter(context,tag);
            gr.setAdapter(adapter);
        }
        class MyAdapter extends BaseAdapter{
            List<String> tag;
            Context context;
            public MyAdapter(Context context, List<String> tag) {
                this.tag=tag;
                this.context=context;
            }
            @Override
            public int getCount() {
                return tag.size();
            }

            @Override
            public Object getItem(int position) {
                return tag.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position,  View convertView, ViewGroup parent) {
                String str =tag.get(position);
                if (convertView==null){
                    convertView=new DrawCloseLinearLayout(context,str,position, new DrawCloseLinearLayout.CloseCallback(){
                        @Override
                        public void onCloseCallback(DrawCloseLinearLayout drawCloseLinearLayout) {
                            if (drawCloseLinearLayout.isMyFocused()) {
                                selectTag.remove(String.valueOf(drawCloseLinearLayout.getThisI()).trim());
                                drawCloseLinearLayout.setIv(R.drawable.oo);
                            } else {
                                selectTag.add(String.valueOf(drawCloseLinearLayout.getThisI()).trim());
                                drawCloseLinearLayout.setIv(R.drawable.yy);
                            }
                        }
                    });
                    ((DrawCloseLinearLayout)convertView).setThisI(position + 1);
                    ((DrawCloseLinearLayout)convertView).setIv(R.drawable.oo);
                }else{
                    convertView.getTag();
                }
                return convertView;
            }
        }
        }

    }


