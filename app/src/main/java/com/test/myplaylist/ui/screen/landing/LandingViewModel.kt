package com.test.myplaylist.ui.screen.landing


import androidx.lifecycle.viewModelScope
import com.test.myplaylist.base.BaseViewModel
import com.test.myplaylist.base.NavigationEvent
import com.test.myplaylist.util.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    dispatchers: DispatchersProvider
) : BaseViewModel(dispatchers) {

    fun sleepTimeStart() {
        viewModelScope.launch {
            val totalSeconds = TimeUnit.SECONDS.toSeconds(2)
            val tickSeconds = 1
            for (second in totalSeconds downTo tickSeconds) {
                delay(500)
            }
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        viewModelScope.launch {
            _navigator.emit(NavigationEvent.OnHome)
        }
    }
}
