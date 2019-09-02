package com.beyond.popscience.module.point;


import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.PopularMainActivity;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.module.home.shopcart.SignActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 赚绿币
 */
public class MakePointActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.read_article_rl)
    RelativeLayout read_article_rl;//阅读文章
    @BindView(R.id.right_read)
    TextView right_read;//阅读

    @BindView(R.id.share_article_rl)
    RelativeLayout share_article_rl;//分享文章
    @BindView(R.id.right_read_share)
    TextView right_read_share;//分享

    @BindView(R.id.fab_article_rl)
    RelativeLayout fab_article_rl;//发布文章
    @BindView(R.id.right_read_fab)
    TextView right_read_fab;//发布

    @BindView(R.id.agree_article_rl)
    RelativeLayout agree_article_rl;//意见反馈
    @BindView(R.id.right_read_agree)
    TextView right_read_agree;//意见反馈

    @BindView(R.id.join_article_rl)
    RelativeLayout join_article_rl;//立即参与
    @BindView(R.id.right_read_join)
    TextView right_read_join;//立即参与

    @BindView(R.id.shop_article_rl)
    RelativeLayout shop_article_rl;//立即参与
    @BindView(R.id.right_read_shop)
    TextView right_read_shop;//立即参与

    @BindView(R.id.invitation_article_rl)
    RelativeLayout invitation_article_rl;//邀请收徒
    @BindView(R.id.right_read_invitation)
    TextView right_read_invitation;//邀请收徒

    @BindView(R.id.answer_article_rl)
    RelativeLayout answer_article_rl;//互动答题
    @BindView(R.id.right_read_answer)
    TextView right_read_answer;//互动答题

    @BindView(R.id.fabu_article_rl)
    RelativeLayout fabu_article_rl0;//发布动态
    @BindView(R.id.right_read_fabu)
    TextView right_read_fabu;//发布动态

    @BindView(R.id.like_article_rl)
    RelativeLayout like_article_rl0;//点赞
    @BindView(R.id.right_read_like)
    TextView right_read_like;//点赞

    @BindView(R.id.sign_article_rl)
    RelativeLayout sign_article_rl0;//签到
    @BindView(R.id.right_read_sign)
    TextView right_read_sign;//签到

    @BindView(R.id.sun_article_rl)
    RelativeLayout sun_article_rl0;//晒一晒
    @BindView(R.id.right_read_sun)
    TextView right_read_sun;//晒一晒

    @BindView(R.id.sign_rule_tv)
    TextView tvSignRule;
    @BindView(R.id.get_point_num_sign)
    TextView tvGetPointSign;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_make_point;
    }

    @Override
    public void initUI() {
        super.initUI();
        tvTitle.setText("赚绿币");
        String point = getIntent().getStringExtra("sign_days");
        tvGetPointSign.setText("+" + point +"绿币");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @OnClick({R.id.right_read_agree, R.id.right_read_invitation, R.id.right_read_sun, R.id.right_read,
            R.id.right_read_share, R.id.right_read_fab, R.id.right_read_like, R.id.right_read_shop,
            R.id.right_read_fabu, R.id.right_read_sign})
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.right_read_agree:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.right_read_invitation:
                startActivity(new Intent(this, InvitePepleActivity.class));
                break;
            case R.id.right_read_sun:
                showInput();
                break;
            case R.id.right_read://阅读
            case R.id.right_read_share://分享
            case R.id.right_read_fab://发布评论
            case R.id.right_read_like://点赞
                i = new Intent(this, PopularMainActivity.class);
                i.putExtra("fragment_id", "0");
                startActivity(i);
                break;
            case R.id.right_read_shop://购物
                i = new Intent(this, PopularMainActivity.class);
                i.putExtra("fragment_id", "3");
                startActivity(i);
                break;
            case R.id.right_read_fabu://发布动态
                i = new Intent(this, PopularMainActivity.class);
                i.putExtra("fragment_id", "2");
                startActivity(i);
                break;
            case R.id.right_read_sign://签到
                Intent  intent   = new Intent(MakePointActivity.this,SignActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showInput() {
        ShowInputPopup showInputPopup = new ShowInputPopup(this);
        showInputPopup.showPopupWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
