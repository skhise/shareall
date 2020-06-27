package com.ktechsoft.shareall;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class WifiDirectBR extends BroadcastReceiver {

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    WifiP2pManager.PeerListListener peerListListener;
    Activity activity;
    TextView txt;
    WifiP2pManager.ConnectionInfoListener connectionInfoListener;

    public WifiDirectBR(WifiP2pManager.ConnectionInfoListener connectionInfoListener,TextView text,WifiP2pManager wifiP2pManager, WifiP2pManager.Channel mChannel, WifiP2pManager.PeerListListener peerListListener,Activity activity){

        this.wifiP2pManager = wifiP2pManager;
        this.mChannel = mChannel;
        this.peerListListener = peerListListener;
        this.activity = activity;
        this.txt = text;
        this.connectionInfoListener = connectionInfoListener;
        Log.e("WifiDirectBR","WifiDirectBR");

    }

    @Override
    public void onReceive(Context context, Intent intent) {

       String action = intent.getAction();
       Log.e("action",action);
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            Log.e("onReceive","WIFI_P2P_STATE_CHANGED_ACTION");
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
                if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                    Toast.makeText(activity.getApplicationContext(), "Wifi Enabled", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity.getApplicationContext(), "Wifi Disabled", Toast.LENGTH_SHORT).show();
                }
       } else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
              Log.e("onReceive","WIFI_P2P_PEERS_CHANGED_ACTION");
            if(wifiP2pManager !=null){
                wifiP2pManager.requestPeers(mChannel,peerListListener);
            }
       } else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            if(wifiP2pManager == null){
                return;
            }
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if(networkInfo.isConnected()){
                wifiP2pManager.requestConnectionInfo(mChannel,connectionInfoListener);
            } else {
                txt.setText("Device Disconnected");
            }
       } else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            Log.e("onReceive","WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");

       }
    }
}
