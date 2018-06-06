package com.sirui.inquiry.hospital.chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiepc on 2017/3/14 17:01
 */

public class TAdapter<T> extends BaseAdapter{

    protected final Context context;

    private final List<T> items;

    private Object tag;

    private final LayoutInflater inflater;

    private final Map<Class<?>, Integer> viewTypes;

    private TAdapterDelegate delegate;

    public TAdapter(Context context, List<T> items, TAdapterDelegate delegate) {
        this.context = context;
        this.items = items;
        this.delegate = delegate;
        this.inflater = LayoutInflater.from(context);
        this.viewTypes = new HashMap<Class<?>, Integer>(getViewTypeCount());
    }
    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public T getItem(int position) {
        return position < getCount() ? items.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getItems() {
        return items;
    }

    @Override
    public int getViewTypeCount() {
        return delegate.getViewTypeCount();
    }


    @Override
    public int getItemViewType(int position) {
        if (getViewTypeCount() == 1) {
            return 0;
        }
        Class<?> clazz = delegate.viewHolderAtPosition(position);
        if (viewTypes.containsKey(clazz)) {
            return viewTypes.get(clazz);
        } else {
            int type = viewTypes.size();
            if (type < getViewTypeCount()) {
                viewTypes.put(clazz, type);
                return type;
            }
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
          return getView(position,convertView,parent,true);
    }

    public View getView(final int position, View convertView, ViewGroup parent, boolean needRefresh) {
        if (convertView == null) {
            convertView = viewAtPosition(position);
        }
        TViewHolder holder = (TViewHolder) convertView.getTag();
        holder.setPosition(position);
        if (needRefresh) {
            try {
                holder.refresh(getItem(position));
            } catch (RuntimeException e) {
                Log.e("TAdapter", "refresh viewholder error. " + e.getMessage());
            }
        }

        return convertView;
    }

    public View viewAtPosition(int position) {
        TViewHolder holder = null;
        View view = null;
        try {
            Class<?> viewHolder = delegate.viewHolderAtPosition(position);
            holder = (TViewHolder) viewHolder.newInstance();
            holder.setAdapter(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view = holder.getView(inflater);
        view.setTag(holder);
        holder.setContext(view.getContext());
        return view;
    }
}
