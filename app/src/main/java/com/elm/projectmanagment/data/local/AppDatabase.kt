package com.elm.projectmanagment.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elm.projectmanagment.data.local.dao.ProjectDao
import com.elm.projectmanagment.data.local.entites.Attachment
import com.elm.projectmanagment.data.local.entites.Project
import com.elm.projectmanagment.data.local.entites.ProjectTaskCrossRef
import com.elm.projectmanagment.data.local.entites.Task
import com.elm.projectmanagment.data.local.entites.User

@Database(
    entities = [
        User::class, 
        Project::class, 
        Task::class, 
        Attachment::class,
        ProjectTaskCrossRef::class
    ], 
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    
    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context = context.applicationContext,
                    AppDatabase::class.java,
                    "project_management_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}