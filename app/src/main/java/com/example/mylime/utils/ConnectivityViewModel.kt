package com.example.mylime.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    private val connectionHandler: ConnectionHandler
) : ViewModel() {


    private val _event = Channel<ConnectionEvent>(capacity = Channel.BUFFERED)
    val event: Flow<ConnectionEvent>
        get() = _event.receiveAsFlow()

    private val state: MutableStateFlow<ConnectionState> = MutableStateFlow(ConnectionState())

    init {
        viewModelScope.launch {
            connectionHandler.callBack.collect { connectionState ->
                if (connectionState != state.value.isConnected) {
                    if (connectionState) {
                        _event.trySend(OnReconnected)
                    } else
                        _event.trySend(OnDisconnected)
                }
                state.update { it.copy(isConnected = connectionState) }
            }
        }
    }

    data class ConnectionState(val isConnected: Boolean? = true)

    sealed class ConnectionEvent
    data object OnReconnected : ConnectionEvent()
    data object OnDisconnected : ConnectionEvent()


}