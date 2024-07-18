package com.example.petbem.formspetbem

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.petbem.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormsReminderActivity : AppCompatActivity() {
    private lateinit var editTitle:EditText
    private lateinit var editDate:EditText
    private lateinit var editReminder:EditText
    private lateinit var btnReminderAdd:Button

    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.activity_forms_reminder)
        editTitle = findViewById(R.id.reminder_title_input)
        editDate = findViewById(R.id.reminder_date_input)
        editReminder = findViewById(R.id.reminder_text_input)
        btnReminderAdd = findViewById(R.id.reminder_add)
        btnReminderAdd.setOnClickListener {
            val data = editDate.text.toString()
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
            sendNotification()
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val descriptionText = "This is a notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification() {
        val title = editTitle.text.toString()
        val reminder = editReminder.text.toString()
        val intent = Intent(this, FormsReminderActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_pet_bem)
            .setContentTitle("Lembrete: $title")
            .setContentText(reminder)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }


}