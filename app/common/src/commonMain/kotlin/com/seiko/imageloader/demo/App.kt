package com.seiko.imageloader.demo

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.demo.scene.BigImagesScene
import com.seiko.imageloader.demo.scene.GifImagesScene
import com.seiko.imageloader.demo.scene.NetworkImagesScene
import com.seiko.imageloader.demo.scene.PokemonScene
import com.seiko.imageloader.demo.scene.SvgImagesScene

@Composable
fun App() {
    ComposeImageLoaderTheme {
        var currentRoute by remember { mutableStateOf<Route>(Route.Home) }
        fun onBack() {
            currentRoute = Route.Home
        }
        fun onNavigate(route: Route) {
            currentRoute = route
        }
        Crossfade(currentRoute) {
            when (it) {
                Route.Home -> HomeScene(::onNavigate)
                Route.Network -> NetworkImagesScene(::onBack)
                Route.Gif -> GifImagesScene(::onBack)
                Route.Svg -> SvgImagesScene(::onBack)
                Route.BigImage -> BigImagesScene(::onBack)
                Route.Pokemon -> PokemonScene(::onBack)
            }
        }
    }
}

@Composable
private fun HomeScene(
    onNavigate: (Route) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(Icons.Default.Home, "home")
                },
                title = {
                    Text("ImageLoader")
                },
            )
        },
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            val routes = remember {
                listOf(
                    Route.Network,
                    Route.Gif,
                    Route.Svg,
                    Route.BigImage,
                    Route.Pokemon,
                )
            }
            routes.forEach { route ->
                key(route) {
                    Button({ onNavigate(route) }) {
                        Text(route.name)
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

private val Route.name: String
    get() = when (this) {
        Route.Home -> "Home"
        Route.Network -> "Network"
        Route.Gif -> "Gif"
        Route.Svg -> "Svg"
        Route.BigImage -> "BigImage"
        Route.Pokemon -> "Pokemon"
    }

private sealed interface Route {
    object Home : Route
    object Network : Route
    object Gif : Route
    object Svg : Route
    object BigImage : Route
    object Pokemon : Route
}
