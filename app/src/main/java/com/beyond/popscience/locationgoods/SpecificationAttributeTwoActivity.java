package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.bean.SpecificationAttributeBean;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品规格
 */
public class SpecificationAttributeTwoActivity extends BaseActivity {

    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    private List<SpecificationAttributeBean> datas;
    private List<SpecificationAttributeBean> listss = new ArrayList<>();
    private CommonAdapter<SpecificationAttributeBean> adapter;
    private String tips;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_specification_attribute;
    }

    @Override
    public void initUI() {
        super.initUI();
        title.setText("商品参数");
        datas = (List<SpecificationAttributeBean>) getIntent().getSerializableExtra("data");
        tips = getIntent().getStringExtra("tips");
        iniListView();
    }



    private void iniListView() {
        listss.addAll(datas);
        CreatLayoutUtils.creatLinearLayout(this, listView);
        adapter = new CommonAdapter<SpecificationAttributeBean>(this, R.layout.item_spectification_attrbute, listss) {

            @Override
            protected void convert(ViewHolder holder, SpecificationAttributeBean specificationAttributeBean, final int position) {
                holder.setText(R.id.title, specificationAttributeBean.title);
                EditText content =  holder.getView(R.id.content);
                content.setText(specificationAttributeBean.content);
                if (specificationAttributeBean.title.equals("库  存:")) {
                    content.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                if (specificationAttributeBean.title.equals("价  格:")) {
                    content.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }

                if (specificationAttributeBean.title.equals("拼团价:")) {
                    content.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
                content.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        listss.get(position).setContent(editable.toString());
                    }
                });
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.go_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.submit)
    public void onViewClickeds() {
        //获取数据源
        //整个数据列表传递过去
        //查看价格
        boolean is = true;
        for (SpecificationAttributeBean bean : listss) {
            if (bean.title.equals("价  格:")) {
                if ((bean.getContent().equals(""))) {
                    com.beyond.library.util.ToastUtil.showCenter(SpecificationAttributeTwoActivity.this, "请填写价格");
                    is = false;
                    break;
                }
            }
            if (bean.title.equals("库  存:")) {
                if ((bean.getContent().equals(""))) {
                    com.beyond.library.util.ToastUtil.showCenter(SpecificationAttributeTwoActivity.this, "请填写库存");
                    is = false;
                    break;
                }
            }
            if (bean.title.equals("价格单位:")) {
                if ((bean.getContent().equals(""))) {
                    com.beyond.library.util.ToastUtil.showCenter(SpecificationAttributeTwoActivity.this, "请填写价格单位");
                    is = false;
                    break;
                }
            }
            if (bean.title.equals("拼团价:")){
                if (bean.getContent().equals("0")){
                    com.beyond.library.util.ToastUtil.showCenter(SpecificationAttributeTwoActivity.this, "拼团价不能填写0");
                    is = false;
                    break;
                }
            }
        }
        if (is) {
            Intent intent = new Intent();
            intent.putExtra("lists", (Serializable) listss);
            intent.putExtra("tips", tips);
            setResult(10089, intent);
            finish();
        }
    }
}
