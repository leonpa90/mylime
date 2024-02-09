package com.example.mylime.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionHandler @Inject constructor(
    @ApplicationContext context: Context
) {
    private val connectivityManager =
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    val callBack = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(false)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(false)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        if (connectivityManager.activeNetwork == null) {
            trySend(false)
        }
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}
