package com.elm.projectmanagment.data.local.entites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "projects",
    indices = [Index("ownerId")],
)
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val ownerId: Int
)