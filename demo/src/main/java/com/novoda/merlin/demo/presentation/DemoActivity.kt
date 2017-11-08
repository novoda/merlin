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

class DemoActivity : MerlinActivity(), Connectable, Disconnectable, Bindable {

    private lateinit var merlinsBeard: MerlinsBeard
    private lateinit var networkStatusDisplayer: NetworkStatusDisplayer;

    private val viewToAttachDisplayerTo by bind<View>(R.id.current_status)
    private val currentStatus by bind<View>(R.id.current_status)
    private val wifiConnected by bind<View>(R.id.wifi_connected)
    private val mobileConnected by bind<View>(R.id.mobile_connected)
    private val networkSubtype by bind<View>(R.id.network_subtype)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        merlinsBeard = MerlinsBeard.from(this)
        networkStatusDisplayer = NetworkStatusDisplayer(resources, merlinsBeard)

        merlin = Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this)

        currentStatus.setOnClickListener(networkStatusOnClick)
        wifiConnected.setOnClickListener(wifiConnectedOnClick)
        mobileConnected.setOnClickListener(mobileConnectedOnClick)
        networkSubtype.setOnClickListener(networkSubtypeOnClick)
    }

    private val networkStatusOnClick = View.OnClickListener {
        if (merlinsBeard.isConnected()) {
            networkStatusDisplayer.displayPositiveMessage(R.string.current_status_network_connected, viewToAttachDisplayerTo)
        } else {
            networkStatusDisplayer.displayNegativeMessage(R.string.current_status_network_disconnected, viewToAttachDisplayerTo)
        }
    }

    private val wifiConnectedOnClick = View.OnClickListener {
        if (merlinsBeard.isConnectedToWifi()) {
            networkStatusDisplayer.displayPositiveMessage(R.string.wifi_connected, viewToAttachDisplayerTo)
        } else {
            networkStatusDisplayer.displayNegativeMessage(R.string.wifi_disconnected, viewToAttachDisplayerTo)
        }
    }

    private val mobileConnectedOnClick = View.OnClickListener {
        if (merlinsBeard.isConnectedToMobileNetwork()) {
            networkStatusDisplayer.displayPositiveMessage(R.string.mobile_connected, viewToAttachDisplayerTo)
        } else {
            networkStatusDisplayer.displayNegativeMessage(R.string.mobile_disconnected, viewToAttachDisplayerTo)
        }
    }

    private val networkSubtypeOnClick = View.OnClickListener {
        networkStatusDisplayer.displayNetworkSubtype(viewToAttachDisplayerTo)
    }

    override fun onResume() {
        super.onResume()
        registerConnectable(this)
        registerDisconnectable(this)
        registerBindable(this)
    }

    override fun onConnect() {
        networkStatusDisplayer.displayPositiveMessage(R.string.connected, viewToAttachDisplayerTo)
    }

    override fun onDisconnect() {
        networkStatusDisplayer.displayNegativeMessage(R.string.disconnected, viewToAttachDisplayerTo)
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
