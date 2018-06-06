package com.sirui.inquiry.hospital.chat.viewholder;

import android.graphics.Color;
import android.widget.TextView;

import com.sirui.basiclib.utils.ConvertUtil;
import com.sirui.inquiry.R;


/**
 * Created by xiepc on 2017/3/14 20:39
 */

public class MsgViewHolderText extends MsgViewHolderBase {

    @Override
    protected void inflateContentView() {

    }

    @Override
    protected void bindContentView() {
        layoutDirection();

        TextView bodyTextView = findViewById(R.id.messageItemBodyText);
        bodyTextView.setTextColor(isReceivedMessage() ? bodyTextView.getContext().getResources().getColor(R.color.gray_666) : Color.WHITE);
//        bodyTextView.setOnClickListener(new View.OnClickListener() {    //暂时不需要点击响应
//            @Override
//            public void onClick(View v) {
//                 onItemClick();
//            }
//        });
        //  MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, getDisplayText(), ImageSpan.ALIGN_BOTTOM);   //表情，转换显示
        bodyTextView.setText(getDisplayText());  //暂时不需要表情，电话号码识别转换，直接显示出来..
        //bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected int getContentResId() {
        return R.layout.item_message_text;
    }


    private void layoutDirection() {
        TextView bodyTextView = findViewById(R.id.messageItemBodyText);
        if (isReceivedMessage()) {
            bodyTextView.setBackgroundResource(R.drawable.bg_message_left);
            bodyTextView.setPadding(ConvertUtil.dp2px(20), ConvertUtil.dp2px(8), ConvertUtil.dp2px(10), ConvertUtil.dp2px(8));
        } else {
            bodyTextView.setBackgroundResource(R.drawable.bg_message_right);
            bodyTextView.setPadding(ConvertUtil.dp2px(10), ConvertUtil.dp2px(8), ConvertUtil.dp2px(20), ConvertUtil.dp2px(8));
        }
    }

    protected String getDisplayText() {
        return message.getContent();
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
