package com.sirui.basiclib.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sirui.basiclib.R;
import com.sirui.basiclib.utils.ConvertUtil;

/**
 * Created by xiepc on 2018/3/28 18:24
 */

public class BaseDialog extends Dialog {


    private TextView tvTitle;
    private TextView tvMsg;
    private LinearLayout llContent;

    private TextView btnPositive;
    private TextView btnNegative;
    private FrameLayout flCustom;

    private View btnDividing;
    private View.OnClickListener onDefaultClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            cancel();
        }

    };
    private View.OnClickListener onPositiveListener = onDefaultClickListener;
    private View.OnClickListener onNegativeListener = onDefaultClickListener;
    private String mTitle;
    private String mMessage;
    private String positiveText;
    private String negativeText;
    private boolean isNegativeBtnShow = true;
    private boolean isTitleShow = true;


    private View mView;
    private Context mContext;

    private BaseDialog(Context context) {
        super(context, R.style.Dialog);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        flCustom = (FrameLayout) findViewById(R.id.fl_custom_content);
        llContent = findViewById(R.id.ll_content);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        btnPositive = (TextView) findViewById(R.id.btn_positive);
        btnNegative = (TextView) findViewById(R.id.btn_negative);
        btnDividing =  findViewById(R.id.btn_dividing);
    }

    /**
     * 调用完Builder类的create()方法后显示该对话框的方法
     */
    @Override
    public void show() {
        super.show();
        show(this);
    }

    private void show(final BaseDialog mDialog) {
        if(!mDialog.isTitleShow){
            tvTitle.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llContent.getLayoutParams();
            layoutParams.height = ConvertUtil.dp2px(108);
            llContent.setLayoutParams(layoutParams);
        }else if(!TextUtils.isEmpty(mDialog.mTitle)){
            mDialog.tvTitle.setText(mDialog.mTitle);
        }
        if (mDialog.mView != null) {
            mDialog.flCustom.addView(mDialog.mView);
            mDialog.llContent.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(mDialog.mMessage)) {
                mDialog.tvMsg.setText(mDialog.mMessage);
                mDialog.llContent.setVisibility(View.VISIBLE);
            }
        }

        if (!mDialog.isNegativeBtnShow) {
            mDialog.btnNegative.setVisibility(View.GONE);
            mDialog.btnDividing.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mDialog.btnPositive
                    .getLayoutParams();
            layoutParams.setMargins(150, layoutParams.topMargin, 150, layoutParams.bottomMargin);
            mDialog.btnPositive.setLayoutParams(layoutParams);
        } else {
            mDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    if(mDialog.onNegativeListener != null){
                        mDialog.onNegativeListener.onClick(v);
                    }
                }
            });
            if (!TextUtils.isEmpty(mDialog.negativeText)) {
                mDialog.btnNegative.setText(mDialog.negativeText);
            }
        }
        mDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mDialog.dismiss();
                 if( mDialog.onPositiveListener != null){
                     mDialog.onPositiveListener.onClick(v);
                 }
            }
        });
        if (!TextUtils.isEmpty(mDialog.positiveText)) {
            mDialog.btnPositive.setText(mDialog.positiveText);
        }
    }

    public static class Builder {

        private BaseDialog mDialog;

        public Builder(Context context) {
            mDialog = new BaseDialog(context);
        }

        /**
         * 设置对话框标题
         *
         * @param title
         */
        public Builder setTitle(String title) {
            mDialog.mTitle = title;
            return this;
        }

        /**
         * 设置对话框文本内容,如果调用了setView()方法，该项失效
         *
         * @param msg
         */
        public Builder setMessage(String msg) {
            mDialog.mMessage = msg;
            return this;
        }

        /**
         * 设置确认按钮的回调
         *
         * @param onClickListener
         */
        public Builder setPositiveButton(View.OnClickListener onClickListener) {
            mDialog.onPositiveListener = onClickListener;
            return this;
        }

        /**
         * 设置确认按钮的回调
         *
         * @param btnText,onClickListener
         */
        public Builder setPositiveButton(String btnText, View.OnClickListener onClickListener) {
            mDialog.positiveText = btnText;
            mDialog.onPositiveListener = onClickListener;
            return this;
        }

        /**
         * 设置取消按钮的回掉
         *
         * @param onClickListener
         */
        public Builder setNegativeButton(View.OnClickListener onClickListener) {
            mDialog.onNegativeListener = onClickListener;
            return this;
        }

        /**
         * 设置取消按钮的回调
         *
         * @param btnText,onClickListener
         */
        public Builder setNegativeButton(String btnText, View.OnClickListener onClickListener) {
            mDialog.negativeText = btnText;
            mDialog.onNegativeListener = onClickListener;
            return this;
        }

        /**
         * 设置是否显示取消按钮，默认显示
         *
         * @param isNegativeBtnShow
         */
        public Builder setNegativeBtnShow(boolean isNegativeBtnShow) {
            mDialog.isNegativeBtnShow = isNegativeBtnShow;
            return this;
        }

        /**
         * 设置是否显示标题栏，默认显示
         *
         * @param isTitleShow
         */
        public Builder setTitleShow(boolean isTitleShow) {
            mDialog.isTitleShow = isTitleShow;
            return this;
        }

        /**
         * 设置自定义内容View
         *
         * @param view
         */
        public Builder setView(View view) {
            mDialog.mView = view;
            return this;
        }

        /**
         * 设置该对话框能否被Cancel掉，默认可以
         *
         * @param cancelable
         */
        public Builder setCancelable(boolean cancelable) {
            mDialog.setCancelable(cancelable);
            return this;
        }

        /**
         * 设置对话框被cancel对应的回调接口，cancel()方法被调用时才会回调该接口
         *
         * @param onCancelListener
         */
        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            mDialog.setOnCancelListener(onCancelListener);
            return this;
        }

        /**
         * 设置对话框消失对应的回调接口，一切对话框消失都会回调该接口
         *
         * @param onDismissListener
         */
        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            mDialog.setOnDismissListener(onDismissListener);
            return this;
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        public BaseDialog create() {
            return mDialog;
        }
    }
}
