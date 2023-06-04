package com.seiko.imageloader.demo.scene

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PokemonScene(
    onBack: () -> Unit,
) {
    BackScene(
        onBack = onBack,
        title = { Text("Pokemon") },
    ) { innerPadding ->
        LazyVerticalGrid(
            GridCells.Adaptive(200.dp),
            Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            items(200) {
                ImageItem(
                    data = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${it + 1}.png",
                )
            }
        }
    }
}
