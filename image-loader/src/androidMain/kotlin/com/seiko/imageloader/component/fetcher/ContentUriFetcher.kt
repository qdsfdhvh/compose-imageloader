package com.seiko.imageloader.component.fetcher

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentResolver.SCHEME_CONTENT
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.provider.MediaStore
import androidx.annotation.VisibleForTesting
import com.eygraber.uri.Uri
import com.eygraber.uri.toAndroidUri
import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.model.metadata
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.option.Options
import okio.buffer
import okio.source
import android.net.Uri as AndroidUri

class ContentUriFetcher private constructor(
    private val context: Context,
    private val data: Uri,
) : Fetcher {

    @SuppressLint("Recycle")
    override suspend fun fetch(): FetchResult {
        val contentResolver = context.contentResolver
        val androidUri = data.toAndroidUri()
        val inputStream = if (isContactPhotoUri(data)) {
            // Modified from ContactsContract.Contacts.openContactPhotoInputStream.
            val stream = contentResolver
                .openAssetFileDescriptor(androidUri, "r")
                ?.createInputStream()
            checkNotNull(stream) { "Unable to find a contact photo associated with '$data'." }
        } else if (SDK_INT >= 29 && isMusicThumbnailUri(data)) {
            val stream = contentResolver
                .openTypedAssetFile(androidUri, "image/*", null, null)
                ?.createInputStream()
            checkNotNull(stream) { "Unable to find a music thumbnail associated with '$data'." }
        } else {
            val stream = contentResolver.openInputStream(androidUri)
            checkNotNull(stream) { "Unable to open '$data'." }
        }

        return FetchResult.Source(
            source = inputStream.source().buffer(),
            extra = extraData {
                mimeType(contentResolver.getType(androidUri))
                metadata(Metadata(androidUri))
            },
        )
    }

    /**
     * Contact photos are a special case of content uris that must be loaded using
     * [ContentResolver.openAssetFileDescriptor] or [ContentResolver.openTypedAssetFile].
     */
    @VisibleForTesting
    internal fun isContactPhotoUri(data: Uri): Boolean {
        return data.authority == ContactsContract.AUTHORITY &&
            data.lastPathSegment == Contacts.Photo.DISPLAY_PHOTO
    }

    /**
     * Music thumbnails on API 29+ are a special case of content uris that must be loaded using
     * [ContentResolver.openAssetFileDescriptor] or [ContentResolver.openTypedAssetFile].
     *
     * Example URI: content://media/external/audio/albums/1961323289806133467
     */
    @VisibleForTesting
    internal fun isMusicThumbnailUri(data: Uri): Boolean {
        if (data.authority != MediaStore.AUTHORITY) return false
        val segments = data.pathSegments
        val size = segments.size
        return size >= 3 && segments[size - 3] == "audio" && segments[size - 2] == "albums"
    }

    class Factory(
        private val context: Context,
    ) : Fetcher.Factory {

        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Uri) return null
            if (!isApplicable(data)) return null
            return ContentUriFetcher(context, data)
        }

        private fun isApplicable(data: Uri) = data.scheme == SCHEME_CONTENT
    }

    data class Metadata(val uri: AndroidUri)
}
