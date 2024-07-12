package com.example.petbem

import android.annotation.SuppressLint
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
import com.example.petbem.formspetbem.FormsVaccine
import com.example.petbem.model.entities.HygieneItem
import com.example.petbem.model.entities.VaccineItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VaccineActivity : AppCompatActivity() {

    private var petId : Int = 0
    private lateinit var rvVaccine: RecyclerView
    private var vaccineItem = mutableListOf<VaccineItem>()
    private lateinit var adapter: MainAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine)
        title = "Vacinas"
        petId = intent.getIntExtra("petId", 0)
        Log.d("PetId-vacinas", "$petId")


        adapter = MainAdapter(this, vaccineItem)
        rvVaccine = findViewById(R.id.rv_vaccine)
        rvVaccine.adapter = adapter
        rvVaccine.layoutManager = LinearLayoutManager(this)

        loadVaccinesFromDatabase(petId)


        val fab = findViewById<FloatingActionButton>(R.id.fab_vaccine)
        fab.setOnClickListener {

            val intent = Intent(this, FormsVaccine::class.java)
            intent.putExtra("petId", petId)
            Log.d("Send-PetId", "$petId")
            startActivity(intent)

        }
    }
    private fun loadVaccinesFromDatabase(idVaccines: Int){
        Thread {

            val app = application as App
            val dao = app.db.vaccineDao()
            val vaccine = dao.getAllVaccine(idVaccines)
            runOnUiThread {
                vaccineItem.clear()
                vaccineItem.addAll(vaccine)
                adapter.notifyDataSetChanged()
//                checkIfEmpty()
            }
        }.start()
    }
    private inner class MainAdapter(private val context: Context,
                                    private val petVaccineItem: List<VaccineItem>)
        : RecyclerView.Adapter<MainAdapter.ItemViewHolder>(){

        inner class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            val titleVaccinePet: TextView = itemView.findViewById(R.id.title_vaccine)
            val dataVaccinePet: TextView = itemView.findViewById(R.id.data_vaccine)
            val cardMenuPet: ImageButton = itemView.findViewById(R.id.card_menu_vaccine)

            init{
                cardMenuPet.setOnClickListener {
                    val poup = PopupMenu(context, cardMenuPet)
                    poup.inflate(R.menu.menu)
                    poup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.btn_delete -> {
                                deleteVaccine(petVaccineItem[adapterPosition])
                                Toast.makeText(context, "delete clicked", Toast.LENGTH_SHORT).show()
                                true
                            }

                            R.id.btn_edit -> {
                                val intent = Intent(context, FormsVaccine::class.java)
                                intent.putExtra("petId", petVaccineItem[adapterPosition].id)
                                intent.putExtra("vaccineId",petVaccineItem[adapterPosition].idVaccine)

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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.vaccine_recycler_view, parent, false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = petVaccineItem[position]
            holder.titleVaccinePet.text = item.nameVaccine
            holder.dataVaccinePet.text = item.dataVaccine
        }


        override fun getItemCount(): Int {
            return petVaccineItem.size
        }
        fun deleteVaccine(vaccinePet: VaccineItem) {
            Thread {
                val app = context.applicationContext as App
                val dao = app.db.vaccineDao()
                dao.deleteVaccine(vaccinePet)

                runOnUiThread {
                    vaccineItem.remove(vaccinePet)
                    notifyDataSetChanged()
//                    checkIfEmpty()
                }
            }.start()
        }

    }
}