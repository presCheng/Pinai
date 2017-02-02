package com.jl.atys.dsgy;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.domain.GetCatBean;
import com.jl.net.ForumGetCat;

import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类描述：爱诊所
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-24
 * 下午12:42:11
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyDsgyForum extends AtySupport {
    //第一次传空
    private static final String FLAG = "null";
    private PullToRefreshListView listView;// 下拉刷新
    private ForumAdapter adapter;
    // 翻页记录
    private String count;
    private String userId;
    private String categoryId;
    private Button postNewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_dsgy_forum);
        setStatusBarTint(AtyDsgyForum.this, getResources().getColor(R.color.background_pink));
        userId = (String) getIntent().getExtras().get("userId");
        categoryId = (String) getIntent().getExtras().get("categoryId");
        String sex = Config.getCacheSex(context);
        init();
        if (categoryId.equals(Config.KEY_CAT_AZS)) {
            ((TextView) findViewById(R.id.bbs_title)).setText(getString(R.string.aqzs));
        } else if (categoryId.equals(Config.KEY_CAT_NRB)) {
            ((TextView) findViewById(R.id.bbs_title)).setText(getString(R.string.mens));
            if (sex.equals("F")) {
                postNewBtn.setVisibility(View.INVISIBLE);
            }
        } else if (categoryId.equals(Config.KEY_CAT_NRW)) {
            ((TextView) findViewById(R.id.bbs_title)).setText(getString(R.string.womens));
            if (sex.equals("M")) {
                postNewBtn.setVisibility(View.INVISIBLE);
            }
        }

    }

    /**
     * 发帖子
     *
     * @param v
     */
    public void postNew(View v) {
        Intent i = new Intent(AtyDsgyForum.this, AtyDsgyPostNew.class);
        i.putExtra("categoryId", categoryId);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                findViewById(R.id.playout).setVisibility(View.VISIBLE);
                getInfo(categoryId, FLAG, Config.KEY_SHOW_COUNT);
                break;
            default:

                break;
        }
    }

    private void init() {
        listView = (PullToRefreshListView) findViewById(R.id.bbs_lv);
        adapter = new ForumAdapter(AtyDsgyForum.this);
        listView.getRefreshableView().setAdapter(adapter);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        postNewBtn = (Button) findViewById(R.id.dsgy_bbs_submit);
        // Set a listener to be invoked when the list should be refreshed.
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                ILoadingLayout iLoadingLayout = refreshView.getLoadingLayoutProxy(
                        false, true);
                iLoadingLayout.setPullLabel(label);// 刚下拉时，显示的提示
                iLoadingLayout.setRefreshingLabel("正在载入…");// 刷新时
                iLoadingLayout.setPullLabel("上拉刷新…");
                iLoadingLayout.setReleaseLabel("放开刷新…");// 下来达到一定距离时，显示的提示
                getInfo(categoryId, count, Config.KEY_SHOW_COUNT);
            }
        });
        getInfo(categoryId, FLAG, Config.KEY_SHOW_COUNT);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //如果不是置顶的
                if (adapter.getItem(position - 1) instanceof GetCatBean) {
                    String postId = ((GetCatBean) adapter.getItem(position - 1)).getPostId();
                    Intent intent = new Intent(AtyDsgyForum.this, AtyDsgyComment.class);
                    intent.putExtra("postId", postId);
                    startActivity(intent);
                }
            }
        });


    }

    /**
     * 获取服务器端的论坛帖子列表装载到listview中
     *
     * @param catid   论坛号
     * @param lastid  最后一个id号
     * @param perpage 每一页显示的数量
     *                每页显示多少 void
     * @since 1.0.0
     */
    private void getInfo(String catid, final String lastid, String perpage) {

        new ForumGetCat(catid, lastid, perpage, "50", userId, new ForumGetCat.SuccessCallback() {
            @Override
            public void onSuccess(List<Object> data) {
                //论坛未开放，不能点击发送帖子
                postNewBtn.setEnabled(true);
                try {
                    //如果除了置顶还有帖子
                    if (data.size() > 1) {
                        //获得最后一个的postid
                        count = ((GetCatBean) data.get(data.size() - 1)).getPostId();
                    }
                    if (lastid.equals(FLAG)) {
                        //关闭进度等待
                        adapter.clear();
                        adapter.addAll(data);
                        findViewById(R.id.playout).setVisibility(View.GONE);
                    } else {
                        adapter.addAll(data);
                    }
                } catch (Exception e) {
                    showToast("没有数据加载啦");
                }
                //刷新完成
                listView.onRefreshComplete();
            }
        }, new ForumGetCat.FailCallback() {
            @Override
            public void onFail(String error) {
                //论坛未开放
                if (error.equals("2")) {
                    findViewById(R.id.playout).setVisibility(View.GONE);
                    findViewById(R.id.forum_no_open).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.pbar).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.ptext)).setText("数据获取异常");
                    listView.onRefreshComplete();
                }
            }
        });
    }


    public void back(View view) {
        finish();
    }

}
