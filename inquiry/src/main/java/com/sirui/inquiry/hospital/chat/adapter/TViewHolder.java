package com.sirui.inquiry.hospital.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by xiepc on 2017/3/14 18:45
 */

public abstract class TViewHolder {

    /**
     * context
     */
    protected Context context;

    /**
     * list item view
     */
    protected View view;

    /**
     * adapter providing data
     */
    protected TAdapter adapter;

    /**
     * index of item
     */
    protected int position;

    public boolean isFirstItem() {
        return position == 0;
    }

    public boolean isLastItem() {
        return position == adapter.getCount() - 1;
    }

    public View getView(LayoutInflater inflater) {
        int resId = getResId();
        view = inflater.inflate(resId, null);
        inflate();
        return view;
    }

    protected abstract int getResId();

    protected abstract void inflate();

    protected abstract void refresh(Object item);


    protected <T extends View> T findView(int resId) {
        return (T) (view.findViewById(resId));
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    protected TAdapter getAdapter() {
        return adapter;
    }

    protected void setAdapter(TAdapter adapter) {
        this.adapter = adapter;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
