package com.sirui.inquiry.hospital.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.net.client.HttpDialogListener;
import com.net.client.HttpGo;
import com.sirui.basiclib.http.JsonUtil;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.data.DataManager;
import com.sirui.basiclib.data.bean.User;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.widget.DialogUtil;
import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.chat.constant.InquiryTypeEnum;
import com.sirui.inquiry.hospital.config.InquiryNetUrl;
import com.sirui.inquiry.hospital.ui.model.InquiryRecordInfo;
import com.sirui.inquiry.hospital.util.TimeUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by xiepc on 2016/12/23 15:53
 */

public class RecordAskListAdapter extends BaseAdapter {

    private List<InquiryRecordInfo> list;
    private Context context;
    private AdapterCallback callback;

    private LayoutInflater inflater;

    public RecordAskListAdapter(Context context, List<InquiryRecordInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setCallback(AdapterCallback callback) {
        this.callback = callback;
    }

    public void setDoctorInfoList(List<InquiryRecordInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final InquiryRecordInfo info = (InquiryRecordInfo) list.get(position);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        }
        if (view == null || holder == null) {
            view = inflater.inflate(R.layout.item_list_record_ask, null);
            holder = new ViewHolder();
            holder.askNameText = (TextView) view.findViewById(R.id.askNameText);
            holder.timeText = (TextView) view.findViewById(R.id.timeText);
            holder.departmentText = (TextView) view.findViewById(R.id.departmentText);
            holder.askTypeText = (TextView) view.findViewById(R.id.askTypeText);
            holder.statusText = (TextView) view.findViewById(R.id.statusText);
//            holder.lookDetailBtn = (Button) view.findViewById(R.id.lookDetailBtn);
//            holder.prescriptionBtn = (Button) view.findViewById(R.id.prescriptionBtn);
            holder.recoverOrderBtn = (Button) view.findViewById(R.id.recoverOrderBtn);
//            holder.deleteBtn = (TextView) view.findViewById(R.id.deleteBtn);
            holder.listener = new AdapterOnClickListener();
//            holder.lookDetailBtn.setOnClickListener(holder.listener);
//            holder.prescriptionBtn.setOnClickListener(holder.listener);
            holder.recoverOrderBtn.setOnClickListener(holder.listener);
//            holder.deleteBtn.setOnClickListener(holder.listener);
            view.setTag(holder);
        }
        holder.listener.setRecordAskInfo(info);
        holder.askNameText.setText(info.getPatientName());
        holder.timeText.setText(parseTimeString(info.getQueueDate()));
        if (InquiryTypeEnum.typeText.getValue().equals(info.getInquiryType())) {
            holder.askTypeText.setText(R.string.imageText);
        } else {
            holder.askTypeText.setText(R.string.video);
        }
        holder.departmentText.setText(info.getDeptName());
        holder.statusText.setText("问诊状态："+info.getOrderStatusDesc());
        if ("2".equals(info.getOrderStatus()) || "1".equals(info.getOrderStatus())) { //如果是在队列中或者问诊中，则可以恢复
            holder.recoverOrderBtn.setVisibility(View.VISIBLE);
        } else {
            holder.recoverOrderBtn.setVisibility(View.GONE);
        }
        return view;
    }


    private String parseTimeString(String millis) {
        String string;
        try {
            long timeMillis = Long.parseLong(millis);
            string = "问诊时间："+ TimeUtil.millis2String(timeMillis, "MM-dd HH:mm");
        } catch (Exception e) {
            MyLog.e("日期转换出错：" + e.getMessage());
            return "";
        }
        return string;
    }

    private void deleteRecord(final InquiryRecordInfo info) {
        User user = DataManager.getInstance().getUser();
        if (user != null) {
            String userId = user.getPatientId();
            HttpGo.post(InquiryNetUrl.DEL_INQUIRY_INFO)
                    .params("orderNo", info.getOrderNo())
                    .params("userId", userId)
                    .execute(new HttpDialogListener((Activity) context, "删除中") {
                        @Override
                        public void onSuccess(String s) {
                            JSONObject result = JsonUtil.checkResult(s);
                            if (result != null &&
                                    SRConstant.SUCCESS_CODE.equals(result.optString(SRConstant.ERR_CODE_KEY))) {
                                list.remove(info);
                                notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    private void showDeleteConfirmDialog(final InquiryRecordInfo info) {
         DialogUtil.both(context, "确认删除？", new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 deleteRecord(info);
             }
         });
    }


//    private String parseStatus(String flag){
//        String status = "";
//        if("1".equals(flag)){
//            status = "等待接单";
//         }else if("2".equals(flag)){
//            status = "问诊中";
//         }else if("3".equals(flag)){
//            status = "开处方中";
//        }else if("4".equals(flag)){
//            status = "处方审核中";
//        }else if("5".equals(flag)){
//            status = "问诊完成";
//        }else if("6".equals(flag)){
//            status = "已关闭";
//        }
//        return status;
//    }


    static class ViewHolder {
        TextView askNameText;
        TextView timeText;
        TextView askTypeText;
        TextView departmentText;
        TextView statusText;
//        Button lookDetailBtn;
//        Button prescriptionBtn;
        Button recoverOrderBtn;
        AdapterOnClickListener listener;
    }


    class AdapterOnClickListener implements View.OnClickListener {
        private InquiryRecordInfo inquiryRecordInfo;

        public void setRecordAskInfo(InquiryRecordInfo inquiryRecordInfo) {
            this.inquiryRecordInfo = inquiryRecordInfo;
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.recoverOrderBtn){
                if (callback != null) {
                    callback.onRecoverOrder(inquiryRecordInfo.getOrderNo());
                }
            }
        }
    }


    public interface AdapterCallback {
        void onRecoverOrder(String orderNo);
    }
}
