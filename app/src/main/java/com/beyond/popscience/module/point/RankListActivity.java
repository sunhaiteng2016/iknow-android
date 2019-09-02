package com.beyond.popscience.module.point;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.PointRestUsage;
import com.beyond.popscience.frame.pojo.RankingListResult;
import com.beyond.popscience.frame.util.ImageLoaderUtil;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.point.adapter.RankingAdapter;
import com.beyond.popscience.widget.CircleImageView;
import com.beyond.popscience.widget.GridSpacingItemDecoration;

import butterknife.BindView;

/**
 * 排行榜界面
 */
public class RankListActivity extends BaseActivity {

    private static final int TASK_RANKING_LIST = 800004;

    @Request
    private PointRestUsage restUsage;
    @BindView(R.id.rank_user_head)
    CircleImageView civRankUserHead;
    @BindView(R.id.rank_point)
    TextView tvRankPoint;
    @BindView(R.id.rank_class)
    TextView tvRankClass;
    @BindView(R.id.rank_list)
    RecyclerView mlvRankList;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.shadowView)
    View shadowView;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_rank_list_layout;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("排行榜");
        shadowView.setVisibility(View.GONE);
        getRankingList();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case TASK_RANKING_LIST:
                if (msg.getIsSuccess() && msg.getObj() != null) {
                    RankingListResult ranking = (RankingListResult) msg.getObj();
                    Log.e("RankingList===", ranking.getListstq().get(0).toString());
                    RankingAdapter adapter = new RankingAdapter(this);
                    adapter.getDataList().addAll(ranking.getListstq());
                    mlvRankList.setLayoutManager(new LinearLayoutManager(this));
                    mlvRankList.addItemDecoration(new GridSpacingItemDecoration(1,
                            DensityUtil.dp2px(this, 1), false));
                    mlvRankList.setAdapter(adapter);
                    if (ranking.getScore() != null) {
                        tvRankPoint.setText(ranking.getScore().getRecordintegral());
                        tvRankClass.setText("排名：" + ranking.getScore().getRecordid());
                        ImageLoaderUtil.display(this, ranking.getScore().getRecordimg(), civRankUserHead);
                    }
                }
                break;
        }
    }

    private void getRankingList() {
//        restUsage.rankingList(TASK_RANKING_LIST, UserInfoUtil.getInstance().getUserInfo().getUserId());
        restUsage.rankingList(TASK_RANKING_LIST, "12");
    }
}
