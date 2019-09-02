package com.beyond.popscience.module.home.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.locationgoods.shopcar.util.ToastUtil;
import com.beyond.popscience.module.home.ChatGroup;
import com.beyond.popscience.module.home.ContextMenuActivity;
import com.beyond.popscience.module.home.GroupManagertActivity;
import com.beyond.popscience.module.home.ShareMessageTxlActivity;
import com.beyond.popscience.module.home.ShareTxLActivity;
import com.beyond.popscience.module.home.ShareTxLTwoActivity;
import com.beyond.popscience.module.home.entity.UserDate;
import com.beyond.popscience.module.home.fragment.view.QQTipItem;
import com.beyond.popscience.module.home.fragment.view.QQTipView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseDingMessageHelper;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseDingMsgSendActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;

public class MyChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {


    private UserDate.DataBean mdata;
    private String remarkName;
    private String userName;

    @Override
    protected void setUpView() {
        super.setUpView();
        if (null != getArguments()) {
            if (null != getArguments().getString("name")) {
                titleBar.setTitle(getArguments().getString("name"));
            }
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleBar.setLayoutParams(layoutParams);
        titleBar.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                    emptyHistory();
                } else {
                    //跳转到群组管理
                    Intent intent = new Intent(getActivity(), GroupManagertActivity.class);
                    intent.putExtra("group_id", toChatUsername);
                    startActivity(intent);
                }
            }
        });
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            getUserDates();
        }
        if (!EventBus.getDefault().isRegistered(true)) {
            EventBus.getDefault().register(this);
        }
        messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {
            @Override
            public boolean onBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public boolean onResendClick(EMMessage message) {
                return false;
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                contextMenuMessage = message;

                int[] location = new int[2];
                messageList.getListView().getLocationOnScreen(location);
                float OldListY = (float) location[1];
                float OldListX = (float) location[0];

               /* new QQTipView.Builder(getActivity(), messageList.getListView(), (int) OldListX + messageList.getListView().getWidth() / 2, (int) OldListY)
                        .addItem(new QQTipItem("复制"))
                        .addItem(new QQTipItem("删除"))
                        .addItem(new QQTipItem("转发"))
                        .setOnItemClickListener(new QQTipView.OnItemClickListener() {
                            @Override
                            public void onItemClick(String str, final int position) {
                                if (position == 0) {



                                } else if (position == 1) {

                                } else if (position == 2) {
                                } else {
                                }
                            }
                            @Override
                            public void dismiss() {

                            }
                        })
                        .create();*/
                startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message", message)
                                .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM),
                        14);

            }

            @Override
            public void onUserAvatarClick(String username) {

            }

            @Override
            public void onUserAvatarLongClick(String username) {

            }

            @Override
            public void onMessageInProgress(EMMessage message) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 14) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    // To delete the ding-type message native stored acked users.
                    EaseDingMessageHelper.get().delete(contextMenuMessage);
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
                    try {
                        boolean cusMess = contextMenuMessage.getBooleanAttribute("associationCards");
                        if (cusMess) {
                            String type = contextMenuMessage.getStringAttribute("type");
                            if (type.equals("NewsShare")){
                                Intent intent = new Intent(getActivity(), ShareTxLTwoActivity.class);
                                intent.putExtra("titles", contextMenuMessage.getStringAttribute("title"));
                                intent.putExtra("link", contextMenuMessage.getStringAttribute("newsUrl"));
                                intent.putExtra("pics", contextMenuMessage.getStringAttribute("image"));
                                startActivity(intent);
                                getActivity().finish();
                            }else{
                                Intent intent = new Intent(getActivity(), ShareTxLActivity.class);
                                intent.putExtra("articleid", contextMenuMessage.getStringAttribute("articleId"));
                                intent.putExtra("acatar", contextMenuMessage.getStringAttribute("image"));
                                intent.putExtra("content", contextMenuMessage.getStringAttribute("content"));
                                startActivity(intent);
                                getActivity().finish();
                            }

                        } else {
                            Intent intent = new Intent(getActivity(), ShareMessageTxlActivity.class);
                            intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
                            intent.putExtra("userId", contextMenuMessage.getUserName());
                            startActivity(intent);
                            getActivity().finish();
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        Intent intent = new Intent(getActivity(), ShareMessageTxlActivity.class);
                        intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
                        intent.putExtra("userId", contextMenuMessage.getUserName());
                        startActivity(intent);
                        getActivity().finish();
                    }
                    break;
                case ContextMenuActivity.RESULT_CODE_RECALL://recall
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMMessage msgNotification = EMMessage.createTxtSendMessage(" ", contextMenuMessage.getTo());
                                EMTextMessageBody txtBody = new EMTextMessageBody("您撤回了一条消息");
                                msgNotification.addBody(txtBody);
                                msgNotification.setMsgTime(contextMenuMessage.getMsgTime());
                                msgNotification.setLocalTime(contextMenuMessage.getMsgTime());
                                msgNotification.setAttribute(Constant.MESSAGE_TYPE_RECALL, true);
                                msgNotification.setStatus(EMMessage.Status.SUCCESS);
                                EMClient.getInstance().chatManager().recallMessage(contextMenuMessage);
                                EMClient.getInstance().chatManager().saveMessage(msgNotification);
                                messageList.refresh();
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();

                    // Delete group-ack data according to this message.
                    EaseDingMessageHelper.get().delete(contextMenuMessage);
                    break;

                default:
                    break;
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(ChatGroup chatGroup) {
        if (chatGroup.getName().equals("")) {

        } else {
            titleBar.setTitle(chatGroup.getName());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(true)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserDates() {
        //获取用户资料
        HashMap map = new HashMap();
        AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
        appBaseRestUsageV1.get(BeyondApplication.BaseUrl + "/im/getOthorUser/" + toChatUsername + "/" + UserInfoUtil.getInstance().getUserInfo().getUserId(), map, new NewCustomResponseHandler() {
            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                UserDate userDate = JSON.parseObject(responseStr, UserDate.class);
                if (userDate.getCode() == 0) {
                    mdata = userDate.getData();
                    remarkName = mdata.getRemakname();
                    titleBar.setTitle(mdata.getRemakname().equals("") ? mdata.getNickname() : mdata.getRemakname());
                }
            }

            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }
        });
    }

    @Override
    protected void sendMessage(EMMessage message) {
        super.sendMessage(message);
        // 设置消息扩展属性
        message.setAttribute("em_robot_message", true);

        String userPic = UserInfoUtil.getInstance().getUserInfo().getAvatar();
        if (!TextUtils.isEmpty(userPic)) {
            message.setAttribute("headImg", userPic);
        }
        if (null == remarkName || remarkName.equals("")) {
            userName = UserInfoUtil.getInstance().getUserInfo().getNickName();
            if (!TextUtils.isEmpty(userName)) {
                message.setAttribute("nickName", userName);
            }
        } else {
            message.setAttribute("nickName", remarkName);
        }


        // 设置自定义推送提示
        JSONObject extObject = new JSONObject();
        try {
            extObject.put("em_push_name", "科普中国户户通");
            String ticker = EaseCommonUtils.getMessageDigest(message, getContext());
            if (message.getType() == EMMessage.Type.TXT) {
                ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
            }
            EaseUser user = getUserInfo(message.getFrom());
            if (user != null) {
                if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                    extObject.put("em_push_content", String.format(getContext().getString(R.string.at_your_in_group), userName));
                }//
                extObject.put("em_push_content", userName + ": " + ticker);
            } else {
                if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                    extObject.put("em_push_content", String.format(getContext().getString(R.string.at_your_in_group), message.getFrom()));
                }
                extObject.put("em_push_content", message.getFrom() + ": " + ticker);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 将推送扩展设置到消息中
        message.setAttribute("em_apns_ext", extObject);
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {

        // 设置消息扩展属性
        message.setAttribute("em_robot_message", true);

        // 通过扩展属性，将userPic和userName发送出去。
        String userPic = UserInfoUtil.getInstance().getUserInfo().getAvatar();
        if (!TextUtils.isEmpty(userPic)) {
            message.setAttribute("headImg", userPic);
        }
        String userName = UserInfoUtil.getInstance().getUserInfo().getNickName();
        if (!TextUtils.isEmpty(userName)) {
            message.setAttribute("nickName", userName);
        }
        // 设置自定义推送提示
        JSONObject extObject = new JSONObject();
        try {
            extObject.put("em_push_name", "科普中国户户通");
            String ticker = EaseCommonUtils.getMessageDigest(message, getContext());
            if (message.getType() == EMMessage.Type.TXT) {
                ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
            }
            EaseUser user = getUserInfo(message.getFrom());
            if (user != null) {
                if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                    extObject.put("em_push_content", String.format(getContext().getString(R.string.at_your_in_group), userName));
                }//
                extObject.put("em_push_content", userName + ": " + ticker);
            } else {
                if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                    extObject.put("em_push_content", String.format(getContext().getString(R.string.at_your_in_group), message.getFrom()));
                }
                extObject.put("em_push_content", message.getFrom() + ": " + ticker);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 将推送扩展设置到消息中
        message.setAttribute("em_apns_ext", extObject);
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        super.onMessageRead(messages);
    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }
}
