package com.sirui.basiclib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * author: hewei
 * created on: 2018/4/2 14:36
 * description:获取手机信息字段工具类
 */
public class DevicesInfoUtils {

    private static final String KEY_FLYME_ID_FALG_KEY = "ro.build.display.id";
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";

    public static String setJsonData(Context context) {
        JSONObject object = new JSONObject();
        JSONObject objects = new JSONObject();
        try {
            objects.put("dvcSysName", "android");
            objects.put("dvcSysVersn", DevicesInfoUtils.getOSVersion());//系统版本
            objects.put("dvcIpAdr", DevicesInfoUtils.getIpAddress());
            objects.put("dvcSerNo", DevicesInfoUtils.getSerial());
            objects.put("dvcModel", DevicesInfoUtils.getPhoneModel());//设备型号
            objects.put("dvcBrnd", DevicesInfoUtils.getPhoneBRAND());
            objects.put("appName", VersionUtils.getAppName(context));
            objects.put("appVersn", VersionUtils.getVersionName(context));
            objects.put("dvcWlanMac", DevicesInfoUtils.getMacAddrOld(context));
            objects.put("dvcSysBit", "32");//系统位数
            objects.put("dvcKernVersn", DevicesInfoUtils.getKernelVersion());//内核版本
            objects.put("dvcNuclrCnt", String.valueOf(DevicesInfoUtils.getNumCores()));//设备核数
            objects.put("dvcRomAllSize", String.valueOf(DevicesInfoUtils.getSDAllSize()));
            objects.put("dvcRomLeftSize", String.valueOf(DevicesInfoUtils.getSDFreeSize()));
            objects.put("dvcSDAllSize", String.valueOf(DevicesInfoUtils.getSDAllSize()));//sd卡
            objects.put("dvcSDLeftSize", String.valueOf(DevicesInfoUtils.getSDFreeSize()));
            objects.put("dvcMenSize", DevicesInfoUtils.getRomTotalSize(context));//运行内存总大小
            objects.put("dvcMenLeftSize", DevicesInfoUtils.getRomAvailableSize(context));//运行内存剩余大小
            objects.put("dvcNtwkType", DevicesInfoUtils.getNetworkType(context));
            objects.put("dvcImei", DevicesInfoUtils.getPhoneImei(context));
            objects.put("dvcCpuInfo", DevicesInfoUtils.getCpuName());
            objects.put("dvcSimSerNo", DevicesInfoUtils.getSysSIMSerialNum(context));
            objects.put("dvcPhone", DevicesInfoUtils.getPhoneNum(context));
            objects.put("dvcCommCorp", DevicesInfoUtils.getSysCarrier(context));
            objects.put("dvcImsi", DevicesInfoUtils.getIMSI(context));
            objects.put("dvcBtAdr", DevicesInfoUtils.getBluetoothMAC(context));
            objects.put("dvcBbVersn", DevicesInfoUtils.getRadioVersion());
            objects.put("dvcRomVersn", DevicesInfoUtils.getEMUI());//ROM版本号无
            objects.put("dvcPhoneWidth", String.valueOf(DevicesInfoUtils.getPhoneWidth(context)));
            objects.put("dvcPhoneHeight", String.valueOf(DevicesInfoUtils.getPhoneHeight(context)));
            objects.put("dvcWifiLevl", String.valueOf(DevicesInfoUtils.getWifiLevel(context)));
            object.put("comm", objects);
            String a = object.toString();
            return a;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    * 获取华为EMUI版本号
    * */
    public static String getEMUI() {
        Class<?> classType = null;
        String buildVersion = null;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            buildVersion = (String) getMethod.invoke(classType, new Object[]{KEY_FLYME_ID_FALG_KEY});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buildVersion;
    }

    /**
     * 获得客户端操作系统名称
     *
     * @return
     */
    public static String getSysClientOs() {
        String OsName = Build.ID;
        return OsName;
    }

    /**
     *获取系统版本:5.1.1
     * @return
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机品牌
     */
    public static String getPhoneBRAND() {
        return Build.BRAND;
    }

    /**
     * 获取手机品牌
     */
    public static String getBatStat(Context context) {
        BatteryManager manager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        manager.getIntProperty(BatteryManager.BATTERY_HEALTH_GOOD);
        return Build.BRAND;
    }

    /*
    * 内核版本号
    * */
    public static String getKernelVersion() {
        String kernelVersion = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/proc/version");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return kernelVersion;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8 * 1024);
        String info = "";
        String line = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                info += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (info != "") {
                final String keyword = "version ";
                int index = info.indexOf(keyword);
                line = info.substring(index + keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return kernelVersion;
    }

    /**
     * 获取sd卡空间的总大小
     */
    @SuppressWarnings("deprecation")
    public static long getSDAllSize() {
        File path = Environment.getExternalStorageDirectory(); // 取得SD卡文件路径
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize(); // 获取单个数据块的大小(Byte)
        long allBlocks = sf.getBlockCount(); // 获取所有数据块数
        // 返回SD卡大小
        return (allBlocks * blockSize) ; // 单位B
    }

    /**
     * 获取sd卡剩余空间的大小
     */
    @SuppressWarnings("deprecation")
    public static long getSDFreeSize() {
        File path = Environment.getExternalStorageDirectory(); // 取得SD卡文件路径
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize(); // 获取单个数据块的大小(Byte)
        long freeBlocks = sf.getAvailableBlocks();// 空闲的数据块的数量
        // 返回SD卡空闲大小
        return (freeBlocks * blockSize) ; // 单位B
    }

    /**
     * 获取手机宽度
     */
    @SuppressWarnings("deprecation")
    public static int getPhoneWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取手机高度
     * @param context
     */
    @SuppressWarnings("deprecation")
    public static int getPhoneHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

        /**
         * 获取手机imei串号 ,GSM手机的 IMEI 和 CDMA手机的 MEID.
         * @param context
         */
    public static String getPhoneImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null)
            return null;
        return tm.getDeviceId();
    }

    /**
     * 读取SIM卡序列号
     * @param content 上下文
     * @return String SIM卡序列号
     */
    public static String getSysSIMSerialNum(Context content) {
        String simSerialNumber = getSysTelephonyManager(content).getSimSerialNumber();
        return simSerialNumber;
    }

    /**
     * 获得电话管理实例对象
     * @param content  上下文
     * @return TelephonyManager 电话管理实例对象
     */
    private static TelephonyManager getSysTelephonyManager(Context content) {
        TelephonyManager telephonyManager = null;
        telephonyManager = (TelephonyManager) content.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager;
    }

    /**
     * 获取设备IMSI码
     * @param ctx
     * @return
     */
    public static String getIMSI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId() != null ? tm.getSubscriberId() : null;
    }

    /**
     * 获取Cpu内核数
     * @return
     */
    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }
            });
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    // 获取CPU名字
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机号
     * @param context
     */
    public static String getPhoneNum(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null)
            return null;
        return tm.getLine1Number();
    }

    /**
     * 获取运营商信息(三大运营商)
     * @param content 上下文
     * @return String 获取运营商名称
     */
    public static String getSysCarrier(Context content) {
        String moblieType = "";
        TelephonyManager telephonyManager = (TelephonyManager) content.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();
        if (imsi != null && imsi.length() > 0) {
            //因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                //中国移动CMCC
                moblieType = "CMCC";
            } else if (imsi.startsWith("46001")) {
                //中国联通CUCC
                moblieType = "CUCC";
            } else if (imsi.startsWith("46003")) {
                //中国电信CTCC
                moblieType = "CTCC";
            }
        }
        return moblieType;

    }

    /**
     * 获取蓝牙地址
     * @param context
     * @return
     */
    //<uses-permission android:name="android.permission.BLUETOOTH"/>
    @SuppressWarnings("MissingPermission")
    public static String getBluetoothMAC(Context context) {
        String result = null;
        try {
            if (context.checkCallingOrSelfPermission(Manifest.permission.BLUETOOTH)
                    == PackageManager.PERMISSION_GRANTED) {
                BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
                result = bta.getAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *  获取基带版本(无线电固件版本 Api14以上)
     * @return
     */
    public static String getRadioVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH ? Build.getRadioVersion() : "";
    }

    /*
    * 内存ram大小
    * */
    public static String getTotalRam(Context context){//GB
        String path = "/proc/meminfo";
        String firstLine = null;
        int totalRam = 0 ;
        try{
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader,8192);
            firstLine = br.readLine().split("\\s+")[1];
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(firstLine != null){
            totalRam = (int)Math.ceil((new Float(Float.valueOf(firstLine) /*/ (1024 * 1024)*/).doubleValue()));
        }
        return String.valueOf(totalRam* 1024);//单位B
    }

    /**
     * 获得机身内存总大小 * * @return
     */
    public static String getRomTotalSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return String.valueOf(blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存 * * @return
     */
    public static String getRomAvailableSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return String.valueOf(blockSize * availableBlocks);//单位B
    }

    /*
    * 获取当前连接wifi信号的强度
    * */
    public static int getWifiLevel(Context context){
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        return wifiInfo.getRssi();
    }

    /**
     * 获取设备序列号
     * @return
     * unknow
     */
    public static String getSerial() {
        return Build.SERIAL;
    }

    // Android 6.0之前的版本可以用的方法（模拟器可以使用）
    public static String getMacAddrOld(Context context)
    {
        String macString = "";
        WifiManager wifimsg = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if (wifimsg != null)
        {
            if (wifimsg.getConnectionInfo() != null)
            {
                if (wifimsg.getConnectionInfo().getMacAddress() != null)
                {
                    macString = wifimsg.getConnectionInfo().getMacAddress();
                }
            }
        }
        return macString;
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     *
     * @return MAC地址
     */
    @SuppressLint("HardwareIds")
    public static String getMacAddressByWifiInfo(Context context) {
        try {
            @SuppressLint("WifiManagerLeak")
            WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) return info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return "02:00:00:00:00:00";
        return "";
    }

    /**
     * 获取本机IP地址
     * @return null：没有网络连接
     */
    public static String getIpAddress() {
        try {
            NetworkInterface nerworkInterface;
            InetAddress inetAddress;
            for (Enumeration<NetworkInterface> en
                 = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                nerworkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr
                     = nerworkInterface.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
            return null;
        } catch (SocketException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*
    * 网络类型
    * */
    public static String getNetworkType(Context context)
    {
        String strNetworkType = "";
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                strNetworkType = "WIFI";
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String _strSubTypeName = networkInfo.getSubtypeName();
//                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
                        {
                            strNetworkType = "3G";
                        }
                        else
                        {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
//                Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }
//        Log.e("cocos2d-x", "Network Type : " + strNetworkType);
        return strNetworkType;
    }

}
