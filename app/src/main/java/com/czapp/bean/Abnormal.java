package com.czapp.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by DELL on 2018/5/12.
 */

public class Abnormal extends BmobObject {

    private String indicator;//指标
    private BmobDate startime;//开始时间
    private BmobDate endtime;//结束时间
    private Float peak1;
    private Float peak2;
    private String userid;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public BmobDate getEndtime() {
        return endtime;
    }

    public void setEndtime(BmobDate endtime) {
        this.endtime = endtime;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public BmobDate getStartime() {
        return startime;
    }

    public void setStartime(BmobDate startime) {
        this.startime = startime;
    }
}
