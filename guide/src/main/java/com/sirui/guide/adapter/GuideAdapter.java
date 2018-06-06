package com.sirui.guide.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * @author hw
 *         Created on 2017/8/29.
 */

public class GuideAdapter extends PagerAdapter {

    private ArrayList<ImageView> viewLists;

    public GuideAdapter(ArrayList<ImageView> list) {
        viewLists = list;
    }

    @Override
    public int getCount() {
        if (null != viewLists && viewLists.size() > 0)
            return viewLists.size();
        else
            return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /***
     * 初始化Item
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = viewLists.get(position);
        container.addView(view);
        return view;
    }

    /**
     * 销毁Item
     **/
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
