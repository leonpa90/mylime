package com.example.mylime.view.detail

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylime.domain.model.Beer
import com.example.mylime.model.ResponseItem
import com.example.mylime.repository.BeersRepository
import com.example.mylime.utils.ConnectionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    val beersRepository: BeersRepository,
    val connectionHandler: ConnectionHandler,
) : ViewModel() {

    val state: MutableStateFlow<BeerState> = MutableStateFlow(BeerState())

    fun getDetail(id: Int) {
        state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            beersRepository.getBeer(id).fold(
                onSuccess = {response ->
                    state.update { it.copy(beer = response, isLoading = false) }
                }, onFailure = {
                    state.update { it.copy(isError = true, isLoading = false) }
                }
            )
            launch {
                connectionHandler.callback.collectLatest { connected ->
                    val previousStateConnection = state.value.isConnected
                    var currentConnection: Boolean? = null

                    if (previousStateConnection == false) {
                        currentConnection = true
                    } else if (!connected) {
                        currentConnection = connected
                    }

                    state.update {
                        it.copy(isConnected = currentConnection)
                    }
                }
            }
        }

    }

    fun shareDetail(detailObject: ResponseItem, context: Context) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${detailObject.name} ${detailObject.description}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    data class BeerState(
        val beer: Beer? = null,
        val isLoading: Boolean = false,
        val isConnected: Boolean? = null,
        val isError: Boolean = false,
    )
}
