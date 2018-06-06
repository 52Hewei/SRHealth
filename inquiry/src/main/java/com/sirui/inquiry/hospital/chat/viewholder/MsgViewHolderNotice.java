package com.sirui.inquiry.hospital.chat.viewholder;

import android.widget.TextView;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.chat.model.NoticeAttachment;

/**
 * 显示在中间的系统通知布局
 * 本布局只支持云信自定义消息
 * Created by xiepc on 2017/3/28 18:06
 */

public class MsgViewHolderNotice extends MsgViewHolderBase {

    // 返回该消息是不是居中显示
    protected boolean isMiddleItem() {
        return true;
    }
    @Override
    protected void inflateContentView() {

    }

    @Override
    protected void bindContentView() {
        TextView bodyTextView = findViewById(R.id.messageNoticeItemBodyText);
//        bodyTextView.setTextColor(isReceivedMessage() ? Color.BLACK : Color.WHITE);
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
        return R.layout.item_message_notice;
    }


    protected String getDisplayText() {
        if(message.getBindingMsg() instanceof IMMessage){
            IMMessage imMessage = (IMMessage) message.getBindingMsg();
            NoticeAttachment noticeAttachment = (NoticeAttachment) imMessage.getAttachment();
           return noticeAttachment.getContent();
        }
        return "";
    }
}
