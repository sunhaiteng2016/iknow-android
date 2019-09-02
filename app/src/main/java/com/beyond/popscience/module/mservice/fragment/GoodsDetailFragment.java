package com.beyond.popscience.module.mservice.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.pojo.GoodsDescImgObj;
import com.beyond.popscience.frame.pojo.GoodsDetail;
import com.beyond.popscience.module.mservice.adapter.GoodsDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by linjinfa on 2017/10/11.
 * email 331710168@qq.com
 */
public class GoodsDetailFragment extends BaseFragment {

    /**
     *
     */
    private static final String EXTRA_GOODS_DETAIL_KEY = "goodsDetail";

    /**
     * @return
     */
    public static GoodsDetailFragment newInstance(GoodsDetail goodsDetail) {
        GoodsDetailFragment goodsDetailFragment = new GoodsDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_GOODS_DETAIL_KEY, goodsDetail);
        goodsDetailFragment.setArguments(bundle);

        return goodsDetailFragment;
    }

    /**
     *
     */
    @BindView(R.id.listView)
    protected ListView listView;
    /**
     *
     */
    private GoodsDetailAdapter goodsDetailAdapter;
    /**
     *
     */
    private GoodsDetail goodsDetail;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    public void initUI() {
        super.initUI();

        goodsDetail = (GoodsDetail) getArguments().getSerializable(EXTRA_GOODS_DETAIL_KEY);
        if (goodsDetail == null) {
            return;
        }
        dealGoodsDetail();

        addHeaderView();
        goodsDetailAdapter = new GoodsDetailAdapter(this);
        if (goodsDetail.goods != null) {
            goodsDetailAdapter.getDataList().addAll(goodsDetail.goods);
        }
        listView.setAdapter(goodsDetailAdapter);
    }

    /**
     * 处理数据
     */
    private void dealGoodsDetail() {
        if ("1".equals(goodsDetail.appGoodsType) || "2".equals(goodsDetail.appGoodsType)) {   //技能、任务
            goodsDetail.productId = !TextUtils.isEmpty(goodsDetail.skillId) ? goodsDetail.skillId: goodsDetail.taskId;

            if(goodsDetail.goods == null || goodsDetail.goods.size() == 0){
                List<GoodsDescImgObj> goodsDescImgObjList = new ArrayList<>();
                goodsDetail.goods = goodsDescImgObjList;

                //封面
                GoodsDescImgObj coverGoodsDescImgObj1 = new GoodsDescImgObj();
                coverGoodsDescImgObj1.setGoodsDesc(goodsDetail.introduce);
                coverGoodsDescImgObj1.setGoodsDescImg(goodsDetail.coverPic);
                goodsDescImgObjList.add(coverGoodsDescImgObj1);

                List<String> detailPicList = goodsDetail.getDetailPicList();
                if (detailPicList != null && detailPicList.size() != 0) {
                    for (String picUrl : detailPicList) {
                        GoodsDescImgObj picGoodsDescImgObj = new GoodsDescImgObj();
                        picGoodsDescImgObj.setGoodsDescImg(picUrl);
                        goodsDescImgObjList.add(picGoodsDescImgObj);
                    }
                }
            }
        }
    }

    /**
     *
     */
    private void addHeaderView() {
        View view = View.inflate(getContext(), R.layout.header_goods_detail_v2, null);
        TextView titleTxtView = (TextView) view.findViewById(R.id.titleTxtView);
        TextView categoryTxtView = (TextView) view.findViewById(R.id.categoryTxtView);
        TextView timeTxtView = (TextView) view.findViewById(R.id.timeTxtView);

        titleTxtView.setText(goodsDetail.title);

        categoryTxtView.setText(goodsDetail.classifyName);
        if (!TextUtils.isEmpty(goodsDetail.startTime)) {
            //timeTxtView.setText("任务开始时间：" + goodsDetail.startTime);
            timeTxtView.setText(goodsDetail.startTime);
        }

        listView.addHeaderView(view);
    }

}
