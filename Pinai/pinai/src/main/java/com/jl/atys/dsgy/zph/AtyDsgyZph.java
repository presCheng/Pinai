
package com.jl.atys.dsgy.zph;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.customs.age.NumericWheelProAdpter;
import com.jl.customs.age.YearDialog;
import com.jl.net.LikeJobs;

import java.util.List;

import hrb.jl.pinai.R;

public class AtyDsgyZph extends AtySupport {
    private PullToRefreshListView listView;
    private ZphAdapter zAdpter;
    private String count;

    private String province = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_dsgy_zph);
        setStatusBarTint(AtyDsgyZph.this, getResources().getColor(R.color.background_pink));

        initView();
    }

    public void qzp(View v) {
        startActivity(new Intent(AtyDsgyZph.this, AtyDsgyQzp.class));
    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.listview);
        zAdpter = new ZphAdapter(context);
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
                zAdpter.clear();
                getInfos("", province, true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                getInfos(count, province, false);
            }
        });
        listView.getRefreshableView().setAdapter(zAdpter);
        getInfos("", province, true);
    }

    private void getInfos(String lastId, String province, final boolean isFirst) {

        new LikeJobs(Config.getCacheID(context), lastId, "10", province,new LikeJobs.SuccessCallback() {
            @Override
            public void onSuccess(String status, List<ZphData> datas) {
                try {
                    if (isFirst) {
                        zAdpter.clear();
                    }
                    count = datas.get(datas.size() - 1).getPostId();
                    zAdpter.addAll(datas);
                    showToast("刷新成功");
                } catch (Exception e) {
                    showToast("没有数据加载啦");
                }
                if (status.equals("2")) {
                    findViewById(R.id.qzp).setVisibility(View.GONE);
                    showToast("亲，您的资料不全，请填写完全哦");
                }
                listView.onRefreshComplete();
            }
        }, new LikeJobs.FailCallback() {
            @Override
            public void onFail(String error) {
                showToast("没有匹配的信息");
            }
        });
        if (isFirst) {
            findViewById(R.id.playout).setVisibility(View.GONE);
        }
    }

    public void back(View v) {
        finish();
    }

    public void selectProvince(View v) {
        setTimerSelect(Config.MIN_PROVINCE, Config.MAX_PROVINCE, (TextView) v);
    }

    private void setTimerSelect(final int start, int end, final TextView tv) {
        YearDialog.Builder builder = new YearDialog.Builder(context,
                AtyDsgyZph.this);
        builder.setPositiveButton(new YearDialog.PositiveListener() {
            @Override
            public void onClick(DialogInterface dialog, String time) {
                tv.setText(NumericWheelProAdpter.PROVINCE[Integer.parseInt(time) - 1]);
                province = time;
                getInfos("",time, true);
                dialog.cancel();
            }
        });
        builder.setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create(start, end).show();
    }

}
