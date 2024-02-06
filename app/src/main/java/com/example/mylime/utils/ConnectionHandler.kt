package com.example.mylime.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ConnectionHandler @Inject constructor(
    @ApplicationContext
    context: Context
) {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)

    val callback = callbackFlow {
        val networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(true)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(false)

            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(false)
            }
        }
        (cm as ConnectivityManager).registerDefaultNetworkCallback(networkCallback)
        awaitClose()
    }
}