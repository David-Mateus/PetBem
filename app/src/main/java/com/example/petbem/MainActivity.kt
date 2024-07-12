package com.example.petbem

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineStart
import android.util.Base64

class MainActivity : AppCompatActivity() {
    private var petId: Int = 0
    private lateinit var namePetCardView: TextView
    private lateinit var idPet: TextView // id
    private lateinit var namePet: TextView // name
    private lateinit var agePet: TextView // age
    private lateinit var racePet: TextView // race
    private lateinit var nameMedicinesPet: TextView // nameMedicines
    private lateinit var startMedicinesPet: TextView // startMedicines
    private lateinit var endMedicinesPet: TextView // endMedicines

    //  private lateinit var photoPetImg : CircleImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Resumo"
        petId = intent.getIntExtra("petId", 0)
        Log.d("PetId-MainActity1", "$petId")
        loadPetDetailsMainActivity(petId)
        namePetCardView = findViewById(R.id.name_pet)
        idPet = findViewById(R.id.id_pet)
        namePet = findViewById(R.id.name_pet_remmeber)
        agePet = findViewById(R.id.age_pet)
        racePet = findViewById(R.id.race_pet)
        nameMedicinesPet = findViewById(R.id.nameMedicamento)
        startMedicinesPet = findViewById(R.id.startMedicamentos)
        endMedicinesPet = findViewById(R.id.endMedicamentos)
//      photoPetImg = findViewById(R.id.photo_profile)
        val fab = findViewById<FloatingActionButton>(R.id.fab_categories)
        fab.setOnClickListener {
            val intent = Intent(this, CategoriesActivity::class.java)
            intent.putExtra("petId", petId)
            Log.d("Send-PetId-Categories", "$petId")
            startActivity(intent)
        }
    }

    private fun loadPetDetailsMainActivity(petId: Int) {
        Thread {
            val app = application as App
            val dao = app.db.petDao()
            val pets = dao.getPetById(petId)
            val medicinesDao = app.db.medicinesDao()
            val medicines = medicinesDao.getOrderById(petId)
//          val imageBytes = Base64.decode(pets.photoPet, Base64.DEFAULT)
//          val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            val nameMedication: String
            val startMedication: String
            val endMedication: String

            if (medicines != null) {
                nameMedication = medicines.nameMedication ?: "Informação não disponível"
                startMedication = medicines.startMedication ?: "Informação não disponível"
                endMedication = medicines.endMedication ?: "Informação não disponível"
            } else {
                nameMedication = "Não há nada registrado"
                startMedication = "Não há nada registrado"
                endMedication = "Não há nada registrado"
            }
            Handler(Looper.getMainLooper()).post {
//              photoPetImg.setImageBitmap(bitmap)
                namePetCardView.text = pets.namePet
                idPet.text = pets.id.toString()
                namePet.text = pets.namePet
                agePet.text = pets.agePet
                racePet.text = pets.racePet
                nameMedicinesPet.text = nameMedication
                startMedicinesPet.text = startMedication
                endMedicinesPet.text = endMedication
            }
        }.start()
    }
}

