package com.elm.projectmanagment.data.local.entites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attachments",
    indices = [Index("taskId")],

)
data class Attachment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val filePath: String,
    val taskId: Int
)