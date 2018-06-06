package com.sirui.inquiry.hospital.chat.widget;

import android.text.TextUtils;
import android.view.View;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.inquiry.hospital.chat.adapter.MessageAdapter;
import com.sirui.inquiry.hospital.chat.client.IMChlient;
import com.sirui.inquiry.hospital.chat.client.NimHistoryMsgManager;
import com.sirui.inquiry.hospital.chat.constant.MsgDirectionEnum;
import com.sirui.inquiry.hospital.chat.constant.MsgStatusEnum;
import com.sirui.inquiry.hospital.chat.constant.MsgTypeEnum;
import com.sirui.inquiry.hospital.chat.model.BaseMessage;
import com.sirui.inquiry.hospital.chat.module.MessageProxyCallback;
import com.sirui.inquiry.hospital.chat.util.ListViewUtil;
import com.sirui.inquiry.hospital.chat.viewholder.MsgViewHolderBase;

import java.util.ArrayList;
import java.util.List;


/**
 * 消息接收发送列表显示
 * Created by xiepc on 2017/3/27 19:41
 */

public class MessageActionListPanel extends MessageListPanel implements MessageProxyCallback {

//    /**一个时间基准点，早于该时间的消息，不显示*/
//    private long anchorTime;


    public MessageActionListPanel(View rootView) {
        super(rootView);
        initAction();
    }

    private void initAction() {
        adapter.setEventListener(new MsgViewHolderEvertListener());
    }


    @Override
    public void onMessageSend(BaseMessage message) {
        MyLog.i("消息发送--" + message.getContent());
        if (message.getMsgType() != MsgTypeEnum.TXT) {  //暂时只支持文字消息 <------------------------------------------------
            return;
        }
        // add to listView and refresh
        items.add(message);
        List<BaseMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);
        adapter.updateShowTimeItem(addedListItems, false, true);
        adapter.notifyDataSetChanged();
        ListViewUtil.scrollToBottom(messageListView);
    }

    @Override
    public void onMessageStatusChange(BaseMessage message) {
      //  MyLog.i("消息状态改变--" + message.getContent() + "状态:" + message.getStatus().getValue());
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            BaseMessage item = items.get(index);
            item.setStatus(message.getStatus());
            // item.set(message.getAttachStatus());
//            if (item.getAttachment() instanceof AVChatAttachment
//                    || item.getAttachment() instanceof AudioAttachment) {
//                item.setAttachment(message.getAttachment());
//            }
            refreshViewHolderByIndex(index);
        }
    }

    @Override
    public void onMessageIncoming(List<BaseMessage> messages) {
        MyLog.i("收到新消息--" + messages.get(0).getContent());
        loadMessages(messages,false);//不需要刷新加载消息,在后面添加
    }


    @Override
    public void sendMsgReceipt() {
        BaseMessage message = getLastReceivedMessage();
        if (!sendReceiptCheck(message)) { //过滤掉一些不需要显示已读的消息
            return;
        }
        IMChlient.getInstance().sendMsgReceipt(message);
    }

    @Override
    public void receiveReceipt() { //已读回执接收
        updateReceipt(items);
        refreshListView();
    }

    /**
     * 刷新单条消息
     * @param index
     */
    protected void refreshViewHolderByIndex(final int index) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (index < 0) {
                    return;
                }
                Object tag = ListViewUtil.getViewHolderByIndex(messageListView, index);
                if (tag instanceof MsgViewHolderBase) {
                    MsgViewHolderBase viewHolder = (MsgViewHolderBase) tag;
                    viewHolder.refreshCurrentItem();
                }
            }
        });
    }

    /**
     * 通过uuid找到该消息在列表中的位置
     *
     * @param uuid
     * @return
     */
    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            BaseMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }
        return -1;
    }

    class MsgViewHolderEvertListener implements MessageAdapter.ViewHolderEventListener {

        @Override
        public boolean onViewHolderLongClick(View clickView, View viewHolderView, BaseMessage item) {
            return false;
        }

        @Override
        public void onFailedBtnClick(BaseMessage resendMessage) {
            MyLog.i("消息重发点击");
            resendMessage(resendMessage);
        }
    }

    // 重发消息到服务器
    private void resendMessage(BaseMessage message) {
        // 重置状态为unsent
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            BaseMessage item = items.get(index);
            item.setStatus(MsgStatusEnum.sending);
            deleteItem(item, true);
            onMessageSend(message);
        }
        IMChlient.getInstance().sendTextMessage(message,true);
    }

    // 删除消息
    private void deleteItem(BaseMessage messageItem, boolean isRelocateTime) {
        IMChlient.getInstance().deleteChattingHistory(messageItem);
        List<BaseMessage> messages = new ArrayList<>();
        for (BaseMessage message : items) {
            if (message.getUuid().equals(messageItem.getUuid())) {
                continue;
            }
            messages.add(message);
        }
        updateReceipt(messages);
        adapter.deleteItem(messageItem, isRelocateTime);
    }

    /**
     * 消息已读更新显示
     */
    public void updateReceipt(final List<BaseMessage> messages) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (receiveReceiptCheck(messages.get(i))) {
                adapter.setUuid(messages.get(i).getUuid());
                break;
            }
        }
    }

    private boolean receiveReceiptCheck(BaseMessage baseMessage) {
        if(baseMessage.getMsgDirection() != MsgDirectionEnum.Out){
            return false;
        }
        return baseMessage.getMsgType() == MsgTypeEnum.TXT || baseMessage.getMsgType() == MsgTypeEnum.IMG;
    }

    private BaseMessage getLastReceivedMessage() {
        BaseMessage lastMessage = null;
        for (int i = items.size() - 1; i >= 0; i--) {
            if (sendReceiptCheck(items.get(i))) {
                lastMessage = items.get(i);
                break;
            }
        }
        return lastMessage;
    }

    private boolean sendReceiptCheck(final BaseMessage msg) {
        return !(msg == null || msg.getMsgDirection() != MsgDirectionEnum.In || msg.getMsgType() == MsgTypeEnum.NOTICE);
    }

    private BaseMessage getLastAVChatMessage(){
        BaseMessage lastMessage = null;
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).getMsgType() == MsgTypeEnum.AVCHAT) {
                lastMessage = items.get(i);
                break;
            }
        }
        return lastMessage;
    }

    public void qureyAVChatMsg(){
        BaseMessage laseAVChatMsg = getLastAVChatMessage();
        if(laseAVChatMsg == null){
            return;
        }
        List<String> uuids = new ArrayList<>();
        uuids.add(laseAVChatMsg.getUuid());
        NimHistoryMsgManager.getInstance().qureyMessageByUuid(uuids, new NimHistoryMsgManager.onQueryMessageCallback() {
            @Override
            public void onQueryListener(List<IMMessage> param) {
                if(param != null && param.size() > 0){
                    MyLog.i("查询消息数量："+ param.size());
                    updateLastAVChatMsg(param.get(param.size()-1));
                }
            }
            @Override
            public void onFailed(int code) {
                MyLog.i("查询消息失败：code = "+ code);
            }
        });
    }
    private void updateLastAVChatMsg(IMMessage imMessage){
        BaseMessage laseAVChatMsg = getLastAVChatMessage();
        laseAVChatMsg.setBindingMsg(imMessage);
        refreshViewHolderByIndex(getItemIndex(laseAVChatMsg.getUuid()));
    }
}
