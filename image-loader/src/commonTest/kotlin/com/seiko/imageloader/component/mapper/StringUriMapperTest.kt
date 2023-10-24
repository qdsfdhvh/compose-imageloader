package com.seiko.imageloader.component.mapper

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class StringUriMapperTest : BaseMapperTest<StringUriMapper>() {

    override fun createMapper() = StringUriMapper()

    @Test
    fun test() {
        assertNull(map("data:image/png;base64,iVBOR..."))
        assertNull(map("http://www.google.com"))
        assertNull(map("https://www.google.com"))
        assertNotNull(map("content://com.example.project:80/folder/etc"))
        assertNotNull(map("imarsthink://www.marsthink.com/travel/oversea?id=1000"))
        assertNotNull(map("tel:10086"))
        assertNotNull(map("geo:52.76,-79.0342"))
    }
}