package com.elm.projectmanagment.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elm.projectmanagment.data.local.entites.Project
import com.elm.projectmanagment.data.local.entites.Task

data class ProjectWithTasks(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val tasks: List<Task>
)

data class TasksWithProject(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId",
        entity = Project::class
    )
    val project: Project?
)
