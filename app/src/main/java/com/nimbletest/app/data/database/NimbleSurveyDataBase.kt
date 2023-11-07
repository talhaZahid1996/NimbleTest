package com.nimbletest.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nimbletest.app.BuildConfig
import com.nimbletest.app.data.database.dao.SurveyDao
import com.nimbletest.app.data.database.entities.SurveyEntity

@Database(
    entities = [SurveyEntity::class],
    version = 1
)
abstract class NimbleSurveyDataBase : RoomDatabase() {
    abstract fun surveyDao(): SurveyDao

    companion object {
        private const val DATABASE_NAME = "nimble_survey_app_db"

        // For Singleton instantiation
        @Volatile
        private var instance: NimbleSurveyDataBase? = null

        fun getInstance(context: Context): NimbleSurveyDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): NimbleSurveyDataBase {
            return Room.databaseBuilder(context, NimbleSurveyDataBase::class.java, DATABASE_NAME)
                .apply {
                    if (BuildConfig.DEBUG) {
                        fallbackToDestructiveMigration()
                    }
                }
                .build()
        }
    }
}