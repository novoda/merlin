package com.novoda.merlin.contracts

typealias InternetAccessCallback = (Boolean) -> Unit

interface MerlinsBeard {

    fun isConnected(): Boolean

    fun isConnectedToMobileNetwork(): Boolean

    fun isConnectedToWifi(): Boolean

    fun mobileNetworkSubtype(): String

    fun hasInternetAccess(callback: InternetAccessCallback)

    fun hasInternetAccess(): Boolean

}
