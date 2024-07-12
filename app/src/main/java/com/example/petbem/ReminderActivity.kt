package com.example.petbem
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petbem.formspetbem.FormsReminderActivity
import com.example.petbem.model.entities.ReminderItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReminderActivity : AppCompatActivity() {
    private lateinit var rvReminder : RecyclerView
    private var reminderItem = mutableListOf<ReminderItem>()
    private lateinit var adapter : MainAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        rvReminder = findViewById(R.id.rv_reminder)
        adapter = MainAdapter(reminderItem)
        rvReminder.adapter = adapter
        rvReminder.layoutManager = LinearLayoutManager(this)


        val fab = findViewById<FloatingActionButton>(R.id.fab_reminder)
        fab.setOnClickListener {

            val intent = Intent(this, FormsReminderActivity::class.java)
            startActivity(intent)

        }
    }

    private inner class MainAdapter(private val reminderItem: List<ReminderItem>) : RecyclerView.Adapter<MainViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.reminder_recycler_view, parent, false)
            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = reminderItem[position]

        }

        override fun getItemCount(): Int {
            return reminderItem.size
        }

    }
    private class MainViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){

    }
}