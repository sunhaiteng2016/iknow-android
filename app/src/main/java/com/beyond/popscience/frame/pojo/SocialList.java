package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * 社团列表
 *
 * Created by yao.cui on 2017/7/18.
 */

public class SocialList extends BaseList {
    private List<SocialInfo> communityList;

    public List<SocialInfo> getCommunityList() {
        return communityList;
    }

    public void setCommunityList(List<SocialInfo> communityList) {
        this.communityList = communityList;
    }

}
