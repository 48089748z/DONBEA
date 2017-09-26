package com.oriolcunado.pro.multi.POJOs;
/**
 compile 'com.fasterxml.jackson.core:jackson-core:2.5.+'
 compile 'com.fasterxml.jackson.core:jackson-annotations:2.5.+'
 compile 'com.fasterxml.jackson.core:jackson-databind:2.5.+'
 import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
 */
public class VideoYoutube extends GenericYoutube
{
    private String channelId;
    private String channelTitle;
    private String viewCount;
    private String likeCount;
    private String dislikeCount;
    private String favoriteCount;
    private String commentCount;
    private String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * @param channelId
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * @return
     */
    public String getChannelTitle() {
        return channelTitle;
    }

    /**
     * @param channelTitle
     */
    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    /**
     * @return
     */
    public String getViewCount() {
        return viewCount;
    }

    /**
     * @param viewCount
     */
    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * @return
     */
    public String getLikeCount() {
        return likeCount;
    }

    /**
     * @param likeCount
     */
    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    /**
     * @return
     */
    public String getDislikeCount() {
        return dislikeCount;
    }

    /**
     * @param dislikeCount
     */
    public void setDislikeCount(String dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    /**
     * @return
     */
    public String getFavoriteCount() {
        return favoriteCount;
    }

    /**
     * @param favoriteCount
     */
    public void setFavoriteCount(String favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    /**
     * @return
     */
    public String getCommentCount() {
        return commentCount;
    }

    /**
     * @param commentCount
     */
    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }
}
