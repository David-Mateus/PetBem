package com.example.petbem.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PetItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "namePet") val namePet: String,
    @ColumnInfo(name = "agePet")  val agePet: String,
    @ColumnInfo(name = "racePet") val racePet: String,
    @ColumnInfo(name = "photoPet") val photoPet: String
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PetItem) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}
