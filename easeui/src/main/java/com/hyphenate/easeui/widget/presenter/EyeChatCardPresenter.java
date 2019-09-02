package com.hyphenate.easeui.widget.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EyeChatCard;
import com.hyphenate.exceptions.HyphenateException;

public class EyeChatCardPresenter  extends EaseChatRowPresenter {


    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new EyeChatCard(cxt, message, position, adapter);
    }

    @Override
    protected void handleReceiveMessage(EMMessage message) {
        if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBubbleClick(EMMessage message) {
        super.onBubbleClick(message);

        try {
            String type = message.getStringAttribute("type");
            if (type.equals("NewsShare")){
                //跳转到网页
                Intent intent = new Intent();
                intent.setAction("com.test.six");
                intent.addCategory("com.test.six.category");
                intent.putExtra("url",message.getStringAttribute("newsUrl"));
                intent.putExtra("title",message.getStringAttribute("title"));
                getContext().startActivity(intent);
            }
            if (type.equals("Share")){
                Intent intent = new Intent();
                intent.setAction("com.test.second");
                intent.addCategory("com.test.second.category");
                intent.putExtra("articleInfo",message.getStringAttribute("articleId"));
                getContext().startActivity(intent);
            }

        } catch (HyphenateException e) {
            e.printStackTrace();
        }

    }

}
