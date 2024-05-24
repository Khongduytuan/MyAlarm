package com.eagletech.myalarm


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class AlarmReceiver : BroadcastReceiver() {
    private var ringtone: Ringtone? = null

    override fun onReceive(context: Context, intent: Intent) {
        // Lấy URI của âm thanh báo thức mặc định
        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Phát âm thanh báo thức
        ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone?.play()

        // Rung điện thoại nếu có
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(2000)
        }
    }

    // Đảm bảo ngừng phát âm thanh khi cần thiết
    fun stopRingtone() {
        ringtone?.stop()
    }
}

