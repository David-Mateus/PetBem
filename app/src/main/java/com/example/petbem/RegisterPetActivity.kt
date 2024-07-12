package com.example.petbem

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petbem.model.entities.PetItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception

class RegisterPetActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var emptyImageView: LinearLayout
    private val itemList = mutableListOf<PetItem>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_pet)
        title = "Meus Pets"
        emptyImageView = findViewById(R.id.linear_layout_isEmpty)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = ItemAdapter(this, itemList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadPetsFromDatabase()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, FormsPet::class.java)
            startActivity(intent)
        }
    }

    private fun loadPetsFromDatabase() {
        Thread {
            val app = application as App
            val dao = app.db.petDao()
            val pets = dao.getAllPets()
            runOnUiThread {
                itemList.clear()
                itemList.addAll(pets)
                adapter.notifyDataSetChanged()
                checkIfEmpty()
            }
        }.start()
    }

    private fun checkIfEmpty() {
        if (itemList.isEmpty()) {
            emptyImageView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyImageView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private inner class ItemAdapter(
        private val context: Context,
        private val petItemList: List<PetItem>
    ) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val linearLayoutItem: CardView = itemView.findViewById(R.id.list_linear_layout)
            val textViewItem: TextView = itemView.findViewById(R.id.text_name_pet)
            val imageView: ImageView = itemView.findViewById(R.id.icon_pet_image)
            val cardMenuBtn: ImageButton = itemView.findViewById(R.id.card_menu_pet)

            init {
                // Clique para visualizar os detalhes do pet
                linearLayoutItem.setOnClickListener {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("petId", petItemList[adapterPosition].id)
                    context.startActivity(intent)
                }

                cardMenuBtn.setOnClickListener {
                    val popup = PopupMenu(context, cardMenuBtn)
                    popup.inflate(R.menu.menu)
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.btn_delete -> {
                                deletePet(petItemList[adapterPosition])
                                true
                            }
                            R.id.btn_edit -> {
                                val intent = Intent(context, FormsPet::class.java)
                                intent.putExtra("petId", petItemList[adapterPosition].id)
                                context.startActivity(intent)
                                true
                            }
                            else -> false
                        }
                    }
                    popup.show()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.register_recycler_view, parent, false)
            return ItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = petItemList[position]
            holder.textViewItem.text = item.namePet

            val imageUri = Uri.parse(item.photoPet)
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                holder.imageView.setImageBitmap(bitmap)
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
                holder.imageView.setImageResource(R.drawable.dica1) // Imagem de fallback
            }
        }

        override fun getItemCount(): Int {
            return petItemList.size
        }

        fun deletePet(pet: PetItem) {
            Thread {
                val app = context.applicationContext as App
                val dao = app.db.petDao()
                dao.delete(pet)

                runOnUiThread {
                    itemList.remove(pet)
                    notifyDataSetChanged()
                    checkIfEmpty()
                }
            }.start()
        }
    }
}
