package com.test.myplaylist.base

sealed class NavigationEvent{
    object PopBackStack : NavigationEvent()
    object OnHome : NavigationEvent()

}

