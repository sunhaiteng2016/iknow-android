package com.beyond.popscience.module.building;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.BuildingRestUsage;
import com.beyond.popscience.frame.net.JobRestUsage;
import com.beyond.popscience.frame.pojo.ClassifyResponse;
import com.beyond.popscience.frame.pojo.SelectStatusInfo;
import com.beyond.popscience.module.building.adapter.ClassifyGridAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by danxiang.feng on 2017/10/14.
 */

public class ClassifyBuildingActivity extends BaseActivity {

    private final int REQ_CLASSIFY_ID = 2001;

    public static final String EXTRA_SELECT_TYPE = "extra_select_type";
    public static final String EXTRA_SELECT_KEY = "extra_select_key";
    public static final String EXTRA_SELECT_MULT = "extra_select_mult";
    public static final String EXTRE_MAX_SELECT_NUM = "extra_max_select_num";

    public static final String EXTRA_CLASSIFY_RESULT_KEY = "extra_classify_result_key";

    public static final String TRADE_BUILDING_ITEM[] = {"出租", "出售"};
    public static final String TRADE_RENT_ITEM[] = {"求租", "求购"};

    public static final int TYPE_DECORATE = 1;  //装修情况
    public static final int TYPE_CONFIG = 2;    //房屋配置
    public static final int TYPE_BUILDING_TRADE = 3;   //出租出售交易方式
    public static final int TYPE_RENT_TRADE = 4;   //求租求购交易方式
    public static final int TYPE_JOB_INDUSTRY = 5;  //求职招聘  行业


    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.tv_right)
    protected TextView tv_right;
    @BindView(R.id.gridView)
    protected GridView gridView;
    @BindView(R.id.emptyReLay)
    protected RelativeLayout emptyReLay;

    @Request
    private BuildingRestUsage buildingRestUsage;
    @Request
    private JobRestUsage jobRestUsage;

    private ClassifyGridAdapter gridAdapter;
    private int selectedType;  //分类类型
    private boolean isMultiselect;  //是否多选
    private List<String> selectList; //当前已经选择的内容，单选的数组里就一个
    private int maxSelecterNum = -1;   //多选最大选择个数

    /**
     * @param activity
     * @param selectedType  选择类型
     * @param selectedKey   已选参数 已选参数（多选 用英文逗号分隔）
     * @param isMultiselect 是否多选
     * @param requestCode
     */
    public static void startActivityForResult(Activity activity, int selectedType, String selectedKey, boolean isMultiselect, int requestCode) {
        Intent intent = new Intent(activity, ClassifyBuildingActivity.class);
        intent.putExtra(EXTRA_SELECT_TYPE, selectedType);
        intent.putExtra(EXTRA_SELECT_KEY, selectedKey);
        intent.putExtra(EXTRA_SELECT_MULT, isMultiselect);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param fragment
     * @param selectedType  选择类型
     * @param selectedKey   已选参数 （多选 用英文逗号分隔）
     * @param isMultiselect 是否多选
     * @param requestCode
     */
    public static void startActivityForResult(Fragment fragment, int selectedType, String selectedKey, boolean isMultiselect, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), ClassifyBuildingActivity.class);
        intent.putExtra(EXTRA_SELECT_TYPE, selectedType);
        intent.putExtra(EXTRA_SELECT_KEY, selectedKey);
        intent.putExtra(EXTRA_SELECT_MULT, isMultiselect);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity
     * @param selectedType   选择类型
     * @param selectedKey    已选参数 （多选 用英文逗号分隔）
     * @param isMultiselect  是否多选
     * @param maxSelecterNum 最大选择个数，前提：isMultiselect=true， -1为任意
     * @param requestCode
     */
    public static void startActivityForResult(Activity activity, int selectedType, String selectedKey, boolean isMultiselect, int maxSelecterNum, int requestCode) {
        Intent intent = new Intent(activity, ClassifyBuildingActivity.class);
        intent.putExtra(EXTRA_SELECT_TYPE, selectedType);
        intent.putExtra(EXTRA_SELECT_KEY, selectedKey);
        intent.putExtra(EXTRA_SELECT_MULT, isMultiselect);
        intent.putExtra(EXTRE_MAX_SELECT_NUM, maxSelecterNum);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_classify_building_layout;
    }

    @Override
    public void initUI() {
        super.initUI();

        selectedType = getIntent().getIntExtra(EXTRA_SELECT_TYPE, 0);
        String selectedKey = getIntent().getStringExtra(EXTRA_SELECT_KEY);
        isMultiselect = getIntent().getBooleanExtra(EXTRA_SELECT_MULT, false);
        maxSelecterNum = getIntent().getIntExtra(EXTRE_MAX_SELECT_NUM, -1);

        if (isMultiselect) {
            tv_right.setText("确定");
            tv_right.setVisibility(View.VISIBLE);
        } else {
            tv_right.setVisibility(View.GONE);
        }
        selectList = new ArrayList<>();
        if (!TextUtils.isEmpty(selectedKey)) {
            if (isMultiselect) {
                selectList.addAll(Arrays.asList(selectedKey.split(",")));
            } else {
                selectList.add(selectedKey);
            }
        }
        initGridView();
        initClassifyType();
    }

    private void initGridView() {
        gridAdapter = new ClassifyGridAdapter(this);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SelectStatusInfo info = gridAdapter.getItem(i);
                if (info == null || TextUtils.isEmpty(info.getTitle())) {
                    return;
                }
                if (info.isSelected()) {
                    info.setSelected(false);
                } else {
                    if (isMultiselect) {
                        if (maxSelecterNum > 0) {
                            int maxNum = 0;
                            for (SelectStatusInfo statusInfo : gridAdapter.getDataList()) {
                                if (statusInfo.isSelected()) {
                                    maxNum++;
                                }
                                if (maxNum >= maxSelecterNum) {
                                    ToastUtil.showCenter(ClassifyBuildingActivity.this, "最多选择" + maxNum + "个");
                                    return;
                                }
                            }
                        }
                        info.setSelected(true);
                    } else {
                        for (SelectStatusInfo statusInfo : gridAdapter.getDataList()) {
                            statusInfo.setSelected(false);
                        }
                        info.setSelected(true);
                        tv_right();
                    }
                }
                gridAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initClassifyType() {
        switch (selectedType) {
            case TYPE_DECORATE:
                tv_title.setText("装修情况");
                showProgressDialog();
                buildingRestUsage.getDecorateClassify(REQ_CLASSIFY_ID);
                break;
            case TYPE_CONFIG:
                tv_title.setText("配置情况");
                showProgressDialog();
                buildingRestUsage.getConfigClassify(REQ_CLASSIFY_ID);
                break;
            case TYPE_BUILDING_TRADE:
                tv_title.setText("交易方式");
                showClassify(Arrays.asList(TRADE_BUILDING_ITEM));
                break;
            case TYPE_RENT_TRADE:
                tv_title.setText("交易方式");
                showClassify(Arrays.asList(TRADE_RENT_ITEM));
                break;
            case TYPE_JOB_INDUSTRY:
                tv_title.setText("选择行业");
                showProgressDialog();
                jobRestUsage.getIndustry(REQ_CLASSIFY_ID);
                break;
            default:
                tv_title.setText("分类");
                break;
        }
    }

    private void showClassify(List<String> list) {
        List<SelectStatusInfo> selectStatusInfos = new ArrayList<>();
        SelectStatusInfo selectStatusInfo = null;
        for (String str : list) {
            boolean isSelected = false;
            if (selectList.size() > 0) {
                for (String selectedStr : selectList) {
                    if (!TextUtils.isEmpty(selectedStr) && selectedStr.equals(str)) {
                        isSelected = true;
                        break;
                    }
                }
            }
            selectStatusInfo = new SelectStatusInfo(str, str, isSelected);
            selectStatusInfos.add(selectStatusInfo);
        }
        if (selectedType == TYPE_DECORATE) {
            SelectStatusInfo noLimit = new SelectStatusInfo("不限", null, selectList.size() == 0);
            //selectStatusInfos.add(noLimit);
        }
        if (selectStatusInfos.size() % 2 == 1) {
            SelectStatusInfo zhanwei = new SelectStatusInfo("", null, false);
            //selectStatusInfos.add(zhanwei);
        }
        gridAdapter.getDataList().clear();
        gridAdapter.getDataList().addAll(selectStatusInfos);
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        switch (taskId) {
            case REQ_CLASSIFY_ID:
                if (msg.getIsSuccess()) {
                    ClassifyResponse response = (ClassifyResponse) msg.getObj();
                    if (response != null && response.getList() != null && response.getList().size() > 0) {
                        showClassify(response.getList());
                    }
                    emptyReLay.setVisibility(View.GONE);
                } else {
                    emptyReLay.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @OnClick(R.id.tv_right)
    void tv_right() {
        String result = "";
        for (SelectStatusInfo statusInfo : gridAdapter.getDataList()) {
            if (statusInfo.isSelected()) {
                result += statusInfo.getValue() + ",";
            }
        }
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        if (!TextUtils.isEmpty(result)) {
            Intent data = new Intent();
            data.putExtra(EXTRA_CLASSIFY_RESULT_KEY, result);
            setResult(Activity.RESULT_OK, data);
        } else {
            ToastUtil.showCenter(this, "至少选择一项");
            return;
        }
        finish();
    }

    @OnClick(R.id.emptyReLay)
    void emptyReLay() {
        initClassifyType();
    }

}
