package com.example.petbem.formspetbem

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
import com.example.petbem.VaccineActivity
import com.example.petbem.model.dao.VaccineDao
import com.example.petbem.model.entities.HygieneItem
import com.example.petbem.model.entities.VaccineItem

class FormsVaccine : AppCompatActivity() {
    private lateinit var vaccineBtn: Button
    private var petId: Int = 0
    private var vaccineId: Int = 0
    private lateinit var vaccineDao: VaccineDao
    private lateinit var nameVaccinePet: EditText
    private lateinit var dataVaccinePet: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forms_vaccine)

        nameVaccinePet = findViewById(R.id.vaccine_input)
        dataVaccinePet = findViewById(R.id.vaccine_date_input)
        vaccineBtn = findViewById(R.id.add_vaccine_btn)

        vaccineDao = (application as App).db.vaccineDao()
        petId = intent.getIntExtra("petId", 0)
        vaccineId = intent.getIntExtra("vaccineId", 0)
        Log.d("PetId", "receive-petId: $petId")
        Log.d("vaccineId", "receive-petId: $vaccineId")
        if(vaccineId != 0){
            loadPetDetails(vaccineId)
        }
        vaccineBtn.setOnClickListener {
            val name = nameVaccinePet.text.toString()
            val data = dataVaccinePet.text.toString()
            if(vaccineId == 0){
                if (name.isNotEmpty() && data.isNotEmpty()) {
                    saveNewVaccine(name, data, petId)
                } else {
                    Toast.makeText(this, "Preencher todos os dados", Toast.LENGTH_SHORT).show()
                }

            }else{
                updateVaccine(vaccineId, name, data, petId)

            }
        }

    }
    private fun saveNewVaccine(title: String, data: String, petId: Int) {
        Thread {

            val app = application as App
            val dao = app.db.vaccineDao()
            dao.insertVaccine(VaccineItem(nameVaccine = title, dataVaccine = data, id = petId))
            runOnUiThread {
                val intent = Intent(this@FormsVaccine, VaccineActivity::class.java)
                intent.putExtra("petId", petId)
                startActivity(intent)
                Toast.makeText(this@FormsVaccine, "Vacina adicionada", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
    private fun updateVaccine(vaccineId: Int,title: String, data: String, petId: Int){

        Thread {
            val app = application as App
            val dao = app.db.vaccineDao()
            val pet = VaccineItem(idVaccine = vaccineId, nameVaccine = title, dataVaccine = data, id = petId)
            dao.updateVaccine(pet)
            runOnUiThread {
                Toast.makeText(this@FormsVaccine, "Atualizado", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@FormsVaccine, VaccineActivity::class.java)
                intent.putExtra("petId", petId)
                startActivity(intent)
                finish()
            }
        }.start()
    }
    private fun loadPetDetails(petId: Int){
        Log.d("carregando detalhes", "entrou")
        Thread{
            val vaccine = vaccineDao.getVaccineById(petId)
            Log.d("carregando detalhes", "veja $vaccine")

            runOnUiThread{
                nameVaccinePet.setText(vaccine.nameVaccine)
                dataVaccinePet.setText(vaccine.dataVaccine)

            }
        }.start()
    }
}