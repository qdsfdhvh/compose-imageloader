package com.seiko.imageloader.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.seiko.imageloader.rememberImageSuccessPainter

class ImageActionTest {

    @Composable
    fun test() {
        val request = remember { ImageRequest("") }
        val action by rememberImageAction(request)
        when_normal_test(action)
        when_image_action_test(action)
        when_image_action_no_result_test(action)
        when_image_event_test(action)
        when_image_event_no_result_test(action)
        when_image_result_test(action)
        when_image_result_success_no_result_failure_test(action)
        when_all_test(action)
    }

    @Composable
    private fun when_normal_test(action: ImageAction) {
        when (action) {
            is ImageAction.Loading -> LoadingUI()
            is ImageAction.Success -> SuccessUI(rememberImageActionPainter(action))
            is ImageAction.Failure -> ErrorUI(action.error)
        }
    }

    @Composable
    private fun when_image_action_test(action: ImageAction) {
        when (action) {
            is ImageEvent -> LoadingUI()
            is ImageResult -> {
                when (action) {
                    is ImageAction.Success -> SuccessUI(rememberImageSuccessPainter(action))
                    is ImageAction.Failure -> ErrorUI(action.error)
                }
            }
        }
    }

    @Composable
    private fun when_image_action_no_result_test(action: ImageAction) {
        when (action) {
            is ImageEvent -> LoadingUI()
            is ImageAction.Success -> SuccessUI(rememberImageSuccessPainter(action))
            is ImageAction.Failure -> ErrorUI(action.error)
        }
    }

    @Composable
    private fun when_image_event_test(action: ImageAction) {
        when (action) {
            is ImageEvent.Start,
            is ImageEvent.StartWithMemory,
            is ImageEvent.StartWithDisk,
            is ImageEvent.StartWithFetch,
            -> LoadingUI()
            is ImageResult -> {
                when (action) {
                    is ImageAction.Success -> SuccessUI(rememberImageSuccessPainter(action))
                    is ImageAction.Failure -> ErrorUI(action.error)
                }
            }
        }
    }

    @Composable
    private fun when_image_event_no_result_test(action: ImageAction) {
        when (action) {
            is ImageEvent.Start,
            is ImageEvent.StartWithMemory,
            is ImageEvent.StartWithDisk,
            is ImageEvent.StartWithFetch,
            -> LoadingUI()
            is ImageAction.Success -> SuccessUI(rememberImageSuccessPainter(action))
            is ImageAction.Failure -> ErrorUI(action.error)
        }
    }

    @Composable
    private fun when_image_result_test(action: ImageAction) {
        when (action) {
            is ImageEvent -> LoadingUI()
            is ImageResult.OfImage -> SuccessUI(rememberImageSuccessPainter(action))
            is ImageResult.OfBitmap -> SuccessUI(rememberImageSuccessPainter(action))
            is ImageResult.OfPainter -> SuccessUI(rememberImageSuccessPainter(action))
            is ImageResult.OfError -> ErrorUI(action.error)
            is ImageResult.OfSource -> ErrorUI(action.error)
        }
    }

    @Composable
    private fun when_image_result_success_no_result_failure_test(action: ImageAction) {
        when (action) {
            is ImageAction.Loading -> LoadingUI()
            is ImageResult.OfImage -> SuccessUI(rememberImageSuccessPainter(action))
            is ImageResult.OfBitmap -> SuccessUI(rememberImageSuccessPainter(action))
            is ImageResult.OfPainter -> SuccessUI(rememberImageSuccessPainter(action))
            is ImageAction.Failure -> ErrorUI(action.error)
        }
    }

    @Composable
    private fun when_all_test(action: ImageAction) {
        when (action) {
            is ImageEvent.Start,
            is ImageEvent.StartWithMemory,
            is ImageEvent.StartWithDisk,
            is ImageEvent.StartWithFetch,
            -> LoadingUI()
            is ImageResult.OfImage -> SuccessUI(rememberImageSuccessPainter(action))
            is ImageResult.OfBitmap -> SuccessUI(rememberImageSuccessPainter(action))
            is ImageResult.OfPainter -> SuccessUI(rememberImageSuccessPainter(action))
            is ImageResult.OfError -> ErrorUI(action.error)
            is ImageResult.OfSource -> ErrorUI(action.error)
        }
    }

    @Composable
    private fun LoadingUI() = Unit

    @Suppress("UNUSED_PARAMETER")
    @Composable
    private fun SuccessUI(painter: Painter) = Unit

    @Suppress("UNUSED_PARAMETER")
    @Composable
    private fun ErrorUI(error: Throwable) = Unit
}
