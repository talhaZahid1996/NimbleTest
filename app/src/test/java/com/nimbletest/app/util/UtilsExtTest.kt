package com.nimbletest.app.util

import org.junit.Test

class UtilsExtTest {

    @Test
    fun `get correct date 'ago' format`() {
        val date = "2017-01-23T07:48:12.991Z"
        val formatDate = date.toTimeAgo()

        assert(formatDate == "6 years ago")
    }

    @Test
    fun `get correct data 'named' format`() {
        val date = "2017-01-23T07:48:12.991Z"
        val formatDate = date.toNamedDateFormat()

        assert(formatDate == "Monday, January 23")
    }

    @Test
    fun `string is not black or empty`() {
        val string = "test"
        val isNotBlankOrEmpty = string.isNotBlankOrEmpty()

        assert(isNotBlankOrEmpty)
    }
}