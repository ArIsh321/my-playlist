package com.test.myplaylist.domain

import java.io.Serializable

data class Music(
    var name: String = "",
    var filePath: String = "",
    var isPlaying: Boolean = false,
    var duration: Int = -1,
) : Serializable
