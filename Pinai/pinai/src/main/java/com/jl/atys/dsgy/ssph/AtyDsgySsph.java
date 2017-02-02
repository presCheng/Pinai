package com.jl.atys.dsgy.ssph;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jl.atys.gopin.GoPinData;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.net.MembersRank;

import java.util.List;

import hrb.jl.pinai.R;

public class AtyDsgySsph extends AtySupport {
    private PullToRefreshListView listView;
    private SsphAdapter sAdpter;
    private String count;
    private  TextView paiMing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_dsgy_ssph);
        initView();
    }

    private void initView() {
        paiMing = (TextView) findViewById(R.id.paiming_num);
        ImageView bg =(ImageView) findViewById(R.id.background);
        if (Config.getCacheSex(context).equals("F")){
            bg.setBackgroundResource(R.color.background_pink);

        }else{
            bg.setBackgroundResource(R.color.background_blue);
        }
        setSystemBar(0);
        listView = (PullToRefreshListView) findViewById(R.id.listview);
        sAdpter = new SsphAdapter(context);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                ILoadingLayout iLoadingLayout = refreshView.getLoadingLayoutProxy(true, false);
                iLoadingLayout.setPullLabel(label);// 刚下拉时，显示的提示
                iLoadingLayout.setRefreshingLabel("正在载入…");// 刷新时
                iLoadingLayout.setPullLabel("下拉刷新…");
                iLoadingLayout.setReleaseLabel("放开刷新…");// 下来达到一定距离时，显示的提示
                getInfo("", Config.KEY_SHOW_COUNT, true,false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                getInfo(count, Config.KEY_SHOW_COUNT, false,false);
            }
        });
        listView.getRefreshableView().setAdapter(sAdpter);
        getInfo("", Config.KEY_SHOW_COUNT, false, true);
    }
    /**
     * 获取服务器端的用户列表装载到listview中
     *
     * @param lastid  根据用户id来翻页
     * @param perpage 每页显示多少 void
     * @param isFresh 是否刷新
     * @param first 是第一次
     * @since 1.0.0
     */
    private void getInfo(final String lastid, String perpage, final boolean isFresh, final boolean first) {
        String userid = Config.getCacheID(context);
        new MembersRank(lastid, perpage, userid, new MembersRank.SuccessCallback() {
            @Override
            public void onSuccess(List<GoPinData> data,String rank) {
                try {
                    count = data.get(data.size() - 1).getId();
                    if (isFresh) {
                        sAdpter.clear();
                        showToast("刷新成功");
                    }
                    sAdpter.addAll(data);
                    if (lastid.equals("")) {
                        //关闭进度等待
                        findViewById(R.id.playout).setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    showToast("没有数据加载啦");
                }
                if(first){
                    paiMing.setText(rank);
                }
                //刷新完成
                listView.onRefreshComplete();
            }

        }, new MembersRank.FailCallback() {
            @Override
            public void onFail() {
                findViewById(R.id.pbar).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.ptext)).setText("数据获取异常");
                listView.onRefreshComplete();
            }

        });
    }
    public void back(View v) {
        finish();
    }
}
