package com.dualtimer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var selected: TextView
    var timerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // progress = 20
                // out = 20:00

                var minutes = (progress +1).toString().padStart(2, '0')
                selected.text = "$minutes : 00"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        }

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(listener)
        selected = findViewById(R.id.timer1)
    }

    fun selectTimer(timer: View) {
        selected.scaleX = 1f
        selected.scaleY = 1f
        selected = timer as TextView
        selected.scaleX = 1.1f
        selected.scaleY = 1.1f
    }

    fun toggleTimer(view: View) {
        val toast = Toast.makeText(applicationContext, "Lolol", Toast.LENGTH_SHORT)
        toast.show()
        /*
        val vibrator = getSystemService(VIBRATOR_SERVICE)
        val vibe = VibrationEffect.createOneShot(100, 1)
        val attributes = VibrationAttributes.USAGE_ALARM
        */



        if (timerRunning) {

        }
        else {
            var text = selected.text.toString()
            var seconds = text.substring(0, 2).toLong()
            val time = seconds * 60000

            object : CountDownTimer(time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var leftSeconds = millisUntilFinished / 1000
                    var shownSeconds = leftSeconds % 60
                    var shownMinutes = (leftSeconds - shownSeconds) / 60
                    var shownMinutesString = shownMinutes.toString().padStart(2, '0')
                    var shownSecondsString = shownSeconds.toString().padStart(2, '0')

                    selected.text = "$shownMinutesString : $shownSecondsString"
                }

                override fun onFinish() {
                    Toast.makeText(applicationContext, "timer done", Toast.LENGTH_SHORT).show()
                    MediaPlayer.create(applicationContext, R.raw.lol).start()
                }
            }.start()
        }
    }
}