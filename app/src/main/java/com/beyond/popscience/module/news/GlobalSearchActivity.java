package com.beyond.popscience.module.news;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.net.bean.BaseResponse;
import com.beyond.library.util.DensityUtil;
import com.beyond.library.view.AutoWrapView;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.db.operation.SearchHistoryV4Operation;
import com.beyond.popscience.frame.net.BuildingRestUsage;
import com.beyond.popscience.frame.net.SearchRestUsage;
import com.beyond.popscience.frame.net.ServiceRestUsage;
import com.beyond.popscience.frame.net.SocialRestUsage;
import com.beyond.popscience.frame.net.SquareRestUsage;
import com.beyond.popscience.frame.net.TownNoticeRestUsage;
import com.beyond.popscience.frame.net.TownRestUsage;
import com.beyond.popscience.frame.pojo.SearchHistoryV4;
import com.beyond.popscience.frame.pojo.SuggestVo;
import com.beyond.popscience.frame.util.D;
import com.beyond.popscience.module.building.BuildingActivity;
import com.beyond.popscience.module.job.JobActivity;
import com.beyond.popscience.module.mservice.GoodsSearchActivity;
import com.beyond.popscience.module.mservice.GoodsSearchV2Activity;
import com.beyond.popscience.module.news.task.LoadHistoryTask;
import com.beyond.popscience.module.social.SocialCircleSearchResultActivity;
import com.beyond.popscience.module.square.SquareActivity;
import com.beyond.popscience.module.town.TownAddressSearchResultActivity;

import java.util.List;

import butterknife.BindView;

