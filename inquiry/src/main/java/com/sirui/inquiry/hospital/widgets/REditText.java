package com.sirui.inquiry.hospital.widgets;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;

import com.sirui.basiclib.utils.Utils;
import com.sirui.inquiry.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿微博话题输入控件
 *
 * @author Ruffian
 */
public class REditText extends AppCompatEditText {

    /**
     * 开发者可设置内容
     */
    private int mBlackColor = Utils.getContext().getResources().getColor(R.color.black_333333);
    private int mWhiteColor = Color.WHITE;// 话题文本高亮颜色
    private int mBlueColor = Utils.getContext().getResources().getColor(R.color.color_069a9c);// 话题背景高亮颜色
    private List<RObject> mRObjectsList = new ArrayList<>();// object集合
    //删除
    private String mLastTag;
    private int selectionStart;
    private int selectionEnd;

    public REditText(Context context) {
        this(context, null);
    }

    public REditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 初始化设置
        initView();
    }

    /**
     * 监听光标的位置,若光标处于话题内容中间则移动光标到话题结束位置
     */
    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (mRObjectsList == null || mRObjectsList.size() == 0) {
            return;
        }

        int startPosition = 0;
        int endPosition = 0;
        String tag = "";
        for (int i = 0; i < mRObjectsList.size(); i++) {
            tag = mRObjectsList.get(i).toString();// 文本
            startPosition = getText().toString().indexOf(tag);// 获取文本开始下标
            endPosition = startPosition + tag.length();
            if (startPosition != -1 && selStart > startPosition
                    && selStart <= endPosition) {// 若光标处于话题内容中间则移动光标到话题结束位置
                setSelection(endPosition);
            }
        }

    }

    /**
     * 初始化控件,一些监听
     */
    private void initView() {

        /**
         * 输入框内容变化监听<br/>
         * 1.当文字内容产生变化的时候实时更新UI
         */
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文字改变刷新UI
                refreshEditTextUI(s.toString());
            }
        });

    }

    /**
     * 监听删除键 <br/>
     * 1.光标在话题后面,将整个话题内容删除 <br/>
     * 2.光标在普通文字后面,删除一个字符
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            int selectionStart = this.selectionStart == 0 ? getSelectionStart() : this.selectionStart;
            int selectionEnd = this.selectionEnd == 0 ? getSelectionEnd() : this.selectionEnd;

            /**
             * 如果光标起始和结束不在同一位置,删除文本
             */
            if (selectionStart != selectionEnd) {
                // 查询文本是否属于目标对象,若是移除列表数据
                String tagetText;
                try {
                    tagetText = getText().toString().substring(
                            selectionStart, selectionEnd);
                } catch (Exception e) {
                    restoreLastTag();
                    setSelection(0, 0);
                    return true;
                }
                for (int i = 0; i < mRObjectsList.size(); i++) {
                    RObject object = mRObjectsList.get(i);
                    if (tagetText.equals(object.toString())) {
                        String content = getText().toString().replace(tagetText, "");
                        setText(content);
                        setSelection(selectionStart);
                        object.getDisease().setSelected(false);
                        mRObjectsList.remove(object);
                        this.selectionStart = 0;
                        this.selectionEnd = 0;
                        break;
                    }
                }
                return false;
            }

            int lastPos = 0;
            Editable editable = getText();
            // 遍历判断光标的位置
            for (int i = 0; i < mRObjectsList.size(); i++) {
                String tag = mRObjectsList.get(i).toString();

                lastPos = getText().toString().indexOf(tag, 0);
                if (lastPos != -1) {
                    if (selectionStart > lastPos
                            && selectionStart <= (lastPos + tag
                            .length())) {
                        // 选中话题
                        setSelection(lastPos,
                                lastPos + tag.length());

                        this.selectionStart = lastPos;
                        this.selectionEnd = lastPos + tag.length();

                        // 设置背景色
                        editable.setSpan(new BackgroundColorSpan(
                                        mBlueColor), lastPos, lastPos
                                        + tag.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editable.setSpan(new ForegroundColorSpan(
                                        mWhiteColor), lastPos, lastPos
                                        + tag.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mLastTag = tag;
                        return true;
                    }
                }
            }
        } else {
            restoreLastTag();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        restoreLastTag();
        return super.onTouchEvent(event);
    }

    private void restoreLastTag() {
        if (mLastTag == null)
            return;

        int lastPos = 0;
        this.selectionEnd = 0;
        this.selectionStart = 0;
        int selectionStart = getSelectionStart();
        Editable editable = getText();

        String tag = mLastTag;

        lastPos = getText().toString().indexOf(tag, 0);
        if (lastPos != -1) {
            if (selectionStart >= lastPos
                    && selectionStart <= (lastPos + tag
                    .length())) {
                // 设置背景色
                editable.setSpan(new BackgroundColorSpan(Color.TRANSPARENT)
                        , lastPos
                        , lastPos + tag.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editable.setSpan(new ForegroundColorSpan(
                                mBlackColor), lastPos, lastPos
                                + tag.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mLastTag = null;
            }
        }
    }

    /**
     * EditText内容修改之后刷新UI
     *
     */
    private void refreshEditTextUI(String content) {
        /**
         * 内容变化时操作<br/>
         * 1.查找匹配所有话题内容 <br/>
         * 2.设置话题内容特殊颜色
         */

        if (mRObjectsList.size() == 0)
            return;

        if (TextUtils.isEmpty(content)) {
            mRObjectsList.clear();
            return;
        }

        /**
         * 重新设置span
         */
        Editable editable = getText();
        int findPosition = 0;
        for (int i = 0; i < mRObjectsList.size(); i++) {
            final RObject object = mRObjectsList.get(i);
            String tag = object.toString();// 文本

            if (tag.equals(mLastTag))
                continue;

            findPosition = content.indexOf(tag);// 获取文本开始下标

            if (findPosition != -1) {// 设置话题内容前景色高亮
                editable.setSpan(new ForegroundColorSpan(
                                mBlackColor), findPosition, findPosition
                                + tag.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

    }

    /**
     * 插入/设置话题
     *
     */
    public void add(RObject object) {

        if (object == null)
            return;

        String objectRule = object.getObjectRule();
        String objectText = object.getObjectText();
        if (TextUtils.isEmpty(objectText) || TextUtils.isEmpty(objectRule))
            return;

        restoreLastTag();

        /**
         * 添加话题<br/>
         * 1.将话题内容添加到数据集合中<br/>
         * 2.将话题内容添加到EditText中展示
         */

        /**
         * 1.添加话题内容到数据集合
         */
        mRObjectsList.add(object);

        /**
         * 2.将话题内容添加到EditText中展示
         */
        int selectionStart = getSelectionStart();// 光标位置
        Editable editable = getText();// 原先内容

        // 拼接字符# %s #
        String tag = object.toString();
        if (selectionStart >= 0) {
            editable.insert(selectionStart, tag);// 在光标位置插入内容
//            editable.insert(getSelectionStart(), " ");// 话题后面插入空格,至关重要
            setSelection(getSelectionStart());// 移动光标到添加的内容后面
        }

    }

    /**
     * 获取object列表数据
     */
    public List<RObject> getObjects() {
        return mRObjectsList;
    }

    /**
     * 获取object列表数据
     */
    public boolean contains(RObject rObject) {
        return getText().toString().contains(rObject.toString());
    }

    /**
     * 删除文本[删除最后匹配]
     *
     * @param rObject
     */
    public void remove(RObject rObject) {
        if (rObject == null)
            return;

        restoreLastTag();

        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        int pos = builder.toString().indexOf(rObject.toString());
        if (pos > -1) {
            try {
                mRObjectsList.remove(rObject);
                builder = builder.delete(pos, pos + rObject.toString().length());
                setText(builder);
                setSelection(pos);
            } catch (Exception ignored) {
            }
        }
    }

}
