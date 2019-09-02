package com.beyond.popscience.locationgoods;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyond.library.network.Request;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.BaseActivity;
import com.beyond.popscience.locationgoods.http.AddressRestUsage;
import com.beyond.popscience.utils.CreatLayoutUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品分类
 */
public class GoodClassfyActivity extends BaseActivity {

    @BindView(R.id.go_back)
    RelativeLayout goBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.left_listView)
    RecyclerView leftListView;
    @BindView(R.id.right_listView)
    RecyclerView rightListView;
    private List<Mybean> leftList = new ArrayList<>();
    private List<String> rightList = new ArrayList<>();
    private CommonAdapter<Mybean> leftAdapter;
    private CommonAdapter<String> rightAdapter;
    //数据源
    private String[] fruit = new String[]{"杨梅", "柑橘橙柚", "葡萄/提子", "草莓/蓝莓", "梨/桃", "猕猴桃", "瓜类",
            "桑果", "樱桃/车厘子", "柿/杏/李/枣", "山楂", "石榴", "甘蔗", "榴莲",
            "苹果", "香蕉", "芒果", "龙眼", "荔枝", "火龙果", "山竹", "木瓜", "百香果", "椰子", "牛油果", "水果干货", "其它果品"};
    private String[] vegetables = new String[]{"叶菜", "根茎类", "豆类", "鲜菌菇", "瓜/果/茄/花", "葱姜蒜椒香", "豆制品", "南北干货", "干制蔬菜", "其它蔬菜"};
    private String[] aquaticproduct = new String[]{"蟹类", "鱼类", "虾类", "水产干货", "贝类", "藻类",  "其它"};
    private String[] meatandpoultryeggs = new String[]{"猪肉", "牛肉", "羊肉", "活鸡", "活鸭", "活鹅", "活猪", "活牛", "活羊", "蛋品", "腌腊类", "其它"};
    private String[] grainandoil = new String[]{"大米", "小麦", "小米", "高粱", "玉米", "黄豆", "红豆", "面", "其他杂粮", "山茶油", "菜籽油", "花生油", "大豆油", "其他食用油", "其它"};
    private String[] Game = new String[]{"野味", "其它"};


    private int parentPosition;

    @Request
    AddressRestUsage addressRestUsage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_good_classfy;
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

    @Override
    public void initUI() {
        super.initUI();
        title.setText("商品分类");
        initListView();
        getFl();
    }

    private void getFl() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("area", "椒江区");
        addressRestUsage.getFl(1088611, map);
    }

    private void initListView() {
        CreatLayoutUtils.creatLinearLayout(this, leftListView);
        leftList.add(new Mybean("1", "果品"));
        leftList.add(new Mybean("1", "蔬菜"));
        leftList.add(new Mybean("1", "水产"));
        leftList.add(new Mybean("1", "肉禽蛋"));
        leftList.add(new Mybean("1", "粮油"));
        leftList.add(new Mybean("1", "其它"));
        leftAdapter = new CommonAdapter<Mybean>(this, R.layout.item_left_list, leftList) {

            @Override
            protected void convert(ViewHolder holder, Mybean s, int position) {
                holder.setText(R.id.textView, s.content);
                if (s.flag.equals("1")) {
                    //未选择
                    holder.setBackgroundColor(R.id.ll_item, getResources().getColor(R.color.black_f3));
                } else {
                    holder.setBackgroundColor(R.id.ll_item, getResources().getColor(R.color.white));
                }
            }
        };
        leftListView.setAdapter(leftAdapter);
        leftAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                rightList.clear();
                //改变选择状态
                if (leftList.get(position).flag.equals("1")) {
                    leftList.get(position).setFlag("2");
                } else {
                    leftList.get(position).setFlag("1");
                }
                //其余的状态 全部改成未选择

                for (int i=0;i<leftList.size();i++){
                    if (i!=position){
                        leftList.get(i).setFlag("1");
                    }
                }
                leftAdapter.notifyDataSetChanged();
                //切换数据源
                if (position == 0) {
                    for (String bean : fruit) {
                        rightList.add(bean);
                    }
                }
                if (position == 1) {
                    for (String bean : vegetables) {
                        rightList.add(bean);
                    }
                }
                if (position == 2) {
                    for (String bean : aquaticproduct) {
                        rightList.add(bean);
                    }
                }
                if (position == 3) {
                    for (String bean : meatandpoultryeggs) {
                        rightList.add(bean);
                    }
                }
                if (position == 4) {
                    for (String bean : grainandoil) {
                        rightList.add(bean);
                    }
                }
                if (position == 5) {
                    for (String bean : Game) {
                        rightList.add(bean);
                    }
                }
                rightAdapter.notifyDataSetChanged();
                //记录当前父标签的位置
                parentPosition = position;
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        CreatLayoutUtils.cretGridViewLayout(this, rightListView, 3);
        //右边的列表
        for (String bean : fruit) {
            rightList.add(bean);
        }
        rightAdapter = new CommonAdapter<String>(this, R.layout.item_right_list_two, rightList) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.textView, s);
            }
        };

        rightListView.setAdapter(rightAdapter);
        rightAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                //回传数据
                Intent intent = new Intent();
                if (parentPosition == 0) {
                    intent.putExtra("classfy", "果品/" + rightList.get(position));
                }
                if (parentPosition == 1) {
                    intent.putExtra("classfy", "蔬菜/" + rightList.get(position));
                }
                if (parentPosition == 2) {
                    intent.putExtra("classfy", "水产/" + rightList.get(position));
                }
                if (parentPosition == 3) {
                    intent.putExtra("classfy", "肉禽蛋/" + rightList.get(position));
                }
                if (parentPosition == 4) {
                    intent.putExtra("classfy", "粮油/" + rightList.get(position));
                }
                if (parentPosition == 5) {
                    intent.putExtra("classfy", "其它/" + rightList.get(position));
                }
                setResult(10086, intent);
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public class Mybean {
        public String flag;
        public String content;

        public Mybean(String flag, String content) {
            this.flag = flag;
            this.content = content;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
