package com.novoda.merlin.contracts

interface Merlin {

    fun setEndpoint(endpoint: Endpoint, responseCodeValidator: ResponseCodeValidator)

    fun addConnectionCallback()

    fun addDisconnectionCallback()

    fun addBindingCallback()

    interface Builder {

        fun withConnectionCallbacks(): Builder

        fun withDisconnectionCallbacks(): Builder

        fun withBindingCallbacks(): Builder

        fun withAllCallbacks(): Builder

        fun withLogging(): Builder

        fun withEndpoint(endpoint: Endpoint): Builder

        fun withResponseCodeValidator(responseCodeValidator: ResponseCodeValidator): Builder

        fun build(): Merlin
    }

}
