package com.example.mylime.view.detail

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun DetailScreen(
    id: Int?,
    detailViewModel: DetailViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val state = detailViewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            id?.let {
                detailViewModel.setIntent(DetailViewModel.FetchBeer(it))
            }
        }
    }
    if (state.isLoading) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    } else if (!state.isError) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = ImageRequest.Builder(LocalContext.current).data(state.beer?.imageUrl)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Inside,
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.beer?.name.orEmpty(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = TextStyle(lineHeight = 50.sp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = state.beer?.description.orEmpty())
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        Toast.makeText(
                            context,
                            "${state.beer?.name}\nhttps://api.punkapi.com/v2/beers/${state.beer?.id}",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    Text(text = "Informazioni")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(modifier = Modifier.weight(1f), onClick = {
                    state.beer?.let {
                        detailViewModel.setIntent(
                            DetailViewModel.Share(
                                beer = it,
                                context = context
                            )
                        )
                    }
                }) {
                    Text(text = "Condividi")
                }
            }

        }

    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){

            Text(text = "NON APPENA TORNA INTERNET VEDRAI IL CONTENUTO",
                fontSize = 34.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(lineHeight = 50.sp)
            )
        }
        }
    }
