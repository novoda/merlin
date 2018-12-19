package com.novoda.kotlin.demo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.novoda.demo.resources.CommonDemoActivity

class DemoActivity : CommonDemoActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentStatus().setOnClickListener(reportNetworkStatus())
        hasInternetAccess().setOnClickListener(reportInternetAccess())
        wifiConnected().setOnClickListener(reportWifiConnected())
        mobileConnected().setOnClickListener(reportMobileConnected())
        networkSubtype().setOnClickListener(reportNetworkSubtype())
        nextActivity().setOnClickListener(navigateToNextActivity())
    }

    private fun reportNetworkStatus(): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(this, "Network status", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reportInternetAccess(): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(this, "Has internet access", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reportWifiConnected(): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(this, "Wifi connected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reportMobileConnected(): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(this, "Mobile connected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reportNetworkSubtype(): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(this, "Network subtype", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToNextActivity(): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(this, "Navigate to next activity", Toast.LENGTH_SHORT).show()
        }
    }
}
