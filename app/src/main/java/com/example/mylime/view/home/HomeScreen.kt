package com.example.mylime.view.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mylime.domain.model.Beer
import com.example.mylime.preview.BeerProvider
import com.example.mylime.ui.theme.MyLimeTheme

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit
) {
    val state = homeViewModel.state.collectAsState().value

    if (state.isLoading) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    } else if (!state.isError) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            state.beers?.let {
                items(state.beers) {
                    BeerItem(item = it, onItemClick)
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){

            Text(text = "NON APPENA TORNA INTERNET VEDRAI IL CONTENUTO", fontSize = 34.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(lineHeight = 50.sp)
                )
        }
    }
}

@Composable
private fun BeerItem(item: Beer, onItemClick: (Int) -> Unit) {
    Column(modifier = Modifier.clickable {
        item.id?.let { onItemClick(it) }
    }) {
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth()) {

            AsyncImage(
                modifier = Modifier.size(50.dp),
                model = ImageRequest.Builder(LocalContext.current).data(item.imageUrl)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Inside,
            )
            Column {

                Text(text = item.name.orEmpty(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = item.description.orEmpty())
            }

        }
    }

}

@Preview
@Composable
private fun PreviewBeerItem(
    @PreviewParameter(BeerProvider::class) parameter: Beer
) {
    MyLimeTheme {
        BeerItem(item = parameter, onItemClick = {})
    }
}