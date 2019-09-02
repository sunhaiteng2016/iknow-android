package com.beyond.popscience.module.home.shopcart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.Request;
import com.beyond.library.network.enity.MSG;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.frame.net.NewsRestUsage;
import com.beyond.popscience.frame.pojo.NavObj;
import com.beyond.popscience.frame.pojo.ServiceCategory;
import com.beyond.popscience.frame.util.SPUtils;
import com.beyond.popscience.locationgoods.ZczxActivity;
import com.beyond.popscience.module.building.BuildingActivity;
import com.beyond.popscience.module.home.adapter.MoreMenusAdapter;
import com.beyond.popscience.module.home.adapter.MyElvAdapter;
import com.beyond.popscience.module.home.entity.HomeMessage;
import com.beyond.popscience.module.home.entity.Menus;
import com.beyond.popscience.module.job.JobActivity;
import com.beyond.popscience.module.mservice.GoodsListActivity;
import com.beyond.popscience.module.mservice.WebViewActivity;
import com.beyond.popscience.module.point.PonitShopActivity;
import com.beyond.popscience.module.square.SquareActivity;
import com.beyond.popscience.view.MyGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreMenuActivity extends BaseActivity {


    @BindView(R.id.my_menu_lv)
    MyGridView myMenuLv;
    @BindView(R.id.wntj_lv)
    MyGridView wntjLv;
    @BindView(R.id.lpln_lv)
    MyGridView lplnLv;
    @BindView(R.id.dsf_lv)
    MyGridView dsfLv;
    @BindView(R.id.black)
    ImageView black;
    @BindView(R.id.iv_edit)
    TextView ivEdit;
    @BindView(R.id.elv)
    ExpandableListView elv;
    @Request
    private NewsRestUsage mRestUsage;
    private List<Menus.UserAllTabBean> myTab;
    private List<Menus.ResultListBean> resultlsit = new ArrayList<>();
    private MoreMenusAdapter adapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_more_menu;
    }

    @Override
    public void initUI() {
        super.initUI();
        mRestUsage.getMenu(1008611,SPUtils.get(MoreMenuActivity.this, "detailedArea", "").toString().split("-")[2]);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(Menus.UserAllTabBean bean) {
        myTab.add(bean);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void refreshUI(int taskId, MSG msg) {
        super.refreshUI(taskId, msg);
        switch (taskId) {
            case 1008612:
                EventBus.getDefault().post(new HomeMessage.menuRefresh());
                finish();
                break;
            case 1008611:
                if (msg.getIsSuccess()) {
                    Menus menus = (Menus) msg.getObj();
                    //这个是我定制的菜单
                    myTab = menus.getUserAllTab();
                    adapter = new MoreMenusAdapter(myTab, MoreMenuActivity.this);
                    myMenuLv.setAdapter(adapter);
                    final List<Menus.ResultListBean> resultList = menus.getResultList();
                    resultlsit.clear();
                    resultlsit.addAll(resultList);
                    final MyElvAdapter elvAdapter = new MyElvAdapter(MoreMenuActivity.this, resultlsit);
                    elv.setAdapter(elvAdapter);
                    int intgroupCount = elv.getCount();
                    for (int i = 0; i < intgroupCount; i++) {
                        elv.expandGroup(i);
                    }
                    elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                            return true;
                        }
                    });
                    //我的栏目
                    adapter.setOnAddClick(new MoreMenusAdapter.OnAddClickLinenter() {
                        @Override
                        public void onClicks(View view, int position) {
                            int category = myTab.get(position).getCategory();
                            Menus.ResultListBean.TabListBean tabListBean = new Menus.ResultListBean.TabListBean();
                            tabListBean.setTabname(myTab.get(position).getTabname());
                            tabListBean.setTabpic(myTab.get(position).getTabpic());
                            tabListBean.setCategory(category);
                            tabListBean.setId(myTab.get(position).getId());
                            tabListBean.setType(myTab.get(position).getType());
                            for (int i = 0; i < resultList.size(); i++) {
                                if (resultList.get(i).getCateId() == category) {
                                    resultlsit.get(i).getTabList().add(tabListBean);
                                }
                            }
                            elvAdapter.notifyDataSetChanged();

                            myTab.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    myMenuLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            onClickss(myTab, i);
                        }
                    });
                }
                break;
        }
    }


    /**
     * 所有菜单的点击事件
     */
    private void onClickss(List<Menus.UserAllTabBean> myTab, int position) {
        final int tabId = myTab.get(position).getId();
        final String tabName = myTab.get(position).getTabname();
        //外链
        if (myTab.get(position).getType() == 1) {
            WebViewActivity.startActivity(MoreMenuActivity.this, myTab.get(position).getTaburl(), myTab.get(position).getTabname());
        }
        //文章菜单
        if (myTab.get(position).getType() == 2) {
            int classId = myTab.get(position).getClassify();
            NavObj obj = new NavObj();
            obj.setClassId(classId + "");
            obj.setClassName(tabName);
            Intent intent = new Intent(MoreMenuActivity.this, NewFragmentActivity.class);
            intent.putExtra("name", "文章");
            intent.putExtra("nav", obj);
            MoreMenuActivity.this.startActivity(intent);
        }
        //主菜单
        if (myTab.get(position).getType() == 3) {
            if (tabId == 0) {
                //自产自销
                Intent intent = new Intent(MoreMenuActivity.this, ZczxActivity.class);
                startActivity(intent);
            }
            if (3 == tabId) {
                Intent intent = new Intent(MoreMenuActivity.this, NewFragmentActivity.class);
                intent.putExtra("name", "乡镇");
                MoreMenuActivity.this.startActivity(intent);
            }
            if (2 == tabId) {
                Intent intent = new Intent(MoreMenuActivity.this, NewFragmentActivity.class);
                intent.putExtra("name", "社团");
                MoreMenuActivity.this.startActivity(intent);
            }
            if (1 == tabId) {
                ServiceCategory category = new ServiceCategory();
                category.setTabId("5");
                category.setTabType("2");
                category.setTabName("商品买卖");
                category.setTabPic("'http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/7d1c6754e74941fa8d3e45208e8032c1.jpg");
                GoodsListActivity.startActivity(MoreMenuActivity.this, category);
            }
            if (6 == tabId) {
                SquareActivity.startActivity(MoreMenuActivity.this);
            }
            if (7 == tabId) {
                Intent intent = new Intent(MoreMenuActivity.this, PonitShopActivity.class);
                intent.putExtra("score", SPUtils.get(MoreMenuActivity.this, "score", "") + "");
                startActivity(intent);
            }
            if (5 == tabId) {
                JobActivity.startActivity(MoreMenuActivity.this);
            }
            if (4 == tabId) {
                BuildingActivity.startActivity(MoreMenuActivity.this);
            }
            if (22 == tabId) {
                Intent intent = new Intent(MoreMenuActivity.this, NewFragmentActivity.class);
                NavObj obj = new NavObj();
                obj.setClassId("1");
                obj.setClassName(tabName);
                intent.putExtra("name", "科普");
                intent.putExtra("nav", obj);
                MoreMenuActivity.this.startActivity(intent);
            }
            if (24 == tabId) {
                Intent intent = new Intent(MoreMenuActivity.this, NewFragmentActivity.class);
                intent.putExtra("name", "服务");
                MoreMenuActivity.this.startActivity(intent);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.black)
    public void onViewClicked() {
        //上传本地菜单
        List<Integer> upList = new ArrayList<>();
        for (Menus.UserAllTabBean bean : myTab) {
            upList.add(bean.getId());
        }
        Map<String, String> map = new HashMap();
        map.put("tabList", JSON.toJSONString(upList));
        mRestUsage.getCustomMenu(1008612, map);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            List<Integer> upList = new ArrayList<>();
            for (Menus.UserAllTabBean bean : myTab) {
                upList.add(bean.getId());
            }
            Map<String, String> map = new HashMap();
            map.put("tabList", JSON.toJSONString(upList));
            mRestUsage.getCustomMenu(1008612, map);
        }
        return true;
    }


}
