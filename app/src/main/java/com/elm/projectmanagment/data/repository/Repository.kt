package com.elm.projectmanagment.data.repository

import android.util.Log
import androidx.sqlite.db.SupportSQLiteQuery
import com.elm.projectmanagment.data.local.dao.ProjectDao
import com.elm.projectmanagment.data.local.entites.Project
import com.elm.projectmanagment.data.local.entites.Task
import com.elm.projectmanagment.data.local.relations.Relations

class Repository(private  val  projectDao : ProjectDao) {

    suspend fun insertProject(project: Project){
        projectDao.insertProject(project)
        Log.d("Repository", "Project inserted: ${project.title}")
    }

    suspend fun insertTask(task: Task) {
        projectDao.insertTask(task)
        Log.d("Repository", "Task ID inserted: ${task.id}")
    }

    suspend fun getProjectWithTasks(projectId: Int): Relations {
        Log.d("Repository", "Fetching project with ID: $projectId")
        return projectDao.getProjectWithTasks(projectId)

    }

    suspend fun getAllProjectsOnce(): List<Project> {
        Log.d("Repository", "Fetching all projects once")
        return projectDao.getAllProjectsOnce()
    }
    suspend fun getAllProjectsFlow() = projectDao.getAllProjectcsFlow()
    suspend fun getProjectsWithMoreThan3Tasks(): List<Project> {
        Log.d("Repository", "Fetching projects with more than 3 tasks")
        return projectDao.getProjectsWithMoreThan3Tasks()
    }
    suspend fun getProjectsWithMoreThan3TasksRaw(query: SupportSQLiteQuery): List<Project> {
        Log.d("Repository", "Fetching projects with more than 3 tasks using raw query")
        return projectDao.getProjectsWithMoreThan3TasksRaw(query)
    }
}