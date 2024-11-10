package com.example.worditory.game.audio

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import com.example.worditory.R
import com.example.worditory.setSoundEnabled
import com.example.worditory.soundEnabled
import kotlinx.coroutines.GlobalScope
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
            context.soundEnabled().collect {
                enabled = it
            }
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
            GlobalScope.launch {
                gameOn.start()
            }
        }
    }

    internal fun wordPlayed(numLetters: Int) {
        if (enabled) {
            val time = when (numLetters) {
                2 -> 175L
                3 -> 315L
                4 -> 475L
                5 -> 650L
                6 -> 750L
                7 -> 850L
                else -> 1500L
            }
            val timer = object : CountDownTimer(time, time) {
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
            GlobalScope.launch {
                gameOverWin.start()
            }
        }
    }

    internal fun gameOverLose() {
        if (enabled) {
            GlobalScope.launch {
                gameOverLose.start()
            }
        }
    }
}