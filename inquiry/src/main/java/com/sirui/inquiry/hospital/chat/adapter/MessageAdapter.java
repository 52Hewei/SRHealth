package com.sirui.inquiry.hospital.chat.adapter;

import android.content.Context;
import android.view.View;

import com.sirui.inquiry.hospital.chat.model.BaseMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 消息显示适配器
 * Created by xiepc on 2017/3/15 9:57
 */

public class MessageAdapter extends TAdapter<BaseMessage> {
    /**列表item点击事件监听*/
    private ViewHolderEventListener eventListener;
    /**
     * 需要显示已读回执的消息id
     */
    private String messageId;

    public MessageAdapter(Context context, List items, TAdapterDelegate delegate) {
        super(context, items, delegate);
        timedItems = new HashSet<>();
    }


    /*********************** 时间显示处理 ****************************/

    private Set<String> timedItems; // 需要显示消息时间的消息ID
    private BaseMessage lastShowTimeItem; // 用于消息时间显示,判断和上条消息间的时间间隔

    public boolean needShowTime(BaseMessage message) {
        return timedItems.contains(message.getUuid());
    }

    public void setEventListener(ViewHolderEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public ViewHolderEventListener getEventListener() {
        return eventListener;
    }

    /**
     * 列表加入新消息时，更新时间显示
     */
    public void updateShowTimeItem(List<BaseMessage> items, boolean fromStart, boolean update) {
        BaseMessage anchor = fromStart ? null : lastShowTimeItem;
        for (BaseMessage message : items) {
            if (setShowTimeFlag(message, anchor)) {
                anchor = message;
            }
        }
        if (update) {
            lastShowTimeItem = anchor;
        }
    }

    /**
     * 是否显示时间item
     */
    private boolean setShowTimeFlag(BaseMessage message, BaseMessage anchor) {
        boolean update = false;
//        if (hideTimeAlways(message)) {
//            setShowTime(message, false);
//        } else {
        if (anchor == null) {
            setShowTime(message, true);
            update = true;
        } else {
            long time = anchor.getSendtime();
            long now = message.getSendtime();

            if (now - time == 0) {
                // 消息撤回时使用
                setShowTime(message, true);
                lastShowTimeItem = message;
                update = true;
            } else if (now - time < (long) (5 * 60 * 1000)) {
                setShowTime(message, false);
            } else {
                setShowTime(message, true);
                update = true;
            }
//            }
        }

        return update;
    }

    private void setShowTime(BaseMessage message, boolean show) {
        if (show) {
            timedItems.add(message.getUuid());
        } else {
            timedItems.remove(message.getUuid());
        }
    }

    public void deleteItem(BaseMessage message, boolean isRelocateTime) {
        if (message == null) {
            return;
        }
        int index = 0;
        for (BaseMessage item : getItems()) {
            if (item.isTheSame(message)) {
                break;
            }
            ++index;
        }
        if (index < getCount()) {
            getItems().remove(index);
            if (isRelocateTime) {
                relocateShowTimeItemAfterDelete(message, index);
            }
            notifyDataSetChanged();
        }
    }

    private void relocateShowTimeItemAfterDelete(BaseMessage messageItem, int index) {
        // 如果被删的项显示了时间，需要继承
        if (needShowTime(messageItem)) {
            setShowTime(messageItem, false);
            if (getCount() > 0) {
                BaseMessage nextItem;
                if (index == getCount()) {
                    //删除的是最后一项
                    nextItem = getItem(index - 1);
                } else {
                    //删除的不是最后一项
                    nextItem = getItem(index);
                }
                // 增加其他不需要显示时间的消息类型判断
                if (hideTimeAlways(nextItem)) {
                    setShowTime(nextItem, false);
                    if (lastShowTimeItem != null && lastShowTimeItem != null && lastShowTimeItem.isTheSame(messageItem)) {
                        lastShowTimeItem = null;
                        for (int i = getCount() - 1; i >= 0; i--) {
                            BaseMessage item = getItem(i);
                            if (needShowTime(item)) {
                                lastShowTimeItem = item;
                                break;
                            }
                        }
                    }
                } else {
                    setShowTime(nextItem, true);
                    if (lastShowTimeItem == null || (lastShowTimeItem != null && lastShowTimeItem.isTheSame(messageItem))) {
                        lastShowTimeItem = nextItem;
                    }
                }
            } else {
                lastShowTimeItem = null;
            }
        }
    }

    private boolean hideTimeAlways(BaseMessage message) {
//        if (message.getMsgType() == SessionTypeEnum.ChatRoom) {
//            return true;
//        }
//        switch (message.getMsgType()) {
//            case notification:
//                return true;
//            default:
//                return false;
//        }
        return false;
    }

    public void setUuid(String messageId) {
        this.messageId = messageId;
    }

    public String getUuid() {
        return messageId;
    }

    public interface ViewHolderEventListener {
        // 长按事件响应处理
        boolean onViewHolderLongClick(View clickView, View viewHolderView, BaseMessage item);

        // 发送失败或者多媒体文件下载失败指示按钮点击响应处理
        void onFailedBtnClick(BaseMessage resendMessage);
    }
}
