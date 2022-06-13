package com.seiko.imageloader.cache.disk

import okio.Closeable
import okio.FileSystem
import okio.Path

/**
 * An LRU cache of files.
 */
interface DiskCache {

    /** The current size of the cache in bytes. */
    val size: Long

    /** The maximum size of the cache in bytes. */
    val maxSize: Long

    /** The directory where the cache stores its data. */
    val directory: Path

    /** The file system that contains the cache's files. */
    val fileSystem: FileSystem

    /**
     * Get the entry associated with [key].
     *
     * IMPORTANT: **You must** call either [Snapshot.close] or [Snapshot.closeAndEdit] when finished
     * reading the snapshot. An open snapshot prevents editing the entry or deleting it on disk.
     */
    operator fun get(key: String): Snapshot?

    /**
     * Edit the entry associated with [key].
     *
     * IMPORTANT: **You must** call one of [Editor.commit], [Editor.commitAndGet], or [Editor.abort]
     * to complete the edit. An open editor prevents opening new [Snapshot]s or opening a new
     * [Editor].
     */
    fun edit(key: String): Editor?

    /**
     * Delete the entry referenced by [key].
     *
     * @return 'true' if [key] was removed successfully. Else, return 'false'.
     */
    fun remove(key: String): Boolean

    /** Delete all entries in the disk cache. */
    fun clear()

    /**
     * A snapshot of the values for an entry.
     *
     * IMPORTANT: You must **only read** [metadata] or [data]. Mutating either file can corrupt the
     * disk cache. To modify the contents of those files, use [edit].
     */
    interface Snapshot : Closeable {

        /** Get the metadata for this entry. */
        val metadata: Path

        /** Get the data for this entry. */
        val data: Path

        /** Close the snapshot to allow editing. */
        override fun close()

        /** Close the snapshot and call [edit] for this entry atomically. */
        fun closeAndEdit(): Editor?
    }

    /**
     * Edits the values for an entry.
     *
     * Calling [metadata] or [data] marks that file as dirty so it will be persisted to disk
     * if this editor is committed.
     *
     * IMPORTANT: You must **only read or modify the contents** of [metadata] or [data].
     * Renaming, locking, or other mutating file operations can corrupt the disk cache.
     */
    interface Editor {

        /** Get the metadata for this entry. */
        val metadata: Path

        /** Get the data for this entry. */
        val data: Path

        /** Commit the edit so the changes are visible to readers. */
        fun commit()

        /** Commit the edit and open a new [Snapshot] atomically. */
        fun commitAndGet(): Snapshot?

        /** Abort the edit. Any written data will be discarded. */
        fun abort()
    }
}
