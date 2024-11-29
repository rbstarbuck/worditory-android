package com.example.worditory.audio

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import com.example.worditory.R
import com.example.worditory.setSoundEnabled
import com.example.worditory.soundEnabled
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal object AudioPlayer {
    private lateinit var gameOn: MediaPlayer
    private lateinit var pops: MediaPlayer
    private lateinit var gameOverWin: MediaPlayer
    private lateinit var gameOverLose: MediaPlayer

    private var enabled = true

    internal fun init(context: Context) {
        gameOn = MediaPlayer.create(context, R.raw.game_on)
        pops = MediaPlayer.create(context, R.raw.word_played)
        gameOverWin = MediaPlayer.create(context, R.raw.game_over_win)
        gameOverLose = MediaPlayer.create(context, R.raw.game_over_lose)

        GlobalScope.launch {
            enabled = context.soundEnabled().first()
        }
    }

    internal fun setEnabled(e: Boolean, context: Context) {
        enabled = e

        GlobalScope.launch {
            context.setSoundEnabled(e)
        }
    }

    internal fun gameOn() {
        if (enabled) {
            gameOn.start()
        }
    }

    internal fun wordPlayed(numLetters: Int) {
        if (enabled) {
            val time = numLetters * 225L
            val timer = object: CountDownTimer(time, time) {
                override fun onTick(p0: Long) {}

                override fun onFinish() {
                    pops.pause()
                    pops.seekTo(0)
                }
            }

            pops.start()
            timer.start()
        }
    }

    internal fun gameOverWin() {
        if (enabled) {
            gameOverWin.start()
        }
    }

    internal fun gameOverLose() {
        if (enabled) {
            gameOverLose.start()
        }
    }
}