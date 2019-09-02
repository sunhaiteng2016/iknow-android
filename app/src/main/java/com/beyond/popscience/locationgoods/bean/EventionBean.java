package com.beyond.popscience.locationgoods.bean;

import com.beyond.library.network.net.bean.BaseResponse;

public class EventionBean  extends BaseResponse {


    /**
     * id : 100006
     * userId : 285436
     * productId : 10000053
     * score : 5
     * detail : Good very good
     * createTime : 2019-05-12 21:05:29
     * imglist : http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/a20f0926d20546f192d30cadb3996f3f.jpeg,http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/a03a56ab45df4607b1f6f3441836f422.jpeg,http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/582c4d5a17ec49cc8d9182ae92aeee35.jpeg,http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/4c310ee8d2f04ec6a8a66c22cd067c96.jpeg,http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/e5df203505c34b4694162e49651d5ae7.jpeg,http://ikow.oss-cn-shanghai.aliyuncs.com/newsImages/c72b7f1038a94138a17247236d988e19.jpeg
     * nickName : 132
     * avatar : http://www.appwzd.cn/micro_mart/ncss/logo.png
     */

    private int id;
    private int userId;
    private int productId;
    private int score;
    private String detail;
    private String createTime;
    private String imglist;
    private String nickName;
    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImglist() {
        return imglist;
    }

    public void setImglist(String imglist) {
        this.imglist = imglist;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
