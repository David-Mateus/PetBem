package com.example.petbem.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "medicines",
    foreignKeys = [ForeignKey(
        entity = PetItem::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("petId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class MedicinesItem(
    @PrimaryKey(autoGenerate = true) val idMedication :Int  = 0,
    @ColumnInfo(name = "nameMedication") val nameMedication: String,
    @ColumnInfo(name = "startMedication") val startMedication: String,
    @ColumnInfo(name = "endMedication") val endMedication:String,
    @ColumnInfo(name = "petId") val petId: Int // Correção aqui
)
