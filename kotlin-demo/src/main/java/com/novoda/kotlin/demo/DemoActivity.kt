package com.novoda.kotlin.demo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.novoda.demo.resources.CommonDemoActivity

class DemoActivity : CommonDemoActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentStatus().onClick("Network status")
        hasInternetAccess().onClick("Has internet access")
        wifiConnected().onClick("Wifi connected")
        mobileConnected().onClick("Mobile connected")
        networkSubtype().onClick("Network subtype")
        nextActivity().onClick("Navigate to next activity")
    }

    private fun View.onClick(message: String) {
        setOnClickListener {
            Toast.makeText(this@DemoActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}
