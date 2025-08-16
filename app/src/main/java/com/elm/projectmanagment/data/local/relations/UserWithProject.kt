package com.elm.projectmanagment.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elm.projectmanagment.data.local.entites.Project
import com.elm.projectmanagment.data.local.entites.User


data class UserWithProject (
    @Embedded val user: User,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId",
        entity = Project::class
    )

    val projects: List<Project>
)