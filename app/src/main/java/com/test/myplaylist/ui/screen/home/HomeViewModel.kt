package com.test.myplaylist.ui.screen.home


import android.media.AudioManager
import android.media.MediaPlayer
import com.test.myplaylist.base.BaseViewModel
import com.test.myplaylist.data.storage.NormalSharedPreferences
import com.test.myplaylist.domain.Music
import com.test.myplaylist.util.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dispatchers: DispatchersProvider,
    ) : BaseViewModel(dispatchers) {

    private val _audioPath = MutableStateFlow(ArrayList<Music>())
     val audioPath: StateFlow<List<Music>>
        get() = _audioPath

    private val _mediaData= MutableStateFlow(Music())
     val mediaData: StateFlow<Music>
        get() = _mediaData

    fun getAudioFile(file: File) {
       val music = Music(file.name, file.absolutePath)
        _audioPath.value.add(music)
    }

    fun mediaListData(data: Music, mediaPlayer: MediaPlayer?) {
        _mediaData.value = data
        if(data.isPlaying){
            _mediaData.update { it.copy(isPlaying = false) }

        }else{
            _mediaData.update { it.copy(duration = mediaPlayer!!.duration) }
            _mediaData.update { it.copy(isPlaying = true) }
        }

    }
}
