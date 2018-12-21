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

        on("checking mobile subtype") {
            val merlinsBeard = MerlinsBeard(connectivityManager)
            val mobileNetworkSubtype = merlinsBeard.mobileNetworkSubtype()

            it("is empty") {
                assertThat(mobileNetworkSubtype).isEmpty()
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
            on { subtypeName }.thenReturn("")
        }
        val connectivityManager: ConnectivityManager = mock {
            on { activeNetworkInfo }.thenReturn(networkInfo)
            on { allNetworks }.thenReturn(Array(1) { network })
            on { getNetworkCapabilities(network) }.thenReturn(networkCapabilities)
            on { getNetworkInfo(network) }.thenReturn(networkInfo)
            on { getNetworkInfo(ConnectivityManager.TYPE_WIFI) }.thenReturn(networkInfo)
        }

        on("checking mobile subtype") {
            val merlinsBeard = MerlinsBeard(connectivityManager)
            val mobileNetworkSubtype = merlinsBeard.mobileNetworkSubtype()

            it("is empty") {
                assertThat(mobileNetworkSubtype).isEmpty()
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

                it("is connected") {
                    assertThat(connectedToWifi).isTrue()
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

    given("Connected to Mobile Network") {
        val network: Network = mock()
        val networkCapabilities: NetworkCapabilities = mock {
            on { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) }.thenReturn(true)
        }
        val networkInfo: NetworkInfo = mock {
            on { isConnected }.thenReturn(true)
            on { subtypeName }.thenReturn("subtype")
        }
        val connectivityManager: ConnectivityManager = mock {
            on { activeNetworkInfo }.thenReturn(networkInfo)
            on { allNetworks }.thenReturn(Array(1) { network })
            on { getNetworkCapabilities(network) }.thenReturn(networkCapabilities)
            on { getNetworkInfo(network) }.thenReturn(networkInfo)
            on { getNetworkInfo(ConnectivityManager.TYPE_MOBILE) }.thenReturn(networkInfo)
        }

        on("checking mobile subtype") {
            val merlinsBeard = MerlinsBeard(connectivityManager)
            val mobileNetworkSubtype = merlinsBeard.mobileNetworkSubtype()

            it("is subtype") {
                assertThat(mobileNetworkSubtype).isEqualTo("subtype")
            }
        }

        and("android version is lower than Lollipop") {
            val merlinsBeard = MerlinsBeard(connectivityManager, AndroidVersion(Build.VERSION_CODES.KITKAT))

            on("checking if connected to mobile network") {
                val connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork()

                it("is connected") {
                    assertThat(connectedToMobileNetwork).isTrue()
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

                it("is connected") {
                    assertThat(connectedToMobileNetwork).isTrue()
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
})

fun SpecBody.and(description: String, body: SpecBody.() -> Unit) {
    group("and $description", body = body)
}
