package com.novoda.merlin.contracts

interface Callback

interface ConnectionCallback : Callback {
    fun onConnect()
}

interface DisconnectionCallback : Callback {
    fun onDisconnect()
}

interface BindingCallback : Callback {
    fun onBind()
}
