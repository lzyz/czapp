package com.czapp.bean;

/**
 * Created by admin on 2018/5/1.
 */

public class SetBean {
    private boolean isDistanceShow;
    private int leftImgId;
    private String title;
    private boolean isrightImgShow;
    private int rightImgId;

    public boolean isDistanceShow() {
        return isDistanceShow;
    }

    public void setDistanceShow(boolean distanceShow) {
        isDistanceShow = distanceShow;
    }

    public boolean isrightImgShow() {
        return isrightImgShow;
    }

    public void setIsrightImgShow(boolean isrightImgShow) {
        this.isrightImgShow = isrightImgShow;
    }

    public int getLeftImgId() {
        return leftImgId;
    }

    public void setLeftImgId(int leftImgId) {
        this.leftImgId = leftImgId;
    }

    public int getRightImgId() {
        return rightImgId;
    }

    public void setRightImgId(int rightImgId) {
        this.rightImgId = rightImgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
