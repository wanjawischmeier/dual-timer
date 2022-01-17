package com.dualtimer

import android.media.MediaPlayer
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.getSystemService

class MainActivity : AppCompatActivity() {
    lateinit var seekBar: SeekBar
    lateinit var selected: TextView
    lateinit var timer: CountDownTimer
    lateinit var sound1: MediaPlayer
    lateinit var sound2: MediaPlayer
    var timerRunning = false
    var time1 = 0
    var time2 = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                var minutes = progress +1
                if (selected.id == R.id.timer1)
                    time1 = minutes
                else
                    time2 = minutes

                var minuteString = minutes.toString().padStart(2, '0')
                selected.text = "$minuteString : 00"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        }

        seekBar = findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(listener)
        selected = findViewById(R.id.timer1)
        selected.scaleX = 1.1f
        selected.scaleY = 1.1f

        sound1 = MediaPlayer.create(applicationContext, R.raw.wooden_train_whistle)
        sound2 = MediaPlayer.create(applicationContext, R.raw.wooden_train_whistle)
    }

    fun selectTimer(timerView: View) {
        var time: Int
        if (selected.id == R.id.timer1)
            time = time1
        else
            time = time2

        selected.text = time.toString().padStart(2, '0') + " : 00"
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

    fun toggleTimer(view: View? = null) {
        /*
        val vibrator = getSystemService(Vibrator::class.java)
        val vibe = VibrationEffect.createOneShot(100, 1)
        vibrator.vibrate(vibe)
         */

        if (timerRunning) {
            timer.cancel()
            timerRunning = false

            val timer1 = findViewById<TextView>(R.id.timer1)
            val timer2 = findViewById<TextView>(R.id.timer2)
            timer1.text = time1.toString().padStart(2, '0') + " : 00"
            timer2.text = time2.toString().padStart(2, '0') + " : 00"

            seekBar.isEnabled = true
        }
        else {
            seekBar.isEnabled = false

            val seconds: Int
            if (selected.id == R.id.timer1)
                seconds = time1
            else
                seconds = time2
            // val time = seconds.toLong() * 60000
            val time = seconds.toLong() * 1000

            timer = object : CountDownTimer(time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val leftSeconds = millisUntilFinished / 1000
                    val shownSeconds = leftSeconds % 60
                    val shownMinutes = (leftSeconds - shownSeconds) / 60
                    val shownMinutesString = shownMinutes.toString().padStart(2, '0')
                    val shownSecondsString = shownSeconds.toString().padStart(2, '0')

                    selected.text = "$shownMinutesString : $shownSecondsString"
                }

                override fun onFinish() {
                    timerRunning = false

                    var time: Int
                    var newSelected: TextView

                    if (selected.id == R.id.timer1) {
                        sound1.start()
                        time = time1
                        newSelected = findViewById(R.id.timer2)
                    }
                    else {
                        sound2.start()
                        time = time2
                        newSelected = findViewById(R.id.timer1)
                    }

                    selected.text = time.toString().padStart(2, '0') + " : 00"

                    selectTimer(newSelected)
                    toggleTimer()
                }
            }
            timer.start()
            timerRunning = true
        }
    }
}