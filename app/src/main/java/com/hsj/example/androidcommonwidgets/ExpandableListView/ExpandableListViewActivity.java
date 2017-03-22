package com.hsj.example.androidcommonwidgets.ExpandableListView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hsj.example.androidcommonwidgets.R;
import com.hsj.example.androidcommonwidgets.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ExpandableListViewActivity extends Activity implements ExpandableListViewAdapter.ModifyGoodsCountInterface,
        ExpandableListViewAdapter.CheckGoodsInterface{
    private ExpandableListView mElvCopyShopCar;

    private ExpandableListViewAdapter adapterExpandableListView;

    private List<ShopDto> shopList;

    private CheckBox mCbAllCheck;

    private TextView mTvShowPrice;

    private Button mBtnSettleAccounts;

    private int totalAccount = 0;

    private int totalPrice = 0;

    private ArrayMap<String, GoodsDto> arrayGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandablelistview);

        initView();
        iniData();
        initListener();
    }

    private void initView() {
        mElvCopyShopCar = (ExpandableListView) findViewById(R.id.elv_aelv_copyshopcar);
        mCbAllCheck = (CheckBox) findViewById(R.id.cb_aelv_allcheck);
        mTvShowPrice = (TextView) findViewById(R.id.tv_aelv_showprice);
        mBtnSettleAccounts = (Button) findViewById(R.id.btn_aelv_settleaccounts);
    }

    private void iniData() {
        shopList  = new ArrayList<ShopDto>();
        for (int i = 0;i < 5;i++){
            ShopDto shop = new ShopDto();
            shop.setShopHeadImage(R.drawable.shop);
            shop.setShopName("零食小铺" + CommonUtils.toString(i));
            List<GoodsDto> goodsList = new ArrayList<GoodsDto>();
            for (int j = 0;j < 3;j++){
                GoodsDto goods = new GoodsDto();
                goods.setGoodsHeadImage(R.drawable.goods);
                goods.setGoodsName("薯条" + CommonUtils.toString(i) + CommonUtils.toString(j));
                goods.setGoodsCount(1);
                goods.setGoodsPrice(j + 1);
                goodsList.add(goods);
            }
            shop.setGoods(goodsList);
            shopList.add(shop);
        }

        arrayGoods = getArrayGoods(shopList);
        adapterExpandableListView = new ExpandableListViewAdapter(this,shopList,arrayGoods);
        adapterExpandableListView.setModifyGoodsCountInterface(this);
        adapterExpandableListView.setCheckGoodsInterface(this);
        mElvCopyShopCar.setAdapter(adapterExpandableListView);
        //去掉自带的箭头
        mElvCopyShopCar.setGroupIndicator(null);
        int parentCount = adapterExpandableListView.getGroupCount();

        //展开所有项
        for (int i = 0;i < parentCount;i++) {
            mElvCopyShopCar.expandGroup(i);
        }
    }

    private ArrayMap<String, GoodsDto> getArrayGoods(List<ShopDto> list) {
        ArrayMap<String, GoodsDto> arrayGoods = new ArrayMap<String, GoodsDto>();
        String ss = null;
        List<GoodsDto> goods;
        if (null != list) {
            for (int i=0; i<list.size(); i++) {
                goods = list.get(i).getGoods();
                if (null != goods) {
                    for (int j=0; j<goods.size(); j++) {
                        ss = i + "_" + j;
                        arrayGoods.put(ss, goods.get(j));
                    }
                }
            }
        }
        return arrayGoods;
    }

    private void initListener() {

        //不能点击收缩
        mElvCopyShopCar.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

        mCbAllCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allCheckOrUnCheck();
            }
        });

    }

    @Override
    public void deleteGoods(String position) {

        String[] positionArray = position.split("_");


        int groupPosition = Integer.parseInt(positionArray[0]);
        int childPosition = Integer.parseInt(positionArray[1]);
        shopList.get(groupPosition).getGoods().remove(childPosition);
        arrayGoods = getArrayGoods(shopList);
        List<GoodsDto> list = new ArrayList<GoodsDto>();
        list = shopList.get(groupPosition).getGoods();
        if (!(list.size() > 0)){
            shopList.remove(groupPosition);
        }
        if(isAllChecked()){
            mCbAllCheck.setChecked(true);
        }else {
            mCbAllCheck.setChecked(false);
        }
        adapterExpandableListView.notifyDataSetChanged();
    }

    @Override
    public void addGoods(GoodsDto goods) {
        int account = goods.getGoodsCount();
        account++;
        goods.setGoodsCount(account);
        adapterExpandableListView.notifyDataSetChanged();
    }

    @Override
    public void reduceGoods(GoodsDto goods) {
        int account = goods.getGoodsCount();
        if(account <= 1){
            Toast.makeText(ExpandableListViewActivity.this,"宝贝不能再少了，亲！",Toast.LENGTH_SHORT).show();
        }else{
            account--;
        }
        goods.setGoodsCount(account);
        adapterExpandableListView.notifyDataSetChanged();
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {

        shopList.get(groupPosition).setIsSelected(isChecked);
        List<GoodsDto> listGoods = shopList.get(groupPosition).getGoods();
        for(GoodsDto g:listGoods){
            g.setIsSelected(isChecked);
        }
        if(isAllChecked()){
            mCbAllCheck.setChecked(true);
        }else {
            mCbAllCheck.setChecked(false);
        }
        adapterExpandableListView.notifyDataSetChanged();
        calculatePrice();
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {

        boolean isChildAllSame = true;
        List<GoodsDto> listGoods = shopList.get(groupPosition).getGoods();
        listGoods.get(childPosition).setIsSelected(isChecked);
        //判断店铺下面的商品是否状态一致
        for(GoodsDto o:listGoods){
            if(o.isSelected() != isChecked){
                isChildAllSame = false;
                break;
            }
        }
        //如果isChildAllSame为true，那么isisChecked是什么值就设置成什么值，如果isChildAllSame为false，那么就设置成未选中
        if (isChildAllSame){
            shopList.get(groupPosition).setIsSelected(isChecked);
        }else {
            shopList.get(groupPosition).setIsSelected(false);
        }

        if(isAllChecked()){
            mCbAllCheck.setChecked(true);
        }else {
            mCbAllCheck.setChecked(false);
        }
        adapterExpandableListView.notifyDataSetChanged();
        calculatePrice();
    }

    /**
     * 设置全选按钮
     */
    private void allCheckOrUnCheck(){
        boolean isAllCheck = mCbAllCheck.isChecked();
        for (ShopDto s:shopList){
            s.setIsSelected(isAllCheck);
            List<GoodsDto> listGoods = s.getGoods();
            for(GoodsDto g:listGoods){
                g.setIsSelected(isAllCheck);
            }
        }
        adapterExpandableListView.notifyDataSetChanged();
        calculatePrice();
    }

    /**
     * 只要一个店铺违背选中，就返回false
     * @return
     */
    private boolean isAllChecked(){

        for (ShopDto h:shopList){
            if (!h.isSelected()){
                return false;
            }
        }
        return true;
    }

    /**
     * 计算需要支付的金额
     */
    private void calculatePrice(){
        totalAccount = 0;
        totalPrice = 0;
        for (ShopDto o:shopList){
            List<GoodsDto> listGood = o.getGoods();
            for (GoodsDto d:listGood){
                if(d.isSelected()){
                    totalAccount = totalAccount + d.getGoodsCount();
                    totalPrice = totalPrice + d.getGoodsCount() * d.getGoodsPrice();
                }
            }
        }
        mTvShowPrice.setText("合计：￥" + totalPrice);
        mBtnSettleAccounts.setText("结算(" + totalAccount + ")");
    }
}
