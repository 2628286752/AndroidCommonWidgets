package com.hsj.example.androidcommonwidgets.ExpandableListView;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hsj.example.androidcommonwidgets.R;
import com.hsj.example.androidcommonwidgets.Utils.CommonUtils;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by huangsaijin on 2017/1/12.
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter{
    private Context mContext;

    private List<ShopDto> mList;

    private ChildViewHolder childViewHolder;

    private GroupViewHolder groupViewHolder;

    private ModifyGoodsCountInterface modifyGoodsCountInterface;

    private CheckGoodsInterface checkGoodsInterface;

    private ArrayMap<String, GoodsDto> arrayGoods;

    private ArrayMap<String, ChildViewHolder> arrayHolder;

    public void setModifyGoodsCountInterface(ModifyGoodsCountInterface modifyGoodsCountInterface) {
        this.modifyGoodsCountInterface = modifyGoodsCountInterface;
    }

    public void setCheckGoodsInterface(CheckGoodsInterface checkGoodsInterface) {
        this.checkGoodsInterface = checkGoodsInterface;
    }

    class GroupViewHolder {
        CheckBox groupSelect;
        ImageView groupImage;
        TextView groupName;
        TextView groupEdit;
    }

    class ChildViewHolder {
        CheckBox childSelect;
        ImageView childImage;
        ImageView childReduce;
        ImageView childAdd;
        TextView childName;
        EditText childAccount;
        TextView childDelete;
        TextView childTvAccount;
        TextView childPrice;
        LinearLayout childUnEdit;
        RelativeLayout childEdit;
    }

    public ExpandableListViewAdapter(Context context,List<ShopDto> list,ArrayMap<String, GoodsDto> arrayGoods){
        this.mContext = context;
        this.mList = list;
        this.arrayGoods = arrayGoods;
        arrayHolder = new ArrayMap<String, ChildViewHolder>();
    }



    //获得父项的数量
    @Override
    public int getGroupCount() {
        if(mList != null){
            return mList.size();
        }
        return 0;
    }

    //获得某个父项的子项数目
    @Override
    public int getChildrenCount(int groupPos) {
        if(mList != null && mList.get(groupPos).getGoods() != null){
            return mList.get(groupPos).getGoods().size();
        }
        return 0;
    }

    //获得某个父项
    @Override
    public Object getGroup(int groupPos) {
        return mList.get(groupPos);
    }

    //获得某个父项的某个子项
    @Override
    public Object getChild(int groupPos, int childPos) {
        return mList.get(groupPos).getGoods().get(childPos);
    }

    //获得某个父项的id
    @Override
    public long getGroupId(int groupPos) {
        return groupPos;
    }

    //获得某个父项的某个子项的id
    @Override
    public long getChildId(int groupPos, int childPos) {
        return childPos;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean b, View groupConvertView, ViewGroup viewGroup) {

        if(groupConvertView == null) {
            groupViewHolder = new GroupViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            groupConvertView = inflater.inflate(R.layout.item_groupexpandablelistview, null);
            groupViewHolder.groupSelect = (CheckBox) groupConvertView.findViewById(R.id.cb_igelv_shopselect);
            groupViewHolder.groupImage = (ImageView) groupConvertView.findViewById(R.id.iv_igelv_headimage);
            groupViewHolder.groupName = (TextView) groupConvertView.findViewById(R.id.tv_igelv_name);
            groupViewHolder.groupEdit = (TextView) groupConvertView.findViewById(R.id.tv_igelv_edit);
            groupConvertView.setTag(groupViewHolder);
        }else{
            groupViewHolder = (GroupViewHolder) groupConvertView.getTag();
        }

        final ShopDto shop = mList.get(groupPosition);
        groupViewHolder.groupSelect.setChecked(shop.isSelected());
        groupViewHolder.groupImage.setBackgroundResource(shop.getShopHeadImage());
        groupViewHolder.groupName.setText(shop.getShopName());
        if(shop.isEdit()){
            groupViewHolder.groupEdit.setText("完成");
        }else{
            groupViewHolder.groupEdit.setText("编辑");
        }

        groupViewHolder.groupEdit.setOnClickListener(new OnEditChildViewListener(groupViewHolder.groupEdit,shop));
        groupViewHolder.groupSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isGroupChecked = ((CheckBox)view).isChecked();
                shop.setIsSelected(isGroupChecked);
                checkGoodsInterface.checkGroup(groupPosition,isGroupChecked);
            }
        });

        return groupConvertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean b, View childConvertView, ViewGroup viewGroup) {
        final String position = groupPosition + "_" +childPosition;
        childViewHolder =  arrayHolder.get(position);
        if(childViewHolder == null) {
            childViewHolder = new ChildViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childConvertView = inflater.inflate(R.layout.item_childexpandablelistview, null);
            childViewHolder.childSelect = (CheckBox) childConvertView.findViewById(R.id.cb_icelv_select);
            childViewHolder.childImage = (ImageView) childConvertView.findViewById(R.id.iv_icelv_headimage);
            childViewHolder.childName = (TextView) childConvertView.findViewById(R.id.tv_icelv_name);
            childViewHolder.childReduce = (ImageView) childConvertView.findViewById(R.id.iv_icelv_reduce);
            childViewHolder.childAdd = (ImageView) childConvertView.findViewById(R.id.iv_icelv_add);
            childViewHolder.childAccount = (EditText) childConvertView.findViewById(R.id.et_icelv_account);
            childViewHolder.childDelete = (TextView) childConvertView.findViewById(R.id.tv_icelv_delete);
            childViewHolder.childUnEdit = (LinearLayout) childConvertView.findViewById(R.id.ll_icelv_unedit);
            childViewHolder.childEdit = (RelativeLayout) childConvertView.findViewById(R.id.rl_icelv_edit);
            childViewHolder.childTvAccount = (TextView) childConvertView.findViewById(R.id.tv_icelv_account);
            childViewHolder.childPrice = (TextView) childConvertView.findViewById(R.id.tv_icelv_price);

            arrayHolder.put(position, childViewHolder);
        }
        GoodsDto goods = arrayGoods.get(position);
        boolean isEdit = mList.get(groupPosition).isEdit();
        if (isEdit){
            childViewHolder.childEdit.setVisibility(View.VISIBLE);
            childViewHolder.childUnEdit.setVisibility(View.GONE);
        }else {
            childViewHolder.childUnEdit.setVisibility(View.VISIBLE);
            childViewHolder.childEdit.setVisibility(View.GONE);
        }
        childViewHolder.childSelect.setChecked(goods.isSelected());
        childViewHolder.childImage.setBackgroundResource(goods.getGoodsHeadImage());
        childViewHolder.childName.setText(goods.getGoodsName());
        childViewHolder.childAccount.setText(CommonUtils.toString(goods.getGoodsCount()));
        childViewHolder.childPrice.setText("价格：" + CommonUtils.toString(goods.getGoodsPrice()));
        childViewHolder.childTvAccount.setText("数量：" + CommonUtils.toString(goods.getGoodsCount()) + "件");
        childViewHolder.childAccount.addTextChangedListener(new editGoodsAccount(goods));
        childViewHolder.childSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChildChecked = ((CheckBox) view).isChecked();

                arrayGoods.get(position).setIsSelected(isChildChecked);
                checkGoodsInterface.checkChild(groupPosition, childPosition, isChildChecked);
            }
        });

        //删除这个商品
        childViewHolder.childDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示！");
                dialog.setMessage("您确定要将这些商品从购物车中移除吗？");
                dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        modifyGoodsCountInterface.deleteGoods(position);

                    }
                });
                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
                dialog.create();
                dialog.show();
            }
        });

        childViewHolder.childAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null != modifyGoodsCountInterface) {
                    modifyGoodsCountInterface.addGoods(arrayGoods.get(position));
                }
            }
        });

        childViewHolder.childReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modifyGoodsCountInterface.reduceGoods(arrayGoods.get(position));
            }
        });

        return childConvertView;
    }

    //允许子item点击事件
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * 点击编辑按钮，改变子项目里面的布局
     */
    public class OnEditChildViewListener implements View.OnClickListener {
        private TextView edit;

        private ShopDto shop;

        public OnEditChildViewListener(TextView edit,ShopDto shop) {

            this.edit = edit;
            this.shop = shop;
        }

        @Override
        public void onClick(View view) {
            int gruopId = view.getId();
            if (gruopId == edit.getId()){
                if (shop.isEdit()){
                    shop.setIsEdit(false);
                }else{
                    shop.setIsEdit(true);
                }
            }
            notifyDataSetChanged();
        }
    }


    /**
     * 包含删除，增加，减少商品的接口
     */
    public interface ModifyGoodsCountInterface{

        /**
         * 删除商品
         * @param position
         */
        public void deleteGoods(String position);

        /**
         * 增加商品
         * @param goods
         */
        public void addGoods(GoodsDto goods);

        /**
         * 减少商品
         * @param goods
         */
        public void reduceGoods(GoodsDto goods);
    }

    /**
     * 修改商品数量
     */
    private class editGoodsAccount implements TextWatcher {

        private GoodsDto goods;

        public editGoodsAccount(GoodsDto goods) {

            this.goods = goods;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String account = editable.toString();
            if (!account.isEmpty()){
                goods.setGoodsCount(Integer.parseInt(account));
            }
        }
    }

    /**
     * 商品或店铺的选中接口
     */
    public interface CheckGoodsInterface{

        /**
         * 店铺的checkBox
         * @param groupPosition
         * @param isChecked
         */
        public void checkGroup(int groupPosition,boolean isChecked);

        /**
         * 商品的checkBox
         * @param groupPosition
         * @param childPosition
         * @param isChecked
         */
        public  void checkChild(int groupPosition,int childPosition,boolean isChecked);
    }
}
