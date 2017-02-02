package com.jl.atys.dsgy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jl.atys.chat.adapter.ExpressionAdapter;
import com.jl.atys.chat.adapter.ExpressionPagerAdapter;
import com.jl.atys.chat.utils.SmileUtils;
import com.jl.atys.chat.widget.ExpandGridView;
import com.jl.atys.chat.widget.PasteEditText;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.domain.GetPostChildBean;
import com.jl.net.ForumGetPost;
import com.jl.net.ForumPostcomment;
import com.jl.net.UploadImage;
import com.jl.opengallery.AtyOpenPic;
import com.jl.utils.XuDecodeBase64;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

public class AtyDsgyComment extends AtySupport {

    public static final int RESULT_MORE_REPLY = 2321;
    boolean isChecked = false;
    private String postId;
    private CommentAdapter adapter;
    private PullToRefreshListView commentLv;
    //private ListView ;
    private PasteEditText sendMessage;
    private String flagReply = "comments";//回复标识
    private String commentID;//逻辑不对 mark
    private String replyID;
    //------------------------------------------------------
    //表情指标
    private int currIndex = -1;
    private RelativeLayout emojiIconContainer;
    //private ViewPager expressionViewpager;
    private List<String> resList;
    //------------------------------------------------------
    private GridView moreGridView;//点击更多面板
    //-----------------------------------------------------
    private List<String> pathall;//插入图片
    private ViewPager viewPagerInsert;//插入图片
    // 翻页记录
    private String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_dsgy_comment);
        setStatusBarTint(AtyDsgyComment.this, getResources().getColor(R.color.background_pink));
        postId = (String) getIntent().getExtras().get("postId");
        init();
        setFacialPage();
    }

    private void init() {
        commentLv = (PullToRefreshListView) findViewById(R.id.comment_list);
        commentLv.setMode(Mode.PULL_FROM_END);
        commentLv.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                //获得数据
                getData(count, "", "10");
            }
        });
        adapter = new CommentAdapter(context, postId);
        commentLv.getRefreshableView().setAdapter(adapter);

        //获得数据
        getData(Config.KEY_NULL, "", "10");
        //===============================================================================
        sendMessage = (PasteEditText) findViewById(R.id.comment_sendmessage);
        adapter.setSendMessageCallback(new CommentAdapter.SendMessageCallback() {
            @Override
            public void sendMessage(String commentID, String replyID, long postion, String nickname) {
                flagReply = "reply";
                sendMessage.setHint("回复 " + nickname + ":");
                sendMessage.setTag(Config.getCacheNickName(context) + " 回复 " + nickname + ":");
                InputMethodManager imm = (InputMethodManager)
                        sendMessage.getContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                AtyDsgyComment.this.commentID = commentID;
                AtyDsgyComment.this.replyID = replyID;
            }
        });

        //------------------------------------
        moreGridView = (GridView) findViewById(R.id.more_gridview);
        MoreAdapter moreAdapter = new MoreAdapter(context);
        moreGridView.setAdapter(moreAdapter);
        moreGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //表情
                    if (emojiIconContainer.getVisibility() == View.VISIBLE) {
                        emojiIconContainer.setVisibility(View.GONE);
                    } else {
                        emojiIconContainer.setVisibility(View.VISIBLE);
                        moreGridView.setVisibility(View.GONE);
                    }
                } else if (position == 1) {
                    //插入图片
                    insertPic();
                }
            }
        });
