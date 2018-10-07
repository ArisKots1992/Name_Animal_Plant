package aris.kots.nameanimalplantobject.events

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import android.widget.Toast
import aris.kots.nameanimalplantobject.ui.AwesomeActivity
import kotlinx.android.synthetic.main.activity_awesome.*
import org.greenrobot.eventbus.EventBus


class WiFiDirectBroadcastReceiver : BroadcastReceiver {

    var manager: WifiP2pManager
    var channel: WifiP2pManager.Channel
    var activity: AwesomeActivity

    constructor(manager: WifiP2pManager, channel: WifiP2pManager.Channel, activity: AwesomeActivity) : super() {
        this.manager = manager
        this.channel = channel
        this.activity = activity
    }


    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent!!.action) {

            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                var state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)

                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    Toast.makeText(context, "Wifi is ON", Toast.LENGTH_SHORT).show()
                    EventBus.getDefault().post(WifiStateMessage(true))

                } else {
                    Toast.makeText(context, "Wifi is OFF", Toast.LENGTH_SHORT).show()
                    EventBus.getDefault().post(WifiStateMessage(false))
                }
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {

                    manager.requestPeers(channel,activity.peerListener)

            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {

                val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiP2pManager.EXTRA_NETWORK_INFO)

                if(networkInfo.isConnected)
                    manager.requestConnectionInfo(channel,activity.connectionListener)
                else
                    activity.titleTextView.text="Disconnected"

            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {

            }
        }
    }

    class WifiStateMessage(val isOn: Boolean)
}