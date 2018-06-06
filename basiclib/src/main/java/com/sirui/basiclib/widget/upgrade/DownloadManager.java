package com.sirui.basiclib.widget.upgrade;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.listener.DownloadListener;
import com.net.client.HttpFileListener;
import com.net.client.HttpGo;
import com.sirui.basiclib.R;

import java.io.File;


/**
 * author: hewei
 * created on: 2018/4/12 13:47
 * description:okdownload下载apk管理工具
 */
public class DownloadManager {

    public static final String apkUrl="https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/apk/app-debug.apk";
    public static final String path = Environment.getExternalStorageDirectory().getPath() + "/apkhealth/";
    public static final String apkName = "hw.apk";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private static final String CHANNEL_ID = "app_update_id";
    private static final int NOTIFY_ID = 0;
    public static final int REQ_CODE_INSTALL_APP = 99;

//    private OkDownload okDownload;

    public DownloadManager() {
//        if (null == okDownload){
//            okDownload = OkDownload.getInstance();
//        }
    }

    public static void downloadApk(){
        String path = Environment.getExternalStorageDirectory().getPath() + "/apkhealth/";
        com.lzy.okserver.download.DownloadManager downloadManager = com.lzy.okserver.download.DownloadManager.getInstance();
        downloadManager.setTargetFolder(path);
        downloadManager.getThreadPool().setCorePoolSize(3);
        GetRequest request = OkGo.get(apkUrl);
        downloadManager.addTask("apktag", request, new DownloadListener() {
            @Override
            public void onProgress(DownloadInfo downloadInfo) {
                float a = downloadInfo.getProgress();
                long b = downloadInfo.getNetworkSpeed();
            }

            @Override
            public void onFinish(DownloadInfo downloadInfo) {

            }

            @Override
            public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {

            }
        });
    }

    public void initOkDownload(){
//        String path = Environment.getExternalStorageDirectory().getPath();
//        okDownload.setFolder(path);//文件路径
//        okDownload.getThreadPool().setCorePoolSize(3);//同时下载数量
//        okDownload.addOnAllTaskEndListener(new XExecutor.OnAllTaskEndListener() {
//            @Override
//            public void onAllTaskEnd() {
//            }
//        });
    }

    public void downapk(final Activity context , final UpgradeDialog dialog){
        setNotifcation(context);
        HttpGo.get(apkUrl)
                .execute(new HttpFileListener(path,apkName) {
                    @Override
                    public void onProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        int progresses = (int) (progress*100);
                        dialog.setProgress(progresses);
                        mBuilder.setProgress(100, progresses ,false);
                        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
                        mBuilder.setContentText("已下载 "+progresses+"%");
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        dialog.dismiss();
                        mNotificationManager.cancel(NOTIFY_ID);
                        installApp(context,new File(path+apkName));
                    }
                });
    }

    public void setNotifcation(Context context){
        mNotificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        mBuilder.setContentTitle("开始下载")
                .setContentText("正在连接服务器")
                .setSmallIcon(R.mipmap.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_logo))
//                .setOngoing(true)
//                .setAutoCancel(true)
                .setProgress(100 , 0 ,false)
                .setWhen(System.currentTimeMillis());
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
    }

    public boolean installApp(Activity activity, File appFile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri fileUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".fileProvider", appFile);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
            }
            if (activity.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                activity.startActivityForResult(intent, REQ_CODE_INSTALL_APP);
            }
            return true;
        } catch (Exception e) {
            // TODO 后续可以考虑这种情况应该通知应用开发者
            e.printStackTrace();
        }
        return false;
    }

}
