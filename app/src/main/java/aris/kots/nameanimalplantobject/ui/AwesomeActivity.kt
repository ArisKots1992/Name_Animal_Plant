package aris.kots.nameanimalplantobject.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import aris.kots.nameanimalplantobject.R
import aris.kots.nameanimalplantobject.events.WiFiDirectBroadcastReceiver
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_awesome.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode



class AwesomeActivity : AppCompatActivity() {

    lateinit var wifiManager: WifiManager
    lateinit var wifiP2pManager: WifiP2pManager
    lateinit var channel: WifiP2pManager.Channel
    lateinit var wifiDirectBroadcastReceiver: BroadcastReceiver
    lateinit var intentFilter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_awesome)

        if(!isWifiDirectSupported(this))
            return

        initWifiButton()

        initWifiDirectBroadcastReceiver()

    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
        registerReceiver(wifiDirectBroadcastReceiver,intentFilter)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
        unregisterReceiver(wifiDirectBroadcastReceiver)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: WiFiDirectBroadcastReceiver.WifiStateMessage) {
        if (event.isOn) {
            openWifiButton.text = "DISABLE WiFi"
        } else {
            openWifiButton.text = "ENABLE WiFi"
        }
    }

    private fun initWifiDirectBroadcastReceiver() {
        wifiP2pManager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = wifiP2pManager.initialize(this, Looper.getMainLooper(),null)

        wifiDirectBroadcastReceiver = WiFiDirectBroadcastReceiver(wifiP2pManager,channel,this)

        intentFilter = IntentFilter()

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

    }

    private fun initWifiButton() {

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager.isWifiEnabled) {
            openWifiButton.text = "DISABLE WiFi"
        } else {
            openWifiButton.text = "ENABLE WiFi"
        }

        openWifiButton.setOnClickListener {

            wifiManager.isWifiEnabled = !wifiManager.isWifiEnabled
        }

    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


    private fun isWifiDirectSupported(ctx: Context): Boolean {
        val pm = ctx.packageManager
        val features = pm.systemAvailableFeatures
        for (info in features) {
            if (info?.name != null && info.name.equals("android.hardware.wifi.direct", ignoreCase = true)) {
                titleTextView.text = "Wifi Direct Supported"
                return true
            }
        }
        titleTextView.text = "Wifi Direct Not Supported"
        return false
    }

}