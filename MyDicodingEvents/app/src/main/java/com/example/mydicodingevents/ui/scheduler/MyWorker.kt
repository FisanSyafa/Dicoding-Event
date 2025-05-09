package com.example.mydicodingevents.ui.scheduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mydicodingevents.R
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        private val TAG = MyWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding event channel"
    }

    override fun doWork(): Result {
        return try {
            getFirstActive()
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error in MyWorker", e)
            Result.failure()
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_24dp)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    private fun getFirstActive() {
        val client = OkHttpClient()
        val url = "https://event-api.dicoding.dev/events?active=-1&limit=1"
        Log.d(TAG, "Get Event Current Active: $url")

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        val result = response.body?.string() ?: return

        Log.d(TAG, result)
        try {
            val responseObject = JSONObject(result)
            val currentEventName: String = responseObject.getJSONArray("listEvents").getJSONObject(0).getString("name")
            val currentEventDate: String = responseObject.getJSONArray("listEvents").getJSONObject(0).getString("beginTime")

            val title = "Current Dicoding Event"
            val message = "$currentEventName\nat $currentEventDate"

            showNotification(title, message)

        } catch (e: Exception) {
            showNotification("Get Current Event Not Success", e.message ?: "Unknown error")
            Log.d(TAG, "onSuccess: Gagal.....")
        }
    }
}