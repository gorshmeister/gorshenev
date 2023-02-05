package com.example.tinkofffilms.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FilmEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun filmDao(): FilmDao

    companion object {
        private const val DB_NAME = "appDataBase"
        const val FILM = "film"

        private var instance: AppDataBase? = null

        @Synchronized
        fun initInstance(context: Context) {
            if (instance == null)
                instance = Room.databaseBuilder(
                    /* context = */ context.applicationContext,
                    /* klass = */ AppDataBase::class.java,
                    /* name = */ DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
        }

        fun getDatabase(): AppDataBase = instance!!
    }
}