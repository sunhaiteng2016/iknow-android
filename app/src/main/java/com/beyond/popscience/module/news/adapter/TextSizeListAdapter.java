package com.beyond.popscience.module.news.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.beyond.library.util.DensityUtil;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.util.SPUtils;

/**
 * Created by linjinfa on 2017/6/25.
 * email 331710168@qq.com
 */
public class TextSizeListAdapter extends CustomBaseAdapter<TextSizeListAdapter.TextSizeInfo> {

    /**
     *
     */
    private int selectedTxtSize;

    public TextSizeListAdapter(Activity context) {
        super(context);
        init();
    }

    public TextSizeListAdapter(Fragment fragment) {
        super(fragment);
        init();
    }

    /**
     *
     * @return
     */
    public int selectedTxtSize(){
        return selectedTxtSize;
    }

    /**
     *
     * @param pos
     */
    public void setTxtSizeByPos(int pos){
        TextSizeInfo textSizeInfo = getItem(pos);
        if(textSizeInfo!=null){
            selectedTxtSize = textSizeInfo.textSize;
            notifyDataSetChanged();
        }
    }

    /**
     *
     */
    private void init(){
        selectedTxtSize = (int) SPUtils.get(context, "txtSize", DensityUtil.sp2px(context, 17));
        dataList.add(new TextSizeInfo("特大号字", DensityUtil.sp2px(context, 30)));
        dataList.add(new TextSizeInfo("大号字", DensityUtil.sp2px(context, 25)));
        dataList.add(new TextSizeInfo("中号字", DensityUtil.sp2px(context, 17)));
        dataList.add(new TextSizeInfo("小号字", DensityUtil.sp2px(context, 14)));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.dialog_select_text_size, parent, false);

            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TextSizeInfo textSizeInfo = dataList.get(position);
        viewHolder.checkbox.setText(textSizeInfo.name);

        if(selectedTxtSize == textSizeInfo.textSize){
            viewHolder.checkbox.setChecked(true);
        }else{
            viewHolder.checkbox.setChecked(false);
        }

        return convertView;
    }

    class ViewHolder{
        CheckBox checkbox;
    }

    class TextSizeInfo{
        String name;
        int textSize;

        public TextSizeInfo(String name, int textSize) {
            this.name = name;
            this.textSize = textSize;
        }
    }

}
