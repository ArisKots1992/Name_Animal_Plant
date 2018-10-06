package aris.kots.nameanimalplantobject.ui

import androidx.appcompat.app.AppCompatActivity
import aris.kots.nameanimalplantobject.R

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.peak.salut.Callbacks.SalutCallback
import com.peak.salut.Callbacks.SalutDataCallback
import com.peak.salut.Salut
import com.peak.salut.SalutDataReceiver
import com.peak.salut.SalutServiceData
import kotlinx.android.synthetic.main.activity_wifi_direct.*

class WifiDirect : AppCompatActivity(), SalutDataCallback {
    val TAG = "Salut"
    var isHost: Boolean? = null
    var network: Salut? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_direct)


        Log.d(TAG, "" + isWifiDirectSupported(this))

        if (!isWifiDirectSupported(this))
            return

        val dataReceiver = SalutDataReceiver(this, this)
        val serviceData = SalutServiceData("aris", 60606, "HOST")

        network = Salut(dataReceiver, serviceData, SalutCallback {
            Log.e(TAG, "Sorry, but this device does not support WiFi Direct.")
        })

        startServerButton.setOnClickListener {
            Log.d(TAG, "Starting Server..")
            Toast.makeText(this, "Starting Server..", Toast.LENGTH_SHORT).show()

            isHost = true
            network!!.startNetworkService { device ->
                Toast.makeText(this, device.readableName + " has connected!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, device.readableName + " has connected!")
            }
        }


        scanDevicesButton.setOnClickListener {
            Log.d(TAG, "Scanning for Devices..")
            isHost = false

            network!!.discoverNetworkServices({ device ->

                Log.d(TAG, "A device has connected with the name " + device.deviceName)

                network!!.registerWithHost(device,
                        {
                            Toast.makeText(this, "We're now registered.", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "We're now registered.")
                        },
                        {
                            Toast.makeText(this, "We failed to register.", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "We failed to register.")
                        })
            }, true)
        }
    }

    private fun isWifiDirectSupported(ctx: Context): Boolean {
        val pm = ctx.packageManager
        val features = pm.systemAvailableFeatures
        for (info in features) {
            if (info?.name != null && info.name.equals("android.hardware.wifi.direct", ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    override fun onDataReceived(o: Any) {

    }

    public override fun onDestroy() {
        super.onDestroy()

        if (isHost!!)
            network!!.stopNetworkService(true)
        else
            network!!.unregisterClient(true)
    }

}
