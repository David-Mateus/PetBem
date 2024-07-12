package com.example.petbem.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.petbem.model.entities.VaccineItem

@Dao
interface VaccineDao {

    @Insert
    fun insertVaccine(vaccineItem: VaccineItem)

    @Query("SELECT * FROM vaccine WHERE id = :id")
    fun getAllVaccine(id : Int): List<VaccineItem>

    @Delete
    fun deleteVaccine(vaccine: VaccineItem)
    @Update
    fun updateVaccine(vaccine: VaccineItem)
    @Query("SELECT * FROM vaccine WHERE idVaccine = :id LIMIT 1")
    fun getVaccineById(id:Int): VaccineItem
}