package com.elm.projectmanagment.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elm.projectmanagment.data.local.entites.Project
import com.elm.projectmanagment.data.local.entites.Task


data class Relations(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId",
        entity = Task::class
    )

    val tasks:List<Task>,

    )