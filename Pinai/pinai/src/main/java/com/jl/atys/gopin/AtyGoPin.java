package com.jl.atys.gopin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jl.atys.AtyMain;
import com.jl.basic.AtySupport;
import com.jl.basic.Config;
import com.jl.customs.OnTabActivityResultListener;
import com.jl.customs.age.NumericWheelProAdpter;
import com.jl.customs.age.YearDialog;
import com.jl.net.Market;
import com.jl.utils.UserTools;

import java.io.ByteArrayOutputStream;
import java.util.List;

import hrb.jl.pinai.R;

/**
 * 类描述：去招聘_推荐页面
 * 创建人：徐志国
 * 修改人：徐志国
 * 修改时间：2014-11-10
 * 下午1:48:52
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AtyGoPin extends AtySupport implements OnTabActivityResultListener {
    private PullToRefreshListView goPinLv;// 下拉刷新
    private GoPinAdapter gAdapter;
    // 翻页记录
    private String count;
    private TextView school;
    //---------------------------------------
    private String university = "";
    private boolean isFirstUni = true;//第一次选择学校
    private String birth = "";
    private String province = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_gopin);


//        //变色龙
//        setStatusBarTint(AtyGoPin.this, getResources().getColor(R.color.background));
        //----------------------------
        AtyMain parent = (AtyMain) getParent();
        // 放到父类中，可以回调
        parent.putChildGoPin(this);
        init();
//        school = (TextView) findViewById(R.id.gopin_school);
//
//        school.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               Bitmap bit = convertBitmapSize(UserTools.takeScreenShot(getParent()));
//                Intent i = new Intent(AtyGoPin.this, AtyGoPinSelect.class).putExtra("image",bit);
//                getParent().startActivityForResult(i, 0);
//            }
//        });
        ImageView head = (ImageView) findViewById(R.id.ind_head);
        UserTools.displayImage(Config.getCachePortrait(context), head, getOptions());
    }

    /**
     * 转换图片大小
     * @param bit
     * @return
     */
    private Bitmap convertBitmapSize(Bitmap bit) {
        //if (bit != null) {
            // Intent传输的bytes不能超过40k。
            // 图片允许最大空间 单位：KB
            double maxSize = 40.00;
            // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while (bit.getByteCount() >= maxSize * 1024) {
                bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                // 将字节换成KB
                double mid = b.length / 1024;
                // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
                if (mid > maxSize) {
                    // 获取bitmap大小 是允许最大大小的多少倍
                    double i = mid / maxSize;
                    // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
                    // （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
                    bit = UserTools.zoomImage(bit, bit.getWidth() / Math.sqrt(i),
                            bit.getHeight() / Math.sqrt(i));
                }
            }
            return bit;
        //}
    }

    private void init() {
        goPinLv = (PullToRefreshListView) findViewById(R.id.gopin_lv);
        goPinLv.setMode(Mode.BOTH);
        goPinLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                ILoadingLayout iLoadingLayout = refreshView.getLoadingLayoutProxy(true, false);
                iLoadingLayout.setPullLabel(label);// 刚下拉时，显示的提示
                getInfo("", Config.KEY_SHOW_COUNT, university, "", "", true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                ILoadingLayout iLoadingLayout = refreshView.getLoadingLayoutProxy(
                        false, true);
                iLoadingLayout.setPullLabel(label);// 刚下拉时，显示的提示
                getInfo(count, Config.KEY_SHOW_COUNT, university,  birth, province, false);
            }
        });
        gAdapter = new GoPinAdapter(context);
        goPinLv.getRefreshableView().setAdapter(gAdapter);
        getInfo("", Config.KEY_SHOW_COUNT, university, birth, province, false);
    }

    /**
     * 获取服务器端的用户列表装载到listview中
     *
     * @param lastid  根据用户id来翻页
     * @param perpage 每页显示多少 void
     * @param isFresh 是否刷新
     * @since 1.0.0
     */
    private void getInfo(final String lastid, String perpage, String university, String born_year, String province, final boolean isFresh) {
        String userid = Config.getCacheID(context);
        new Market(lastid, perpage, university, userid, born_year, province, new Market.SuccessCallback() {
            @Override
            public void onSuccess(List<GoPinData> data) {
                try {
                    count = data.get(data.size() - 1).getId();
                    if (isFresh) {
                        gAdapter.clear();
                        showToast("刷新成功");
                    }
                    gAdapter.addAll(data);
                    if (lastid.equals("")) {
                        //关闭进度等待
                        findViewById(R.id.playout).setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    showToast("没有数据加载啦");
                }
                //刷新完成
                goPinLv.onRefreshComplete();
            }

        }, new Market.FailCallback() {
            @Override
            public void onFail(String code) {
                if (code.equals("0")) {
                    showToast("没有匹配数据");
                    goPinLv.onRefreshComplete();
                } else {
                    findViewById(R.id.pbar).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.ptext)).setText("数据获取异常");
                    goPinLv.onRefreshComplete();
                }
            }

        });
    }

    // 这里需要的onActivityResult是父类的，所以不能传递到子类的Activity，所以实现一个接口从父类的Activity重新写
    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b = data.getExtras(); // data为B中回传的Intent
                String str;
                try {
                    str = b.getString("name");
                } catch (Exception e) {
                    str = "";
                }
                //第一次选择学校，需要把count重置成""
                if (isFirstUni) {
                    count = "";
                    isFirstUni = false;
                }
                if (str.equals("")) {
                    university = "";
                    school.setText("请输入学校名称");
                } else {
                    university = str;
                    school.setText(str);
                }
                getInfo("", Config.KEY_SHOW_COUNT, university, birth, province, true);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return getParent().onKeyDown(keyCode, event);
    }


    /**
     * 选择接口
     */
    public interface Callback {
        void cb(int select);
    }

    public void selectProvince(View v) {
        setTimerSelect(Config.MIN_PROVINCE, Config.MAX_PROVINCE, (Button) v);
    }

    public void selectBirth(View v) {
        setTimerSelect(Config.MIN_BIRTH, Config.MAX_BIRTH, (Button) v);
    }

    private void setTimerSelect(final int start, int end, final Button btn) {
        YearDialog.Builder builder = new YearDialog.Builder(context,
                AtyGoPin.this);
        builder.setPositiveButton(new YearDialog.PositiveListener() {
            @Override
            public void onClick(DialogInterface dialog, String time) {
                if (start == Config.MIN_BIRTH) {
                    //出生年
                    birth = time;
                    btn.setText(time + "年");
                    getInfo("", Config.KEY_SHOW_COUNT, university, birth, province, true);
                } else if (start == Config.MIN_PROVINCE) {
                    //省份
                    province = time;
                    btn.setText(NumericWheelProAdpter.PROVINCE[Integer.parseInt(time) - 1]);
                    getInfo("", Config.KEY_SHOW_COUNT, university, birth, province, true);
                }

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
