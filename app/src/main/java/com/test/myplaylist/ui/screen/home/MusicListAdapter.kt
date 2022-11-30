package com.test.myplaylist.ui.screen.home

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.test.myplaylist.R
import com.test.myplaylist.common.ItemClickable
import com.test.myplaylist.common.ItemClickableImpl
import com.test.myplaylist.domain.Music
import com.test.myplaylist.databinding.ItemAudioListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit

@SuppressLint("NotifyDataSetChanged")
internal class MusicListAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ItemClickable<MusicListAdapter.OnItemClick> by ItemClickableImpl() {
    var items = mutableListOf<Music?>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var mediaPlayer: MediaPlayer
    var seekHandler = Handler()
    var runnable: Runnable? = null

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return ViewHolderItem(ItemAudioListBinding.inflate(inflate, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MusicListAdapter.ViewHolderItem -> {
                holder.bind(items[position])
            }
        }
    }

    fun pauseAudio() {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }


    internal inner class ViewHolderItem(
        private val binding: ItemAudioListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                items[adapterPosition]?.let { data ->
                    CoroutineScope(Dispatchers.IO).launch {
                        data.let {
                        }
                    }
                }
            }
        }

        fun bind(model: Music?) {
            model?.let { data ->
                with(binding) {
                    fileName = data.name
                    playAudio(data.filePath)
                }
            }
        }

        private fun playAudio(data: String) {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                mediaPlayer.setDataSource(data)
                mediaPlayer.prepare()
            } catch (e: IOException) {
                Timber.d("error ${e.printStackTrace()}")
            }

            with(binding) {
                seekBar.max = mediaPlayer.duration
                seekBar.tag = layoutPosition
                audioFileName.text = "(" + "0:0/" + calculateDuration(
                    mediaPlayer.duration
                ) + ")"

                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar,
                        progress: Int,
                        fromUser: Boolean,
                    ) {
                        if (mediaPlayer != null && fromUser) {
                            mediaPlayer.seekTo(progress)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar) {}
                })

                ivIcon.setOnClickListener {
                    if (!mediaPlayer.isPlaying) {
                        mediaPlayer.start()
//                        holder.btn_play.setText("Pause")
                        ivIcon.setImageResource(R.drawable.ic_pause)
                        runnable = Runnable {
                            // Updateing SeekBar every 100 miliseconds
                            seekBar.progress = mediaPlayer.currentPosition
                            seekHandler.postDelayed(runnable!!, 100)
                            //For Showing time of audio(inside runnable)
                            val miliSeconds = mediaPlayer.currentPosition
                            val totalTime = mediaPlayer.duration

                            if (miliSeconds != 0) {
                                //if audio is playing, showing current time;
                                val minutes =
                                    TimeUnit.MILLISECONDS.toMinutes(miliSeconds.toLong())
                                val seconds =
                                    TimeUnit.MILLISECONDS.toSeconds(miliSeconds.toLong())
                                if (minutes == 0L) {
                                    audioFileName.text = "(0:$seconds/" + calculateDuration(
                                        mediaPlayer.duration
                                    ) + ")"
                                } else {
                                    if (seconds >= 60) {
                                        val sec = seconds - minutes * 60
                                        audioFileName.text = "($minutes:$sec/" + calculateDuration(
                                            mediaPlayer.duration
                                        ) + ")"
                                    }
                                }
                            } else {
                                //Displaying total time if audio not playing
                                val minutes =
                                    TimeUnit.MILLISECONDS.toMinutes(totalTime.toLong())
                                val seconds =
                                    TimeUnit.MILLISECONDS.toSeconds(totalTime.toLong())
                                if (minutes == 0L) {
                                    audioFileName.text = "(0:$seconds)"
                                } else {
                                    if (seconds >= 60) {
                                        val sec = seconds - minutes * 60
                                        audioFileName.text = "($minutes:$sec)"
                                    }
                                }
                            }
                        }
                        (runnable as Runnable).run()
                    } else {
                        mediaPlayer.pause()
                        ivIcon.setImageResource(R.drawable.ic_play)
                    }

                }
            }
        }

        private fun calculateDuration(duration: Int): String? {
            var finalDuration = ""
            val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
            val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
            if (minutes == 0L) {
                finalDuration = "0:$seconds"
            } else {
                if (seconds >= 60) {
                    val sec = seconds - minutes * 60
                    finalDuration = "$minutes:$sec"
                }
            }
            return finalDuration
        }


    }

    sealed class OnItemClick {
        data class AUDIOITEM(val data: String) : OnItemClick()
    }
}