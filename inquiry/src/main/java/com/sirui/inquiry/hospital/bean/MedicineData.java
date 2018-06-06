package com.sirui.inquiry.hospital.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 处方药品对象
 * <p>
 * Created by yellow on 2017/3/20.
 */

public class MedicineData implements Serializable {
    /**订单号*/
    private String orderNo;
    /**开药量*/
    private String totallAmount;
    /**开药量单位*/
    private String totallUnit;
    /**药品序号*/
    private String drugNo;
    /**药品id*/
    private String drugId;

    private String drugStatus;
    /**单次剂量单位*/
    private String singleDoseUnit;
    /**单次剂量*/
    private String singleDose;

    private String productionUnit;

    private String medicaFrequency;

    private String createDate;

    private String createId;

    private String drugUnit;

    private String status;

    private String approvalNo;

    private String updateId;

    private String commodityName;
    /**药品名称*/
    private String genericName;
    /**规格*/
    private String specifications;

    private String updateDate;

    private String price;
    /**用法*/
    private String usage;

    private String dosageForm;

    private String remark1;
    private String remark2;
    public MedicineData(){}

    public MedicineData(JSONObject object){
        this.drugNo = object.optString("drugNo");
        this.drugId = object.optString("drugId");
        this.drugStatus = object.optString("drugStatus");
        this.singleDoseUnit = object.optString("singleDoseUnit");
        this.singleDose = object.optString("singleDose");
        this.productionUnit = object.optString("productionUnit");
        this.medicaFrequency = object.optString("medicaFrequency");
        this.createDate = object.optString("createDate");
        this.createId = object.optString("createId");
        this.drugUnit = object.optString("drugUnit");
        this.status = object.optString("status");
        this.approvalNo = object.optString("approvalNo");
        this.updateId = object.optString("updateId");
        this.commodityName = object.optString("commodityName");
        this.genericName = object.optString("genericName");
        this.specifications = object.optString("specifications");
        this.updateDate = object.optString("updateDate");
        this.price = object.optString("price");
        this.usage = object.optString("usage");
        this.dosageForm = object.optString("dosageForm");
        this.remark1 = object.optString("remark1");
        this.remark2 = object.optString("remark2");
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTotallAmount() {
        return totallAmount;
    }

    public void setTotallAmount(String totallAmount) {
        this.totallAmount = totallAmount;
    }

    public String getTotallUnit() {
        return totallUnit;
    }

    public void setTotallUnit(String totallUnit) {
        this.totallUnit = totallUnit;
    }

    public String getDrugNo() {
        return drugNo;
    }

    public void setDrugNo(String drugNo) {
        this.drugNo = drugNo;
    }

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public String getDrugStatus() {
        return drugStatus;
    }

    public void setDrugStatus(String drugStatus) {
        this.drugStatus = drugStatus;
    }

    public String getSingleDoseUnit() {
        return singleDoseUnit;
    }

    public void setSingleDoseUnit(String singleDoseUnit) {
        this.singleDoseUnit = singleDoseUnit;
    }

    public String getSingleDose() {
        return singleDose;
    }

    public void setSingleDose(String singleDose) {
        this.singleDose = singleDose;
    }

    public String getProductionUnit() {
        return productionUnit;
    }

    public void setProductionUnit(String productionUnit) {
        this.productionUnit = productionUnit;
    }

    public String getMedicaFrequency() {
        return medicaFrequency;
    }

    public void setMedicaFrequency(String medicaFrequency) {
        this.medicaFrequency = medicaFrequency;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getDrugUnit() {
        return drugUnit;
    }

    public void setDrugUnit(String drugUnit) {
        this.drugUnit = drugUnit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovalNo() {
        return approvalNo;
    }

    public void setApprovalNo(String approvalNo) {
        this.approvalNo = approvalNo;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }
}
