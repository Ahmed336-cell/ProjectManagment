package com.elm.projectmanagment.data.local.entites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    indices = [Index("projectId")],
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val projectId: Int
)