package com.example.petbem

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.petbem.model.dao.PetDao
import com.example.petbem.model.entities.PetItem
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FormsPet : AppCompatActivity() {
    private lateinit var btnAddPet: Button
    private lateinit var nameInput: EditText
    private lateinit var petAgeInput: EditText
    private lateinit var petRacaInput: EditText
    private lateinit var selectImageButton: ImageButton
    private lateinit var petImageView: ImageView
    private var selectedImageUri: Uri? = null
    private var petId: Int? = null
    private lateinit var petDao: PetDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forms_pet)
        btnAddPet = findViewById(R.id.addPetButton)
        nameInput = findViewById(R.id.petNameInput)
        petAgeInput = findViewById(R.id.petAgeInput)
        petRacaInput = findViewById(R.id.petBreedInput)
        selectImageButton = findViewById(R.id.selectImageButton)
        petImageView = findViewById(R.id.petImageView)

        petDao = (application as App).db.petDao()
        petId = intent.getIntExtra("petId", -1)

        selectImageButton.setOnClickListener {
            openGallery()
        }

        btnAddPet.setOnClickListener {
            val name = nameInput.text.toString()
            val age = petAgeInput.text.toString()
            val breed = petRacaInput.text.toString()

            if (name.isNotEmpty() && age.isNotEmpty() && breed.isNotEmpty()) {
                if (selectedImageUri != null) {
                    val imageUri = saveImageToInternalStorage(selectedImageUri!!)
                    if (imageUri != null) {
                        if (petId == null || petId == -1) {
                            saveNewPet(name, age, breed, imageUri)
                        } else {
                            updatePet(petId!!, name, age, breed, imageUri)
                        }
                    } else {
                        Toast.makeText(this, "Erro ao salvar a imagem", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Por favor, selecione uma imagem", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Preencher todos os dados", Toast.LENGTH_SHORT).show()
            }
        }

        // Verifique se estamos editando um pet existente
        if (petId != -1) {
            loadPetDetails(petId!!)
        }
    }

    private fun saveNewPet(name: String, age: String, breed: String, imageUri: String) {
        Thread {
            val dao = petDao
            dao.insert(PetItem(namePet = name, agePet = age, racePet = breed, photoPet = imageUri))
            runOnUiThread {
                PetDataHolder.namePet = name
                val intent = Intent(this@FormsPet, RegisterPetActivity::class.java)
                Log.d("FormsPet", "Intent Extra (namePet): $name")
                startActivity(intent)
                Toast.makeText(this@FormsPet, R.string.pet_save, Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun updatePet(id: Int, name: String, age: String, breed: String, imageUri: String) {
        Thread {
            val dao = petDao
            val pet = PetItem(id = id, namePet = name, agePet = age, racePet = breed, photoPet = imageUri)
            dao.update(pet)
            runOnUiThread {
                val intent = Intent(this@FormsPet, RegisterPetActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@FormsPet, "Atualizado", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun loadPetDetails(petId: Int) {
        Thread {
            val pet = petDao.getPetById(petId)
            runOnUiThread {
                nameInput.setText(pet.namePet)
                petAgeInput.setText(pet.agePet)
                petRacaInput.setText(pet.racePet)
                selectedImageUri = Uri.parse(pet.photoPet)

                val localUri = selectedImageUri
                if (localUri != null) {
                    try {
                        val inputStream = contentResolver.openInputStream(localUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        petImageView.setImageBitmap(bitmap)
                        inputStream?.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "URI da imagem não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        val inputStream = contentResolver.openInputStream(uri)
        val fileName = "${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)
        return try {
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    val obterImagem = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            petImageView.setImageURI(it)
        }
    }

    private fun openGallery() {
        obterImagem.launch("image/*")
    }
}
