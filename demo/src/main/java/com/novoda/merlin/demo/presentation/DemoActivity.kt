package com.novoda.merlin.demo.presentation

import android.os.Bundle
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

    override val merlin by lazy {
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

        currentStatus.onClick {
            displayMessageFor(merlinsBeard.isConnected, R.string.current_status_network_connected, R.string.current_status_network_disconnected)
        }

        wifiConnected.onClick {
            displayMessageFor(merlinsBeard.isConnectedToWifi, R.string.wifi_connected, R.string.wifi_disconnected)
        }

        mobileConnected.onClick {
            displayMessageFor(merlinsBeard.isConnectedToMobileNetwork, R.string.mobile_connected, R.string.mobile_disconnected)
        }

        networkSubtype.onClick {
            networkStatusDisplayer.displayNetworkSubtype(displayerAttachableView)
        }
    }

    private fun displayMessageFor(condition: Boolean, positiveMessage: Int, negativeMessage: Int) {
        if (condition) {
            networkStatusDisplayer.displayPositiveMessage(positiveMessage, displayerAttachableView)
        } else {
            networkStatusDisplayer.displayNegativeMessage(negativeMessage, displayerAttachableView)
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
