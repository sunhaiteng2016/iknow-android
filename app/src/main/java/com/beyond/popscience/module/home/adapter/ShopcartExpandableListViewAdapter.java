package com.beyond.popscience.module.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.pojo.GroupInfoBean;
import com.beyond.popscience.frame.pojo.ProductInfoBean;
import com.beyond.popscience.widget.CircleImageView;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;


public class ShopcartExpandableListViewAdapter extends BaseExpandableListAdapter {
    private List<GroupInfoBean> groups;
    private Map<String, List<ProductInfoBean>> children;
    private Context context;
    //HashMap<Integer, View> groupMap = new HashMap<Integer, View>();
    //HashMap<Integer, View> childrenMap = new HashMap<Integer, View>();
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private List<ProductInfoBean> childs;

    /**
     * 构造函数
     *
     * @param groups   组元素列表
     * @param children 子元素列表
     * @param context
     */
    public ShopcartExpandableListViewAdapter(List<GroupInfoBean> groups, Map<String, List<ProductInfoBean>> children, Context context) {
        super();
        this.groups = groups;
        this.children = children;
        this.context = context;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getStoreId();
        return children.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        childs = children.get(groups.get(groupPosition).getStoreId());
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder gholder;
        if (convertView == null) {
            gholder = new GroupHolder();
            convertView = View.inflate(context, R.layout.item_shopcar, null);
            gholder.cb_check = (CheckBox) convertView.findViewById(R.id.determine_chekbox);
            gholder.ic_store_img = (CircleImageView) convertView.findViewById(R.id.ic_store_img);
            gholder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_source_name);
            gholder.ll_determine_chekbox = (LinearLayout) convertView.findViewById(R.id.ll_determine_chekbox);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupHolder) convertView.getTag();
        }
        //购物车商品列表标记的goods_type 0为普通商品 1为土特产 2旅游产品 3乐点商城商品
        final GroupInfoBean group = (GroupInfoBean) getGroup(groupPosition);
        if (group != null) {

            Glide.with(context).load(group.getLogo()).into(gholder.ic_store_img);
            if (!TextUtils.isEmpty(group.getName())) {
                gholder.tv_group_name.setText(group.getName());
            } else {
                gholder.tv_group_name.setText("天下乐商城");
            }
            gholder.cb_check.setChecked(group.isChoosed());

            //进入店铺
            gholder.tv_group_name.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkInterface.instore(group.getStoreId(), group.getName());
                }
            });

            gholder.ll_determine_chekbox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean nowBeanChecked = group.isChoosed();
                    group.setChoosed(!nowBeanChecked);
                    checkInterface.checkGroup(groupPosition, !nowBeanChecked);// 暴露组选接口
                }
            });
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final ChildHolder cholder;
        if (convertView == null) {
            cholder = new ChildHolder();
            convertView = View.inflate(context, R.layout.item_item_shopcar, null);
            cholder.check_box = (CheckBox) convertView.findViewById(R.id.check_box);
            cholder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            cholder.tv_product_desc = (TextView) convertView.findViewById(R.id.item_sku);
            cholder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            cholder.iv_increase = (ImageView) convertView.findViewById(R.id.tv_add);
            cholder.iv_decrease = (ImageView) convertView.findViewById(R.id.tv_reduce);
            cholder.tv_count = (TextView) convertView.findViewById(R.id.tv_num);
            cholder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            cholder.ll_check_box = (LinearLayout) convertView.findViewById(R.id.ll_check_box);

            convertView.setTag(cholder);
        } else {
            cholder = (ChildHolder) convertView.getTag();
        }

        final ProductInfoBean product = (ProductInfoBean) getChild(groupPosition, childPosition);


        //购物车商品列表标记的goods_type 0为普通商品 1为土特产 2旅游产品 3乐点商城商品
        if (product != null) {
            if (product.getType_status().equals("3")) {
                Glide.with(context).load(product.getLogo_pic()).into(cholder.item_img);
                cholder.item_title.setText(product.getGoods_name());
                cholder.tv_product_desc.setVisibility(TextUtils.isEmpty(product.getSku_name())?View.INVISIBLE:View.VISIBLE);
                cholder.tv_product_desc.setText(product.getSku_name());
                cholder.tv_price.setText("乐点" + product.getPrice() + "");
                cholder.tv_count.setText(product.getNum() + "");
                cholder.check_box.setChecked(product.isChecked());
            } else {
                Glide.with(context).load(product.getLogo_pic()).into(cholder.item_img);
                cholder.item_title.setText(product.getGoods_name());
                cholder.tv_product_desc.setVisibility(TextUtils.isEmpty(product.getSku_name())?View.INVISIBLE:View.VISIBLE);
                cholder.tv_product_desc.setText(product.getSku_name());
                cholder.tv_price.setText("¥" + product.getPrice() + "");
                cholder.tv_count.setText(product.getNum() + "");
                cholder.check_box.setChecked(product.isChecked());
            }


            cholder.check_box.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean nowBeanChecked = product.isChecked();
                    product.setChecked(!nowBeanChecked);
                    cholder.check_box.setChecked(!nowBeanChecked);
                    checkInterface.checkChild(groupPosition, childPosition, !nowBeanChecked);// 暴露子选接口
                }
            });


            cholder.iv_increase.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, cholder.tv_count, cholder.check_box.isChecked());// 暴露增加接口
                }
            });

            cholder.iv_decrease.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, cholder.tv_count, cholder.check_box.isChecked());// 暴露删减接口
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 组元素绑定器
     */
    private class GroupHolder {
        CheckBox cb_check;
        TextView tv_group_name;
        CircleImageView ic_store_img;
        LinearLayout ll_determine_chekbox;
    }

    /**
     * 子元素绑定器
     */
    private class ChildHolder {
        CheckBox check_box;
        TextView item_title;
        TextView tv_product_desc;
        TextView tv_price;
        ImageView iv_increase;
        ImageView iv_decrease;
        TextView tv_count;
        ImageView item_img;
        LinearLayout ll_check_box;
    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);

        void instore(String storeid, String storeName);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
    }

}
