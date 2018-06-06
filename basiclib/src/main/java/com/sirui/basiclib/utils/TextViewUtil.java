package com.sirui.basiclib.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * Created by xiepc on 2018/4/25 14:35
 */
public class TextViewUtil {

    /**
     * SpannableString spannableString = new SpannableString("jakjfkajfjaj");
     //设置颜色
     spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FE6026")), 3, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
     //设置字体大小，true表示前面的字体大小20单位为dip
     spannableString.setSpan(new AbsoluteSizeSpan(20, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
     //设置链接
     spannableString.setSpan(new URLSpan("www.baidu.com"), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
     //设置字体，BOLD为粗体
     spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


      //采用Html的方式改变字体颜色
      //首先是拼接字符串
     String content = "<font color=\"#FE6026\">" + data + "</font>"
     //然后直接setText()
     TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
     tvContent.setText(Html.fromHtml(content));

     *
     * @param all
     * @param target
     * @param what
     * @return
     */

    public static SpannableString getSpannableString(CharSequence all, CharSequence target, Object what) {
        SpannableString result = new SpannableString(all);
        String allStr = all.toString();
        String targetStr = target.toString();
        int index = allStr.indexOf(targetStr);
        if (-1 == index) {
            return result;
        }
        result.setSpan(what,index, index + targetStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return result;
    }

    public static SpannableString getColorSannableString(CharSequence all, CharSequence target,int color){
        return getSpannableString(all,target,new ForegroundColorSpan(color));
    }
}
