package com.test.myplaylist.ui.screen.home

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.myplaylist.base.BaseFragment
import com.test.myplaylist.common.AlertDialogMessageData
import com.test.myplaylist.databinding.FragmentHomeBinding
import com.test.myplaylist.extension.provideViewModels
import com.test.myplaylist.extension.showAlertDialogFragment
import com.test.myplaylist.util.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import org.apache.commons.io.FileUtils
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
    lateinit var cacheDir: File
    private lateinit var musicListAdapter: MusicListAdapter


    override fun setupView() {
        super.setupView()
        binding.lifecycleOwner = this
        with(binding) {

            onCheckOpenGalleriesWithPermissionCheck()

            val selectAudioActivityResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val data: Intent? = result.data
                        //If multiple image selected
                        if (data?.clipData != null) {
                            val count = data.clipData?.itemCount ?: 0
                            for (i in 0 until count) {
                                val audioUri: Uri? = data.clipData?.getItemAt(i)?.uri
                                val file = getAudioFromUri(audioUri)
                                file?.let {
                                    this@HomeFragment.viewModel.getAudioFile(it.absolutePath)
                                }
                            }
                        }
                        //If single audio selected
                        else if (data?.data != null) {
                            val audioUri: Uri? = data.data
                            val file = getAudioFromUri(audioUri)
                            file?.let {
                                this@HomeFragment.viewModel.getAudioFile(it.absolutePath)
                            }
                        }
                    }
                }

            btnSelectImage.setOnClickListener {
                val intent = Intent(ACTION_GET_CONTENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.type = "*/*"
                selectAudioActivityResult.launch(intent)
            }
            try {
                deleteTempFiles()
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
        }
    }


    override fun bindViewModel() {
        viewModel.audioPath bindTo ::bindData
    }

    private fun bindData(data: List<String>) {
        with(musicListAdapter) {
            items = data.toMutableList()
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
            val mimeType = getMimeType(requireContext(), uri)
            mimeType?.let {
                val file = createTmpFileFromUri(requireContext(), audioUri, "temp_audio", ".$it")
                file?.let {
                    Timber.d("audio Url = ${file.absolutePath}")
                }
                return file
            }
        }
        return null
    }


    private fun getMimeType(context: Context, uri: Uri): String? {
        //Check uri format to avoid null
        val extension: String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //If scheme is a content
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
        }
        return extension
    }

    private fun createTmpFileFromUri(
        context: Context,
        uri: Uri,
        fileName: String,
        mimeType: String
    ): File? {
        return try {
            val stream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile(fileName, mimeType, cacheDir)
            FileUtils.copyInputStreamToFile(stream, file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun deleteTempFiles(file: File = cacheDir): Boolean {
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (f in files) {
                    if (f.isDirectory) {
                        deleteTempFiles(f)
                    } else {
                        f.delete()
                    }
                }
            }
        }
        return file.delete()
    }

    companion object {
        private const val PERMISSIONS_SETTING = "permissions_setting"
    }
}