package com.sirui.inquiry.hospital.chat.adapter;


/**
 * Created by xiepc on 2017/3/14 18:44
 */

public interface TAdapterDelegate {

    int getViewTypeCount();

    Class<? extends TViewHolder> viewHolderAtPosition(int position);
}
