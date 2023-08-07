package com.seiko.imageloader.cache.disk

// import okio.Path.Companion.toPath
// import okio.fakefilesystem.FakeFileSystem
// import okio.use
// import kotlin.test.AfterTest
// import kotlin.test.BeforeTest
// import kotlin.test.Test
// import kotlin.test.assertEquals
// import kotlin.test.assertNotNull
// import kotlin.test.assertNull
// import kotlin.test.assertTrue
//
// internal class DiskCacheTest {
//
//     private lateinit var diskCache: DiskCache
//
//     @BeforeTest
//     fun before() {
//         diskCache = DiskCache(FakeFileSystem().apply { emulateUnix() }) {
//             directory("build/cache".toPath())
//         }
//     }
//
//     @AfterTest
//     fun after() {
//         diskCache.clear()
//         diskCache.fileSystem.deleteRecursively(diskCache.directory) // Ensure we start fresh.
//     }
//
//     @Test
//     fun can_read_and_write_empty() {
//         diskCache.openSnapshot("test").use { assertNull(it) }
//         diskCache.openEditor("test")?.use { /* Empty edit to create the file on disk. */ }
//         diskCache.openSnapshot("test").use { assertNotNull(it) }
//     }
//
//     @Test
//     fun can_read_and_write_data() {
//         assertEquals(0, diskCache.size)
//         diskCache.openSnapshot("test").use { assertNull(it) }
//
//         diskCache.openEditor("test")!!.use { editor ->
//             diskCache.fileSystem.write(editor.metadata) {
//                 writeDecimalLong(12345).writeByte('\n'.code)
//             }
//             diskCache.fileSystem.write(editor.data) {
//                 writeDecimalLong(54321).writeByte('\n'.code)
//             }
//         }
//
//         assertTrue(diskCache.size > 0)
//
//         diskCache.openSnapshot("test")!!.use { snapshot ->
//             assertEquals(
//                 12345,
//                 diskCache.fileSystem.read(snapshot.metadata) { readUtf8LineStrict().toLong() },
//             )
//             assertEquals(
//                 54321,
//                 diskCache.fileSystem.read(snapshot.data) { readUtf8LineStrict().toLong() },
//             )
//         }
//     }
//
//     @Test
//     fun can_remove_singular_entries() {
//         diskCache.openEditor("test1")!!.use { /* Empty edit to create the file on disk. */ }
//         diskCache.openEditor("test2")!!.use { /* Empty edit to create the file on disk. */ }
//         assertTrue(diskCache.remove("test1"))
//         diskCache.openSnapshot("test1").use { assertNull(it) }
//         diskCache.openSnapshot("test2").use { assertNotNull(it) }
//     }
//
//     @Test
//     fun can_clear_all_entries() {
//         diskCache.openEditor("test1")!!.use { /* Empty edit to create the file on disk. */ }
//         diskCache.openEditor("test2")!!.use { /* Empty edit to create the file on disk. */ }
//         diskCache.clear()
//         diskCache.openSnapshot("test1").use { assertNull(it) }
//         diskCache.openSnapshot("test2").use { assertNull(it) }
//     }
//
//     private inline fun <T : DiskCache.Editor?, R> T.use(block: (T) -> R): R {
//         try {
//             return block(this).also { this?.commit() }
//         } catch (e: Exception) {
//             this?.abort()
//             throw e
//         }
//     }
// }
