package com.example.petbem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petbem.formspetbem.FormsHygiene
import com.example.petbem.model.entities.HygieneItem
import com.example.petbem.model.entities.PetItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HygieneActivity : AppCompatActivity() {
    private var petId : Int = 0
    private var hygieneId: Int = 0
    private lateinit var rvHygiene: RecyclerView
    private var hygieneItem = mutableListOf<HygieneItem>()
    private lateinit var adapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hygiene)
        title = "Higiene"
        petId = intent.getIntExtra("petId", 0)
        Log.d("receive-petId", "$petId")


        adapter = MainAdapter(this, hygieneItem)
        rvHygiene = findViewById(R.id.rv_hygiene)
        rvHygiene.adapter = adapter
        rvHygiene.layoutManager = LinearLayoutManager(this)

        loadPetsFromDatabase(petId)

        val fab = findViewById<FloatingActionButton>(R.id.fab_higiene)
        fab.setOnClickListener {

            val intent = Intent(this, FormsHygiene::class.java)
            intent.putExtra("petId", petId)
            Log.d("Send-PetId", "$petId")
            startActivity(intent)

        }
    }




    private fun loadPetsFromDatabase(hygieneId:Int) {
        Thread {

            val app = application as App
            val dao = app.db.HygieneDao()
            val hygiene = dao.getAllHygiene(hygieneId)
            runOnUiThread {
                hygieneItem.clear()
                hygieneItem.addAll(hygiene)
                adapter.notifyDataSetChanged()
//                checkIfEmpty()
            }
        }.start()
    }

    private inner class MainAdapter(private val context: Context,
                                    private val petHygieneList: List<HygieneItem>)
        : RecyclerView.Adapter<MainAdapter.ItemViewHolder>(){

        inner class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            val textTitlePet: TextView = itemView.findViewById(R.id.title_hygiene)
            val textDataPet: TextView = itemView.findViewById(R.id.data_hygiene)
            val cardMenuPet: ImageButton = itemView.findViewById(R.id.card_menu_hygiene)

            init{
                cardMenuPet.setOnClickListener {
                    val poup = PopupMenu(context, cardMenuPet)
                    poup.inflate(R.menu.menu)
                    poup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.btn_delete -> {
                                deleteHygiene(petHygieneList[adapterPosition])
                                Toast.makeText(context, "delete clicked", Toast.LENGTH_SHORT).show()
                                true
                            }

                            R.id.btn_edit -> {
                                val intent = Intent(context, FormsHygiene::class.java)
                                intent.putExtra("petId", hygieneItem[adapterPosition].id)
                                intent.putExtra("hygieneId",hygieneItem[adapterPosition].idHygiene)

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
            val view = layoutInflater.inflate(R.layout.hygiene_recycler_view, parent, false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = petHygieneList[position]
            holder.textTitlePet.text = item.titleHygiene
            holder.textDataPet.text = item.dateHygiene
        }


        override fun getItemCount(): Int {
            return petHygieneList.size
        }
        fun deleteHygiene(hygienePet: HygieneItem) {
            Thread {
                val app = context.applicationContext as App
                val dao = app.db.HygieneDao()
                dao.deleteHygiene(hygienePet)

                runOnUiThread {
                    hygieneItem.remove(hygienePet)
                    notifyDataSetChanged()
//                    checkIfEmpty()
                }
            }.start()
        }

    }
    // carregar todos

}