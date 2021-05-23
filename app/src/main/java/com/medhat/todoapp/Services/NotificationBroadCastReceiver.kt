package com.medhat.todoapp.Services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.medhat.todoapp.R
import com.medhat.todoapp.ui.listUi.MainActivity

class NotificationBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val title = intent?.getStringExtra("Title") ?: "Title"
            val content = intent?.getStringExtra("Content") ?: "Content"
            var builder = NotificationCompat.Builder(it, MainActivity.CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(it)) {
                notify(222, builder.build())
            }
        }

    }

}