package com.beyond.popscience.module.home.fragment;

import android.text.TextUtils;
import android.widget.ImageView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.util.ImageLoaderUtil;

/**
 * 引导页fragment
 */
public class GuideFragment extends BaseFragment {

    private ImageView mIvGuide;

    private String mImgUrl;
    private int mImgResId = 0;

    @Override
    public void initUI() {
        super.initUI();
        mIvGuide = (ImageView) mRootView.findViewById(R.id.ivGuide);

        setupView();
    }

    public void setImgUrl(String url) {
        this.mImgUrl = url;
        setupView();
    }

    public void setImgResId(int resId) {
        this.mImgResId = resId;
        setupView();
    }

    private void setupView() {
        if (mIvGuide == null) {
            return;
        }
        if (!TextUtils.isEmpty(mImgUrl)) {
            ImageLoaderUtil.display(getContext(),mImgUrl,mIvGuide);
        } else if (mImgResId != -100) {
            mIvGuide.setImageResource(mImgResId);
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_guide;
    }
}
