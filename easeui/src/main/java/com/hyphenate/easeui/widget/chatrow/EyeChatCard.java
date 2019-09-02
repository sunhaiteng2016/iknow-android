package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.exceptions.HyphenateException;

/**
 * 自定义消息  卡片
 */

public class EyeChatCard extends EaseChatRow {
    ImageView img, iv_userhead;//图片
    TextView title;//title
    private TextView text, tv_content;
    private TextView tv_type;

    public EyeChatCard(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.eye_row_received_cards : R.layout.eye_row_sent_cards, this);
    }

    /**
     * 实例化控件
     */
    @Override
    protected void onFindViewById() {
        title = (TextView) findViewById(R.id.tv_title_name);
        img = (ImageView) findViewById(R.id.ease_chat_item_share_img);
        iv_userhead = (ImageView) findViewById(R.id.iv_userhead);
        text = (TextView) findViewById(R.id.timestamp);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_type = (TextView) findViewById(R.id.tv_type);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置内容
     */
    @Override
    protected void onSetUpView() {
        try {

            String userHead = message.getStringAttribute("userHead");
            String userName = message.getStringAttribute("userName");
            String titleString = message.getStringAttribute("title");
            String image = message.getStringAttribute("image");
            String content = message.getStringAttribute("content");
            String type = message.getStringAttribute("type");
            title.setText(titleString);
            Glide.with(context).load(image).into(img);
            Glide.with(context).load(userHead).into(iv_userhead);
            tv_content.setText(content);
            if (type.equals("NewsShare")){
                tv_type.setText("文章分享");
            }
            if (type.equals("Share")){
                tv_type.setText("说说分享");
            }
            //消息状态变化
            handleTextMessage();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }


    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } else {
            if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
