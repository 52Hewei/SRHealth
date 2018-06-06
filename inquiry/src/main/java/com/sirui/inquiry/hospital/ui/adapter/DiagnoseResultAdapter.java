package com.sirui.inquiry.hospital.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.sirui.inquiry.R;
import com.sirui.inquiry.hospital.bean.MedicineData;
import com.sirui.inquiry.hospital.ui.module.loadmore.CommonBaseAdapter;
import com.sirui.inquiry.hospital.ui.module.loadmore.ViewHolder;

import java.util.List;

/**
 * Created by Lilin on 2017/10/13.
 */

public class DiagnoseResultAdapter extends CommonBaseAdapter<MedicineData> {

    public DiagnoseResultAdapter(Context context, List<MedicineData> datas) {
        super(context, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MedicineData data, int position) {
        TextView tvMedicineName = holder.getView(R.id.tv_medicine_name);
        TextView tvMedicineCount = holder.getView(R.id.tv_medicine_count);
        TextView tvMedicineSpecification = holder.getView(R.id.tv_medicine_specification);
        TextView tvMedicineUsage = holder.getView(R.id.tv_medicine_usage);
        TextView tvMedicineIndex = holder.getView(R.id.tv_medicine_index);

        tvMedicineIndex.setText(String.format("%d、", position + 1));
        tvMedicineName.setText(data.getGenericName());
        tvMedicineCount.setText(String.format("x%s%s", data.getTotallAmount(), data.getTotallUnit()));
        tvMedicineSpecification.setText(String.format("%s", data.getSpecifications()));
        tvMedicineUsage.setText(String.format("%s，%s，一次%s%s", data.getUsage(), data.getMedicaFrequency(), data.getSingleDose(), data.getSingleDoseUnit()));
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_diagnose_prescribe_sheet_content;
    }
}
