package com.example.petbem.formspetbem

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.petbem.R

class FormsReminderActivity : AppCompatActivity() {
    private lateinit var editTitle:EditText
    private lateinit var editDate:EditText
    private lateinit var editReminder:EditText
    private lateinit var btnReminderAdd:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forms_reminder)
        editTitle = findViewById(R.id.reminder_title_input)
        editDate = findViewById(R.id.reminder_date_input)
        editReminder = findViewById(R.id.reminder_text_input)
        btnReminderAdd = findViewById(R.id.reminder_add)
        btnReminderAdd.setOnClickListener {
            Toast.makeText(this, "pegou lembrete", Toast.LENGTH_SHORT).show()
        }
    }
}