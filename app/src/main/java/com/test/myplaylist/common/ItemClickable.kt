package com.test.myplaylist.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface ItemClickable<T : Any> {

    val itemClick: SharedFlow<T>

    suspend fun notifyItemClick(data: T)
}

class ItemClickableImpl<T : Any> : ItemClickable<T> {

    private val _itemClick = MutableSharedFlow<T>()
    override val itemClick: SharedFlow<T>
        get() = _itemClick

    override suspend fun notifyItemClick(data: T) = _itemClick.emit(data)
}
