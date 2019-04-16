package com.youmai.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PriceBean {

    @Id
    private Long id; //主键id

    private byte startHour; //  开始小时
    private byte startMin;  //  开始分钟
    private byte endHour;   //  结束小时
    private byte endMin;    //  结束分钟
    private int price;      // 费率


    /**
     * @param startHour 开始小时
     * @param startMin  开始分钟
     * @param endHour   结束小时
     * @param endMin    结束分钟
     * @param price     费率 单位分
     */
    public PriceBean(byte startHour, byte startMin, byte endHour,
                     byte endMin, int price) {
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.price = price;
    }

    @Generated(hash = 890807990)
    public PriceBean(Long id, byte startHour, byte startMin, byte endHour,
                     byte endMin, int price) {
        this.id = id;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.price = price;
    }


    @Generated(hash = 1749111358)
    public PriceBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte getStartHour() {
        return this.startHour;
    }

    public void setStartHour(byte startHour) {
        this.startHour = startHour;
    }

    public byte getStartMin() {
        return this.startMin;
    }

    public void setStartMin(byte startMin) {
        this.startMin = startMin;
    }

    public byte getEndHour() {
        return this.endHour;
    }

    public void setEndHour(byte endHour) {
        this.endHour = endHour;
    }

    public byte getEndMin() {
        return this.endMin;
    }

    public void setEndMin(byte endMin) {
        this.endMin = endMin;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
