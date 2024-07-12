package com.example.petbem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petbem.formspetbem.FormsHygiene
import com.example.petbem.formspetbem.FormsMedicines
import com.example.petbem.model.entities.HygieneItem
import com.example.petbem.model.entities.MedicinesItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MedicinesActivity : AppCompatActivity() {
    private var petId : Int = 0
    private lateinit var rvMedicines: RecyclerView
    private var medicinesItem = mutableListOf<MedicinesItem>()
    private lateinit var adapter : MainAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines)
        title = "Medicamentos"

        val fab = findViewById<FloatingActionButton>(R.id.fab_medicines)
        adapter = MainAdapter(this, medicinesItem)
        rvMedicines = findViewById(R.id.rv_medicines)
        rvMedicines.adapter = adapter
        rvMedicines.layoutManager = LinearLayoutManager(this)

        petId = intent.getIntExtra("petId", 0)
        Log.d("receive-petId", "$petId")

        loadPetsFromDatabase(petId)

        fab.setOnClickListener {

            val intent = Intent(this, FormsMedicines::class.java)
            intent.putExtra("petId", petId)
            Log.d("Send-PetId", "$petId")
            startActivity(intent)

        }
    }
    private fun loadPetsFromDatabase(medicinesId:Int) {
        Thread {

            val app = application as App
            val dao = app.db.medicinesDao()
            val medicines = dao.getAllMedicines(medicinesId)
            runOnUiThread {
                medicinesItem.clear()
                medicinesItem.addAll(medicines)
                adapter.notifyDataSetChanged()
//                checkIfEmpty()
            }
        }.start()
    }
    private inner class MainAdapter(private val context: Context,
                                    private val petMedicinesList: List<MedicinesItem>)
        : RecyclerView.Adapter<MainAdapter.ItemViewHolder>(){

        inner class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            val nameMedicinesPet: TextView = itemView.findViewById(R.id.title_medicines)
            val startDataMedicinesPet: TextView = itemView.findViewById(R.id.data_medicines)
            val endDataMedicinesPet: TextView = itemView.findViewById(R.id.last_medicines_data)
            val cardMenuPet: ImageButton = itemView.findViewById(R.id.card_menu_medicines)

            init{
                cardMenuPet.setOnClickListener {
                    val poup = PopupMenu(context, cardMenuPet)
                    poup.inflate(R.menu.menu)
                    poup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.btn_delete -> {
                                deleteMedicines(petMedicinesList[adapterPosition])
                                Toast.makeText(context, "delete clicked", Toast.LENGTH_SHORT).show()
                                true
                            }

                            R.id.btn_edit -> {
                                val intent = Intent(context, FormsMedicines::class.java)
                                intent.putExtra("petId", medicinesItem[adapterPosition].petId)
                                intent.putExtra("medicinesId",medicinesItem[adapterPosition].idMedication)

                                context.startActivity(intent)
                                Toast.makeText(context, "Edit clicked", Toast.LENGTH_SHORT).show()
                                true
                            }

                            else -> false
                        }
                    }
                    poup.show()
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.medicines_recycler_view, parent, false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = petMedicinesList[position]
            holder.nameMedicinesPet.text = item.nameMedication
            holder.startDataMedicinesPet.text = item.startMedication
            holder.endDataMedicinesPet.text = item.endMedication
        }


        override fun getItemCount(): Int {
            return petMedicinesList.size
        }
        fun deleteMedicines(medicinesId: MedicinesItem) {
            Thread {
                val app = context.applicationContext as App
                val dao = app.db.medicinesDao()
                dao.deleteMedicines(medicinesId)

                runOnUiThread {
                    medicinesItem.remove(medicinesId)
                    notifyDataSetChanged()
//                    checkIfEmpty()
                }
            }.start()
        }

    }
}