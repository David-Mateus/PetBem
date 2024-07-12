package com.example.petbem.formspetbem

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.petbem.App
import com.example.petbem.HygieneActivity
import com.example.petbem.MedicinesActivity
import com.example.petbem.R
import com.example.petbem.model.dao.MedicinesDao
import com.example.petbem.model.entities.HygieneItem
import com.example.petbem.model.entities.MedicinesItem

class FormsMedicines : AppCompatActivity() {
    private var petId: Int = 0
    private var medicinesId: Int = 0
    private lateinit var nameMedicinesPet: EditText
    private lateinit var startMedicinesPet: EditText
    private lateinit var endMedicinesPet: EditText
    private lateinit var addMedicinesBtn: Button
    private lateinit var medicinesDao: MedicinesDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forms_medicines)
        nameMedicinesPet = findViewById(R.id.medicines_name)
        startMedicinesPet = findViewById(R.id.medicines_first_date)
        endMedicinesPet = findViewById(R.id.medicines_end_date)
        addMedicinesBtn = findViewById(R.id.addMedicinesButton)

        medicinesDao = (application as App).db.medicinesDao()

        //pegar dados
        petId = intent.getIntExtra("petId", 0)
        medicinesId = intent.getIntExtra("medicinesId", 0)
        Log.d("PetId", "receive-petId: $petId")
        Log.d("medicinesId", "receive-petId: $medicinesId")

        if(medicinesId != 0){
            loadPetDetails(medicinesId)
        }

        addMedicinesBtn.setOnClickListener {
            val name = nameMedicinesPet.text.toString()
            val start = startMedicinesPet.text.toString()
            val end = endMedicinesPet.text.toString()
            if(medicinesId == 0){
                if(name.isNotEmpty() && start.isNotEmpty() && end.isNotEmpty()){
                    saveNewMedicines(name, start, end, petId)
                }else{
                    Toast.makeText(this, "Preencher todos os dados", Toast.LENGTH_SHORT).show()
                }
            }else{
                updateMedicines(medicinesId, name, start, end, petId)
            }
        }
    }
    private fun saveNewMedicines(name: String, start: String, end:String, petId: Int) {
        Thread {

            val app = application as App
            val dao = app.db.medicinesDao()
            dao.insertMedicines(MedicinesItem(nameMedication = name, startMedication = start, endMedication = end, petId = petId))
            runOnUiThread {
                val intent = Intent(this@FormsMedicines, MedicinesActivity::class.java)
                intent.putExtra("petId", petId)
                startActivity(intent)
                Toast.makeText(this@FormsMedicines, "Medicamento Adicionado", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
    private fun updateMedicines(idMedicines: Int, name: String, start: String, end:String, petId: Int){

        Thread {
            val app = application as App
            val dao = app.db.medicinesDao()
            val pet = MedicinesItem( idMedication = idMedicines, nameMedication = name, startMedication = start, endMedication = end, petId = petId)
            dao.updateMedicines(pet)
            runOnUiThread {
                Toast.makeText(this@FormsMedicines, "Atualizado", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@FormsMedicines, MedicinesActivity::class.java)
                intent.putExtra("petId", petId)
                startActivity(intent)
                finish()
            }
        }.start()
    }
    private fun loadPetDetails(petId: Int){
        Log.d("carregando detalhes", "entrou")
        Thread{
            val medicines = medicinesDao.getMedicinesById(petId)
            Log.d("carregando detalhes", "veja $medicines")

            runOnUiThread{
                nameMedicinesPet.setText(medicines.nameMedication)
                startMedicinesPet.setText(medicines.startMedication)
                endMedicinesPet.setText(medicines.endMedication)

            }
        }.start()
    }
}