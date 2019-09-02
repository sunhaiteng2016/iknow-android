package com.beyond.popscience.module.news.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beyond.popscience.R;
import com.beyond.popscience.frame.base.CustomBaseAdapter;
import com.beyond.popscience.frame.pojo.NewsDetailQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by linjinfa on 2017/10/7.
 * email 331710168@qq.com
 */
public class NewsQuestionListAdapter extends CustomBaseAdapter<NewsDetailQuestion> {

    /**
     *
     */
    private HashMap<NewsDetailQuestion, List<String>> selectedNewsDetailQuestionMap = new HashMap();

    public NewsQuestionListAdapter(Activity context) {
        super(context);
    }

    /**
     *
     * @return
     */
    public HashMap<NewsDetailQuestion, List<String>> getSelectedNewsDetailQuestionMap() {
        return selectedNewsDetailQuestionMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_news_question_list_item, parent, false);

            viewHolder.questionTitleTxtView = (TextView) convertView.findViewById(R.id.questionTitleTxtView);
            viewHolder.optionAFrameLay = (FrameLayout) convertView.findViewById(R.id.optionAFrameLay);
            viewHolder.optionBFrameLay = (FrameLayout) convertView.findViewById(R.id.optionBFrameLay);
            viewHolder.optionCFrameLay = (FrameLayout) convertView.findViewById(R.id.optionCFrameLay);
            viewHolder.optionDFrameLay = (FrameLayout) convertView.findViewById(R.id.optionDFrameLay);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewsDetailQuestion newsDetailQuestion = dataList.get(position);

        viewHolder.questionTitleTxtView.setText(newsDetailQuestion.getTitle()+"（"+("1".equals(newsDetailQuestion.getType()) ? "单选" : "多选")+"）");

        switchOptionView(newsDetailQuestion, "A", newsDetailQuestion.getOptionA(), viewHolder.optionAFrameLay);
        switchOptionView(newsDetailQuestion, "B", newsDetailQuestion.getOptionB(), viewHolder.optionBFrameLay);
        switchOptionView(newsDetailQuestion, "C", newsDetailQuestion.getOptionC(), viewHolder.optionCFrameLay);
        switchOptionView(newsDetailQuestion, "D", newsDetailQuestion.getOptionD(), viewHolder.optionDFrameLay);

        return convertView;
    }

    /**
     * 切换选项
     */
    private void switchOptionView(NewsDetailQuestion newsDetailQuestion, String optionKey, String optionContent, FrameLayout optionFrameLay){
        if(TextUtils.isEmpty(optionContent)){
            optionFrameLay.setVisibility(View.GONE);
        }else{
            optionFrameLay.setVisibility(View.VISIBLE);

            TextView choiceContentTxtView = (TextView) optionFrameLay.findViewById(R.id.choiceContentTxtView);
            TextView choiceFlagView = (TextView) optionFrameLay.findViewById(R.id.choiceFlagView);
            choiceFlagView.setText(optionKey);
            choiceContentTxtView.setText(optionContent);

            List<String> selectedKeyList = selectedNewsDetailQuestionMap.get(newsDetailQuestion);
            if(selectedKeyList!=null && selectedKeyList.size()!=0){
                if(selectedKeyList.contains(optionKey)){    //被选中
                    choiceFlagView.setTextColor(Color.parseColor("#3598da"));
                    choiceFlagView.setBackgroundResource(R.drawable.bg_hollow_circle_blue);
                }else{
                    choiceFlagView.setTextColor(Color.BLACK);
                    choiceFlagView.setBackgroundResource(R.drawable.bg_hollow_circle_black);
                }
            }else{
                choiceFlagView.setTextColor(Color.BLACK);
                choiceFlagView.setBackgroundResource(R.drawable.bg_hollow_circle_black);
            }
        }

        optionFrameLay.setTag(newsDetailQuestion);
        optionFrameLay.setTag(R.mipmap.ic_launcher, optionKey);
        optionFrameLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String optionKey = (String) v.getTag(R.mipmap.ic_launcher);
                NewsDetailQuestion newsDetailQuestion = (NewsDetailQuestion) v.getTag();
                List<String> selectedKeyList = selectedNewsDetailQuestionMap.get(newsDetailQuestion);
                if(selectedKeyList == null){
                    selectedKeyList = new ArrayList<String>();
                    selectedNewsDetailQuestionMap.put(newsDetailQuestion, selectedKeyList);
                }

                if("1".equals(newsDetailQuestion.getType())){   //单选
                    selectedKeyList.clear();
                }

                if(selectedKeyList.contains(optionKey)){
                    selectedKeyList.remove(optionKey);
                }else{
                    selectedKeyList.add(optionKey);
                }
                notifyDataSetChanged();
            }
        });
    }

    class ViewHolder{
        TextView questionTitleTxtView;
        FrameLayout optionAFrameLay;
        FrameLayout optionBFrameLay;
        FrameLayout optionCFrameLay;
        FrameLayout optionDFrameLay;
    }

}
