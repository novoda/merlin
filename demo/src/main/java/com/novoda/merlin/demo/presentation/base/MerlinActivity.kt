package com.novoda.merlin.demo.presentation.base

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.novoda.merlin.Merlin
import com.novoda.merlin.logger.Logger
import com.novoda.merlin.registerable.bind.Bindable
import com.novoda.merlin.registerable.connection.Connectable
import com.novoda.merlin.registerable.disconnection.Disconnectable

abstract class MerlinActivity() : AppCompatActivity() {

    private val logHandle = DemoLogHandle()
    protected abstract val merlin: Merlin

    protected fun registerConnectable(connectable: Connectable) {
        merlin.registerConnectable(connectable)
    }

    protected fun registerDisconnectable(disconnectable: Disconnectable) {
        merlin.registerDisconnectable(disconnectable)
    }

    protected fun registerBindable(bindable: Bindable) {
        merlin.registerBindable(bindable)
    }

    override fun onStart() {
        super.onStart()
        Logger.attach(logHandle)
        merlin.bind()
    }

    override fun onStop() {
        super.onStop()
        merlin.unbind()
        Logger.detach(logHandle)
    }

    class DemoLogHandle : Logger.LogHandle {

        companion object {
            const val TAG = "DemoLogHandle"
        }

        override fun v(vararg message: Any?) {
            Log.v(TAG, message[0].toString())
        }

        override fun i(vararg message: Any?) {
            Log.i(TAG, message[0].toString())
        }

        override fun d(vararg msg: Any?) {
            Log.d(TAG, msg[0].toString())
        }

        override fun d(throwable: Throwable?, vararg message: Any?) {
            Log.d(TAG, message[0].toString(), throwable)
        }

        override fun w(vararg message: Any?) {
            Log.w(TAG, message[0].toString())
        }

        override fun w(throwable: Throwable?, vararg message: Any?) {
            Log.w(TAG, message[0].toString(), throwable)
        }

        override fun e(vararg message: Any?) {
            Log.e(TAG, message[0].toString())
        }

        override fun e(throwable: Throwable?, vararg message: Any?) {
            Log.e(TAG, message[0].toString(), throwable)
        }

    }

}
