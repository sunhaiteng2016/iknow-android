package com.beyond.popscience.module.square;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.util.ToastUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.BuildingRestUsage;
import com.beyond.popscience.frame.net.JobRestUsage;
import com.beyond.popscience.frame.net.SquareRestUsage;
import com.beyond.popscience.frame.pojo.ClassifyInfo;
import com.beyond.popscience.frame.pojo.ClassifyMenu;
import com.beyond.popscience.frame.pojo.ClassifyResponse;
import com.beyond.popscience.module.square.adapter.ClassifyTwoLevelListAdapter;
import com.beyond.popscience.module.square.adapter.ClassifyOneLevelListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by danxiang.feng on 2017/10/12.
 */

public class ClassifyActivity extends BaseActivity {

    private final int REQ_SKILL_CLASSIFY_ID = 1601;

    public static final String EXTRA_SELECT_TYPE = "extra_select_type";
    public static final String EXTRA_SELECT_KEY = "extra_select_key";
    public static final String EXTRA_CLASSIFY_KEY = "extra_classify_key";

    public static final int TYPE_SELECT_SKILL = 1;
    public static final int TYPE_SELECT_BUILDING = 2;
    public static final int TYPE_SELECT_JOB_POSITION = 3;

    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    @BindView(R.id.selectTxtView)
    protected TextView selectTxtView;
    @BindView(R.id.oneLevelListView)
    protected ListView oneLevelListView;
    @BindView(R.id.twoLevelListView)
    protected ListView twoLevelListView;
    @BindView(R.id.emptyReLay)
    protected RelativeLayout emptyReLay;

    @Request
    private SquareRestUsage squareRestUsage;
    @Request
    private BuildingRestUsage buildingRestUsage;
    @Request
    private JobRestUsage jobRestUsage;

    private ClassifyOneLevelListAdapter oneLevelAdapter;
    private ClassifyTwoLevelListAdapter twoLevelAdapter;


    private int selectType;  //选择类型
    private String[] selectStrs;  //选择的值

    public static void startActivityForResult(Fragment context, int selectType, String selectInfo, int requestCode) {
        Intent intent = new Intent(context.getContext(), ClassifyActivity.class);
        intent.putExtra(EXTRA_SELECT_TYPE, selectType);
        intent.putExtra(EXTRA_SELECT_KEY, selectInfo);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Activity context, int selectType, String selectInfo, int requestCode) {
        Intent intent = new Intent(context, ClassifyActivity.class);
        intent.putExtra(EXTRA_SELECT_TYPE, selectType);
        intent.putExtra(EXTRA_SELECT_KEY, selectInfo);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_skill_classify_layout;
    }

    @Override
    public void initUI() {
        super.initUI();

        selectType = getIntent().getIntExtra(EXTRA_SELECT_TYPE, 0);
        String selectStr = getIntent().getStringExtra(EXTRA_SELECT_KEY);
        if (!TextUtils.isEmpty(selectStr)) {
            selectStrs = selectStr.split(",");
        }

        initListAndGridView();
        requestSkillClassify();
    }

    private void initListAndGridView() {
        oneLevelAdapter = new ClassifyOneLevelListAdapter(this);
        oneLevelListView.setAdapter(oneLevelAdapter);

        twoLevelAdapter = new ClassifyTwoLevelListAdapter(this);
        twoLevelListView.setAdapter(twoLevelAdapter);

        oneLevelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClassifyInfo info = oneLevelAdapter.getItem(i);
                oneLevelAdapter.setSelectStr(info.getName());

                twoLevelAdapter.getDataList().clear();
                if (info.getMenu() != null && info.getMenu().size() > 0) {
                    twoLevelAdapter.getDataList().addAll(info.getMenu());
                }
                twoLevelAdapter.notifyDataSetChanged();
                oneLevelAdapter.notifyDataSetChanged();
            }
        });

        twoLevelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClassifyMenu menu = twoLevelAdapter.getItem(i);

                twoLevelAdapter.setSelectStr(menu.getMenuName());

                String result = oneLevelAdapter.getSelectStr() + "," + twoLevelAdapter.getSelectStr();
                backWithReult(result);
            }
        });
    }

    private void backWithReult(String result) {
        Intent data = new Intent();
        data.putExtra(EXTRA_CLASSIFY_KEY, result);
        setResult(Activity.RESULT_OK, data);
        finish();
    }


    private void requestSkillClassify() {
        showProgressDialog();
        switch (selectType) {
            case TYPE_SELECT_SKILL:
                tvTitle.setText("选择类别");
                squareRestUsage.getSkillClassify(REQ_SKILL_CLASSIFY_ID);
                break;
            case TYPE_SELECT_BUILDING:
                tvTitle.setText("选择类别");
                buildingRestUsage.getClassify(REQ_SKILL_CLASSIFY_ID);
                break;
            case TYPE_SELECT_JOB_POSITION:
                tvTitle.setText("选择职位");
                jobRestUsage.getClassify(REQ_SKILL_CLASSIFY_ID);
                break;
            default:
                tvTitle.setText("选择类别");
                dismissProgressDialog();
                ToastUtil.showCenter(this, "参数异常");
                emptyReLay.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void refreshView(List<ClassifyInfo> classifyInfos) {
        ClassifyInfo selectOneLevelInfo = classifyInfos.get(0);

        oneLevelAdapter.getDataList().clear();
        oneLevelAdapter.getDataList().addAll(classifyInfos);
        if (selectStrs != null && selectStrs.length > 0 && !TextUtils.isEmpty(selectStrs[0])) {
            oneLevelAdapter.setSelectStr(selectStrs[0]);
            for (int i = 0; i < classifyInfos.size(); i++) {
                if (selectStrs[0].equals(classifyInfos.get(i).getName())) {
                    selectOneLevelInfo = classifyInfos.get(i);
                }
            }
        } else {
            oneLevelAdapter.setSelectStr(classifyInfos.get(0).getName());
        }
        oneLevelAdapter.notifyDataSetChanged();


        twoLevelAdapter.getDataList().clear();
        twoLevelAdapter.getDataList().addAll(selectOneLevelInfo.getMenu());
        if (selectStrs != null && selectStrs.length > 1) {
            twoLevelAdapter.setSelectStr(selectStrs[1]);
        } else {
            twoLevelAdapter.setSelectStr(null);
        }
        twoLevelAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        dismissProgressDialog();
        switch (taskId) {
            case REQ_SKILL_CLASSIFY_ID:
                if (msg.getIsSuccess()) {
                    ClassifyResponse response = (ClassifyResponse) msg.getObj();
                    if (response != null && response.getCategory() != null && response.getCategory().size() > 0) {
                        refreshView(response.getCategory());
                        emptyReLay.setVisibility(View.GONE);
                        return;
                    }
                }
                emptyReLay.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.selectTxtView)
    void selectTxtView() {
        backWithReult("");
    }

    @OnClick(R.id.emptyReLay)
    void emptyReLay() {
        requestSkillClassify();
    }
}
