package com.novoda.kotlin.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.novoda.demo.resources.CommonDemoActivity
import com.novoda.merlin.contracts.MerlinsBeard
import kotlin.reflect.KFunction0

typealias MerlinsBeardFunc = KFunction0<Boolean>

class DemoActivity : CommonDemoActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val merlinsBeard = MerlinsBeard.create(this)

        currentStatus().onClick(
            merlinsBeard::isConnected,
            R.string.current_status_network_connected,
            R.string.current_status_network_disconnected
        )
        hasInternetAccess().onClick(
            merlinsBeard::hasInternetAccess,
            R.string.has_internet_access_true,
            R.string.has_internet_access_false
        )
        wifiConnected().onClick(
            merlinsBeard::isConnectedToWifi,
            R.string.wifi_connected,
            R.string.wifi_disconnected
        )
        mobileConnected().onClick(
            merlinsBeard::isConnectedToMobileNetwork,
            R.string.mobile_connected,
            R.string.mobile_disconnected
        )
        networkSubtype().onClick(merlinsBeard::mobileNetworkSubtype)
        nextActivity().setOnClickListener {
            startActivity(Intent(applicationContext, DemoActivity::class.java))
        }
    }

    private fun View.onClick(
        isPositive: MerlinsBeardFunc,
        positiveMessage: Int,
        negativeMessage: Int
    ) {
        setOnClickListener {
            val message = if (isPositive()) positiveMessage else negativeMessage
            Toast.makeText(this@DemoActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun View.onClick(subtype: KFunction0<String>) {
        setOnClickListener {
            with(subtype()) {
                if (this.isBlank()) {
                    Toast.makeText(this@DemoActivity, R.string.subtype_not_available, Toast.LENGTH_SHORT).show()
                } else {
                    val message = resources.getString(R.string.subtype_value, this)
                    Toast.makeText(this@DemoActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
