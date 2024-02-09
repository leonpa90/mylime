package com.example.mylime

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.mylime.navigation.NavGraph
import com.example.mylime.ui.theme.MyLimeTheme
import com.example.mylime.utils.ConnectivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current
            val connectivityViewModel: ConnectivityViewModel = hiltViewModel()
            val context = LocalContext.current
            LaunchedEffect(connectivityViewModel.event) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    connectivityViewModel.event.collect { event ->
                        when (event) {
                            ConnectivityViewModel.OnDisconnected ->
                                Toast.makeText(context, "Internet assente", Toast.LENGTH_SHORT).show()
                            ConnectivityViewModel.OnReconnected ->
                                Toast.makeText(context, "Internet è tornato", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            MyLimeTheme {
                NavGraph()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyLimeTheme {
        Greeting("Android")
    }
}