package com.novoda.merlin.internal

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
object MerlinsBeardTest : Spek({

    given("No network information available") {
        val connectivityManager: ConnectivityManager = mock {
            on { allNetworks }.thenReturn(emptyArray())
        }

        on("checking if connected") {
            val merlinsBeard = MerlinsBeard(connectivityManager)
            val connected = merlinsBeard.isConnected()

            it("is not connected") {
                assertThat(connected).isFalse()
            }
        }

        and("android version is Lollipop or higher") {
            val merlinsBeard = MerlinsBeard(connectivityManager, AndroidVersion(Build.VERSION_CODES.LOLLIPOP))

            on("checking if connected to mobile network") {
                val connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork()

                it("is not connected") {
                    assertThat(connectedToMobileNetwork).isFalse()
                }
            }

            on("checking if connected to wifi network") {
                val connectedToWifi = merlinsBeard.isConnectedToWifi()

                it("is not connected") {
                    assertThat(connectedToWifi).isFalse()
                }
            }

        }

        and("android version is lower than Lollipop") {
            val merlinsBeard = MerlinsBeard(connectivityManager, AndroidVersion(Build.VERSION_CODES.KITKAT))

            on("checking if connected to mobile network") {
                val connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork()

                it("is not connected") {
                    assertThat(connectedToMobileNetwork).isFalse()
                }
            }

            on("checking if connected to wifi network") {
                val connectedToWifi = merlinsBeard.isConnectedToWifi()

                it("is not connected") {
                    assertThat(connectedToWifi).isFalse()
                }
            }
        }
    }

    given("Connected to Wifi") {
        val network: Network = mock()
        val networkCapabilities: NetworkCapabilities = mock {
            on { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) }.thenReturn(true)
        }
        val networkInfo: NetworkInfo = mock {
            on { isConnected }.thenReturn(true)
        }
        val connectivityManager: ConnectivityManager = mock {
            on { activeNetworkInfo }.thenReturn(networkInfo)
            on { allNetworks }.thenReturn(Array(1) { network })
            on { getNetworkCapabilities(network) }.thenReturn(networkCapabilities)
            on { getNetworkInfo(network) }.thenReturn(networkInfo)
        }

        and("android version is lower than Lollipop") {
            val merlinsBeard = MerlinsBeard(connectivityManager, AndroidVersion(Build.VERSION_CODES.KITKAT))

            on("checking if connected to mobile network") {
                val connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork()

                it("is not connected") {
                    assertThat(connectedToMobileNetwork).isFalse()
                }
            }

            on("checking if connected to wifi network") {
                val connectedToWifi = merlinsBeard.isConnectedToWifi()

                it("is not connected") {
                    assertThat(connectedToWifi).isFalse()
                }
            }
        }

        and("android version is Lollipop or higher") {
            val merlinsBeard = MerlinsBeard(connectivityManager, AndroidVersion(Build.VERSION_CODES.LOLLIPOP))

            on("checking if connected to mobile network") {
                val connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork()

                it("is not connected") {
                    assertThat(connectedToMobileNetwork).isFalse()
                }
            }

            on("checking if connected to wifi network") {
                val connectedToWifi = merlinsBeard.isConnectedToWifi()

                it("is connected") {
                    assertThat(connectedToWifi).isTrue()
                }
            }
        }
    }

    given("Connected to Wifi") {
        val network: Network = mock()
        val networkCapabilities: NetworkCapabilities = mock {
            on { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) }.thenReturn(true)
        }
        val networkInfo: NetworkInfo = mock {
            on { isConnected }.thenReturn(true)
        }
        val connectivityManager: ConnectivityManager = mock {
            on { activeNetworkInfo }.thenReturn(networkInfo)
            on { allNetworks }.thenReturn(Array(1) { network })
            on { getNetworkCapabilities(network) }.thenReturn(networkCapabilities)
            on { getNetworkInfo(network) }.thenReturn(networkInfo)
        }

        and("android version is Lollipop or higher") {
            val merlinsBeard = MerlinsBeard(connectivityManager, AndroidVersion(Build.VERSION_CODES.LOLLIPOP))

            on("checking if connected to mobile network") {
                val connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork()

                it("is not connected") {
                    assertThat(connectedToMobileNetwork).isFalse()
                }
            }

            on("checking if connected to wifi network") {
                val connectedToWifi = merlinsBeard.isConnectedToWifi()

                it("is connected") {
                    assertThat(connectedToWifi).isTrue()
                }
            }
        }
    }
})

fun SpecBody.and(description: String, body: SpecBody.() -> Unit) {
    group("and $description", body = body)
}
