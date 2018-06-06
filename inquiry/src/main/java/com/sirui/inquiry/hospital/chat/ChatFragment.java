package com.sirui.inquiry.hospital.chat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.chat.constant.Extras;
import com.sirui.inquiry.hospital.chat.model.BaseMessage;
import com.sirui.inquiry.hospital.chat.module.Container;
import com.sirui.inquiry.hospital.chat.module.NimProxy;
import com.sirui.inquiry.hospital.chat.widget.InputPanel;
import com.sirui.inquiry.hospital.chat.widget.MessageActionListPanel;

import java.util.List;


/**
 * 文字聊天界面
 * Created by xiepc on 2017/3/17 13:53
 */

public class ChatFragment extends Fragment {

    private View view;
    private MessageActionListPanel messageActionListPanel;
    private InputPanel inputPanel;
    private String sessionId;
    private NimProxy nimProxy;
    private Container container;
    private boolean isAddFooter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat,null);
        initData();
        initView();
        nimProxy.registerObservers(true);
        return view;
    }

    private void initData(){
        sessionId = getArguments().getString(Extras.EXTRA_SESSIONID);
    }
    private void initView(){
        messageActionListPanel = new MessageActionListPanel(view);
        RelativeLayout inputPanelContainer = (RelativeLayout) view.findViewById(R.id.inputPanelContainer);
        nimProxy = new NimProxy(sessionId,messageActionListPanel,(PatientAVChatActivity)getActivity());
        container = new Container(getActivity(),sessionId,nimProxy);
        inputPanel = new InputPanel(container,inputPanelContainer);

//        sendNoticeBtn = (Button) view.findViewById(sendNoticeBtn);
//        sendNoticeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IMChlient.getInstance().sendNoticeMessage(sessionId,"这是一条系统通知消息");
//            }
//        });
    }

    public void addHeadView(View view){
        messageActionListPanel.addHeaderView(view);
    }
    public void removeHeaderView(View view){
        messageActionListPanel.removeHeaderView(view);
    }
    public void addFooterView(View view){
        if(!isAddFooter){
            isAddFooter = true;
            messageActionListPanel.addFooterView(view);
        }
    }
    public void removeFooterView(View view){
            isAddFooter = false;
            messageActionListPanel.removeFooterView(view);
    }

    public void enableSendMsg(boolean enable){
        inputPanel.enableSendMsg(enable);
    }
    @Override
    public void onDestroy() {
        nimProxy.registerObservers(false);
        super.onDestroy();
    }

    /**
     * 加载消息
     * @param messages
     * @param refresh 是否为重新加载新消息,清空加载所有消息,如果为追加则为false
     */
    public void loadMessages(List<BaseMessage> messages, boolean refresh){
         messageActionListPanel.loadMessages(messages,refresh);
    }

    public void updateAVChatMsg(){
         messageActionListPanel.qureyAVChatMsg();
    }
}
