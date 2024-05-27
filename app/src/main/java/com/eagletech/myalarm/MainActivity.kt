package com.eagletech.myalarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.eagletech.myalarm.appData.MyData
import com.eagletech.myalarm.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myData: MyData

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myData = MyData.getInstance(this)
        funUI()

        binding.setAlarmButton.setOnClickListener {
            if (myData.isPremiumSaves == true){
                val calendar = Calendar.getInstance()
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    calendar.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
                    calendar.set(Calendar.MINUTE, binding.timePicker.minute)
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, binding.timePicker.currentHour)
                    calendar.set(Calendar.MINUTE, binding.timePicker.currentMinute)
                }
                calendar.set(Calendar.SECOND, 0)

                setAlarm(calendar.timeInMillis)
            } else if (myData.getSaves() > 0){
                val calendar = Calendar.getInstance()
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    calendar.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
                    calendar.set(Calendar.MINUTE, binding.timePicker.minute)
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, binding.timePicker.currentHour)
                    calendar.set(Calendar.MINUTE, binding.timePicker.currentMinute)
                }
                calendar.set(Calendar.SECOND, 0)
                setAlarm(calendar.timeInMillis)
                myData.removeSaves()
            }
            else{
                Toast.makeText(this, "You must buy it to continue using it!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PayBuyActivity::class.java)
                startActivity(intent)
            }

        }

        binding.stopButton.setOnClickListener {
            stopAlarm()
            Toast.makeText(this, "Alarm stopped!", Toast.LENGTH_SHORT).show()
        }

        binding.topApp.iconBuy.setOnClickListener {
            val intent = Intent(this, PayBuyActivity::class.java)
            startActivity(intent)
        }
        binding.topApp.iconInfo.setOnClickListener {
            showInfoDialog()
        }


    }


    private fun funUI() {
        if (myData.isPremiumSaves == true){
            binding.setAlarmButton.visibility = View.VISIBLE
            binding.stopButton.visibility = View.VISIBLE

        }else{
            if ((myData.getSaves() > 0)) {
                binding.setAlarmButton.visibility = View.VISIBLE
                binding.stopButton.visibility = View.VISIBLE

            } else {
                binding.setAlarmButton.visibility = View.GONE
                binding.stopButton.visibility = View.GONE
            }
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

    // Show dialog cho dữ liệu SharePreferences
    private fun showInfoDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Infor")
            .setPositiveButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
        if (myData.isPremiumSaves == true){
            dialog.setMessage("You have successfully registered")
        }else{
            dialog.setMessage("You have ${myData.getSaves()}")
        }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        funUI()
    }

}
