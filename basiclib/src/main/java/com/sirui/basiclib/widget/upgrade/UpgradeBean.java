package com.sirui.basiclib.widget.upgrade;

/**
 * @author LuoSiYe
 *         Created on 2017/10/19.
 */

public class UpgradeBean extends RootPojo{

    Upgrade data;

    public Upgrade getData() {
        return data;
    }

    public void setData(Upgrade data) {
        this.data = data;
    }

    public class Upgrade{

        String romCustomerVersion;
        String isDefault;
        String versionUrl;
        String versionType;
        String updFlag;
        String versionCompareNo;
        String version;

        public String getRemark1() {
            return remark1;
        }

        public void setRemark1(String remark1) {
            this.remark1 = remark1;
        }

        String remark1;

        public String getRomCustomerVersion() {
            return romCustomerVersion;
        }

        public void setRomCustomerVersion(String romCustomerVersion) {
            this.romCustomerVersion = romCustomerVersion;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }

        public String getVersionUrl() {
            return versionUrl;
        }

        public void setVersionUrl(String versionUrl) {
            this.versionUrl = versionUrl;
        }

        public String getVersionType() {
            return versionType;
        }

        public void setVersionType(String versionType) {
            this.versionType = versionType;
        }

        public String getUpdFlag() {
            return updFlag;
        }

        public void setUpdFlag(String updFlag) {
            this.updFlag = updFlag;
        }

        public String getVersionCompareNo() {
            return versionCompareNo;
        }

        public void setVersionCompareNo(String versionCompareNo) {
            this.versionCompareNo = versionCompareNo;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

}
