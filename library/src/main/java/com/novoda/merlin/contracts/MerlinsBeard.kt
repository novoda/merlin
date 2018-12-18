package com.novoda.merlin.contracts

interface MerlinsBeard {

    fun isConnected(): Boolean

    fun isConnectedToMobileNetwork(): Boolean

    fun isConnectedToWifi(): Boolean

    fun mobileNetworkSubtype(): String

    fun hasInternetAccess(unit: Unit)

    fun hasInternetAccess(): Boolean

}
