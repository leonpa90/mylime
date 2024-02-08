package com.example.mylime.view.detail

import android.widget.Toast
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mylime.domain.model.Beer
import com.example.mylime.model.ResponseItem

@Composable
fun DetailScreen(
    beer: Beer?,
    isLoading: Boolean = false,
    isConnected: Boolean? = null,
    onShareClick: (Beer) -> Unit
) {
    val context = LocalContext.current
    val connected = remember {
        mutableStateOf(isConnected)
    }
    LaunchedEffect(connected){
        connected.value?.let {
            if (!it){
                Toast.makeText(context,"Internet assente", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Internet è tornato", Toast.LENGTH_SHORT).show()
            }
        }
    }
    if (isLoading) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = ImageRequest.Builder(LocalContext.current).data(beer?.imageUrl)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Inside,
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = beer?.name.orEmpty(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = beer?.description.orEmpty())
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        Toast.makeText(
                            context,
                            "${beer?.name}\nhttps://api.punkapi.com/v2/beers/${beer?.id}",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    Text(text = "Informazioni")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(modifier = Modifier.weight(1f), onClick = {
                    beer?.let { onShareClick(it) }
                }) {
                    Text(text = "Condividi")
                }
            }

        }

    }
}