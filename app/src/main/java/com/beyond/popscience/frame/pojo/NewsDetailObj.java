package com.beyond.popscience.frame.pojo;

import java.util.List;

/**
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class NewsDetailObj extends BaseObject {

    private String topPic;
    private String title;
    private String publishTime;
    private String auchor;
    private String editor;
    private String commentCount;
    private String filename;
    private String fileaddress;
    private List<String> signOff;
    private  List<adj>  fileList;

    public List<adj> getFileList() {
        return fileList;
    }

    public void setFileList(List<adj> fileList) {
        this.fileList = fileList;
    }

    public class  adj {

        /**
         * id : 11
         * newsid : 108
         * type : 1
         * filename : 149afae673d3433588bd28e0a931af01.jsp
         * fileaddress : http://ikow.oss-cn-shanghai.aliyuncs.com/starryfile/149afae673d3433588bd28e0a931af01.jsp
         */

        private int id;
        private int newsid;
        private int type;
        private String filename;
        private String fileaddress;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNewsid() {
            return newsid;
        }

        public void setNewsid(int newsid) {
            this.newsid = newsid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getFileaddress() {
            return fileaddress;
        }

        public void setFileaddress(String fileaddress) {
            this.fileaddress = fileaddress;
        }
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileaddress() {
        return fileaddress;
    }

    public void setFileaddress(String fileaddress) {
        this.fileaddress = fileaddress;
    }

    public List<String> getSignOff() {
        return signOff;
    }

    public void setSignOff(List<String> signOff) {
        this.signOff = signOff;
    }

    /**
     * 点赞数
     */
    private String voteNum;
    /**
     * 不喜欢数
     */
    private String dislikeNum;
    private String likeCount;
    /**
     * 	0:未收藏1:已收藏
     */
    private String myLike;
    /**
     * 不喜欢标记    0:否 1:是
     */
    private String myDislike;
    /**
     * 	是否点赞    0:否 1:是
     */
    private String myVote;
    /**
     * 外链
     */
    private String link;
    /**
     *
     */
    private String linkName;

    private List<NewsDetailContent> content;

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMyVote() {
        return myVote;
    }

    public void setMyVote(String myVote) {
        this.myVote = myVote;
    }

    public String getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(String voteNum) {
        this.voteNum = voteNum;
    }

    public String getDislikeNum() {
        return dislikeNum;
    }

    public void setDislikeNum(String dislikeNum) {
        this.dislikeNum = dislikeNum;
    }

    public String getMyDislike() {
        return myDislike;
    }

    public void setMyDislike(String myDislike) {
        this.myDislike = myDislike;
    }

    public String getTopPic() {
        return topPic;
    }

    public void setTopPic(String topPic) {
        this.topPic = topPic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getAuchor() {
        return auchor;
    }

    public void setAuchor(String auchor) {
        this.auchor = auchor;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public List<NewsDetailContent> getContent() {
        return content;
    }

    public void setContent(List<NewsDetailContent> content) {
        this.content = content;
    }

    public String getMyLike() {
        return myLike;
    }

    public void setMyLike(String myLike) {
        this.myLike = myLike;
    }

    /**
     * 是否已经点过 不喜欢
     * @return
     */
    public boolean isDislike(){
        return "1".equals(myDislike);
    }

    /**
     * 设置是否已经点过 不喜欢
     * @return
     */
    public void setDislike(boolean isDislike){
        myDislike = isDislike ? "1" : "0";
    }

    /**
     * 是否已经点赞
     * @return
     */
    public boolean isVote(){
        return "1".equals(myVote);
    }

    /**
     * 设置是否已经点赞
     * @return
     */
    public void setVote(boolean isVote){
        myVote = isVote ? "1" : "0";
    }

    /**
     * 是否已经被收藏
     * @return
     */
    public boolean isCollected(){
        return "1".equals(myLike);
    }

    /**
     * 设置是否收藏
     * @param isCollected
     */
    public void setCollected(boolean isCollected){
        myLike = isCollected ? "1" : "0";
    }

    /**
     * 收藏
     * @return
     */
    public long getLikeCountLong(){
        try {
            return Long.parseLong(likeCount);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 评论
     * @return
     */
    public long getCommentCountLong(){
        try {
            return Long.parseLong(commentCount);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 点赞数
     * @return
     */
    public long getVoteNumLong(){
        try {
            return Long.parseLong(voteNum);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 不喜欢数
     * @return
     */
    public long getDislikeNumLong(){
        try {
            return Long.parseLong(dislikeNum);
        } catch (Exception e) {
            return 0;
        }
    }

}
