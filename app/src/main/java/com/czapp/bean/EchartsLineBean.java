package com.czapp.bean;

import java.util.Arrays;

/**
 * Created by dell1 on 2017/5/28.
 */
public class EchartsLineBean {

    public String type;
    public String title;
    public double maxValue;
    public double minValue;
    public String imageUrl;
    public String[] times;
    public double[] steps;


    @Override
    public String toString() {
        return "EchartsLineBean{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", maxValue=" + maxValue +
                ", minValue=" + minValue +
                ", imageUrl='" + imageUrl + '\'' +
                ", times=" + Arrays.toString(times) +
                ", steps=" + Arrays.toString(steps) +
                '}';
    }
}
