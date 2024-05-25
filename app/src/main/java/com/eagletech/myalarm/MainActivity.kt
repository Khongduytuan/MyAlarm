package com.eagletech.myalarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timePicker: TimePicker = findViewById(R.id.timePicker)
        val setAlarmButton: Button = findViewById(R.id.setAlarmButton)
        val stopButton: Button = findViewById(R.id.stopButton)

        setAlarmButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                calendar.set(Calendar.MINUTE, timePicker.minute)
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.currentHour)
                calendar.set(Calendar.MINUTE, timePicker.currentMinute)
            }
            calendar.set(Calendar.SECOND, 0)

            setAlarm(calendar.timeInMillis)
        }

        stopButton.setOnClickListener {
            stopAlarm()
            Toast.makeText(this, "Alarm stopped!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

        Toast.makeText(this, "Alarm set!", Toast.LENGTH_SHORT).show()
    }

    private fun stopAlarm() {
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            action = "com.eagletech.myalarm.ACTION_STOP_ALARM"
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        sendBroadcast(intent)
    }
}
