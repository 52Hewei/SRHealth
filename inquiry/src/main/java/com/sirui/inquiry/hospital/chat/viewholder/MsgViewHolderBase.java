package com.sirui.inquiry.hospital.chat.viewholder;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.chat.adapter.MessageAdapter;
import com.sirui.inquiry.hospital.chat.adapter.TViewHolder;
import com.sirui.inquiry.hospital.chat.constant.MsgDirectionEnum;
import com.sirui.inquiry.hospital.chat.constant.MsgStatusEnum;
import com.sirui.inquiry.hospital.chat.model.BaseMessage;
import com.sirui.inquiry.hospital.util.TimeUtil;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by xiepc on 2017/3/14 18:59
 */

public abstract class MsgViewHolderBase extends TViewHolder {
    protected BaseMessage message;

    protected View alertButton;
    protected ProgressBar progressBar;
    protected TextView timeTextView;
    protected TextView nameTextView;
    protected TextView readReceiptTextView;
    protected FrameLayout contentContainer;
    protected LinearLayout nameContainer;

    private CircleImageView avatarLeft;
    private CircleImageView avatarRight;

    // 当是接收到的消息时，内容区域背景的drawable id
    protected int leftBackground() {
        return R.drawable.bg_message_left;
    }

    // 当是发送出去的消息时，内容区域背景的drawable id
    protected int rightBackground() {
        return R.drawable.bg_message_right;
    }

    // 在该接口中根据layout对各控件成员变量赋值
    abstract protected void inflateContentView();

    // 将消息数据项与内容的view进行绑定
    abstract protected void bindContentView();

    // 判断消息方向，是否是接收到的消息
    protected boolean isReceivedMessage() {
        return message.getMsgDirection() == MsgDirectionEnum.In;
    }
    // 是否显示头像，默认为显示
    protected boolean isShowHeadImage() {
        return true;
    }
    // 返回该消息是不是居中显示
    protected boolean isMiddleItem() {
        return false;
    }
    // 是否显示气泡背景，默认为显示
    protected boolean isShowBubble() {
        return true;
    }
    // 根据layout id查找对应的控件
    protected <T extends View> T findViewById(int id) {
        return (T)view.findViewById(id);
    }

    /// -- 以下是基类实现代码
    @Override
    protected final int getResId() {
        return R.layout.item_base_message;
    }
    /// -- 以下接口可由子类覆盖或实现
    // 返回具体消息类型内容展示区域的layout res id
    abstract protected int getContentResId();

    @Override
    protected final void inflate() {
        timeTextView = findViewById(R.id.messageItemTimeText);
        avatarLeft = findViewById(R.id.messageItemPortraitLeftImg);
        avatarRight = findViewById(R.id.messageItemPortraitRightImg);
        nameTextView = findViewById(R.id.messageItemNicknameText);
        contentContainer = findViewById(R.id.messageItemContent);
        alertButton = findViewById(R.id.message_item_alert);
        progressBar = findViewById(R.id.message_item_progress);
        readReceiptTextView = findView(R.id.textViewAlreadyRead);

        View.inflate(view.getContext(), getContentResId(), contentContainer);
        inflateContentView();
    }

    @Override
    protected final void refresh(Object item) {
          message = (BaseMessage) item;
          setHeadImageView();
//        setNameTextView();
          setTimeTextView();
          setStatus();
          setOnClickListener();
//        setLongClickListener();
          setContent();
          setReadReceipt();
          bindContentView();
    }

