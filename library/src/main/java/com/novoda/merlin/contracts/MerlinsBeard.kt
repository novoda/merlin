package com.novoda.merlin.contracts

import android.net.ConnectivityManager
import com.novoda.merlin.internal.AndroidVersion

typealias InternetAccessCallback = (Boolean) -> Unit

interface MerlinsBeard {

    val connectivityManager: ConnectivityManager
    val androidVersion: AndroidVersion

    fun isConnected(): Boolean

    fun isConnectedToMobileNetwork(): Boolean

    fun isConnectedToWifi(): Boolean

    fun mobileNetworkSubtype(): String

    fun hasInternetAccess(callback: InternetAccessCallback)

    fun hasInternetAccess(): Boolean

}
