package com.sirui.inquiry.hospital.ui.module.loadmore.interfaces;


import com.sirui.inquiry.hospital.ui.module.loadmore.ViewHolder;

/**
 * Author: Othershe
 * Time: 2016/8/29 10:48
 */
public interface OnItemClickListener<T> {
    void onItemClick(ViewHolder viewHolder, T data, int position);
}
