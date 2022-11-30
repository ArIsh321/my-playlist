package com.test.myplaylist.ui.screen.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.myplaylist.base.BaseFragment
import com.test.myplaylist.common.AlertDialogMessageData
import com.test.myplaylist.databinding.FragmentHomeBinding
import com.test.myplaylist.domain.Music
import com.test.myplaylist.extension.*
import com.test.myplaylist.util.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@RuntimePermissions
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var navigator: MainNavigator


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = { inflater, container, attachToParent ->
            FragmentHomeBinding.inflate(inflater, container, attachToParent)
        }

    private val viewModel: HomeViewModel by provideViewModels()
    private lateinit var musicListAdapter: MusicListAdapter
    private var lastVisibleItemPosition = -1
    private var firstVisibleItemPosition = -1

    private var mediaPlayer: MediaPlayer? = null

    private var playAudioManager: PlayAudioManager? = null

    override fun setupView() {
        super.setupView()
        binding.lifecycleOwner = this
        with(binding) {
            val selectAudioActivityResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val data: Intent? = result.data
                        Timber.d("audioPath==${data?.data}")

                        //If multiple image selected
                        if (data?.clipData != null) {
                            val count = data.clipData?.itemCount ?: 0
                            Timber.d("audioPath==$count")

                            for (i in 0 until count) {
                                val audioUri: Uri? = data.clipData?.getItemAt(i)?.uri
                                val file = getAudioFromUri(audioUri)
                                file?.let {
                                    Timber.d("audioPath==${it.absolutePath}")
                                    this@HomeFragment.viewModel.getAudioFile(it)
                                }
                            }
                        }
                        //If single audio selected
                        else if (data?.data != null) {
                            val audioUri: Uri? = data.data
                            val file = getAudioFromUri(audioUri)
                            file?.let {
                                this@HomeFragment.viewModel.getAudioFile(it)
                            }
                        }
                    }
                }

            btnSelectImage.setOnClickListener {
                onCheckOpenGalleriesWithPermissionCheck()
                val intent = Intent(ACTION_GET_CONTENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.type = "audio/*"
                selectAudioActivityResult.launch(intent)
            }
            try {
                context?.deleteTempFiles()
            } catch (e: Exception) {
            }
        }
        setupDataList()
    }

    private fun setupDataList() {
        with(binding.rvMusic) {
            adapter = MusicListAdapter().also { musicListAdapter = it }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    when (val layoutManager = recyclerView.layoutManager) {
                        is LinearLayoutManager -> {
                            lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                        }
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager
                    val totalItemCount = layoutManager!!.itemCount
                    val visibleItemCount = layoutManager.childCount
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (visibleItemCount > 0 && lastVisibleItemPosition >= totalItemCount - 1) {

                        }
                        if (firstVisibleItemPosition == 0) {
                        }
                    }
                }

            })
        }
    }


    override fun bindViewModel() {
        viewModel.audioPath bindTo ::bindData
    }

    private fun bindData(data: List<Music>) {
        with(musicListAdapter) {
            items = data.toMutableList()
        }
    }

    override fun bindViewEvents() {
        super.bindViewEvents()
        musicListAdapter.itemClick.bindTo {
            when (it) {
                is MusicListAdapter.OnItemClick.OnPlayAudio ->{
                    onPlayAudioMedia(it.data)
                }
            }
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onCheckOpenGalleries() {
    }


    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onNeverAskAgainGalleries() {
        showAlertDialogFragment(
            AlertDialogMessageData.dataDialogSettings(PERMISSIONS_SETTING)
        )
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showRationaleForGalleries(request: permissions.dispatcher.PermissionRequest) {
        request.proceed()
    }

    private fun getAudioFromUri(audioUri: Uri?): File? {
        audioUri?.let { uri ->
            val mimeType = context?.getMimeType(uri)
            mimeType?.let {
                val file = context?.createTmpFileFromUri( audioUri, "audio", ".$it")
                file?.let {
                    Timber.d("audio Url = ${file.absolutePath}")
                }
                return file
            }
        }
        return null
    }

    private fun onPlayAudioMedia(music: Music) {
            if (playAudioManager != null && playAudioManager!!.isPlaying && music.name != playAudioManager!!.mediaPost.name) {
                musicListAdapter.onPauseAllAudio(music)
                playAudioManager!!.killMediaPlayer()
            }
            if (music.isPlaying) {
                playAudioManager = PlayAudioManager(requireContext(), music, object : PlayAudioManager.ActionAudioListener{
                    override fun duration(countdown: Long) {
                        //todo update item progress

                    }

                    override fun onFinish() {
                        music.isPlaying = false
                        musicListAdapter.updateItem(music)
                    }
                })
                try {
                    playAudioManager!!.playAudio()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                if (playAudioManager != null) {
                    playAudioManager!!.killMediaPlayer()
                }
                music.isPlaying = false
                musicListAdapter.updateItem(music)
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        playAudioManager?.killMediaPlayer()
        if(mediaPlayer!=null){
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }


    companion object {
        private const val PERMISSIONS_SETTING = "permissions_setting"
    }


}