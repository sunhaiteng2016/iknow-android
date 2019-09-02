package com.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.HMSPushHelper;
import com.beyond.popscience.R;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV1;
import com.beyond.popscience.frame.util.UserInfoUtil;
import com.beyond.popscience.module.home.ChatActivity;
import com.beyond.popscience.module.home.entity.UserDate;
import com.helper.db.InviteMessgeDao;
import com.helper.db.UserDao;
import com.helper.domain.RobotUser;
import com.helper.utils.APPConfig;
import com.helper.utils.SharedPreferencesUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/12.
 */

public class HxEaseuiHelper {

    private static HxEaseuiHelper instance = null;
    protected EMMessageListener messageListener = null;
    private Context appContext;
    private String username;
    public static EaseUI easeUI;
    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;
    private Map<String, EaseUser> contactList;
    private Map<String, RobotUser> robotList;
    private DemoModel demoModel = null;

    private String TAG = "HxEaseuiHelper";

    public synchronized static HxEaseuiHelper getInstance() {
        if (instance == null) {
            instance = new HxEaseuiHelper();
        }
        return instance;
    }

    public void init(Context context) {
        demoModel = new DemoModel(context);
        EMOptions options = initChatOptions();
        //use default options if options is null
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;

            //获取easeui实例
            easeUI = EaseUI.getInstance();
            //初始化easeui
            easeUI.init(appContext, options);
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(true);
            setEaseUIProviders();
            //设置全局监听
            setGlobalListeners();

//            broadcastManager = LocalBroadcastManager.getInstance(appContext);
            initDbDao();
            HMSPushHelper.getInstance().initHMSAgent(BeyondApplication.getInstance());
        }
    }


    private EMOptions initChatOptions() {
        EMOptions options = new EMOptions();
        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);

        return options;
    }

    protected void setEaseUIProviders() {
        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), user.getNickname());
                    }
                    return user.getNickname() + ": " + ticker;
                } else {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), user.getNickname());
                    }
                    return user.getNickname() + ": " + ticker;
                } else {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
                //return fromUsersNum + "contacts send " + messageNum + "messages to you";
                //return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(appContext, ChatActivity.class);
                // open calling activity if there is call
                /*if(isVideoCalling){
                    intent = new Intent(appContext, VideoCallActivity.class);
                }else if(isVoiceCalling){
                    intent = new Intent(appContext, VoiceCallActivity.class);
                }else{*/
                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { // single chat message
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                } else { // group chat message
                    // message.getTo() is the group id
                    intent.putExtra("userId", message.getTo());
                    if (chatType == EMMessage.ChatType.GroupChat) {
                        intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                    } else {
                        intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
                    }

                    //}
                }
                return intent;
            }
        });
    }

    private EaseUser getUserInfo(final String username) {
        //获取 EaseUser实例, 这里从内存中读取
        //如果你是从服务器中读读取到的，最好在本地进行缓存
        new Thread() {
            @Override
            public void run() {
                super.run();
                AppBaseRestUsageV1 appBaseRestUsageV1 = new AppBaseRestUsageV1();
                Map<String, String> map = new HashMap<>();
                appBaseRestUsageV1.get(BeyondApplication.BaseUrl + "/im/getOthorUser/" + username + "/" + UserInfoUtil.getInstance().getUserInfo().getUserId(), map, new NewCustomResponseHandler() {
                    @Override
                    public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                        super.onSuccess(httpStatusCode, headerMap, responseStr);
                        UserDate userDate = JSON.parseObject(responseStr, UserDate.class);
                        if (userDate.getCode() == 0) {
                            String remarkName = userDate.getData().getRemakname();
                            String headImg = userDate.getData().getHeadImg();
                            String nickName = userDate.getData().getNickname();
                            EaseUser easeUser = new EaseUser(username);
                            if (null == remarkName || "".equals(remarkName)) {
                                easeUser.setNickname(nickName);
                            } else {
                                easeUser.setNickname(remarkName);
                            }
                            easeUser.setAvatar(headImg);
                            //存入内存
                            getContactList();
                            contactList.put(username, easeUser);
                            //存入db
                            UserDao dao = new UserDao(appContext);

                            List<EaseUser> users = new ArrayList<EaseUser>();
                            users.add(easeUser);
                            dao.saveContactList(users);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headerMap, responseString, throwable);
                    }
                });
            }
        }.start();
        EaseUser user = null;
        //如果用户是本人，就设置自己的头像
        if (null == username) {
            user = new EaseUser(username);
            return user;
        }
        if (username.equals(EMClient.getInstance().getCurrentUser())) {
            user = new EaseUser(username);
            user.setAvatar((String) SharedPreferencesUtils.getParam(appContext, APPConfig.USER_HEAD_IMG, ""));
            user.setNickname((String) SharedPreferencesUtils.getParam(appContext, APPConfig.USER_NAME, "nike"));
            return user;
        }

        //收到别人的消息，设置别人的头像
        if (contactList != null && contactList.containsKey(username)) {
            user = contactList.get(username);
        } else { //如果内存中没有，则将本地数据库中的取出到内存中
            contactList = getContactList();
            user = contactList.get(username);
        }

        //如果用户不是你的联系人，则进行初始化
        if (user == null) {
            user = new EaseUser(username);
            EaseCommonUtils.setUserInitialLetter(user);
        } else {
            if (TextUtils.isEmpty(user.getNickname())) {//如果名字为空，则显示环信号码
                user.setNickname(user.getUsername());
            }
        }

        return user;
    }

    private void initDbDao() {
        inviteMessgeDao = new InviteMessgeDao(appContext);
        userDao = new UserDao(appContext);
    }

    public Map<String, RobotUser> getRobotList() {
        if (isLoggedIn() && robotList == null) {
            robotList = demoModel.getRobotList();
        }
        return robotList;
    }

    /**
     * get current user's id
     */
    public String getCurrentUsernName() {
        if (username == null) {
            username = (String) SharedPreferencesUtils.getParam(appContext, Constant.HX_CURRENT_USER_ID, "");
        }
        return username;
    }

    /**
     * 获取所有的联系人信息
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        if (isLoggedIn() && contactList == null) {
            contactList = demoModel.getContactList();
        }

        // return a empty non-null object to avoid app crash
        if (contactList == null) {
            return new Hashtable<String, EaseUser>();
        }

        return contactList;
    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * set global listener
     */
    protected void setGlobalListeners() {
        registerMessageListener();
    }

    /**
     * Global listener
     * If this event already handled by an activity, you don't need handle it again
     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
     */
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());

                    //接收并处理扩展消息
                    String userName = message.getStringAttribute(Constant.USER_NAME, "");
                    String userId = message.getStringAttribute(Constant.USER_ID, "");
                    String userPic = message.getStringAttribute(Constant.HEAD_IMAGE_URL, "");
                    String hxIdFrom = message.getFrom();
                    System.out.println("helper接收到的用户名：" + userName + "helper接收到的id：" + userId + "helper头像：" + userPic);
                    EaseUser easeUser = new EaseUser(hxIdFrom);
                    easeUser.setAvatar(userPic);
                    easeUser.setNickname(userName);
                    //存入内存
                    getContactList();
                    contactList.put(hxIdFrom, easeUser);

                    //存入db
                    UserDao dao = new UserDao(appContext);
                    List<EaseUser> users = new ArrayList<EaseUser>();
                    users.add(easeUser);
                    dao.saveContactList(users);
                    // in background, do not refresh UI, notify it in notification bar
                    //设置本地消息推送通知
                    if (!easeUI.hasForegroundActivies()) {
                        getNotifier().notify(message);
                    }
                    int count = EMClient.getInstance().chatManager().getUnreadMessageCount();
                    setBadgeNum(count, appContext);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "receive command message");
                    //get message body
                    //end of red packet code
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }


    /**
     * 设置华为角标
     */
    public void setBadgeNum(int num, Context context) {
        try {
            Bundle bunlde = new Bundle();
            bunlde.putString("package", "com.gymj.apk.xj");
            bunlde.putString("class", "com.beyond.popscience.module.home.WelcomeActivity");
            bunlde.putInt("badgenumber", num);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
