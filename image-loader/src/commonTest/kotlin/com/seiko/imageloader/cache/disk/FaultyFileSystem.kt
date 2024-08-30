package com.seiko.imageloader.cache.disk

import com.seiko.imageloader.util.ForwardingSink
import okio.Buffer
import okio.FileSystem
import okio.ForwardingFileSystem
import okio.IOException
import okio.Path
import okio.Sink

class FaultyFileSystem(delegate: FileSystem) : ForwardingFileSystem(delegate) {

    private val writeFaults = mutableSetOf<Path>()
    private val deleteFaults = mutableSetOf<Path>()
    private val renameFaults = mutableSetOf<Path>()

    fun setFaultyWrite(file: Path, faulty: Boolean) {
        if (faulty) {
            writeFaults += file
        } else {
            writeFaults -= file
        }
    }

    fun setFaultyDelete(file: Path, faulty: Boolean) {
        if (faulty) {
            deleteFaults += file
        } else {
            deleteFaults -= file
        }
    }

    fun setFaultyRename(file: Path, faulty: Boolean) {
        if (faulty) {
            renameFaults += file
        } else {
            renameFaults -= file
        }
    }

    override fun atomicMove(source: Path, target: Path) {
        if (source in renameFaults || target in renameFaults) throw IOException("boom!")
        super.atomicMove(source, target)
    }

    override fun delete(path: Path, mustExist: Boolean) {
        if (path in deleteFaults) throw IOException("boom!")
        super.delete(path, mustExist)
    }

    override fun deleteRecursively(fileOrDirectory: Path, mustExist: Boolean) {
        if (fileOrDirectory in deleteFaults) throw IOException("boom!")
        super.deleteRecursively(fileOrDirectory, mustExist)
    }

    override fun appendingSink(file: Path, mustExist: Boolean): Sink =
        FaultySink(super.appendingSink(file, mustExist), file)

    override fun sink(file: Path, mustCreate: Boolean): Sink =
        FaultySink(super.sink(file, mustCreate), file)

    inner class FaultySink(sink: Sink, private val file: Path) : ForwardingSink(sink) {
        override fun write(source: Buffer, byteCount: Long) {
            if (file in writeFaults) throw IOException("boom!")
            super.write(source, byteCount)
        }
    }
}
