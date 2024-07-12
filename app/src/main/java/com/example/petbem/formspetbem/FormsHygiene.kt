package com.example.petbem.formspetbem

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petbem.App
import com.example.petbem.HygieneActivity
import com.example.petbem.R
import com.example.petbem.RegisterPetActivity
import com.example.petbem.model.dao.HygieneDao
import com.example.petbem.model.entities.HygieneItem
import com.example.petbem.model.entities.PetItem

class FormsHygiene : AppCompatActivity() {
    private var petId: Int = 0
    private var hygieneId:Int = 0
    private lateinit var titleHygieneInput: EditText
    private lateinit var dataHygieneInput: EditText
    private lateinit var btnAddHygiene: Button
    private lateinit var hygieneDao: HygieneDao

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forms_hygiene)
        titleHygieneInput = findViewById(R.id.higiene_Input)
        dataHygieneInput = findViewById(R.id.higiene_date_Input)
        btnAddHygiene = findViewById(R.id.add_higiene_btn)

        hygieneDao = (application as App).db.HygieneDao()

        petId = intent.getIntExtra("petId", 0)
        hygieneId = intent.getIntExtra("hygieneId", 0)
        Log.d("PetId", "receive-petId: $petId")
        Log.d("HygieneId", "receive-petId: $hygieneId")


        if(hygieneId != 0){
            loadPetDetails(hygieneId)
        }

        btnAddHygiene.setOnClickListener {
            val title = titleHygieneInput.text.toString()
            val data = dataHygieneInput.text.toString()

                if(hygieneId == 0){
                    if (title.isNotEmpty() && data.isNotEmpty()) {
                        saveNewHygiene(title, data, petId)
                    } else {
                        Toast.makeText(this, "Preencher todos os dados", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    updateHygiene(hygieneId, title, data, petId)
                }


        }



    }

    private fun saveNewHygiene(title: String, data: String, petId: Int) {
        Thread {

            val app = application as App
            val dao = app.db.HygieneDao()
            dao.insertHygiene(HygieneItem(titleHygiene = title, dateHygiene = data, id = petId))
            runOnUiThread {
                val intent = Intent(this@FormsHygiene, HygieneActivity::class.java)
                intent.putExtra("petId", petId)
                startActivity(intent)
                Toast.makeText(this@FormsHygiene, "Higiene adicionada", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
    private fun updateHygiene(idHygiene: Int,title: String, data: String, petId: Int){

        Thread {
            val app = application as App
            val dao = app.db.HygieneDao()
            val pet = HygieneItem(idHygiene = idHygiene, titleHygiene = title, dateHygiene = data, id = petId)
            dao.updateHygiene(pet)
            runOnUiThread {
                Toast.makeText(this@FormsHygiene, "Atualizado", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@FormsHygiene, HygieneActivity::class.java)
                intent.putExtra("petId", petId)
                startActivity(intent)
                finish()
            }
        }.start()
    }
    private fun loadPetDetails(petId: Int){
        Log.d("carregando detalhes", "entrou")
        Thread{
            val hygiene = hygieneDao.getHygieneById(petId)
            Log.d("carregando detalhes", "veja $hygiene")

            runOnUiThread{
                titleHygieneInput.setText(hygiene.titleHygiene)
                dataHygieneInput.setText(hygiene.dateHygiene)

            }
        }.start()
    }
}

