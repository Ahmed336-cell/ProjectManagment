package com.elm.projectmanagment.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elm.projectmanagment.data.local.entites.Attachment
import com.elm.projectmanagment.data.local.entites.Task

data class TaskWithAttachment(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId",
        entity = Attachment::class
    )
    val attachments: List<Attachment>

    )
