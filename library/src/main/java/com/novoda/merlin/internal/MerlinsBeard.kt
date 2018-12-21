package com.novoda.merlin.internal

import android.annotation.TargetApi
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import com.novoda.merlin.contracts.InternetAccessCallback
import com.novoda.merlin.contracts.MerlinsBeard

internal class MerlinsBeard(
    override val connectivityManager: ConnectivityManager,
    override val androidVersion: AndroidVersion = AndroidVersion(Build.VERSION.SDK_INT)
) : MerlinsBeard {

    override fun isConnected(): Boolean {
        val networkInfo = networkInfo()
        return networkInfo?.isConnected.orElse { false }
    }

    private fun networkInfo() = connectivityManager.activeNetworkInfo

    override fun isConnectedToMobileNetwork(): Boolean {
        return isConnectedTo(androidVersion.mobileNetworkType())
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun AndroidVersion.mobileNetworkType(): Int {
        return when (this.isLollipopOrHigher()) {
            true -> NetworkCapabilities.TRANSPORT_CELLULAR
            false -> ConnectivityManager.TYPE_MOBILE
        }
    }

    private fun isConnectedTo(networkType: Int): Boolean {
        return when (androidVersion.isLollipopOrHigher()) {
            true -> connectedToNetworkTypeForLollipop(networkType)
            false -> {
                val networkInfo = connectivityManager.getNetworkInfo(networkType)
                networkInfo?.isConnected.orElse { false }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun connectedToNetworkTypeForLollipop(networkType: Int): Boolean {
        val networks = connectivityManager.allNetworks
        val networkForType = networks.firstOrNull {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(it)
            return networkCapabilities.hasTransport(networkType)
        }

        return networkForType.isConnected()
    }

    override fun isConnectedToWifi(): Boolean {
        return isConnectedTo(androidVersion.wifiNetworkType())
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun AndroidVersion.wifiNetworkType(): Int {
        return when (this.isLollipopOrHigher()) {
            true -> NetworkCapabilities.TRANSPORT_WIFI
            false -> ConnectivityManager.TYPE_WIFI
        }
    }

    override fun mobileNetworkSubtype(): String {
        val networkInfo = networkInfo()
        return if (networkInfo?.isConnected.orElse { false }) {
            networkInfo.subtypeName
        } else {
            ""
        }
    }

    override fun hasInternetAccess(callback: InternetAccessCallback) {
        callback(false)
    }

    override fun hasInternetAccess(): Boolean {
        return false
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun Network?.isConnected(): Boolean {
        val networkInfo = connectivityManager.getNetworkInfo(this)
        return networkInfo?.isConnected.orElse { false }
    }

    private inline fun <T> T?.orElse(crossinline block: () -> T) = this ?: block()
}
