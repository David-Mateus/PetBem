package com.example.petbem.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaccine",
    foreignKeys = [ForeignKey(
        entity = PetItem::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"),
        onDelete = ForeignKey.CASCADE
    )]
)

data class VaccineItem(
    @PrimaryKey(autoGenerate = true) val idVaccine:Int = 0,
    @ColumnInfo(name = "nameVaccine") val nameVaccine: String,
    @ColumnInfo(name = "dataVaccine") val dataVaccine: String,
    val id:Int
)
