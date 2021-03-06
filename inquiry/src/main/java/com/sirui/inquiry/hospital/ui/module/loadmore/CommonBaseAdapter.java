package com.sirui.inquiry.hospital.ui.module.loadmore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sirui.inquiry.hospital.ui.module.loadmore.interfaces.OnItemChildClickListener;
import com.sirui.inquiry.hospital.ui.module.loadmore.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 单一数据类型的加载更多的适配器
 * Create by xiepc on 2017/8/14 9:59
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter<T> {
    private OnItemClickListener<T> mItemClickListener;

    private ArrayList<Integer> mItemChildIds = new ArrayList<>();
    private ArrayList<OnItemChildClickListener<T>> mItemChildListeners = new ArrayList<>();

    public CommonBaseAdapter(Context context, List<T> datas) {
        super(context, datas);
    }

    protected abstract void convert(ViewHolder holder, T data, int position);

    protected abstract int getItemLayoutId();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isCommonItemView(viewType)) {
            return ViewHolder.create(mContext, getItemLayoutId(), parent);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (isCommonItemView(viewType)) {
            bindCommonItem(holder, position);
        }
    }

    private void bindCommonItem(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        convert(viewHolder, mDatas.get(position), position);

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(viewHolder, mDatas.get(position), position);
                }
            }
        });

        for (int i = 0; i < mItemChildIds.size(); i++) {
            final int tempI = i;
            if (viewHolder.getConvertView().findViewById(mItemChildIds.get(i)) != null) {
                viewHolder.getConvertView().findViewById(mItemChildIds.get(i)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemChildListeners.get(tempI).onItemChildClick(viewHolder, mDatas.get(position), position);
                    }
                });
            }
        }
    }

    @Override
    protected int getViewType(int position, T data) {
        return TYPE_COMMON_VIEW;
    }

    public void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setOnItemChildClickListener(int viewId, OnItemChildClickListener<T> itemChildClickListener) {
        mItemChildIds.add(viewId);
        mItemChildListeners.add(itemChildClickListener);
    }

}