    public void refreshCurrentItem() {
        if (message != null) {
            refresh(message);
        }
    }
    private void setHeadImageView() {
        CircleImageView show = isReceivedMessage() ? avatarLeft : avatarRight;
        CircleImageView hide = isReceivedMessage() ? avatarRight : avatarLeft;
        hide.setVisibility(View.GONE);
        if (!isShowHeadImage()) {
            show.setVisibility(View.GONE);
            return;
        }
        if (isMiddleItem()) {
            show.setVisibility(View.GONE);
        } else {
            show.setVisibility(View.VISIBLE);
            if(isReceivedMessage()){
                show.setImageResource(R.drawable.icon_avatar_doctor);
            }else{
                show.setImageResource(R.drawable.icon_avatar_patient);
            }
            // show.loadBuddyAvatar(message.getFromAccount());  //暂时不需要用户头像管理
//            if(UserData.getInstance().getRole() == UserData.UserRole.PATIENT){
//                if(isReceivedMessage()){
//                    show.setImageResource(R.drawable.icon_avatar_doctor);
//                }else{
//                    show.setImageResource(R.drawable.icon_avatar_patient);
//                }
//            }else{
//                if(isReceivedMessage()){
//                    show.setImageResource(R.drawable.icon_avatar_patient);
//                }else{
//                    show.setImageResource(R.drawable.icon_avatar_doctor);
//                }
//            }
        }

    }

    private void setContent() {
        if (!isShowBubble() && !isMiddleItem()) {
            return;
        }
        LinearLayout bodyContainer = (LinearLayout) view.findViewById(R.id.messageItemBody);

//        // 调整container的位置
//        int index = isReceivedMessage() ? 0 : 3;
//        if (bodyContainer.getChildAt(index) != contentContainer) {
//            bodyContainer.removeView(contentContainer);
//            bodyContainer.addView(contentContainer, index);
//        }

        if (isMiddleItem()) {
             setGravity(bodyContainer, Gravity.CENTER);
        } else {
            if (isReceivedMessage()) {
                setGravity(bodyContainer, Gravity.LEFT);
                 contentContainer.setBackgroundResource(leftBackground());
            } else {
                setGravity(bodyContainer, Gravity.RIGHT);
                contentContainer.setBackgroundResource(rightBackground());
            }
        }
    }

    // 设置FrameLayout子控件的gravity参数
    protected final void setGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = gravity;
    }

    // 设置控件的长宽
    protected void setLayoutParams(int width, int height, View... views) {
        for (View view : views) {
            ViewGroup.LayoutParams maskParams = view.getLayoutParams();
            maskParams.width = width;
            maskParams.height = height;
            view.setLayoutParams(maskParams);
        }
    }
    /**
     * 设置时间显示
     */
    private void setTimeTextView() {
        if (getAdapter().needShowTime(message)) {
            timeTextView.setVisibility(View.VISIBLE);
        } else {
            timeTextView.setVisibility(View.GONE);
            return;
        }
        String text = TimeUtil.getTimeShowString(message.getSendtime(), false);
        timeTextView.setText(text);
    }

    /**
     * 设置消息发送状态
     */
    private void setStatus() {

        MsgStatusEnum status = message.getStatus();
        switch (status) {
            case fail:
                progressBar.setVisibility(View.GONE);
                alertButton.setVisibility(View.VISIBLE);
                break;
            case sending:
                progressBar.setVisibility(View.VISIBLE);
                alertButton.setVisibility(View.GONE);
                break;
            default:
                progressBar.setVisibility(View.GONE);
                alertButton.setVisibility(View.GONE);
                break;
        }
    }


    private void setOnClickListener() {
        // 重发/重收按钮响应事件
        if (getAdapter().getEventListener() != null) {
            alertButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getAdapter().getEventListener().onFailedBtnClick(message);
                }
            });
        }

        // 内容区域点击事件响应， 相当于点击了整项
        contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onItemClick();
            }
        });

//        // 头像点击事件响应
//        if (NimUIKit.getSessionListener() != null) {
//            View.OnClickListener portraitListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    NimUIKit.getSessionListener().onAvatarClicked(context, message);
//                }
//            };
//            avatarLeft.setOnClickListener(portraitListener);
//            avatarRight.setOnClickListener(portraitListener);
//        }
    }

    private void setReadReceipt() {
        if (!TextUtils.isEmpty(getAdapter().getUuid()) && message.getUuid().equals(getAdapter().getUuid())) {
            readReceiptTextView.setVisibility(View.VISIBLE);
        } else {
            readReceiptTextView.setVisibility(View.GONE);
        }
    }

    /// -- 以下接口可由子类调用
    // 获取MsgAdapter对象
    protected final MessageAdapter getAdapter() {
        return (MessageAdapter) adapter;
    }
}
