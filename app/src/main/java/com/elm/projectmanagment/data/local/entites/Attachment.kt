package com.elm.projectmanagment.data.local.entites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "attachments" , indices = [Index("taskId")],
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE

        )
    ]

    )
data class Attachment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val filePath: String,
    val taskId: Int
)