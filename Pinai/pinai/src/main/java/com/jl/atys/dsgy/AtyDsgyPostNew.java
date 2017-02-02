package com.jl.atys.dsgy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jl.atys.chat.adapter.ExpressionAdapter;
import com.jl.atys.chat.adapter.ExpressionPagerAdapter;
import com.jl.atys.chat.utils.SmileUtils;
import com.jl.atys.chat.widget.ExpandGridView;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.net.ForumPostnew;
import com.jl.net.UploadImage;
import com.jl.opengallery.AtyOpenPic;
import com.jl.utils.XuDecodeBase64;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * 发帖子
 */
public class AtyDsgyPostNew extends AtySupport {
    private EditText title;
    private EditText content;
    private List<String> pathall = new ArrayList<String>();//图片路径
    private ViewPager viewPagerInsert;//插入图片
    //-----------------------------------------------------
    //表情指标
    private int currIndex = -1;
    private RelativeLayout emojiIconContainer;
    private List<String> reslist;
    //-----------------------------------------------------
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_dsgy_post_new);
        setStatusBarTint(AtyDsgyPostNew.this, getResources().getColor(R.color.background_pink));
        categoryId = (String) getIntent().getExtras().get("categoryId");
        title = (EditText) findViewById(R.id.post_new_send_title);
        content = (EditText) findViewById(R.id.post_new_send_content);
        viewPagerInsert = (ViewPager) findViewById(R.id.vPager_insert);
        setFacialPage();
    }

    /**
     * 发送帖子
     *
     * @param v
     */
    public void send(View v) {
        showProgressDialog(context, "", "发送中...", true);
        final String titlec = title.getText().toString();
        final String contentc = content.getText().toString();
        if (TextUtils.isEmpty(titlec) || TextUtils.isEmpty(contentc)) {
            showToast("不能发送空消息");
            return;
        }
        //有图片的话
        //assert pathall != null;
        if (pathall.size() > 0) {
            XuDecodeBase64 decodeBase64 = new XuDecodeBase64().invoke(pathall);
            String pathBase64 = decodeBase64.getPathBase64();

            if (pathBase64 != null) {
                //有图片
                new UploadImage(pathBase64, new UploadImage.SuccessCallback() {
                    @Override
                    public void onSuccess(String url) {
                        String userid = Config.getCacheID(context);
                        postTextContent(titlec, contentc + url, userid);
                    }
                }, new UploadImage.FailCallback() {
                    @Override
                    public void onFail(String error) {
                        showToast("error");
                    }
                });
            } else {
                //图片解析失败
                String userid = Config.getCacheID(context);
                postTextContent(titlec, contentc, userid);
            }
        } else {
            //没有图片
            String userid = Config.getCacheID(context);
            postTextContent(titlec, contentc, userid);
        }
    }

    private void postTextContent(String titlec, String contentc, String userid) {
        new ForumPostnew(userid, categoryId, titlec, contentc, new ForumPostnew.SuccessCallback() {
            @Override
            public void onSuccess() {
                closeProgressDialog();
                showToast("发送成功");
                Intent i = new Intent(AtyDsgyPostNew.this, AtyDsgyForum.class);
                setResult(RESULT_OK, i);
                AtyDsgyPostNew.this.finish();
            }
        }, new ForumPostnew.FailCallback() {
            @Override
            public void onFail(String error) {
                closeProgressDialog();
                showToast("发送失败");
            }
        });
    }

    /**
     * 插入图片
     *
     * @param v
     */
    public void insertPic(View v) {
        emojiIconContainer.setVisibility(View.GONE);
        Intent i = new Intent(AtyDsgyPostNew.this, AtyOpenPic.class);
        AtyDsgyPostNew.this.startActivityForResult(i, Config.INSERT_PIC);//这里需要改 mark
    }

    public void back(View v) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:

                setInsertPic(data);
                break;
            default:
                break;
        }
    }

    /**
     * 表情
     *
     * @param v
     */
    public void insertexpression(View v) {
        viewPagerInsert.setVisibility(View.GONE);
        emojiIconContainer.setVisibility(View.VISIBLE);
    }


    /**
     * 插入图片的设定
     *
     * @param data
     */
    private void setInsertPic(Intent data) {
        pathall = data.getExtras().getStringArrayList("pathAll");
        //如果有图片就显示，没有就不显示
        if (pathall.size() > 0) {
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
     * 设置表情
     */
    private void setFacialPage() {
        emojiIconContainer = (RelativeLayout) findViewById(R.id.ll_face_container);
        ViewPager expressionViewpager = (ViewPager) findViewById(R.id.vPager);
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
                    if (!filename.equals("delete_expression")) { // 不是删除键，显示表情
                        // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                        Class clz = Class.forName(Config.KEY_SMILE_UTILS);
                        Field field = clz.getField(filename);
                        content.append(SmileUtils.getSmiledText(AtyDsgyPostNew.this, (String) field.get(null), 30));
                    } else { // 删除文字或者表情
                        if (!TextUtils.isEmpty(content.getText())) {
                            int selectionStart = content.getSelectionStart();// 获取光标的位置
                            if (selectionStart > 0) {
                                String body = content.getText().toString();
                                String tempStr = body.substring(0, selectionStart);
                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    CharSequence cs = tempStr.substring(i, selectionStart);
                                    if (SmileUtils.containsKey(cs.toString()))
                                        content.getEditableText().delete(i, selectionStart);
                                    else
                                        content.getEditableText().delete(selectionStart - 1, selectionStart);
                                } else {
                                    content.getEditableText().delete(selectionStart - 1, selectionStart);
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


}
