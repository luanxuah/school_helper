package com.luanxu.bean;

/**
 * Created by Ahmed on 30.01.14.
 */
public class MagnificentChartItem {

// #MARK - Constants

    public int color;
    public float value;
    public String title;

// #MARK - Constructors

    public MagnificentChartItem(String title, float value, int color){
        this.color = color;
        this.value = value;
        this.title = title;
    }

}
