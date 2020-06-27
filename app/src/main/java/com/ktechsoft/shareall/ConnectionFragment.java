package com.ktechsoft.shareall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class ConnectionFragment extends Fragment {

    Button btnonOff,btnDiscover;
    TextView connectionStatus;
    ListView wifiList;
    WifiManager wifiManager;
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceName;
    WifiP2pDevice[] wifiP2pDevices;


    public ConnectionFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        btnonOff = (Button) view.findViewById(R.id.btn_onOff);
        btnDiscover = (Button) view.findViewById(R.id.btn_discover);
        connectionStatus = (TextView) view.findViewById(R.id.connection_status);
        wifiList = (ListView) view.findViewById(R.id.wifiList);

        try{
            wifiManager =(WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiP2pManager = (WifiP2pManager) getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
            channel = wifiP2pManager.initialize(getContext(), Looper.getMainLooper(),null);
            broadcastReceiver = new WifiDirectBR(connectionInfoListener,connectionStatus,wifiP2pManager,channel,peerListListener,getActivity());

            intentFilter = new IntentFilter();
            intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
            intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
            intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
            intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

                    exqListener();
        }catch (Exception e){
            Log.e("Exception",e.getLocalizedMessage());
        }

        return  view;
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            if(!wifiP2pDeviceList.getDeviceList().equals(peers)){
                peers.clear();
                peers.addAll(wifiP2pDeviceList.getDeviceList());

                deviceName = new String[wifiP2pDeviceList.getDeviceList().size()];
                wifiP2pDevices = new WifiP2pDevice[wifiP2pDeviceList.getDeviceList().size()];
                int index=0;
                for(WifiP2pDevice device :wifiP2pDeviceList.getDeviceList()){
                    deviceName[index] = device.deviceName;
                    wifiP2pDevices[index] = device;
                    index++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,deviceName);
                wifiList.setAdapter(adapter);
            }
            if(peers.size()  == 0){
                Toast.makeText(getContext(), "No Device Found", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().getApplicationContext().registerReceiver(broadcastReceiver,intentFilter);
        Toast.makeText(getActivity(), "OnResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().getApplicationContext().unregisterReceiver(broadcastReceiver);
        Toast.makeText(getActivity(), "OnDestroy", Toast.LENGTH_SHORT).show();
    }
    void exqListener(){

           btnonOff.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   try {

                       if(wifiManager.isWifiEnabled()){
                           Toast.makeText(getActivity(), "Off", Toast.LENGTH_SHORT).show();
                           btnonOff.setText("On Wifi");
                           wifiManager.setWifiEnabled(false);
                       } else {
                           wifiManager.setWifiEnabled(true);
                           btnonOff.setText("Off Wifi");
                           Toast.makeText(getActivity(), "On", Toast.LENGTH_SHORT).show();
                       }
                   } catch (Exception e) {
                       Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                   }
               }
           });
           btnDiscover.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   FindSTillSuccess();
               }
           });
           wifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   final  WifiP2pDevice device = wifiP2pDevices[i];
                   WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
                   wifiP2pConfig.deviceAddress = device.deviceAddress;
                   wifiP2pManager.connect(channel, wifiP2pConfig, new WifiP2pManager.ActionListener() {
                       @Override
                       public void onSuccess() {
                           Toast.makeText(getContext(), "Connected to "+device.deviceName, Toast.LENGTH_SHORT).show();
                       }
                       @Override
                       public void onFailure(int i) {
                           Toast.makeText(getContext(), "Not Connected to "+device.deviceName, Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           });
    }
    void FindSTillSuccess(){
        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                connectionStatus.setText("Discovery Started");
            }
            @Override
            public void onFailure(int i) {
                //Toast.makeText(getContext(), "Error Code:"+i, Toast.LENGTH_SHORT).show();
                FindSTillSuccess();
                connectionStatus.setText("Discovery Starting Failed");
            }
        });
    }
    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;
            if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner){
                connectionStatus.setText("Sender");
            } else if(wifiP2pInfo.groupFormed){
                connectionStatus.setText("Receiver");
            }

        }
    };

}
