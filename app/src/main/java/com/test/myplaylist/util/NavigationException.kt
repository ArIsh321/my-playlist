package com.test.myplaylist.util

sealed class NavigationException(
    cause: Throwable?
) : Throwable(cause) {

    class UnsupportedNavigationException(
        currentGraph: String?,
        currentDestination: String?
    ) : NavigationException(RuntimeException("Unsupported navigation on $currentGraph at $currentDestination"))
}
