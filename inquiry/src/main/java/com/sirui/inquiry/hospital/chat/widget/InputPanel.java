package com.sirui.inquiry.hospital.chat.widget;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.chat.module.Container;
import com.sirui.inquiry.hospital.util.string.StringUtil;


/**
 * 底部文本编辑发送模块
 * Created by xiepc on 2017/2/7 14:16
 */

public class InputPanel {
    private View view;
    private ViewGroup inputPanelContainer;
    private EditText sendMessageEdit;
    private TextView tvSendMessage;
    private boolean shiftKeyDwon;
    private Container container;

    public InputPanel(Container container, ViewGroup inputPanelContainer) {
        this.container = container;
        this.inputPanelContainer = inputPanelContainer;
        initView();
    }

    private void initView() {

        view = LayoutInflater.from(inputPanelContainer.getContext()).inflate(R.layout.layout_input_panel, null);
        sendMessageEdit = (EditText) view.findViewById(R.id.sendMessageEdit);
        tvSendMessage = (TextView) view.findViewById(R.id.tv_send_message);
        tvSendMessage.setOnClickListener(listener);
        sendMessageEdit.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int editEnd = sendMessageEdit.getSelectionEnd();
                String content = s.toString();
                if (TextUtils.isEmpty(content)) {
                    tvSendMessage.setEnabled(false);
                } else {
                    tvSendMessage.setEnabled(true);
                }
                sendMessageEdit.removeTextChangedListener(this);
                while (StringUtil.counterChars(s.toString()) > 5000 && editEnd > 0) {
                    s.delete(editEnd - 1, editEnd);
                    editEnd--;
                }
                sendMessageEdit.setSelection(editEnd);
                sendMessageEdit.addTextChangedListener(this);
            }
        });

        sendMessageEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_SHIFT_RIGHT == keyEvent.getKeyCode()) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        shiftKeyDwon = true;
                    }
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        sendMessageEdit.postDelayed(new Runnable() { //防止shift键比回车键先抬起来
                            @Override
                            public void run() {
                                shiftKeyDwon = false;
                            }
                        }, 500);
                    }
                    return false;
                }

                if (KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode()) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        if (shiftKeyDwon) { //换行
                            nextLine();
                        } else { //发送
                            onTextMessageSendButtonPressed();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        tvSendMessage.setEnabled(false);
        inputPanelContainer.addView(view);
    }

    // 发送文本消息
    private void onTextMessageSendButtonPressed() {
        String text = sendMessageEdit.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
      //  IMMessage textMessage = createTextMessage(text);
          container.proxy.sendTextMessage(text);
          sendMessageEdit.setText("");
//             restoreText(true);
    }

//    protected IMMessage createTextMessage(String text) {
//        return MessageBuilder.createTextMessage(container.account, SessionTypeEnum.P2P, text);
//    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tv_send_message) { //发送消息
                onTextMessageSendButtonPressed();
            }
        }
    };

    public void enableSendMsg(boolean enable) {
        if (!enable) {
            sendMessageEdit.setText("");
        }
        sendMessageEdit.setEnabled(enable);
    }

    private void nextLine() {
        if (sendMessageEdit.isEnabled()) {
            sendMessageEdit.append("\n");
        }
    }
}
