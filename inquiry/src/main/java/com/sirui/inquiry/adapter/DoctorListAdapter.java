package com.sirui.inquiry.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.utils.MyLog;
import com.sirui.basiclib.utils.TextViewUtil;
import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.ui.model.DoctorInfo;

import java.util.List;

/**
 * Created by xiepc on 2018/4/23 15:26
 */

public class DoctorListAdapter extends BaseQuickAdapter<DoctorInfo,BaseViewHolder> {

    private Context mContext;
    public DoctorListAdapter(Context context, int layoutResId, @Nullable List<DoctorInfo> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorInfo item) {
        int position = helper.getAdapterPosition();
        MyLog.i("position =" + position + ",,,item.geName=" + item.getDoctorName());

//        if(TextUtils.isEmpty(item.getDoctorId())){
        if(position == 1){
            helper.setText(R.id.doctorNameText,mContext.getString(R.string.quick_item_title));
            helper.setText(R.id.hospitalText,"");
            helper.setText(R.id.departmentText,"");
            helper.setText(R.id.doctorPositionText,"");
            helper.setText(R.id.tv_good_at,"");
            helper.getView(R.id.ll_dapt_name).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_dept_name,item.getDeptName());
            helper.setText(R.id.tv_doctor_ready_for_consult,mContext.getString(R.string.quick_to_inquiry));
            helper.setImageResource(R.id.iv_avatar,R.drawable.icon_avatar_doctor);
            ((TextView)helper.getView(R.id.tv_doctor_status)).setVisibility(View.GONE);
            helper.getView(R.id.tv_free).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_image_text).setVisibility(View.GONE);
            helper.getView(R.id.tv_video).setVisibility(View.GONE);
        }else{
            helper.setText(R.id.doctorNameText,item.getDoctorName());
            helper.setText(R.id.hospitalText,item.getHospitalName());
            helper.setText(R.id.departmentText,item.getDeptName());
            helper.setText(R.id.doctorPositionText,item.getTitleName());
            helper.setText(R.id.tv_good_at,"擅长:"+item.getPersonalGood());
            helper.getView(R.id.ll_dapt_name).setVisibility(View.GONE);
            String queueNumberDesc = String.format(mContext.getString(R.string.number_of_queue),item.getQueueNumber());
            helper.setText(R.id.tv_doctor_ready_for_consult,queueNumberDesc);
            Glide.with(mContext).load(item.getHeadImageUrl()).into((ImageView) helper.getView(R.id.iv_avatar));

            helper.getView(R.id.tv_free).setVisibility(View.INVISIBLE);
            showInquiryStatus(helper,item);
            TextView tvDoctrStatus = ((TextView)helper.getView(R.id.tv_doctor_status));
            tvDoctrStatus.setVisibility(View.VISIBLE);
            if("1".equals(item.getOnlineStatus()) && "1".equals(item.getLoginStatus())){
                 tvDoctrStatus.setText("在线");
                 tvDoctrStatus.setTextColor(mContext.getResources().getColor(R.color.color_09cacd));
            }else{
                 tvDoctrStatus.setText("离线");
                 tvDoctrStatus.setTextColor(mContext.getResources().getColor(R.color.gray_999));
            }
        }
    }

    /**
     * 显示图文、视频问诊的状态
     * @param helper
     * @param item
     */
    private void showInquiryStatus(BaseViewHolder helper, DoctorInfo item){
        TextView tvImageText = helper.getView(R.id.tv_image_text);
        TextView tvVideo = helper.getView(R.id.tv_video);
        tvImageText.setVisibility(("1".equals(item.getImageTextFlag())?View.VISIBLE:View.GONE));
        tvVideo.setVisibility(("1".equals(item.getVideoFlag())?View.VISIBLE:View.GONE));
        if(SRConstant.NEED_PAY && "1".equals(item.getImageTextFlag())){
            String priceImageTextDesc = "￥"+SRConstant.PRICE_IMAGE_TEXT;
            String imageTextDesc = "图文" + priceImageTextDesc;
            tvImageText.setText(TextViewUtil.getColorSannableString(imageTextDesc,priceImageTextDesc,mContext.getResources().getColor(R.color.color_fb6400)));
        }
        if(SRConstant.NEED_PAY && "1".equals(item.getVideoFlag())){
            String priceVideoDesc = "￥"+SRConstant.PRICE_VIDEO;
            String videoDesc = "视频" + priceVideoDesc;
            tvVideo.setText(TextViewUtil.getColorSannableString(videoDesc,priceVideoDesc,mContext.getResources().getColor(R.color.color_fb6400)));
        }
    }
}
