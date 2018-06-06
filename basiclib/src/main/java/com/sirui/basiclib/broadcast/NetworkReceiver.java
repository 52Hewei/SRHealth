package com.sirui.basiclib.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sirui.basiclib.utils.MyLog;


/**
 * 网络变化监听器
 * <p>
 * Created by yellow on 17/5/17.
 */

public class NetworkReceiver extends BroadcastReceiver {

    private int lastConnectivity = -1;
    private NetworkConnectivityListener connectivityListener;

    public NetworkReceiver(NetworkConnectivityListener connectivityListener) {
        this.connectivityListener = connectivityListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        int currentConnectivity = isConnected ? 1 : 0;
        if (connectivityListener != null
                && currentConnectivity != lastConnectivity
                && lastConnectivity != -1) {
            if (!isConnected) {
                connectivityListener.onDisconnected();
            } else {
                connectivityListener.onReconnected();
            }

        }
        lastConnectivity = currentConnectivity;
        MyLog.d("NetworkReceiver onReceive: " + intent.getStringExtra(ConnectivityManager.EXTRA_REASON));
    }

    public interface NetworkConnectivityListener {
        void onDisconnected();
        void onReconnected();
    }
}
