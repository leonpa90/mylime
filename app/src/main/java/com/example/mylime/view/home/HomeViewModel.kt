package com.example.mylime.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylime.model.ResponseItem
import com.example.mylime.repository.BeersRepository
import com.example.mylime.utils.ConnectionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    beersRepository: BeersRepository,
    val connectionHandler: ConnectionHandler,
) : ViewModel() {

    val state: MutableStateFlow<BeerState> = MutableStateFlow(BeerState())

    init {
        state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val beer = beersRepository.getBeers()
            state.update { it.copy(beers = beer, isLoading = false) }
            launch {
                connectionHandler.callback.collectLatest {connected->
                    val previousStateConnection = state.value.isConnected
                    var currentConnection:Boolean? = null
                    if(previousStateConnection==false) {
                        currentConnection = true
                    }else if (!connected){
                        currentConnection =connected
                    }

                   state.update {
                       it.copy(isConnected = currentConnection)
                   }
                }
            }
        }

    }

    data class BeerState(val beers: List<ResponseItem>? = null, val isLoading: Boolean = false, val isConnected : Boolean? = null)
}
