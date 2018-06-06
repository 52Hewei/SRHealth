package com.sirui.inquiry.hospital.chat.widget;

import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.chat.adapter.MessageAdapter;
import com.sirui.inquiry.hospital.chat.adapter.TAdapterDelegate;
import com.sirui.inquiry.hospital.chat.adapter.TViewHolder;
import com.sirui.inquiry.hospital.chat.model.BaseMessage;
import com.sirui.inquiry.hospital.chat.util.ListViewUtil;
import com.sirui.inquiry.hospital.chat.viewholder.MsgViewHolderFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * 显示消息的列表面板
 * Created by xiepc on 2017/3/15 9:50
 */

public class MessageListPanel implements TAdapterDelegate {

    View rootView;
    protected MessageAdapter adapter;
    protected ListView messageListView;
    protected List<BaseMessage> items;
    protected Handler uiHandler;

    public MessageListPanel(View rootView){
        this.rootView = rootView;
        init();
    }

    private void init() {
        this.uiHandler = new Handler();
        initListView();
        //loadHistroyMessage();
    }

    private void initListView(){
        items = new ArrayList<>();
        adapter = new MessageAdapter(rootView.getContext(),items, this);
        messageListView = (ListView) rootView.findViewById(R.id.messageListView);
        messageListView.requestDisallowInterceptTouchEvent(true);
        messageListView.setAdapter(adapter);
    }

    public void addHeaderView(View view){
        messageListView.addHeaderView(view);
    }
    public void removeHeaderView(View view){
        messageListView.removeHeaderView(view);
    }
    public void addFooterView(View view){
        messageListView.addFooterView(view);
        scrollToBottom();
    }
    public void removeFooterView(View view){
        messageListView.removeFooterView(view);
    }
//
//    private void loadHistroyMessage(){
//
//        MessageManager.loadHistroyMessage(recordAskInfo.getOrderNo(), new MessageManager.MessageLoadListener() {
//            @Override
//            public void onLoad(List<BaseMessage> messages) {
//                items.clear();
//                items.addAll(messages);
//                refreshListView();
//                adapter.updateShowTimeItem(messages,true,true);
//            }
//        });
//    }

    public void loadMessages(List<BaseMessage> messages,boolean refresh){
            if(refresh){ //如果是刷新数据
                items.clear();
            }
            items.addAll(messages);
            adapter.updateShowTimeItem(messages,false,true);
            refreshListView();
            ListViewUtil.scrollToBottom(messageListView);
    }
//       items.add(message);
//    List<BaseMessage> addedListItems = new ArrayList<>(1);
//        addedListItems.add(message);
//        adapter.updateShowTimeItem(addedListItems, false, true);
//        adapter.notifyDataSetChanged();
//        ListViewUtil.scrollToBottom(messageListView);

    /**供外部提供数据调用*/
    public List<BaseMessage> getItems(){
        return items;
    }

    public void refreshListView(){
        adapter.notifyDataSetChanged();
    }

    public void scrollToBottom() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListViewUtil.scrollToBottom(messageListView);
            }
        }, 200);
    }
    public void scrollToItem(final int position) {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListViewUtil.scrollToPosition(messageListView, position, 0);
            }
        }, 200);
    }


    @Override
    public int getViewTypeCount() {  //暂时只有文字聊天类型
        return MsgViewHolderFactory.getViewTypeCount();
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return MsgViewHolderFactory.getViewHolderByType(items.get(position));   //暂时只有文字聊天类型
    }
}
