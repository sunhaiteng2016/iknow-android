package com.beyond.library.sharesdk.more;

import android.content.Context;
import android.content.Intent;

import cn.sharesdk.framework.CustomPlatform;

/**
 * 社团
 * Created by linjinfa on 2017/10/7.
 * email 331710168@qq.com
 */
public class ArticleShare extends CustomPlatform {

    public static final String NAME = ArticleShare.class.getSimpleName();

    private Context mContext;

    public ArticleShare(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected boolean checkAuthorize(int i, Object o) {
        return false;
    }

    @Override
    public void share(ShareParams params) {
        super.share(params);
        String titles = params.getTitle();
        String url = params.getUrl();
        String pics = params.getImageUrl();

        Intent intent = new Intent();
        intent.setAction("com.test.ShareShuoShuoTwoActivity");
        intent.addCategory("com.test.ShareShuoShuoTwoActivity.category");
        intent.putExtra("titles",titles);
        intent.putExtra("pics",pics);
        intent.putExtra("link",url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        getContext().startActivity(intent);
        /*try {
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.putExtra(Intent.EXTRA_TEXT, params.getTitle()+params.getTitleUrl());
            intent1.setType("text/plain");
            mContext.startActivity(Intent.createChooser(intent1,"share").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}
