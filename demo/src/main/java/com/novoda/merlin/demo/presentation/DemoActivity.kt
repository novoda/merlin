package com.novoda.merlin.demo.presentation

import android.os.Bundle
import android.view.View
import com.novoda.merlin.Merlin
import com.novoda.merlin.MerlinsBeard
import com.novoda.merlin.NetworkStatus
import com.novoda.merlin.demo.R
import com.novoda.merlin.demo.connectivity.display.NetworkStatusDisplayer
import com.novoda.merlin.demo.presentation.base.MerlinActivity
import com.novoda.merlin.registerable.bind.Bindable
import com.novoda.merlin.registerable.connection.Connectable
import com.novoda.merlin.registerable.disconnection.Disconnectable
import kotlinx.android.synthetic.main.main.displayerAttachableView
import kotlinx.android.synthetic.main.main.current_status as currentStatus
import kotlinx.android.synthetic.main.main.mobile_connected as mobileConnected
import kotlinx.android.synthetic.main.main.network_subtype as networkSubtype
import kotlinx.android.synthetic.main.main.wifi_connected as wifiConnected

class DemoActivity : MerlinActivity(), Connectable, Disconnectable, Bindable {

    override val merlin: Merlin by lazy {
        Merlin.Builder()
            .withConnectableCallbacks()
            .withDisconnectableCallbacks()
            .withBindableCallbacks()
            .build(this)
    }

    private val merlinsBeard by lazy {
        MerlinsBeard.from(this)
    }

    private val networkStatusDisplayer by lazy {
        NetworkStatusDisplayer(resources, merlinsBeard)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        currentStatus.onClick(
            MerlinsBeard::isConnected,
            R.string.current_status_network_connected,
            R.string.current_status_network_disconnected
        )
        wifiConnected.onClick(
            MerlinsBeard::isConnectedToWifi,
            R.string.wifi_connected,
            R.string.wifi_disconnected
        )
        mobileConnected.onClick(
            MerlinsBeard::isConnectedToMobileNetwork,
            R.string.mobile_connected,
            R.string.mobile_disconnected
        )
        networkSubtype.setOnClickListener {
            networkStatusDisplayer.displayNetworkSubtype(it)
        }
    }

    private fun View.onClick(
        isPositive: MerlinsBeard.() -> Boolean,
        positiveMessage: Int,
        negativeMessage: Int
    ) {
        setOnClickListener {
            val message = if (merlinsBeard.isPositive()) positiveMessage else negativeMessage
            networkStatusDisplayer.displayNegativeMessage(message, displayerAttachableView)
        }
    }

    override fun onResume() {
        super.onResume()
        registerConnectable(this)
        registerDisconnectable(this)
        registerBindable(this)
    }

    override fun onConnect() {
        networkStatusDisplayer.displayPositiveMessage(R.string.connected, displayerAttachableView)
    }

    override fun onDisconnect() {
        networkStatusDisplayer.displayNegativeMessage(R.string.disconnected, displayerAttachableView)
    }

    override fun onBind(networkStatus: NetworkStatus) {
        if (!networkStatus.isAvailable) {
            onDisconnect()
        }
    }

    override fun onPause() {
        super.onPause()
        networkStatusDisplayer.reset()
    }

}
