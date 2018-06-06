package com.sirui.basiclib.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;


/**
 * Created by xiepc on 2018/4/10 11:07
 */

public class FragmentUtil {

    public static Fragment switchContent(Activity context, int containerId, Fragment fragment) {
        return switchContent(context,containerId,fragment, false);
    }

    protected static Fragment switchContent(Activity context, int containerId, Fragment fragment, boolean needAddToBackStack) {
        FragmentManager fm = context.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        if (needAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        try {
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {

        }
        return fragment;
    }
}
