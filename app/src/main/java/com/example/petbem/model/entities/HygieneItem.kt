package com.example.petbem.model.entities

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "hygiene",
    foreignKeys = [ ForeignKey(
        entity = PetItem::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class HygieneItem(
    @PrimaryKey(autoGenerate = true) val idHygiene:Int = 0,
    @ColumnInfo(name = "titleHygiene") val titleHygiene: String,
    @ColumnInfo(name = "dateHygiene") val dateHygiene: String,
    val id: Int
)
