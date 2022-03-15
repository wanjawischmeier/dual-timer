package com.dualtimer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private lateinit var timer: CountDownTimer
    lateinit var selected: TextView
    lateinit var sounds: Array<MediaPlayer>
    var timerRunning = false
    var time1 = 0
    var time2 = 0
    var numSounds = 4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val minutes = progress + 1
                if (selected.id == R.id.timer1)
                    time1 = minutes
                else
                    time2 = minutes

                val shownMinutesString = minutes.toString().padStart(2, '0')
                selected.text = getString(R.string.timer_template, shownMinutesString, "00")
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        }

        seekBar = findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(listener)
        selected = findViewById(R.id.timer1)
        selected.scaleX = 1.1f
        selected.scaleY = 1.1f

        sounds = Array(numSounds) {
            i -> val id = resources.getIdentifier("sound$i", "raw", packageName)
            return@Array MediaPlayer.create(applicationContext, id)
        }
    }

    fun selectTimer(timerView: View) {
        val oldTime: Int
        val newTime: Int
        if (selected.id == R.id.timer1) {
            oldTime = time1
            newTime = time2
        }
        else {
            oldTime = time2
            newTime = time1
        }

        if (timerRunning && newTime == 0) return

        val shownMinutesString = oldTime.toString().padStart(2, '0')
        selected.text = getString(R.string.timer_template, shownMinutesString, "00")
        selected.scaleX = 1f
        selected.scaleY = 1f
        selected = timerView as TextView
        selected.scaleX = 1.1f
        selected.scaleY = 1.1f

        if (timerRunning) {
            timerRunning = false
            timer.cancel()
            toggleTimer()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun toggleTimer(view: View? = null) {
        if (timerRunning) {
            timer.cancel()
            timerRunning = false

            val timer1 = findViewById<TextView>(R.id.timer1)
            val timer2 = findViewById<TextView>(R.id.timer2)

            val shownMinutesString1 = time1.toString().padStart(2, '0')
            val shownMinutesString2 = time2.toString().padStart(2, '0')

            timer1.text = getString(R.string.timer_template, shownMinutesString1, "00")
            timer2.text = getString(R.string.timer_template, shownMinutesString2, "00")

            seekBar.isEnabled = true
        }
        else {
            val seconds: Int = if (selected.id == R.id.timer1)
                time1
            else
                time2

            if (seconds == 0) return
            val time = seconds.toLong() * 6000

            seekBar.isEnabled = false

            timer = object : CountDownTimer(time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val leftSeconds = (millisUntilFinished / 1000).toInt()
                    val shownSeconds = leftSeconds % 60
                    val shownMinutes = (leftSeconds - shownSeconds) / 60

                    selected.text = padTimer(shownMinutes, shownSeconds)
                }

                override fun onFinish() {
                    timerRunning = false

                    val oldMinutes: Int
                    val newMinutes: Int
                    val newSelected: TextView

                    if (selected.id == R.id.timer1) {
                        val index = Random.nextInt(numSounds / 2)
                        sounds[index].start()
                        oldMinutes = time1
                        newMinutes = time2
                        newSelected = findViewById(R.id.timer2)
                    }
                    else {
                        val index = Random.nextInt(numSounds / 2, numSounds)
                        sounds[index].start()
                        oldMinutes = time2
                        newMinutes = time1
                        newSelected = findViewById(R.id.timer1)
                    }

                    selected.text = padTimer(oldMinutes)

                    if (newMinutes != 0)
                        selectTimer(newSelected)

                    toggleTimer()
                }
            }
            timer.start()
            timerRunning = true
        }
    }

    fun padTimer(minutes: Int = 0, seconds: Int = 0): String {
        val shownMinutesString = padTime(minutes)
        val shownSecondsString = padTime(seconds)

        return getString(R.string.timer_template, shownMinutesString, shownSecondsString)
    }

    private fun padTime(time: Int): String {
        return time.toString().padStart(2, '0')
    }
}