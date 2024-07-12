package com.example.petbem

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petbem.item.MainItem

class CategoriesActivity : AppCompatActivity(), OnItemClickListener {
    //1. Referencia do recyclerView
    private lateinit var rvMain: RecyclerView


    private var petId:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_categories)
        title = "Categorias"
        petId = intent.getIntExtra("petId", 0)
        Log.d("Receive-PetId-CategoriesActity", "$petId")

        val mainItems = mutableListOf<MainItem>()
        mainItems.add(
            MainItem(
                id = 1,
                drawableId = R.drawable.med_icon,
                textStringId = R.string.medicamentos,
                color = Color.rgb(255, 166, 197)
            ),

            )
        mainItems.add(
            MainItem(
                id = 2,
                drawableId = R.drawable.vacina_icon,
                textStringId = R.string.vacinas,
                color = Color.rgb(55, 72, 172)
            ),

            )
        mainItems.add(
            MainItem(
                id = 3,
                drawableId = R.drawable.higine_icon,
                textStringId = R.string.higiene,
                color = Color.rgb(92, 51, 174)
            ),

            )
        mainItems.add(
            MainItem(
                id = 4,
                drawableId = R.drawable.lembrete_icon,
                textStringId = R.string.lembretes,
                color = Color.rgb(4, 226, 107)
            ),

            )

        val adapter = MainAdapter(mainItems, this)
        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)

    }

    override fun onClick(id: Int) {
        Log.d("Test", "Clicou $id")
        when (id) {
            1 -> {
                val intent = Intent(this, MedicinesActivity::class.java)
                intent.putExtra("petId", petId)
                Log.d("Send-PetId", "$petId")
                startActivity(intent)
            }

            2 -> {
                val intent = Intent(this, VaccineActivity::class.java)
                intent.putExtra("petId", petId)
                Log.d("Send-PetId", "$petId")
                startActivity(intent)
            }

            3 -> {
                val intent = Intent(this, HygieneActivity::class.java)
                intent.putExtra("petId", petId)
                Log.d("Send-PetId", "$petId")

                startActivity(intent)
            }

            4 -> {
                val intent = Intent(this, ReminderActivity::class.java)
                intent.putExtra("petId", petId)
                Log.d("Send-PetId", "$petId")
                startActivity(intent)
            }
        }
    }

    // Quando criamos uma inner class, ela so fica visivel dentro da classe Mãe
    private inner class MainAdapter(
        private val mainItems: List<MainItem>,
        private val onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

        //1. Resposanvel para informa recycler qual o layout xml da celula especifica(item)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.categories_recycler_view, parent, false)
            return MainViewHolder(view)
        }

        //2. disparado toda vez que houver uma rolagem na tela e for necessario troca o conteudo da celula
        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = mainItems[position]
            holder.bind(itemCurrent)
        }

        //3. Informar quantas celular essa listagem terá
        override fun getItemCount(): Int {
            return mainItems.size
        }

        // é a classe da celula em si!
        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: MainItem) {
                val img: ImageView = itemView.findViewById(R.id.item_img_icon)
                val name: TextView = itemView.findViewById(R.id.item_txt_ctegories)
                val container: ConstraintLayout = itemView as ConstraintLayout

                img.setImageResource(item.drawableId)
                name.setText(item.textStringId)
                container.setBackgroundColor(item.color)
                container.setOnClickListener {
                    onItemClickListener.onClick(item.id)

                }
            }

        }
    }


}
