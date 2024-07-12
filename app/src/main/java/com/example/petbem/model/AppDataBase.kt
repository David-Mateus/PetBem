package com.example.petbem.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.petbem.model.dao.HygieneDao
import com.example.petbem.model.dao.MedicinesDao
import com.example.petbem.model.dao.PetDao
import com.example.petbem.model.dao.VaccineDao
import com.example.petbem.model.entities.HygieneItem
import com.example.petbem.model.entities.MedicinesItem
import com.example.petbem.model.entities.PetItem
import com.example.petbem.model.entities.VaccineItem


//As tabelas que o banco vai ter ifca dentro da lista e depois cria ua mfunção
@Database(entities = [PetItem::class, HygieneItem::class, VaccineItem::class, MedicinesItem::class], version = 5)
abstract class AppDataBase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun HygieneDao(): HygieneDao
    abstract fun vaccineDao(): VaccineDao
    abstract fun medicinesDao(): MedicinesDao

    companion object{
        private var INSTANCE: AppDataBase? = null
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Hygiene
                database.execSQL("CREATE TABLE IF NOT EXISTS `hygiene` (`idHygiene` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `titleHygiene` TEXT NOT NULL, `dateHygiene` TEXT NOT NULL, `id` INTEGER NOT NULL, FOREIGN KEY(`id`) REFERENCES `PetItem`(`id`) ON DELETE CASCADE)")
                //Vaccines
                database.execSQL("CREATE TABLE IF NOT EXISTS `vaccine` (`idVaccine` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nameVaccine` TEXT NOT NULL, `dataVaccine `TEXT NOT NULL, `id` INTEGER NOT NULL, FOREIGN KEY(`id`) REFERENCES `PetItem`(`id`) ON DELETE CASCADE)")
                //Medicines
                database.execSQL("CREATE TABLE IF NOT EXISTS `medicines` (`idMedication` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nameMedication` TEXT NOT NULL, `startMedication `TEXT NOT NULL, `endMedication` TEXT NOT NULL, `id` INTEGER NOT NULL, FOREIGN KEY(`id`) REFERENCES `PetItem`(`id`) ON DELETE CASCADE)")
            }
        }
        fun getDatabase(context:Context): AppDataBase{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "pet_bem"
                    )   .addMigrations(MIGRATION_1_2)
                        .fallbackToDestructiveMigration()
                        .build()


                }
                return INSTANCE as AppDataBase
            }else{
                return INSTANCE as AppDataBase
            }

        }
    }
}