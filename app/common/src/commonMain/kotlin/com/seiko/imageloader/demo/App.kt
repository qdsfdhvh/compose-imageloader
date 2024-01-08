package com.seiko.imageloader.demo

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.demo.scene.FilePickerScene
import com.seiko.imageloader.demo.scene.GifImagesScene
import com.seiko.imageloader.demo.scene.LocalResourceScene
import com.seiko.imageloader.demo.scene.NetworkImagesScene
import com.seiko.imageloader.demo.scene.OtherImagesScene
import com.seiko.imageloader.demo.scene.PokemonScene
import com.seiko.imageloader.demo.scene.SvgImagesScene

@Composable
fun App(modifier: Modifier = Modifier) {
    ComposeImageLoaderTheme {
        var currentRoute by remember { mutableStateOf(Route.Home) }
        fun onBack() {
            currentRoute = Route.Home
        }
        fun onNavigate(route: Route) {
            currentRoute = route
        }
        Crossfade(currentRoute, modifier) {
            when (it) {
                Route.Home -> HomeScene(::onNavigate)
                Route.Network -> NetworkImagesScene(::onBack)
                Route.Gif -> GifImagesScene(::onBack)
                Route.Svg -> SvgImagesScene(::onBack)
                Route.Pokemon -> PokemonScene(::onBack)
                Route.LocalResource -> LocalResourceScene(::onBack)
                Route.Other -> OtherImagesScene(::onBack)
                Route.FilePicker -> FilePickerScene(::onBack)
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
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            remember {
                Route.entries.filterNot { it == Route.Home }
            }.forEach { route ->
                Button({ onNavigate(route) }) {
                    Text(route.name)
                }
            }
        }
    }
}

private enum class Route {
    Home,
    Network,
    Gif,
    Svg,
    Pokemon,
    LocalResource,
    Other,
    FilePicker;
}
