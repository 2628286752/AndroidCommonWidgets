package com.hsj.example.androidcommonwidgets.xialakuang;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.hsj.example.androidcommonwidgets.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by huangsaijin on 2017/3/3.
 * 自定义下拉框
 */
public class CustomSpinner extends LinearLayout {

    private Context mContext;

    private TextView mTvShowText;

    private RecyclerView mRvShowData;

    private PopupWindow mPopWindowView;

    private RvPopAdapter mAdapter;

    private List<String> listData;

    public List<String> getBackList() {
        return backList;
    }

    public void setBackList(List<String> backList) {
        this.backList = backList;
    }

    private List<String> backList = new ArrayList<String>();

    public CustomSpinner(Context context) {
        super(context);

        initView(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = (LinearLayout) inflater.inflate(R.layout.layout_customspinner,this);
        mTvShowText = (TextView) layout.findViewById(R.id.tv_lcs_showtext);
        mTvShowText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopWindowView == null) {
                    initPopWindowView(layout);
                } else {
                    closePopWindow();
                }
            }
        });
    }

    private void initPopWindowView(View view){

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_customspinner_popwindow,null);
        mRvShowData = (RecyclerView) layout.findViewById(R.id.rv_lcsp_showdata);
        mRvShowData.setLayoutManager(new LinearLayoutManager(mContext));
        mRvShowData.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mAdapter = new RvPopAdapter(mContext,listData,backList);
        mRvShowData.setAdapter(mAdapter);

        mPopWindowView = new PopupWindow(layout, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopWindowView.setOutsideTouchable(true);
        mPopWindowView.showAsDropDown(view);
    }

    /**
    * 关闭下拉列表弹窗
    */
    private void closePopWindow(){
        mPopWindowView.dismiss();
        mPopWindowView = null;

        backList.clear();
        HashMap<String, Boolean> data = mAdapter.getMapIsSelected();
        Iterator<Map.Entry<String, Boolean>> iter = data.entrySet()
                .iterator();
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            String text = (String) entry.getKey();
            boolean isSelected = (Boolean) entry.getValue();
            if (isSelected) {
               backList.add(text);
            }
        }
    }




    /**
     * 获取数据
     * @param listsource
     * @param selected
     */
    public void setData(List<String> listsource,List<String> selected){
        listData = listsource;
        backList.clear();
        backList = selected;
        this.mTvShowText.setText("--请选择--");
    }

    public static class RvPopAdapter extends RecyclerView.Adapter<RvPopAdapter.HolderView>{

        private Context context;

        private List<String> list;

        private List<String> listselected;

        private HolderView  mHolder;

        public HashMap<String,Boolean> mapIsSelected;

        public HashMap<String, Boolean> getMapIsSelected() {
            return mapIsSelected;
        }

        public void setMapIsSelected(HashMap<String, Boolean> mapIsSelected) {
            this.mapIsSelected = mapIsSelected;
        }

        /**
         * ItemClick的回调接口
         */
        public interface OnItemClickListener{

            void onItemClick(int position);
        }

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener)
        {
            this.onItemClickListener = onItemClickListener;
        }

        class HolderView extends RecyclerView.ViewHolder{

            public TextView mTvData;

            public LinearLayout mLl;

            public CheckBox mCbSelected;

            public HolderView(View itemView) {
                super(itemView);
            }
        }

        public RvPopAdapter(Context context,List<String> listsource,List<String> listselected) {

            this.context = context;
            this.list = listsource;
            this.listselected = listselected;
            initHashMap();
        }

        private void initHashMap(){
            mapIsSelected = new HashMap<String,Boolean>();
            for(int i = 0;i < this.list.size();i++) {
                mapIsSelected.put(this.list.get(i), false);
            }

            if (this.listselected.size() > 0) {
                for (int i = 0; i < this.list.size(); i++) {
                    for (int j = 0; j < this.listselected.size(); j++) {
                        if (listselected.get(j).equals(list.get(i))){
                            mapIsSelected.put(listselected.get(j), true);
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public RvPopAdapter.HolderView onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = (LinearLayout) inflater.inflate(R.layout.item_customspinner_popwindow, null);
            mHolder = new HolderView(view);
            mHolder.mTvData = (TextView) view.findViewById(R.id.tv_icsp_showtext);
            mHolder.mLl = (LinearLayout) view.findViewById(R.id.ll_icsp);
            mHolder.mCbSelected = (CheckBox) view.findViewById(R.id.cb_icsp_selected);
            return mHolder;
        }

        @Override
        public void onBindViewHolder(RvPopAdapter.HolderView holder, final int position) {

            String text = list.get(position);
            holder.mTvData.setText(text);
            mHolder.mCbSelected.setChecked(getMapIsSelected().get(text)); // 根据isSelected来设置checkbox的选中状况
            mHolder.mCbSelected.setTag(text);

            holder.mCbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String text = (String) buttonView.getTag();
                    mapIsSelected.put(text, isChecked);
                }
            });

            if (onItemClickListener != null)
            {
                mHolder.mLl.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        onItemClickListener.onItemClick(position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (null == list){
                return 0;
            }
            return list.size();
        }
    }
}
