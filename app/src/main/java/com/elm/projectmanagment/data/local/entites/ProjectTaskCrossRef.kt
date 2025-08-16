package com.elm.projectmanagment.data.local.entites

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "project_task_cross_ref",
    primaryKeys = ["projectId", "taskId"],
    indices = [Index("projectId"), Index("taskId")]
)
data class ProjectTaskCrossRef(
    val projectId: Int,
    val taskId: Int
)