/**
 * 搜索
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class GlobalSearchActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 默认搜索 新闻
     */
    public final static int TYPE_DEFAULT = 0;
    /**
     * 搜索圈子
     */
    private final static int TYPE_SOCIAL = 1;
    /**
     * 搜索乡镇
     */
    public final static int TYPE_TOWN = 2;
    /**
     * 搜索地址
     */
    private final static int TYPE_TOWN_ADDRESS = 3;
    /**
     * 搜索新地址  城区
     */
    private final static int TYPE_AREA_ADDRESS = 301;
    /**
     * 搜索服务
     */
    private final static int TYPE_SERVICE = 4;
    /**
     * 搜索公告
     */
    public final static int TYPE_TOWN_ANNO = 5;
    /**
     * 搜索服务商品
     */
    private final static int TYPE_SERVICE_GOODS = 6;
    /**
     * 搜索技能
     */
    private final static int TYPE_SKILL_SQUARE = 7;
    /**
     * 搜索任务
     */
    private final static int TYPE_TASK_SQUARE = 8;
    /**
     * 搜索出租出售
     */
    private final static int TYPE_BUILDING = 9;
    /**
     * 搜索求租求购
     */
    private final static int TYPE_RENT = 10;
    /**
     * 搜索招聘
     */
    private final static int TYPE_JOB_PROVIDE = 11;
    /**
     * 搜索求职
     */
    private final static int TYPE_JOB_APPLY = 12;
    /**
     *
     */
    private final static String EXTRA_SEARCH_TYPE_KEY = "searchType";
    /**
     * 请求历史记录
     */
    private final int REQUEST_HISTORY_TASK_ID = 101;
    /**
     * 热门搜索
     */
    private final int REQUEST_HOT_SEARCH_TASK_ID = 102;
    /**
     *
     */
    private SearchHistoryV4Operation searchHistoryV4Operation = new SearchHistoryV4Operation();
    /**
     * 历史记录
     */
    private AutoWrapView historyAutoWrapView;
    /**
     * 热门搜索
     */
    private AutoWrapView hotAutoWrapView;
    /**
     *
     */
    private LinearLayout historyLinLay;
    /**
     *
     */
    private Button searchBtn;
    /**
     *
     */
    private EditText searchEditTxt;
    /**
     *
     */
    private ImageView delHistoryImgView;

    @BindView(R.id.hotLabelTxtView)
    protected TextView hotLabelTxtView;

    @Request
    private SearchRestUsage searchRestUsage;
    @Request
    private ServiceRestUsage serviceRestUsage;
    @Request
    private SocialRestUsage socialRestUsage;
    @Request
    private TownRestUsage townRestUsage;
    @Request
    private TownNoticeRestUsage townNoticeRestUsage;
    @Request
    private SquareRestUsage squareRestUsage;
    @Request
    private BuildingRestUsage buildingRestUsage;
    /**
     *
     */
    private int type = TYPE_DEFAULT;

    /**
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        context.startActivity(intent);
    }

    /**
     * @param context
     */
    public static void startActivitySocial(Context context) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_SOCIAL);
        context.startActivity(intent);
    }

    /**
     * @param context
     */
    public static void startActivityTown(Context context) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_TOWN);
        context.startActivity(intent);
    }

    /**
     * @param context
     */
    public static void startActivityTownAddress(Activity context, int areaType) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, areaType == 1 ? TYPE_AREA_ADDRESS : TYPE_TOWN_ADDRESS);
        context.startActivity(intent);
    }

    /**
     * 服务搜索
     *
     * @param context
     */
    public static void startActivityService(Context context) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_SERVICE);
        context.startActivity(intent);
    }

    /**
     * 服务搜索
     *
     * @param context
     */
    public static void startActivityServiceGoods(Context context) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_SERVICE_GOODS);
        context.startActivity(intent);
    }

    /**
     * 搜索技能
     */
    public static void startActivitySkillSquare(Context context) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_SKILL_SQUARE);
        context.startActivity(intent);
    }

    /**
     * 搜索任务
     *
     * @param context
     */
    public static void startActivityTaskSquare(Context context) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_TASK_SQUARE);
        context.startActivity(intent);
    }

    /**
     * 搜索出租出售
     *
     * @param context
     */
    public static void startActivityBuiling(Context context) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_BUILDING);
        context.startActivity(intent);
    }

    /**
     * 搜索求租求购
     *
     * @param context
     */
    public static void startActivityRent(Context context) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_RENT);
        context.startActivity(intent);
    }

    /**
     * 搜索招聘
     *
     * @param context
     */
    public static void startActivityJobProvide(Context context) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_JOB_PROVIDE);
        context.startActivity(intent);
    }

    /**
     * 搜索求职
     *
     * @param context
     */
    public static void startActivityJobApply(Context context) {
        Intent intent = new Intent(context, GlobalSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_JOB_APPLY);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_global_search;
    }

    @Override
    public void initUI() {
        type = getIntent().getIntExtra(EXTRA_SEARCH_TYPE_KEY, TYPE_DEFAULT);

        searchEditTxt = (EditText) findViewById(R.id.searchEditTxt);
        historyLinLay = (LinearLayout) findViewById(R.id.historyLinLay);
        historyAutoWrapView = (AutoWrapView) findViewById(R.id.historyAutoWrapView);
        hotAutoWrapView = (AutoWrapView) findViewById(R.id.hotAutoWrapView);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        delHistoryImgView = (ImageView) findViewById(R.id.delHistoryImgView);
        searchBtn.setOnClickListener(this);
        delHistoryImgView.setOnClickListener(this);

        hotLabelTxtView.setVisibility(View.GONE);

        initHistory();

        if (!isSearchTownAddress() && !isSearchAreaAddress()) {   //非搜索城镇地址
            initHotSearch();
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case REQUEST_HISTORY_TASK_ID:   //请求历史记录
                List<SearchHistoryV4> searchHistoryV4List = (List<SearchHistoryV4>) msg.getObj();
                addHistoryView(searchHistoryV4List);
                break;
            case REQUEST_HOT_SEARCH_TASK_ID:    //热门搜索
                if (msg.getIsSuccess()) {
                    BaseResponse<SuggestVo> baseResponse = msg.getBaseResponse();
                    if (baseResponse != null && baseResponse.getData() != null && baseResponse.getData().getHotWords() != null && baseResponse.getData().getHotWords().size() != 0) {
                        addHotSearchView(baseResponse.getData().getHotWords());
                    }
                }
                break;
        }
    }

    /**
     * 默认搜索
     *
     * @return
     */
    private boolean isSearchDefault() {
        return type == TYPE_DEFAULT;
    }

    /**
     * 搜索圈子
     *
     * @return
     */
    private boolean isSearchSocial() {
        return type == TYPE_SOCIAL;
    }

    /**
     * 搜索乡镇
     *
     * @return
     */
    private boolean isSearchTown() {
        return type == TYPE_TOWN;
    }

    /**
     * 搜索乡镇公告
     *
     * @return
     */
    private boolean isSearchTownAnno() {
        return type == TYPE_TOWN_ANNO;
    }

    /**
     * 搜索乡镇地址
     *
     * @return
     */
    private boolean isSearchTownAddress() {
        return type == TYPE_TOWN_ADDRESS;
    }

    /**
     * 搜索新地址
     *
     * @return
     */
    private boolean isSearchAreaAddress() {
        return type == TYPE_AREA_ADDRESS;
    }

    /**
     * 搜索服务
     *
     * @return
     */
    private boolean isSearchService() {
        return type == TYPE_SERVICE;
    }

    /**
     * 搜索服务商品
     *
     * @return
     */
    private boolean isSearchServiceGoods() {
        return type == TYPE_SERVICE_GOODS;
    }

    /**
     * 搜索技能
     *
     * @return
     */
    private boolean isSearchSkillSquare() {
        return type == TYPE_SKILL_SQUARE;
    }

    /**
     * 搜索任务
     *
     * @return
     */
    private boolean isSearchTaskSquare() {
        return type == TYPE_TASK_SQUARE;
    }

    /**
     * 搜索出租出售
     *
     * @return
     */
    private boolean isSearchBuilding() {
        return type == TYPE_BUILDING;
    }

    /**
     * 搜索求租求购
     *
     * @return
     */
    private boolean isSearchRent() {
        return type == TYPE_RENT;
    }

    /**
     * 招聘
     *
     * @return
     */
    private boolean isSearchJobProvide() {
        return type == TYPE_JOB_PROVIDE;
    }

    /**
     * 求职
     *
     * @return
     */
    private boolean isSearchJobApply() {
        return type == TYPE_JOB_APPLY;
    }

    /**
     * 初始化 历史记录
     */
    private void initHistory() {
        historyAutoWrapView.setHorizontalSpaceMargin(DensityUtil.dp2px(this, 10));
        historyAutoWrapView.setVerticalSpaceMargin(DensityUtil.dp2px(this, 15));

        //默认隐藏
        historyLinLay.setVisibility(View.GONE);
        historyAutoWrapView.setOnItemClickListener(new AutoWrapView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object targetObj) {
                SearchHistoryV4 searchHistoryV4 = (SearchHistoryV4) targetObj;
                search(searchHistoryV4.getContent());
            }
        });

        requestLoadHistory();
    }

    /**
     * 热门搜索
     */
    private void initHotSearch() {
        hotAutoWrapView.setHorizontalSpaceMargin(DensityUtil.dp2px(this, 10));
        hotAutoWrapView.setVerticalSpaceMargin(DensityUtil.dp2px(this, 15));

        hotAutoWrapView.setOnItemClickListener(new AutoWrapView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object targetObj) {
                search(targetObj.toString());
            }
        });

        requestHotSearch();
    }

    /**
     * 添加历史记录view
     */
    private void addHistoryView(List<SearchHistoryV4> searchHistoryV4List) {
        if (searchHistoryV4List != null && searchHistoryV4List.size() != 0) {
            historyLinLay.setVisibility(View.VISIBLE);
            historyAutoWrapView.removeAllViews();

            for (SearchHistoryV4 searchHistoryV4 : searchHistoryV4List) {
                historyAutoWrapView.addDefaultTxtView(searchHistoryV4, searchHistoryV4.getContent(), getResources().getDimensionPixelSize(R.dimen.sp14), Color.BLACK, R.drawable.bg_round_grey1);
            }
        } else {
            historyLinLay.setVisibility(View.GONE);
        }
    }

    /**
     * 添加热门搜索view
     */
    private void addHotSearchView(List<String> hotList) {
        if (hotList != null && hotList.size() != 0) {
            hotLabelTxtView.setVisibility(View.VISIBLE);
            hotAutoWrapView.removeAllViews();

            for (String hotStr : hotList) {
                hotAutoWrapView.addDefaultTxtView(hotStr, hotStr, getResources().getDimensionPixelSize(R.dimen.sp14), Color.BLACK, R.drawable.bg_round_grey1);
            }
        }
    }

    /**
     * 请求历史记录
     */
    private void requestLoadHistory() {
        execuTask(new LoadHistoryTask(REQUEST_HISTORY_TASK_ID));
    }

    /**
     * 热门搜索
     */
    private void requestHotSearch() {
        if (isSearchSkillSquare()) {  //技能
            serviceRestUsage.hotSearch(REQUEST_HOT_SEARCH_TASK_ID, 1);
        } else if (isSearchTaskSquare()) {  //任务
            serviceRestUsage.hotSearch(REQUEST_HOT_SEARCH_TASK_ID, 2);
        } else if (isSearchBuilding()) {  //出租出售
            serviceRestUsage.hotSearch(REQUEST_HOT_SEARCH_TASK_ID, 3);
        } else if (isSearchRent()) {   //求租求购
            serviceRestUsage.hotSearch(REQUEST_HOT_SEARCH_TASK_ID, 4);
        } else if (isSearchJobProvide()) {  //求职
            serviceRestUsage.hotSearch(REQUEST_HOT_SEARCH_TASK_ID, 5);
        } else if (isSearchJobApply()) {  //招聘
            serviceRestUsage.hotSearch(REQUEST_HOT_SEARCH_TASK_ID, 6);
        } else if (isSearchTown() || isSearchTownAnno()) { //乡镇 & 公告
            townRestUsage.hotSearch(REQUEST_HOT_SEARCH_TASK_ID);
        } else if (isSearchService()) {    //服务
            serviceRestUsage.hotSearch(REQUEST_HOT_SEARCH_TASK_ID);
        } else if (isSearchSocial()) { //社区
            socialRestUsage.hotSearch(REQUEST_HOT_SEARCH_TASK_ID);
        } else {
            searchRestUsage.hotSearch(REQUEST_HOT_SEARCH_TASK_ID);
        }
    }

    /**
     * 保存历史记录
     */
    private void saveHistroy() {
        String searchTxt = searchEditTxt.getText().toString();
        if (!TextUtils.isEmpty(searchTxt) && !TextUtils.isEmpty(searchTxt.trim())) {
            if (!searchHistoryV4Operation.addIfNoExitsByType(searchTxt, SearchHistoryV4.SearchHistoryType.NEWS_TYPE)) {   //历史记录已经存在
                int count = historyAutoWrapView.getChildCount();
                for (int i = 0; i < count; i++) {
                    View childView = historyAutoWrapView.getChildAt(i);
                    if (childView != null && childView instanceof TextView) {
                        if (childView.getTag() != null && childView.getTag() instanceof SearchHistoryV4) {
                            SearchHistoryV4 searchHistoryV4 = (SearchHistoryV4) childView.getTag();
                            if (searchHistoryV4 != null && searchTxt.equals(searchHistoryV4.getContent())) {
                                historyAutoWrapView.removeView(childView);
                                break;
                            }
                        }
                    }
                }
            }
            SearchHistoryV4 searchHistoryV4 = new SearchHistoryV4(searchTxt);
            historyAutoWrapView.addDefaultTxtView(0, searchHistoryV4, searchHistoryV4.getContent(), getResources().getDimensionPixelSize(R.dimen.sp12), Color.BLACK, R.drawable.bg_round_grey1);

            historyLinLay.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 清空历史记录
     */
    private void delHistory() {
        searchHistoryV4Operation.clearByType(SearchHistoryV4.SearchHistoryType.NEWS_TYPE);
        historyAutoWrapView.removeAllViews();
        historyLinLay.setVisibility(View.GONE);
    }

    /**
     * @param keyWord
     */
    private void search(String keyWord) {
        if (isSearchJobProvide()) {   //招聘
            JobActivity.startActivitySearchResult(this, 1, keyWord);
        } else if (isSearchJobApply()) {  //求职
            JobActivity.startActivitySearchResult(this, 2, keyWord);
        } else if (isSearchBuilding()) { //搜索出租出售
            BuildingActivity.startActivitySearchResult(this, 1, keyWord);
        } else if (isSearchRent()) { //搜索求租求购
            BuildingActivity.startActivitySearchResult(this, 2, keyWord);
        } else if (isSearchSkillSquare()) {  //搜索技能
            SquareActivity.startActivitySearchResult(this, 1, keyWord);
        } else if (isSearchTaskSquare()) { //搜索任务
            SquareActivity.startActivitySearchResult(this, 2, keyWord);
        } else if (isSearchSocial()) {   //搜索圈子
            SocialCircleSearchResultActivity.startActivity(this, keyWord);
        } else if (isSearchTown()) {   //搜索城镇
            GlobalSearchResultActivity.startActivityTown(this, keyWord);
        } else if (isSearchTownAddress()) {   //搜索城镇地址
            TownAddressSearchResultActivity.startActivity(this, keyWord,0);
        }else if (isSearchAreaAddress()){   //搜索新地址
            TownAddressSearchResultActivity.startActivity(this, keyWord,1);
        } else if (isSearchTownAddress()) {   //搜索城镇公告
            GlobalSearchResultActivity.startActivityTown(this, keyWord);
        } else if (isSearchService()) {    //搜索服务
            GoodsSearchActivity.startActivity(this, keyWord);
        } else if (isSearchServiceGoods()) {    //搜索商品
            GoodsSearchV2Activity.startActivity(this, keyWord);
        } else if (isSearchSocial()) { //社区
            SocialCircleSearchResultActivity.startActivity(this, keyWord);
        } else {
            GlobalSearchResultActivity.startActivity(this, keyWord);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delHistoryImgView:    //清空历史记录
                D.show(this, null, "确认删除全部历史记录？", "取消", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            delHistory();
                        }
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.searchBtn:    //搜索
                saveHistroy();
                if (!TextUtils.isEmpty(searchEditTxt.getText().toString())) {
                    search(searchEditTxt.getText().toString());
                }
                break;
        }
    }

}
