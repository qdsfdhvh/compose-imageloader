package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.option.Options
import kotlin.test.BeforeTest

abstract class BaseMapperTest<M : Mapper<*>> {

    abstract fun createMapper(): M

    private lateinit var mapper: M

    @BeforeTest
    fun onBefore() {
        mapper = createMapper()
    }

    fun map(data: Any, options: Options = Options()) = mapper.map(data, options)
}
