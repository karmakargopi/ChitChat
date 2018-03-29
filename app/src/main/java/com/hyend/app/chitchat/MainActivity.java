package com.hyend.app.chitchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.aware.WifiAwareManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final boolean DBG = true;

    public static final String AWARE_SERVICE_NAME = "HYEND_AWARE_SERVICE";

    private Context mContext;
    private Handler mHandler;
    private WifiManager mWIfiManager;
    private WifiAwareManager mWifiAwareManager;
    private IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mHandler = new Handler();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiAwareManager.ACTION_WIFI_AWARE_STATE_CHANGED);

        if(DBG) Log.d(TAG, "Before");
        mWIfiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        /**
         * ToDo: Should move below hasSystemFeature.
         */
        mWifiAwareManager = (WifiAwareManager) getSystemService(WIFI_AWARE_SERVICE);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_AWARE)) {
            showShortMessage("Wi-Fi Aware Not Supported");
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mContext.registerReceiver(HyendReceiver, intentFilter);
        mWifiAwareManager.attach(new PublisherAttachCallback(this), mHandler);
        mWifiAwareManager.attach(new SubscriberAttachCallback(this), mHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showShortMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver HyendReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "Received Broadcast = "+ intent.getAction());
            switch (intent.getAction()) {

                case WifiManager.WIFI_STATE_CHANGED_ACTION: {

                    Log.d(TAG, "Wifi State = "+ mWIfiManager.isWifiEnabled());
                }
                break;
                case WifiAwareManager.ACTION_WIFI_AWARE_STATE_CHANGED: {

                    if (mWifiAwareManager.isAvailable()) {
                        showShortMessage("WiFi Is On");
                    } else {
                        showShortMessage("Turn On WiFi");
                    }
                }
                break;
            }
        }
    };
}
