package com.beyond.popscience.frame.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择分类
 * Created by linjinfa on 2017/6/12.
 * email 331710168@qq.com
 */
public class SelectDialog {

    private Dialog dialog;
    private SelectAdapter selectAdapter;
    private OnSelectClickListener onSelectClickListener;

    public SelectDialog(Context context) {
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context){
        dialog = new Dialog(context, R.style.DialogPushUpInAnimStyle);
        dialog.setContentView(R.layout.dialog_select);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = DensityUtil.getScreenWidth(context);
        window.setAttributes(layoutParams);

        View bgView = window.findViewById(R.id.bgView);

        final ListView listView = (ListView) window.findViewById(R.id.listView);
        selectAdapter = new SelectAdapter(context);
        listView.setAdapter(selectAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - listView.getHeaderViewsCount();
                selectAdapter.setSelectedPos(position);
                selectAdapter.notifyDataSetChanged();
            }
        });

        final TextView okTxtView = (TextView) window.findViewById(R.id.okTxtView);

        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSelectClickListener!=null){
                    onSelectClickListener.onOk(getSelectedMenuInfo());
                }
                dismiss();
            }
        });
    }

    /**
     * 添加menu
     * @param menuInfoList
     */
    public void addAllMenu(List<MenuInfo> menuInfoList){
        if(menuInfoList!=null && selectAdapter!=null){
            selectAdapter.getDataList().addAll(menuInfoList);
            selectAdapter.notifyDataSetChanged();
        }
    }

    /**
     *
     */
    public void show(){
        dialog.show();
    }

    /**
     *
     */
    public void dismiss(){
        dialog.dismiss();
    }

    /**
     * 获取被选中的 menuInfo
     * @return
     */
    public MenuInfo getSelectedMenuInfo(){
        if(selectAdapter!=null){
            return selectAdapter.getSelectedMenuInfo();
        }
        return null;
    }

    /**
     * 设置选中的 menuName
     * @param menuName
     */
    public void setSelectMenuName(String menuName){
        if(!TextUtils.isEmpty(menuName) && selectAdapter!=null){
            int count = selectAdapter.getCount();
            for(int i=0;i<count;i++){
                MenuInfo menuInfo = selectAdapter.getItem(i);
                if(menuInfo!=null && menuName.equals(menuInfo.getMenuName())){
                    selectAdapter.setSelectedPos(i);
                    selectAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    /**
     * 设置选中的 menuName
     */
    public void setSelectMenuCode(String menuCode){
        if(!TextUtils.isEmpty(menuCode) && selectAdapter!=null){
            int count = selectAdapter.getCount();
            for(int i=0;i<count;i++){
                MenuInfo menuInfo = selectAdapter.getItem(i);
                if(menuInfo!=null && menuCode.equals(menuInfo.getMenuCode())){
                    selectAdapter.setSelectedPos(i);
                    selectAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public OnSelectClickListener getOnSelectClickListener() {
        return onSelectClickListener;
    }

    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
        this.onSelectClickListener = onSelectClickListener;
    }

    public interface OnSelectClickListener{
        void onOk(MenuInfo menuInfo);
    }

    /**
     *
     */
    class SelectAdapter extends BaseAdapter{

        private List<MenuInfo> dataList = new ArrayList<>();
        private LayoutInflater inflater;
        private int selectedPos = -1;

        public SelectAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        /**
         * 设置被选中的
         * @param position
         */
        public void setSelectedPos(int position){
            this.selectedPos = position;
        }

        /**
         *
         * @return
         */
        public MenuInfo getSelectedMenuInfo(){
            return getItem(selectedPos);
        }

        public List<MenuInfo> getDataList() {
            return dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public MenuInfo getItem(int position) {
            if(position>=0 && position<dataList.size()){
                return dataList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.dialog_select_item, parent, false);

                viewHolder.menuNameTxtView = (TextView) convertView.findViewById(R.id.menuNameTxtView);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            MenuInfo menuInfo = dataList.get(position);

            viewHolder.menuNameTxtView.setText(menuInfo.getMenuName());

            if(position == selectedPos){
                viewHolder.checkBox.setChecked(true);
            }else{
                viewHolder.checkBox.setChecked(false);
            }

            return convertView;
        }

        class ViewHolder{
            TextView menuNameTxtView;
            CheckBox checkBox;
        }

    }

    public static class MenuInfo{
        String menuCode;
        String menuName;
        Object targetObj;

        public MenuInfo(String menuCode, String menuName) {
            this.menuCode = menuCode;
            this.menuName = menuName;
        }

        public MenuInfo(String menuCode, String menuName, Object targetObj) {
            this.menuCode = menuCode;
            this.menuName = menuName;
            this.targetObj = targetObj;
        }

        public String getMenuCode() {
            return menuCode;
        }

        public String getMenuName() {
            return menuName;
        }

        public Object getTargetObj() {
            return targetObj;
        }
    }

}
