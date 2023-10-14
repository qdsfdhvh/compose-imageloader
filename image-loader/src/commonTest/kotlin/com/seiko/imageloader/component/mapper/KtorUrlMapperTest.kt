package com.seiko.imageloader.component.mapper

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KtorUrlMapperTest : BaseMapperTest<KtorUrlMapper>() {

    override fun createMapper() = KtorUrlMapper()

    @Test
    fun test() {
        assertNull(map("data:image/png;base64,iVBOR..."))
        assertNotNull(map("http://www.google.com"))
        assertNotNull(map("https://www.google.com"))
        assertNull(map("content://com.example.project:80/folder/etc"))
        assertNull(map("imarsthink://www.marsthink.com/travel/oversea?id=1000"))
        assertNull(map("tel:10086"))
        assertNull(map("geo:52.76,-79.0342"))
    }
}
