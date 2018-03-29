package com.hyend.app.chitchat;

import android.content.Context;
import android.net.wifi.aware.AttachCallback;
import android.net.wifi.aware.DiscoverySessionCallback;
import android.net.wifi.aware.PeerHandle;
import android.net.wifi.aware.SubscribeConfig;
import android.net.wifi.aware.SubscribeDiscoverySession;
import android.net.wifi.aware.WifiAwareSession;
import android.widget.Toast;

import java.util.List;

public class SubscriberAttachCallback extends AttachCallback {

    private static final String TAG = "PublisherAttachCallback";
    private static final boolean DBG = true;

    private Context mContext;
    private WifiAwareSession mWifiAwareSession;

    public SubscriberAttachCallback(Context context) {
        this.mContext = context;
    }

    private void showShortMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttachFailed() {
        showShortMessage("onAttachFailed");
    }

    @Override
    public void onAttached(WifiAwareSession session) {

        showShortMessage("onAttach");

        this.mWifiAwareSession = session;

        SubscribeConfig config = new SubscribeConfig.Builder()
                .setServiceName(MainActivity.AWARE_SERVICE_NAME)
                .build();

        this.mWifiAwareSession.subscribe(config, new DiscoverySessionCallback() {

            private PeerHandle mPeerHandle = null;

            @Override
            public void onServiceDiscovered(PeerHandle peerHandle,
                                            byte[] serviceSpecificInfo, List<byte[]> matchFilter) {
                mPeerHandle = peerHandle;
            }

            @Override
            public void onSubscribeStarted(SubscribeDiscoverySession session) {
                if (mPeerHandle != null) {
                    int messageId = 1;
                    session.sendMessage(mPeerHandle, messageId, "Test Message".getBytes());
                }
            }
        }, null);
    }
}
