package com.elm.projectmanagment.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.elm.projectmanagment.data.local.entites.Project
import com.elm.projectmanagment.data.local.entites.Task
import com.elm.projectmanagment.data.local.entites.ProjectTaskCrossRef

data class ProjectWithTasks(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ProjectTaskCrossRef::class,
            parentColumn = "projectId",
            entityColumn = "taskId"
        )
    )
    val tasks: List<Task>
)

data class TasksWithProject(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ProjectTaskCrossRef::class,
            parentColumn = "taskId",
            entityColumn = "projectId"
        ),
        entity = Project::class
    )
    val projects: List<Project>
)
