package com.sirui.inquiry.hospital.chat.viewholder;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.inquiry.hospital.chat.constant.MsgTypeEnum;
import com.sirui.inquiry.hospital.chat.model.BaseMessage;
import com.sirui.inquiry.hospital.chat.model.NoticeAttachment;

import java.util.HashMap;

/**
 * Created by xiepc on 2017/3/28 16:57
 */

public class MsgViewHolderFactory {

    private static HashMap<Class<? extends MsgAttachment>, Class<? extends MsgViewHolderBase>> viewHolders = new HashMap<>();

    public static void register(Class<? extends MsgAttachment> attach, Class<? extends MsgViewHolderBase> viewHolder) {
        viewHolders.put(attach, viewHolder);
    }

    static {
        register(NoticeAttachment.class, MsgViewHolderNotice.class);
    }

    public static Class<? extends MsgViewHolderBase> getViewHolderByType(BaseMessage message) {
        if (message.getMsgType() == MsgTypeEnum.TXT) {
            return MsgViewHolderText.class;
        } else if (message.getMsgType() == MsgTypeEnum.NOTICE) {
            MyLog.i("----返回MsgTypeEnum.NOTICE----");
            return MsgViewHolderNotice.class;
        } else if (message.getMsgType() == MsgTypeEnum.AVCHAT) {
           // return MsgViewHolderAVChat.class;
        } else {
            if (message.getBindingMsg() instanceof IMMessage) {
                IMMessage imMessage = (IMMessage) message.getBindingMsg();
                Class<? extends MsgViewHolderBase> viewHolder = null;
                if (imMessage.getAttachment() != null) {
                    Class<? extends MsgAttachment> clazz = imMessage.getAttachment().getClass();
                    while (viewHolder == null && clazz != null) {
                        viewHolder = viewHolders.get(clazz);
                        if (viewHolder == null) {
                            clazz = getSuperClass(clazz);
                        }
                    }
                }
                return viewHolder == null ? MsgViewHolderText.class : viewHolder;
            }
        }
        return null;
    }

    public static int getViewTypeCount() {
        // plus text and unknown
        return 3;
    }

    public static Class<? extends MsgAttachment> getSuperClass(Class<? extends MsgAttachment> derived) {
        Class sup = derived.getSuperclass();
        if (sup != null && MsgAttachment.class.isAssignableFrom(sup)) {
            return sup;
        } else {
            for (Class itf : derived.getInterfaces()) {
                if (MsgAttachment.class.isAssignableFrom(itf)) {
                    return itf;
                }
            }
        }
        return null;
    }
}
