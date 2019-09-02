package com.beyond.popscience;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.beyond.library.network.net.httpclient.NewCustomResponseHandler;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.base.BaseFragment;
import com.beyond.popscience.frame.net.base.AppBaseRestUsageV2;
import com.beyond.popscience.module.home.adapter.ContactTwoAdapter;
import com.beyond.popscience.module.home.fragment.view.AlphabetAdp;
import com.beyond.popscience.module.home.fragment.view.ClearEditText;
import com.beyond.popscience.module.home.fragment.view.ContactBean;
import com.beyond.popscience.module.home.fragment.view.LocationAddressList;
import com.beyond.popscience.module.home.fragment.view.PinYinStyle;
import com.beyond.popscience.module.home.fragment.view.PinYinUtil;
import com.beyond.popscience.module.home.fragment.view.SideLetterBar;
import com.beyond.popscience.module.home.fragment.view.SwipeManager;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddressListFragment extends BaseFragment {
    @BindView(R.id.lv_contact)
    ListView lvContact;
    @BindView(R.id.tv_alphabet)
    TextView tvAlphabet;
    @BindView(R.id.et_clear)
    ClearEditText etClear;
    @BindView(R.id.sideLetterBar)
    SideLetterBar sideLetterBar;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.lv_alphabet)
    ListView lvAlphabet;
    @BindView(R.id.rel_notice)
    RelativeLayout relNotice;
    Unbinder unbinder;
    /**
     * 获取库Phone表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME = 0;

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER = 1;

    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID = 2;

    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID = 3;
    @BindView(R.id.back)
    ImageView back;

    /**
     * 联系人名称
     **/
    private ArrayList<String> mContactsName = new ArrayList<String>();

    /**
     * 联系人头像
     **/
    private ArrayList<String> mContactsNumber = new ArrayList<String>();

    /**
     * 联系人头像
     **/
    private ArrayList<Bitmap> mContactsImg = new ArrayList<Bitmap>();
    private List<String> alphabetList;
    private ArrayList<ContactBean> contactList = new ArrayList<>();
    private ContactTwoAdapter adapter;
    private List<LocationAddressList.DataBean> mData;

    @Override
    protected int getLayoutResID() {
        return R.layout.frgament_address_list;
    }

    @Override
    public void initUI() {
        super.initUI();
        initView();
        initEvent();
        initData();
        searchPhone();
    }


    private void initData() {
        //3.设置Adapter
        adapter = new ContactTwoAdapter(getActivity(), contactList);
        lvContact.setAdapter(adapter);
        alphabetList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * 上传数据
     */
    private void upData() {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < mContactsName.size(); i++) {
            String phoneNumber = mContactsNumber.get(i).replaceAll(" ", "");
            map.put(phoneNumber, mContactsName.get(i));
        }
        AppBaseRestUsageV2 appBaseRestUsageV1 = new AppBaseRestUsageV2();
        appBaseRestUsageV1.post(BeyondApplication.BaseUrl + "/im/queryUserByMailList", map, new NewCustomResponseHandler() {

            @Override
            public void onSuccess(int httpStatusCode, Map headerMap, String responseStr) {
                super.onSuccess(httpStatusCode, headerMap, responseStr);
                LocationAddressList location = JSON.parseObject(responseStr, LocationAddressList.class);
                if (location.getCode() == 0) {
                    mData = location.getData();
                    ArrayList<ContactBean> totalData = dataList();
                    contactList.addAll(totalData);
                    adapter.notifyDataSetInvalidated();
                }
            }
            @Override
            public void onFailure(int statusCode, Map headerMap, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headerMap, responseString, throwable);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        relNotice.post(new Runnable() {
            @Override
            public void run() {
                relNotice.getHeight();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relNotice.getLayoutParams();
                params.height = tvNotice.getHeight() * 5;
                params.width = tvNotice.getWidth();
                relNotice.setLayoutParams(params);
            }
        });
    }

    private void initEvent() {
        sideLetterBar.setOnTouchLetterListener(new SideLetterBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                alphabetList.clear();
                ViewPropertyAnimator.animate(relNotice).alpha(1f).setDuration(0).start();
                //根据当前触摸的字母，去集合中找那个item的首字母和letter一样，然后将对应的item放到屏幕顶端
                for (int i = 0; i < contactList.size(); i++) {
                    String firstAlphabet = contactList.get(i).getPinyin().charAt(0) + "";
                    if (letter.equals(firstAlphabet)) {
                        lvContact.setSelection(i);
                        relNotice.setVisibility(View.VISIBLE);
                        break;
                    }
                    if (letter.equals("#")) {
                        lvContact.setSelection(0);
                        relNotice.setVisibility(View.GONE);
                    }
                }
                for (int i = 0; i < contactList.size(); i++) {
                    String firstAlphabet = contactList.get(i).getPinyin().toString().trim().charAt(0) + "";

                    if (letter.equals(firstAlphabet)) {
                        //说明找到了，那么应该讲当前的item放到屏幕顶端
                        tvNotice.setText(letter);
                        if (!alphabetList.contains(String.valueOf(contactList.get(i).getName().trim().charAt(0)))) {
                            alphabetList.add(String.valueOf(contactList.get(i).getName().trim().charAt(0)));
                        }
                    }

                }
                showCurrentWord(letter);
                //显示当前触摸的字母
                AlphabetAdp alphabetAdp = new AlphabetAdp(getActivity(), alphabetList);
                lvAlphabet.setAdapter(alphabetAdp);
                alphabetAdp.notifyDataSetChanged();
            }
        });
        lvContact.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //如果垂直滑动，则需要关闭已经打开的layout
                    SwipeManager.getInstance().closeCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int pos = lvContact.getFirstVisiblePosition();
                if (contactList.size() > 0) {
                    tvAlphabet.setVisibility(View.VISIBLE);
                    String text = contactList.get(pos).getPinyin().charAt(0) + "";
                    Pattern p = Pattern.compile("[0-9]*");
                    Matcher m1 = p.matcher(text);
                    if (m1.matches()) {
                        tvAlphabet.setText("#");
                    } else {
                        tvAlphabet.setText(text);
                    }
                } else {
                    tvAlphabet.setVisibility(View.GONE);
                }
            }
        });
        etClear.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                fuzzySearch(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        lvAlphabet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String alphabet = alphabetList.get(position).trim();
                setIsVisiable();
                for (int i = 0; i < contactList.size(); i++) {
                    if (alphabet.equals(String.valueOf(contactList.get(i).getName().trim().charAt(0)))) {
                        int pos = i % lvAlphabet.getChildCount();
                        int childCount = lvContact.getChildCount();
                        if (position == 0 && pos - position == 1 || childCount - pos == 1) {
                            lvContact.setSelection(i);
                        } else {
                            lvContact.setSelection(i - 1);
                        }
                        break;
                    }
                }
            }
        });
    }

    protected void showCurrentWord(String letter) {
        tvNotice.setText(letter);
        setIsVisiable();
    }

    private Handler handler = new Handler();

    private void setIsVisiable() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != relNotice) {
                    ViewPropertyAnimator.animate(relNotice).alpha(0f).setDuration(1000).start();
                }

            }
        }, 4000);
    }

    private void fuzzySearch(String str) {
        ArrayList<ContactBean> filterDateList = new ArrayList<ContactBean>();
        // 虚拟数据
        if (TextUtils.isEmpty(str)) {
            sideLetterBar.setVisibility(View.VISIBLE);
            filterDateList = dataList();
        } else {
            filterDateList.clear();
            sideLetterBar.setVisibility(View.GONE);
            for (ContactBean contactBean : dataList()) {
                String name = contactBean.getName();
                Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher m = p.matcher(str);
                if (m.matches()) {
                    str = PinYinUtil.getPinyin(str);
                }
                if (PinYinUtil.getPinyin(name).contains(str.toUpperCase()) || contactBean.pinYinStyle.briefnessSpell.toUpperCase().contains(str.toUpperCase())
                        || contactBean.pinYinStyle.completeSpell.toUpperCase().contains(str.toUpperCase())) {
                    filterDateList.add(contactBean);
                }
            }
        }
        contactList = filterDateList;
        adapter = new ContactTwoAdapter(getActivity(), filterDateList);
        lvContact.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<ContactBean> dataList() {
        ArrayList<ContactBean> mSortList = new ArrayList<>();
        if (null != mData) {
            for (int i = 0; i < mData.size(); i++) {
                if (null != mData.get(i) && !"".equals(mData.get(i))) {
                    ContactBean bean = new ContactBean(mData.get(i).getMobliename());
                    bean.setHeadImg(mData.get(i).getAvatar());
                    bean.setMobile(mData.get(i).getMobile());
                    bean.setName(mData.get(i).getMobliename());
                    bean.setNickname(mData.get(i).getNickname());
                    bean.pinYinStyle = parsePinYinStyle(mData.get(i).getMobliename());
                    bean.setType(mData.get(i).getType());
                    //初始本地状态
                    bean.setFlag1(0);
                    bean.setFlag2(0);
                    mSortList.add(bean);
                }
            }
            Collections.sort(mSortList);
        }
        return mSortList;
    }

    public PinYinStyle parsePinYinStyle(String content) {
        PinYinStyle pinYinStyle = new PinYinStyle();
        if (content != null && content.length() > 0) {
            //其中包含的中文字符
            String[] enStrs = new String[content.length()];
            for (int i = 0; i < content.length(); i++) {
                enStrs[i] = PinYinUtil.getPinyin(String.valueOf(content.charAt(i)));
            }
            for (int i = 0, length = enStrs.length; i < length; i++) {
                if (enStrs[i].length() > 0) {
                    //拼接简拼
                    pinYinStyle.briefnessSpell += enStrs[i].charAt(0);
                }
            }
        }
        return pinYinStyle;
    }

    private void searchPhone() {
        ContentResolver resolver = getActivity().getContentResolver();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME);

                // 得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID);

                // 得到联系人头像ID
                Long imgid = phoneCursor.getLong(PHONES_PHOTO_ID);

                // 得到联系人头像Bitamp
                Bitmap bitmap = null;

                // photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if (imgid > 0) {
                    Uri uri = ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts
                            .openContactPhotoInputStream(resolver, uri);
                    bitmap = BitmapFactory.decodeStream(input);
                } else {
                    // 设置默认
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.m1);
                }

                mContactsName.add(contactName);
                mContactsNumber.add(phoneNumber);
                mContactsImg.add(bitmap);
            }

            phoneCursor.close();
            if (mContactsName.size() > 0) {
                for (int i = 0; i < mContactsName.size() - 1; i++) {
                    for (int j = mContactsName.size() - 1; j > i; j--) {
                        if (null != mContactsName.get(i)) {
                            if (null != mContactsName.get(j)) {
                                if (mContactsName.get(j).equals(mContactsName.get(i))) {
                                    mContactsName.remove(j);
                                    mContactsNumber.remove(j);
                                }
                            }
                        }
                    }
                }
                upData();

            } else {
            }
        }

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        getActivity().finish();
    }
}
