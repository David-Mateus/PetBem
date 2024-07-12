package com.example.petbem.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.petbem.model.entities.MedicinesItem

@Dao
interface MedicinesDao {
    @Insert
    fun insertMedicines(medicinesItem: MedicinesItem)

    @Query("SELECT * FROM medicines WHERE petId = :id")
    fun getAllMedicines(id: Int): List<MedicinesItem>

    @Delete
    fun deleteMedicines(medicinesItem: MedicinesItem)

    @Update
    fun updateMedicines(medicinesItem: MedicinesItem)

    @Query("SELECT * FROM medicines WHERE idMedication = :id LIMIT 1")
    fun getMedicinesById(id: Int): MedicinesItem

    @Query("SELECT * FROM medicines WHERE petId = :id ORDER BY idMedication DESC LIMIT 1")
    fun getOrderById(id : Int): MedicinesItem
}