package com.example.mylime.view.detail

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylime.domain.model.Beer
import com.example.mylime.repository.BeersRepository
import com.example.mylime.utils.ConnectionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val beersRepository: BeersRepository,
    private val connectionHandler: ConnectionHandler,
) : ViewModel() {

    val state: MutableStateFlow<BeerState> = MutableStateFlow(BeerState())

    private val _intent: MutableSharedFlow<DetailIntent> = MutableSharedFlow()

    init {
        viewModelScope.launch {
            launch {
                _intent.asSharedFlow().collect {
                    when (it) {
                        is Share -> shareDetail(it.beer, it.context)
                        is FetchBeer -> getDetail(it.id)
                    }
                }
            }
            launch {
                connectionHandler.callBack.collect{connectionState ->
                    if(!state.value.isConnected && connectionState){
                        state.value.id?.let {
                            getDetail(it)
                        }
                    }
                    state.update { it.copy(isConnected = connectionState) }
                }
            }
        }
    }

    private fun getDetail(id: Int) {
        state.update { it.copy(isLoading = true, id = id) }
        viewModelScope.launch {
            beersRepository.getBeer(id).fold(
                onSuccess = { response ->
                    state.update { it.copy(beer = response, isLoading = false, isError = false) }
                }, onFailure = {
                    state.update { it.copy(isError = true, isLoading = false) }
                }
            )
        }

    }

    private fun shareDetail(detailObject: Beer, context: Context) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${detailObject.name} ${detailObject.description}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun setIntent(intent: DetailIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    data class BeerState(
        val beer: Beer? = null,
        val id:Int? = null,
        val isLoading: Boolean = false,
        val isConnected: Boolean = true,
        val isError: Boolean = false,
    )

    sealed class DetailIntent
    data class Share(val beer: Beer, val context: Context) : DetailIntent()
    data class FetchBeer(val id: Int) : DetailIntent()
}
