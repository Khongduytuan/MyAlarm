package com.eagletech.myalarm

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timePicker: TimePicker = findViewById(R.id.timePicker)
        val setAlarmButton: Button = findViewById(R.id.setAlarmButton)

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
    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

        Toast.makeText(this, "Alarm set!", Toast.LENGTH_SHORT).show()
    }
}
