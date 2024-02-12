package com.example.mylime.view.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylime.domain.model.Beer
import com.example.mylime.repository.BeersRepository
import com.example.mylime.utils.ConnectionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val beersRepository: BeersRepository,
    private val connectionHandler: ConnectionHandler,
) : ViewModel() {

    val state: MutableStateFlow<BeerState> = MutableStateFlow(BeerState())

    init {
        viewModelScope.launch {
            fetchBeers()
            launch {
                connectionHandler.callBack.collect { connectionState ->
                    if (!state.value.isConnected && connectionState) {
                        if (state.value.beers.isNullOrEmpty()) {
                            fetchBeers()
                        }
                    }
                    state.update { it.copy(isConnected = connectionState) }
                }
            }
        }
    }

    private suspend fun fetchBeers() {
        state.update { it.copy(isLoading = true) }
        val beer = beersRepository.getBeers()
        beer.fold(onSuccess = { beer ->
            state.update { it.copy(beers = beer, isLoading = false, isError = false) }

        }, onFailure = {
            state.update { it.copy(isLoading = false, isError = true) }
            Log.e("error", it.message.toString())
        })
    }

    data class BeerState(
        val beers: List<Beer>? = null,
        val isLoading: Boolean = false,
        val isConnected: Boolean = true,
        val isError: Boolean = false,
    )
}
