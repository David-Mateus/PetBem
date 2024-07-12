package com.example.petbem.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.petbem.model.entities.PetItem

@Dao
interface PetDao {
    @Insert
    fun insert(petItem: PetItem)

    @Query("SELECT * FROM PetItem WHERE namePet = :pet")
    fun getPet(pet: String) : List<PetItem>

    @Query("SELECT * FROM PetItem")
    fun getAllPets():List<PetItem>
    @Delete
    fun delete(pet: PetItem)
    @Update
    fun update(pet: PetItem)
    @Query("SELECT * FROM PetItem WHERE id = :id LIMIT 1")
    fun getPetById(id: Int): PetItem

    @Query("SELECT namePet FROM PetItem WHERE id =:id ")
    fun getNamePet(id: Int): String

    @Query("SELECT id FROM PetItem WHERE id = :idPet LIMIT 1")
    fun getIdPet(idPet: Int): Int
}