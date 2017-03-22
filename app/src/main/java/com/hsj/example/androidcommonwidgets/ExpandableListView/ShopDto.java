package com.hsj.example.androidcommonwidgets.ExpandableListView;

import java.util.List;

/**
 * Created by huangsaijin on 2017/1/12.
 */
public class ShopDto {

    /**
     * 是否选中
     */
    private boolean isSelected;

    private int shopHeadImage;

    private String shopName;

    private List<GoodsDto> goods;

    /**
     * 是否处于编辑状态
     */
    private boolean isEdit = false;

    public boolean isEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getShopHeadImage() {
        return shopHeadImage;
    }

    public void setShopHeadImage(int shopHeadImage) {
        this.shopHeadImage = shopHeadImage;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<GoodsDto> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsDto> goods) {
        this.goods = goods;
    }
}
