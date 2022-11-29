package com.test.myplaylist.ui.screen.home


import com.test.myplaylist.base.BaseViewModel
import com.test.myplaylist.util.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dispatchers: DispatchersProvider
) : BaseViewModel(dispatchers) {

    private val _audioPath = MutableStateFlow(ArrayList<String>())
     val audioPath: StateFlow<List<String>>
        get() = _audioPath

    fun getAudioFile(absolutePath: String) {
     _audioPath.value.add(absolutePath)
    }
}
