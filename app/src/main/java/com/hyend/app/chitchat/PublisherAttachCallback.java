package com.hyend.app.chitchat;

import android.content.Context;
import android.net.wifi.aware.AttachCallback;
import android.net.wifi.aware.DiscoverySessionCallback;
import android.net.wifi.aware.PeerHandle;
import android.net.wifi.aware.PublishConfig;
import android.net.wifi.aware.PublishDiscoverySession;
import android.net.wifi.aware.WifiAwareSession;
import android.widget.Toast;

public class PublisherAttachCallback extends AttachCallback {

    private static final String TAG = "PublisherAttachCallback";
    private static final boolean DBG = true;

    private Context mContext;
    private WifiAwareSession mWifiAwareSession;

    public PublisherAttachCallback(Context context) {
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
        PublishConfig config = new PublishConfig.Builder()
                .setServiceName(MainActivity.AWARE_SERVICE_NAME)
                .build();
        this.mWifiAwareSession.publish(config, new DiscoverySessionCallback() {

            @Override
            public void onPublishStarted(PublishDiscoverySession session) {
                showShortMessage("onPublishStarted");
            }

            @Override
            public void onMessageReceived(PeerHandle peerHandle, byte[] message) {
                showShortMessage("onMessageReceived : " + message.toString());
            }
        }, null);
    }
}