//-------------------------------------------------------------
        viewPagerInsert = (ViewPager) findViewById(R.id.vPager_insert);
    }

    /**
     * 发送消息
     *
     * @param v 发送按钮
     */
    public void send(View v) {

        if (TextUtils.isEmpty(sendMessage.getText().toString())) {
            showToast("不能发送空消息，欧巴");
            return;
        }
        showProgressDialog(context, "", "回复中...", false);
        if (flagReply.equals("comments")) {
            //回复帖子，加图片
            if (pathall != null) {
                XuDecodeBase64 decodeBase64 = new XuDecodeBase64().invoke(pathall);
                String pathBase64 = decodeBase64.getPathBase64();
                new UploadImage(pathBase64, new UploadImage.SuccessCallback() {
                    @Override
                    public void onSuccess(String url) {
                        submitMsgComment(url);

                    }
                }, new UploadImage.FailCallback() {
                    @Override
                    public void onFail(String error) {
                        showToast("回复失败");
                        closeProgressDialog();
                    }
                });
            } else {
                //回复帖子，没有图片
                submitMsgComment("");
            }
        } else if (flagReply.equals("reply")) {
            //回复楼层，不加图片
            submitMsgReply();

        }
    }


    /**
     * 回复评论
     */
    private void submitMsgReply() {
        String content = sendMessage.getTag() + sendMessage.getText().toString();
        sendMessage.setText("");
        sendMessage.setHint("");
        String type = ForumPostcomment.TYPE_REPLY;
        String userId = Config.getCacheID(context);
        String commentId = commentID;
        String replyId = replyID;
        //设置回复帖子
        flagReply = "comments";
        new ForumPostcomment(type, content, userId, postId, replyId, commentId, new ForumPostcomment.SuccessCallback() {
            @Override
            public void onSuccess() {
                AtyDsgyComment.this.showToast("回复成功");
                getData("", "replyComments", count);
                closeProgressDialog();
            }
        }, new ForumPostcomment.FailCallback() {
            @Override
            public void onFail(String error) {
                AtyDsgyComment.this.showToast("回复失败");
                closeProgressDialog();
            }
        });
    }

    /**
     * 回复帖子
     *
     * @param url 图片地址
     */
    private void submitMsgComment(String url) {
        String content = sendMessage.getText().toString() + url;
        sendMessage.setText("");
        sendMessage.setHint("");
        String type = ForumPostcomment.TYPE_COMMENTS;
        String userId = Config.getCacheID(context);
        String replyId = "";
        String commentId = "";
        new ForumPostcomment(type, content, userId, postId, replyId, commentId, new ForumPostcomment.SuccessCallback() {
            @Override
            public void onSuccess() {
                showToast("回复成功");
                getData(count, "reply", "10");
                closeProgressDialog();
            }
        }, new ForumPostcomment.FailCallback() {
            @Override
            public void onFail(String error) {
                showToast("回复失败");
                closeProgressDialog();
            }
        });
    }

    /**
     * 获取数据
     */
    private void getData(final String lastId, final String returnOrGet, String perpage) {
        new ForumGetPost(postId, lastId, perpage, new ForumGetPost.SuccessCallback() {
            @Override
            public void onSuccess(List<GetPostChildBean> data) {
                if (returnOrGet.equals("reply")) {
                    try {
                        count = data.get(data.size() - 1).getId();
                    } catch (Exception ignored) {
                    }
                    adapter.addAll(data);
                    commentLv.getRefreshableView().setSelection(commentLv.getRefreshableView().getBottom());
                } else if (returnOrGet.equals("replyComments")) {
                    showToast("comments");
                    adapter.clear();
                    adapter.addAll(data);
                    commentLv.getRefreshableView().invalidateViews();
                } else {
                    try {
                        count = data.get(data.size() - 1).getId();
                    } catch (Exception e) {
                        showToast("没有数据加载啦");
                    }
                    adapter.addAll(data);
                    findViewById(R.id.playout).setVisibility(View.GONE);
                    commentLv.onRefreshComplete();
                }
            }
        }, new ForumGetPost.FailCallback() {
            @Override
            public void onFail(String error) {
                //如果为2就说明帖子已经被删除了
                if (error.equals("2")) {
                    findViewById(R.id.pbar).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.ptext)).setText("帖子已经被删除或不存在 TAT...");
                    findViewById(R.id.comment_bar_bottom).setVisibility(View.GONE);//把回复框去掉
                    commentLv.onRefreshComplete();
                } else {
                    findViewById(R.id.pbar).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.ptext)).setText("数据获取异常");
                    commentLv.onRefreshComplete();
                }
            }
        });
    }

    /**
     * 设置表情
     */
    private void setFacialPage() {
        emojiIconContainer = (RelativeLayout) findViewById(R.id.ll_face_container);
        ViewPager expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        // 表情list
        resList = getExpressionRes(196);
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
                assert anim != null;
                anim.setFillAfter(true);
                anim.setDuration(300);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i 索引
     * @return View
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        switch (i) {
            case 1:
                List<String> list1 = resList.subList(0, 20);
                list.addAll(list1);
                break;
            case 2:
                List<String> list2 = resList.subList(20, 40);
                list.addAll(list2);
                break;
            case 3:
                List<String> list3 = resList.subList(40, 60);
                list.addAll(list3);
                break;
            case 4:
                List<String> list4 = resList.subList(60, 80);
                list.addAll(list4);
                break;

            case 5:
                List<String> list5 = resList.subList(80, 100);
                list.addAll(list5);
                break;
            case 6:
                List<String> list6 = resList.subList(100, 120);
                list.addAll(list6);
                break;
            case 7:
                List<String> list7 = resList.subList(120, 140);
                list.addAll(list7);
                break;
            case 8:
                List<String> list8 = resList.subList(140, 160);
                list.addAll(list8);
                break;
            case 9:
                List<String> list9 = resList.subList(160, 180);
                list.addAll(list9);
                break;
            case 10:
                List<String> list10 = resList.subList(180, 196);
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
                    if (!filename.equals("delete_expression")) { // 不是删除键，显示表情
                        // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                        Class clz = Class.forName(Config.KEY_SMILE_UTILS);
                        Field field = clz.getField(filename);
                        sendMessage.append(SmileUtils.getSmiledText(AtyDsgyComment.this, (String) field.get(null), 50));
                    } else { // 删除文字或者表情
                        if (!TextUtils.isEmpty(sendMessage.getText())) {
                            int selectionStart = sendMessage.getSelectionStart();// 获取光标的位置
                            if (selectionStart > 0) {
                                String body = sendMessage.getText().toString();
                                String tempStr = body.substring(0, selectionStart);
                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    CharSequence cs = tempStr.substring(i, selectionStart);
                                    if (SmileUtils.containsKey(cs.toString()))
                                        sendMessage.getEditableText().delete(i, selectionStart);
                                    else
                                        sendMessage.getEditableText().delete(selectionStart - 1, selectionStart);
                                } else {
                                    sendMessage.getEditableText().delete(selectionStart - 1, selectionStart);
                                }
                            }
                        }
                    }
                } catch (Exception ignored) {
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "emoji_" + x;

            reslist.add(filename);

        }
        return reslist;
    }

    public void back(View view) {

        finish();
    }

    /**
     * 点击更多按钮
     *
     * @param v 更多按钮
     */
    public void more(View v) {
        closeInput();
        v.setTag(false);
        if (!isChecked) {
            isChecked = true;
            moreGridView.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
            viewPagerInsert.setVisibility(View.GONE);
            // this.closeInput();
        } else {
            isChecked = false;
            moreGridView.setVisibility(View.GONE);
            viewPagerInsert.setVisibility(View.GONE);

        }
    }

    /**
     * 插入图片的设定
     *
     * @param data 图片回传信息
     */
    private void setInsertPic(Intent data) {

        pathall = data.getExtras().getStringArrayList("pathAll");
        //如果有图片就显示，没有就不显示
        if (pathall.size() > 0) {
            //隐藏表情
            emojiIconContainer.setVisibility(View.GONE);
            //隐藏更多
            moreGridView.setVisibility(View.GONE);
            viewPagerInsert.setVisibility(View.VISIBLE);
        } else {
            viewPagerInsert.setVisibility(View.INVISIBLE);
        }
        List<View> views = new ArrayList<View>();
        View view = View.inflate(this, R.layout.papage_view, null);
        GridView gv = (GridView) view.findViewById(R.id.gridview);
        InsertPicAdapter insertPicAdapter = new InsertPicAdapter(context, pathall);
        gv.setAdapter(insertPicAdapter);
        views.add(view);
        InsertPicPagerAdapter insertPicPagerAdapter = new InsertPicPagerAdapter(views);
        viewPagerInsert.setAdapter(insertPicPagerAdapter);
        viewPagerInsert.setCurrentItem(0);
    }

    /**
     * 插入图片
     */
    private void insertPic() {

        Intent i = new Intent(AtyDsgyComment.this, AtyOpenPic.class);
        AtyDsgyComment.this.startActivityForResult(i, Config.INSERT_PIC);//这里需要改 mark
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                setInsertPic(data);
                break;
            case RESULT_MORE_REPLY:
                //getData(count,"replyComments");
                // showToast("reFlush");
            default:
                break;
        }
    }
}

