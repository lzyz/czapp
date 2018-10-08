package com.czapp.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by admin on 2018/5/1.
 */

public class HealthBean extends BmobObject {
    private BmobUser u;
    private Double tw;
    private Double tz;
    private Double xt;
    private Double xy;
    private Double xz;

    public HealthBean(){
        this.setTableName("Health");
    }


    public Double getTw() {
        return tw;
    }

    public void setTw(Double tw) {
        this.tw = tw;
    }

    public Double getTz() {
        return tz;
    }

    public void setTz(Double tz) {
        this.tz = tz;
    }

    public BmobUser getUser() {
        return u;
    }

    public void setUser(BmobUser user) {
        this.u = user;
    }

    public Double getXt() {
        return xt;
    }

    public void setXt(Double xt) {
        this.xt = xt;
    }

    public Double getXy() {
        return xy;
    }

    public void setXy(Double xy) {
        this.xy = xy;
    }

    public Double getXz() {
        return xz;
    }

    public void setXz(Double xz) {
        this.xz = xz;
    }
}
