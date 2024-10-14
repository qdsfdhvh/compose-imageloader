package com.seiko.imageloader.demo.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.rememberImageSuccessPainter
import com.seiko.imageloader.ui.AutoSizeBox

@Composable
actual fun FilePickerScene(onBack: () -> Unit) {
    BackScene(
        onBack = onBack,
        title = { Text("FilePicker") },
    ) { innerPadding ->

        val fileType = remember { listOf("jpg", "png", "mp4") }
        var selectPlatformFile by remember { mutableStateOf<Any?>(null) }

        var pathSingleChosen by remember { mutableStateOf("") }
        var showSingleFilePicker by remember { mutableStateOf(false) }
        FilePicker(showSingleFilePicker, fileExtensions = fileType) { mpFile ->
            if (mpFile != null) {
                pathSingleChosen = mpFile.path
                selectPlatformFile = mpFile.platformFile
            }
            showSingleFilePicker = false
        }

        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Button(onClick = { showSingleFilePicker = true }) {
                Text("Choose File")
            }
            Spacer(Modifier.height(6.dp))
            Text("File Chosen: $pathSingleChosen")
            Spacer(Modifier.height(6.dp))
            selectPlatformFile?.let {
                val imageRequest = remember(it) {
                    ImageRequest(it)
                }
                AutoSizeBox(
                    imageRequest,
                    modifier = Modifier.background(Color.Gray)
                        .fillMaxWidth()
                        .weight(1f),
                ) { state ->
                    when (state) {
                        is ImageAction.Loading -> Unit
                        is ImageAction.Success -> {
                            Image(
                                rememberImageSuccessPainter(state),
                                contentDescription = null,
                                modifier = Modifier.matchParentSize(),
                            )
                        }
                        is ImageAction.Failure -> {
                            Text(state.error.message.orEmpty())
                        }
                    }
                }
            }
        }
    }
}
