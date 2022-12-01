package com.test.myplaylist.ui.screen.home

import android.media.MediaPlayer
import android.os.CountDownTimer
import android.content.Context
import android.net.Uri
import com.test.myplaylist.domain.Music
import java.lang.Exception

class PlayAudioManager(
    private val context: Context,
    val mediaPost: Music,
    private val listener: ActionAudioListener?
) {
    private var mediaPlayer: MediaPlayer? = null
    var isPlaying = false

    private var countDownTimer: CountDownTimer? = null

    fun playAudio() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(mediaPost.filePath))
        }
        mediaPlayer!!.start()
        mediaPlayer!!.setOnCompletionListener { killMediaPlayer() }
        mediaPlayer!!.setOnPreparedListener { mediaPlayer: MediaPlayer ->
            countDownTimer = object : CountDownTimer(mediaPlayer.duration.toLong(), 1000) {
                override fun onTick(p0: Long) {
                    listener?.duration(mediaPlayer.duration, p0)
                }

                override fun onFinish() {
                    if (listener != null) {
                        listener.duration(0, 0)
                        listener.onFinish()
                    }
                }
            }
            countDownTimer?.start()
        }
        isPlaying = true
    }

    fun killMediaPlayer() {
        if (mediaPlayer != null) {
            isPlaying = false
            try {
                mediaPlayer!!.reset()
                mediaPlayer!!.release()
                mediaPlayer = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (countDownTimer != null) {
                countDownTimer!!.onFinish()
                countDownTimer!!.cancel()
            }
        }
    }

    interface ActionAudioListener {
        fun duration(totalDuration: Int, countdown: Long)
        fun onFinish()
    }
}