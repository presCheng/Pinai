package com.jl.atys.individualcenter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jl.atys.chat.adapter.ExpressionAdapter;
import com.jl.atys.chat.adapter.ExpressionPagerAdapter;
import com.jl.atys.chat.utils.SmileUtils;
import com.jl.atys.chat.widget.ExpandGridView;
import com.jl.atys.chat.widget.PasteEditText;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类描述：客服反馈
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-12-30 下午2:14:01
 * 修改备注：
 *
 * @version 1.0.0
 */

public class AtyIndFeedBack extends AtySupport implements OnClickListener {
    //表情指标
    private int currIndex = -1;
    private RelativeLayout emojiIconContainer;
    private ViewPager expressionViewpager;
    private PasteEditText mEditTextContent;
    private List<String> reslist;
    private ImageView icon_clicked;
    private ImageView icon_normal;
    //----------------------------------
    private ListView listView;
    private FeedAdpter feedAdpter;
    //----------------------------------
    private Button send;
    private List<FeedBack> list;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_ind_feed_back);
        id = Config.getCacheID(context);
        init();
    }

    private void init() {
        icon_clicked = (ImageView) findViewById(R.id.feedback_emoticons_checked);
        icon_clicked.setOnClickListener(this);
        icon_normal = (ImageView) findViewById(R.id.feedback_emoticons_normal);
        icon_normal.setOnClickListener(this);
        mEditTextContent = (PasteEditText) findViewById(R.id.feedback_sendmessage);
        send = (Button) findViewById(R.id.feedback_send);
        send.setOnClickListener(this);
        setFacialPage();
        setListView();
    }

    /**
     * 设置聊天内容
     */
    private void setListView() {
        listView = (ListView) findViewById(R.id.feedback_list);
        list = new ArrayList<FeedBack>();
        feedAdpter = new FeedAdpter(context, list);
        listView.setAdapter(feedAdpter);
        listView.setSelection(feedAdpter.getCount());
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }

    /**
     * 设置表情
     */
    private void setFacialPage() {
        emojiIconContainer = (RelativeLayout) findViewById(R.id.ll_face_container);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        // 表情list
        reslist = getExpressionRes(196);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        //一个view是一页
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        View gv4 = getGridChildView(4);
        View gv5 = getGridChildView(5);
        View gv6 = getGridChildView(6);
        View gv7 = getGridChildView(7);
        View gv8 = getGridChildView(8);
        View gv9 = getGridChildView(9);
        View gv10 = getGridChildView(10);
        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        views.add(gv4);
        views.add(gv5);
        views.add(gv6);
        views.add(gv7);
        views.add(gv8);
        views.add(gv9);
        views.add(gv10);
        final ImageView spot1 = (ImageView) findViewById(R.id.spot1);
        final ImageView spot2 = (ImageView) findViewById(R.id.spot2);
        final ImageView spot3 = (ImageView) findViewById(R.id.spot3);
        final ImageView spot4 = (ImageView) findViewById(R.id.spot4);
        final ImageView spot5 = (ImageView) findViewById(R.id.spot5);
        final ImageView spot6 = (ImageView) findViewById(R.id.spot6);
        final ImageView spot7 = (ImageView) findViewById(R.id.spot7);
        final ImageView spot8 = (ImageView) findViewById(R.id.spot8);
        final ImageView spot9 = (ImageView) findViewById(R.id.spot9);
        final ImageView spot10 = (ImageView) findViewById(R.id.spot10);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
        expressionViewpager.setCurrentItem(0);
        currIndex = 0;
        //设置滑动监听
        expressionViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                Animation anim = null;
                switch (i) {
                    case 0:
                        spot1.setImageResource(R.drawable.spot_selected);
                        spot2.setImageResource(R.drawable.spot);
                        spot3.setImageResource(R.drawable.spot);
                        spot4.setImageResource(R.drawable.spot);
                        spot5.setImageResource(R.drawable.spot);
                        spot6.setImageResource(R.drawable.spot);
                        spot7.setImageResource(R.drawable.spot);
                        spot8.setImageResource(R.drawable.spot);
                        spot9.setImageResource(R.drawable.spot);
                        spot10.setImageResource(R.drawable.spot);
                        if (currIndex == (i + 1)) {
                            anim = new TranslateAnimation(i + 1, i, 0, 0);
                        }
                        break;
                    case 1:
                        spot1.setImageResource(R.drawable.spot);
                        spot2.setImageResource(R.drawable.spot_selected);
                        spot3.setImageResource(R.drawable.spot);
                        spot4.setImageResource(R.drawable.spot);
                        spot5.setImageResource(R.drawable.spot);
                        spot6.setImageResource(R.drawable.spot);
                        spot7.setImageResource(R.drawable.spot);
                        spot8.setImageResource(R.drawable.spot);
                        spot9.setImageResource(R.drawable.spot);
                        spot10.setImageResource(R.drawable.spot);
                        if (i == (currIndex + 1)) {
                            anim = new TranslateAnimation(i - 1, i, 0, 0);
                        } else if (i == (currIndex - 1)) {
                            anim = new TranslateAnimation(i + 1, i, 0, 0);
                        }
                        break;
                    case 2:
                        spot1.setImageResource(R.drawable.spot);
                        spot2.setImageResource(R.drawable.spot);
                        spot3.setImageResource(R.drawable.spot_selected);
                        spot4.setImageResource(R.drawable.spot);
                        spot5.setImageResource(R.drawable.spot);
                        spot6.setImageResource(R.drawable.spot);
                        spot7.setImageResource(R.drawable.spot);
                        spot8.setImageResource(R.drawable.spot);
                        spot9.setImageResource(R.drawable.spot);
                        spot10.setImageResource(R.drawable.spot);
                        if (i == (currIndex + 1)) {
                            anim = new TranslateAnimation(i - 1, i, 0, 0);
                        } else if (i == (currIndex - 1)) {
                            anim = new TranslateAnimation(i + 1, i, 0, 0);
                        }
                        break;
                    case 3:
                        spot1.setImageResource(R.drawable.spot);
                        spot2.setImageResource(R.drawable.spot);
                        spot3.setImageResource(R.drawable.spot);
                        spot4.setImageResource(R.drawable.spot_selected);
                        spot5.setImageResource(R.drawable.spot);
                        spot6.setImageResource(R.drawable.spot);
                        spot7.setImageResource(R.drawable.spot);
                        spot8.setImageResource(R.drawable.spot);
                        spot9.setImageResource(R.drawable.spot);
                        spot10.setImageResource(R.drawable.spot);
                        if (i == (currIndex + 1)) {
                            anim = new TranslateAnimation(i - 1, i, 0, 0);
                        } else if (i == (currIndex - 1)) {
                            anim = new TranslateAnimation(i + 1, i, 0, 0);
                        }
                        break;
                    case 4:
                        spot1.setImageResource(R.drawable.spot);
                        spot2.setImageResource(R.drawable.spot);
                        spot3.setImageResource(R.drawable.spot);
                        spot4.setImageResource(R.drawable.spot);
                        spot5.setImageResource(R.drawable.spot_selected);
                        spot6.setImageResource(R.drawable.spot);
                        spot7.setImageResource(R.drawable.spot);
                        spot8.setImageResource(R.drawable.spot);
                        spot9.setImageResource(R.drawable.spot);
                        spot10.setImageResource(R.drawable.spot);
                        if (i == (currIndex + 1)) {
                            anim = new TranslateAnimation(i - 1, i, 0, 0);
                        } else if (i == (currIndex - 1)) {
                            anim = new TranslateAnimation(i + 1, i, 0, 0);
                        }
                        break;
                    case 5:
                        spot1.setImageResource(R.drawable.spot);
                        spot2.setImageResource(R.drawable.spot);
                        spot3.setImageResource(R.drawable.spot);
                        spot4.setImageResource(R.drawable.spot);
                        spot5.setImageResource(R.drawable.spot);
                        spot6.setImageResource(R.drawable.spot_selected);
                        spot7.setImageResource(R.drawable.spot);
                        spot8.setImageResource(R.drawable.spot);
                        spot9.setImageResource(R.drawable.spot);
                        spot10.setImageResource(R.drawable.spot);
                        if (i == (currIndex + 1)) {
                            anim = new TranslateAnimation(i - 1, i, 0, 0);
                        } else if (i == (currIndex - 1)) {
                            anim = new TranslateAnimation(i + 1, i, 0, 0);
                        }
                        break;
                    case 6:
                        spot1.setImageResource(R.drawable.spot);
                        spot2.setImageResource(R.drawable.spot);
                        spot3.setImageResource(R.drawable.spot);
                        spot4.setImageResource(R.drawable.spot);
                        spot5.setImageResource(R.drawable.spot);
                        spot6.setImageResource(R.drawable.spot);
                        spot7.setImageResource(R.drawable.spot_selected);
                        spot8.setImageResource(R.drawable.spot);
                        spot9.setImageResource(R.drawable.spot);
                        spot10.setImageResource(R.drawable.spot);
                        if (i == (currIndex + 1)) {
                            anim = new TranslateAnimation(i - 1, i, 0, 0);
                        } else if (i == (currIndex - 1)) {
                            anim = new TranslateAnimation(i + 1, i, 0, 0);
                        }
                        break;
                    case 7:
                        spot1.setImageResource(R.drawable.spot);
                        spot2.setImageResource(R.drawable.spot);
                        spot3.setImageResource(R.drawable.spot);
                        spot4.setImageResource(R.drawable.spot);
                        spot5.setImageResource(R.drawable.spot);
                        spot6.setImageResource(R.drawable.spot);
                        spot7.setImageResource(R.drawable.spot);
                        spot8.setImageResource(R.drawable.spot_selected);
                        spot9.setImageResource(R.drawable.spot);
                        spot10.setImageResource(R.drawable.spot);
                        if (i == (currIndex + 1)) {
                            anim = new TranslateAnimation(i - 1, i, 0, 0);
                        } else if (i == (currIndex - 1)) {
                            anim = new TranslateAnimation(i + 1, i, 0, 0);
                        }
                        break;
                    case 8:
                        spot1.setImageResource(R.drawable.spot);
                        spot2.setImageResource(R.drawable.spot);
                        spot3.setImageResource(R.drawable.spot);
                        spot4.setImageResource(R.drawable.spot);
                        spot5.setImageResource(R.drawable.spot);
                        spot6.setImageResource(R.drawable.spot);
                        spot7.setImageResource(R.drawable.spot);
                        spot8.setImageResource(R.drawable.spot);
                        spot9.setImageResource(R.drawable.spot_selected);
                        spot10.setImageResource(R.drawable.spot);
                        if (i == (currIndex + 1)) {
                            anim = new TranslateAnimation(i - 1, i, 0, 0);
                        } else if (i == (currIndex - 1)) {
                            anim = new TranslateAnimation(i + 1, i, 0, 0);
                        }
                        break;
                    case 9:
                        spot1.setImageResource(R.drawable.spot);
                        spot2.setImageResource(R.drawable.spot);
                        spot3.setImageResource(R.drawable.spot);
                        spot4.setImageResource(R.drawable.spot);
                        spot5.setImageResource(R.drawable.spot);
                        spot6.setImageResource(R.drawable.spot);
                        spot7.setImageResource(R.drawable.spot);
                        spot8.setImageResource(R.drawable.spot);
                        spot9.setImageResource(R.drawable.spot);
                        spot10.setImageResource(R.drawable.spot_selected);
                        if (i == (currIndex + 1)) {
                            anim = new TranslateAnimation(i - 1, i, 0, 0);
                        }
                        break;
                    default:
                        break;
                }
                currIndex = i;
                anim.setFillAfter(true);
                anim.setDuration(300);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "emoji_" + x;

            reslist.add(filename);

        }
        return reslist;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_emoticons_normal:
                setIconNormal(v);
                break;
            case R.id.feedback_emoticons_checked:
                setIconClicked(v);
                break;
            case R.id.feedback_send:
                sendMessage();
                break;
            default:
                break;
        }
    }

    /**
     * 发送消息
     */
    private void sendMessage() {
        final String msg = mEditTextContent.getText().toString();
        showProgressDialog(context, "", "发送中....", false);
//        new Support(id, msg, new Support.SuccessCallback() {
//            @Override
//            public void onSuccess() {
//
//                mEditTextContent.setText("");
//                list.add(new FeedBack("客服", "您的消息已经收到了，谢谢", FeedBack.Type.INCOMEING, new Date(), "drawable://" + R.drawable.kefu));
//                feedAdpter.notifyDataSetChanged();
//                closeProgressDialog();
//            }
//        }, new Support.FailCallback() {
//
//            @Override
//            public void onFail(String error) {
//                mEditTextContent.setText("");
//                list.add(new FeedBack("客服", "您的消息已经收到了，谢谢", FeedBack.Type.INCOMEING, new Date(), ""));
//                feedAdpter.notifyDataSetChanged();
//                closeProgressDialog();
//            }
//        });
        String user = "徐徐";
        list.add(new FeedBack(user, msg, FeedBack.Type.OUTCOMEING, new Date(), Config.getCachePortrait(context)));
        feedAdpter.notifyDataSetChanged();
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        switch (i) {
            case 1:
                List<String> list1 = reslist.subList(0, 20);
                list.addAll(list1);
                break;
            case 2:
                List<String> list2 = reslist.subList(20, 40);
                list.addAll(list2);
                break;
            case 3:
                List<String> list3 = reslist.subList(40, 60);
                list.addAll(list3);
                break;
            case 4:
                List<String> list4 = reslist.subList(60, 80);
                list.addAll(list4);
                break;

            case 5:
                List<String> list5 = reslist.subList(80, 100);
                list.addAll(list5);
                break;
            case 6:
                List<String> list6 = reslist.subList(100, 120);
                list.addAll(list6);
                break;
            case 7:
                List<String> list7 = reslist.subList(120, 140);
                list.addAll(list7);
                break;
            case 8:
                List<String> list8 = reslist.subList(140, 160);
                list.addAll(list8);
                break;
            case 9:
                List<String> list9 = reslist.subList(160, 180);
                list.addAll(list9);
                break;
            case 10:
                List<String> list10 = reslist.subList(180, 196);
                list.addAll(list10);
                break;
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        //这里是把图片显示出来
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    if (filename != "delete_expression") { // 不是删除键，显示表情
                        // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                        Class clz = Class.forName(Config.KEY_SMILE_UTILS);
                        Field field = clz.getField(filename);
                        mEditTextContent.append(SmileUtils.getSmiledText(AtyIndFeedBack.this, (String) field.get(null), 26));
                    } else { // 删除文字或者表情
                        if (!TextUtils.isEmpty(mEditTextContent.getText())) {
                            int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
                            if (selectionStart > 0) {
                                String body = mEditTextContent.getText().toString();
                                String tempStr = body.substring(0, selectionStart);
                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    CharSequence cs = tempStr.substring(i, selectionStart);
                                    if (SmileUtils.containsKey(cs.toString()))
                                        mEditTextContent.getEditableText().delete(i, selectionStart);
                                    else
                                        mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                } else {
                                    mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    /**
     * 设置表情图片
     */
    private void setIconNormal(View v) {
        if (emojiIconContainer.getVisibility() == View.VISIBLE) {
            emojiIconContainer.setVisibility(View.GONE);
            v.setVisibility(View.VISIBLE);
            icon_clicked.setVisibility(View.GONE);
        } else {
            emojiIconContainer.setVisibility(View.VISIBLE);
            v.setVisibility(View.GONE);
            icon_clicked.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置表情图片
     */
    private void setIconClicked(View v) {
        if (emojiIconContainer.getVisibility() == View.VISIBLE) {
            emojiIconContainer.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
            icon_normal.setVisibility(View.VISIBLE);
        } else {
            emojiIconContainer.setVisibility(View.VISIBLE);
            v.setVisibility(View.VISIBLE);
            icon_normal.setVisibility(View.GONE);
        }
    }

    public void back(View v) {
        finish();
    }
}
