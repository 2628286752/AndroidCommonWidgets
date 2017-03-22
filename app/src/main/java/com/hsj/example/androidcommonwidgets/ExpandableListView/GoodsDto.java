package com.hsj.example.androidcommonwidgets.ExpandableListView;

/**
 * Created by huangsaijin on 2017/1/12.
 */
public class GoodsDto {

    private boolean isSelected;

    private int goodsHeadImage;

    private String goodsName;

    private int goodsCount;

    private int goodsPrice;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getGoodsHeadImage() {
        return goodsHeadImage;
    }

    public void setGoodsHeadImage(int goodsHeadImage) {
        this.goodsHeadImage = goodsHeadImage;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public int getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(int goodsPrice) {
        this.goodsPrice = goodsPrice;
    }
}
