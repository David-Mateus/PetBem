package com.example.petbem.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.petbem.model.entities.HygieneItem
import com.example.petbem.model.entities.PetItem

@Dao
interface HygieneDao {
    @Insert
    fun insertHygiene(hygieneItem: HygieneItem)

    @Query("SELECT * FROM hygiene WHERE id = :id")
    fun getAllHygiene(id :Int):List<HygieneItem>

    @Delete
    fun deleteHygiene(pet:HygieneItem)

    @Update
    fun updateHygiene(hygiene: HygieneItem)

    @Query("SELECT * FROM hygiene WHERE idHygiene = :id LIMIT 1")
    fun getHygieneById(id: Int): HygieneItem

}