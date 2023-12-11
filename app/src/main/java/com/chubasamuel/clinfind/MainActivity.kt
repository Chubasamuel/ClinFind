package com.chubasamuel.clinfind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.chubasamuel.clinfind.ui.graphs.Routes
import com.chubasamuel.clinfind.ui.graphs.homeGraph
import com.chubasamuel.clinfind.ui.theme.ClinFindTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClinFindTheme {
                val snackState =  remember { SnackbarHostState() }
                // A surface container using the 'background' color from the theme
                Scaffold(
                    snackbarHost = { SnackbarHost(snackState) },
                    content={ padding->
                Surface(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.start_home ){
                        homeGraph(snackState)
                    }
                }
            })
        }
    }
}}

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
    ClinFindTheme {
        Greeting("Android")
    }
}