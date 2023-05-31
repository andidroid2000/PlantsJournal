package optandroid.plantsjournal.Others

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log

class BuildNotificationChannel : Application() {

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"

    override fun onCreate() {

        super.onCreate()
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("LoginActivity", "Version >= OREO")
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}