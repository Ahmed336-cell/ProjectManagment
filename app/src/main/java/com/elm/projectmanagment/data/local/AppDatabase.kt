package com.elm.projectmanagment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elm.projectmanagment.data.local.dao.ProjectDao
import com.elm.projectmanagment.data.local.entites.Attachment
import com.elm.projectmanagment.data.local.entites.Project
import com.elm.projectmanagment.data.local.entites.Task
import com.elm.projectmanagment.data.local.entites.User

@Database(entities = [User::class, Project::class, Task::class, Attachment::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract  class AppDatabase : RoomDatabase() {

  abstract  fun projectDao(): ProjectDao


}