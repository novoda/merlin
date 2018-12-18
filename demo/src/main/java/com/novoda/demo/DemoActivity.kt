package com.novoda.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        current_status.setOnClickListener(reportNetworkStatus())
        has_internet_access.setOnClickListener(reportInternetAccess())
        wifi_connected.setOnClickListener(reportWifiConnected())
        mobile_connected.setOnClickListener(reportMobileConnected())
        network_subtype.setOnClickListener(reportNetworkSubtype())
        next_activity.setOnClickListener(navigateToNextActivity())
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
