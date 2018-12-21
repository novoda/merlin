package com.novoda.merlin.contracts

import android.annotation.SuppressLint
import android.content.Context
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

    companion object {
        @SuppressLint("NewApi") // Compiles into static create method, there is no problem.
        @JvmStatic
        fun create(context: Context): MerlinsBeard {
            val applicationContext = context.applicationContext
            val connectivityManager =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return com.novoda.merlin.internal.MerlinsBeard(connectivityManager)
        }
    }
}
