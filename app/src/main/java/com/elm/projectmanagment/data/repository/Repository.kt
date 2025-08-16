package com.elm.projectmanagment.data.repository

import android.util.Log
import androidx.sqlite.db.SupportSQLiteQuery
import com.elm.projectmanagment.data.local.dao.ProjectDao
import com.elm.projectmanagment.data.local.entites.Project
import com.elm.projectmanagment.data.local.entites.Task
import com.elm.projectmanagment.data.local.entites.User
import com.elm.projectmanagment.data.local.entites.Attachment
import com.elm.projectmanagment.data.local.entites.ProjectTaskCrossRef
import com.elm.projectmanagment.data.local.relations.ProjectWithTasks
import com.elm.projectmanagment.data.local.relations.TaskWithAttachment
import kotlinx.coroutines.flow.Flow

class Repository(private val projectDao: ProjectDao) {

    suspend fun insertUser(user: User): Long {
        val userId = projectDao.insertUser(user)
        Log.d("Repository", "User inserted: ${user.name} with ID: $userId")
        return userId
    }

    suspend fun getAllUsers(): List<User> {
        Log.d("Repository", "Fetching all users")
        return projectDao.getAllUsers()
    }

    suspend fun insertProject(project: Project): Long {
        val projectId = projectDao.insertProject(project)
        Log.d("Repository", "Project inserted: ${project.title} with ID: $projectId")
        return projectId
    }

    suspend fun getAllProjectsOnce(): List<Project> {
        Log.d("Repository", "Fetching all projects once")
        return projectDao.getAllProjectsOnce()
    }

    fun getAllProjectsFlow(): Flow<List<Project>> = projectDao.getAllProjectsFlow()

    suspend fun insertTask(task: Task): Long {
        Log.d("Repository", "Repository: Inserting task: ${task.description}")
        val taskId = projectDao.insertTask(task)
        Log.d("Repository", "Repository: Task inserted successfully with ID: $taskId")
        return taskId
    }

    suspend fun insertProjectTaskCrossRef(crossRef: ProjectTaskCrossRef) {
        Log.d("Repository", "Repository: Inserting project-task cross-reference: projectId=${crossRef.projectId}, taskId=${crossRef.taskId}")
        projectDao.insertProjectTaskCrossRef(crossRef)
    }

    suspend fun deleteProjectTaskCrossRef(crossRef: ProjectTaskCrossRef) {
        Log.d("Repository", "Repository: Deleting project-task cross-reference: projectId=${crossRef.projectId}, taskId=${crossRef.taskId}")
        projectDao.deleteProjectTaskCrossRef(crossRef)
    }

    suspend fun getAllTasks(): List<Task> {
        Log.d("Repository", "Repository: Fetching all tasks")
        val tasks = projectDao.getAllTasks()
        Log.d("Repository", "Repository: Found ${tasks.size} tasks")
        return tasks
    }

    suspend fun getTasksForProject(projectId: Int): List<Task> {
        Log.d("Repository", "Repository: Fetching tasks for project ID: $projectId")
        val tasks = projectDao.getTasksForProjectViaCrossRef(projectId)
        Log.d("Repository", "Repository: Found ${tasks.size} tasks for project")
        return tasks
    }

    suspend fun getProjectsForTask(taskId: Int): List<Project> {
        Log.d("Repository", "Repository: Fetching projects for task ID: $taskId")
        val projects = projectDao.getProjectsForTask(taskId)
        Log.d("Repository", "Repository: Found ${projects.size} projects for task")
        return projects
    }

    suspend fun insertAttachment(attachment: Attachment): Long {
        val attachmentId = projectDao.insertAttachment(attachment)
        Log.d("Repository", "Repository: Attachment inserted: ${attachment.filePath} with ID: $attachmentId")
        return attachmentId
    }

    suspend fun getAttachmentsForTask(taskId: Int): List<Attachment> {
        Log.d("Repository", "Repository: Fetching attachments for task ID: $taskId")
        val attachments = projectDao.getAttachmentsForTask(taskId)
        Log.d("Repository", "Repository: Found ${attachments.size} attachments for task")
        return attachments
    }

    suspend fun getTaskWithAttachments(taskId: Int): TaskWithAttachment {
        Log.d("Repository", "Repository: Fetching task with attachments for task ID: $taskId")
        val result = projectDao.getTaskWithAttachments(taskId)
        Log.d("Repository", "Repository: Found task: ${result.task.description} with ${result.attachments.size} attachments")
        return result
    }

    suspend fun getProjectWithTasks(projectId: Int): ProjectWithTasks {
        Log.d("Repository", "Repository: Fetching project with ID: $projectId")
        val result = projectDao.getProjectWithTasks(projectId)
        Log.d("Repository", "Repository: Found project: ${result.project.title} with ${result.tasks.size} tasks")
        return result
    }

    suspend fun getProjectsWithMoreThan3Tasks(): List<Project> {
        Log.d("Repository", "Fetching projects with more than 3 tasks")
        return projectDao.getProjectsWithMoreThan3Tasks()
    }

    suspend fun getProjectsWithMoreThan3TasksRaw(query: SupportSQLiteQuery): List<Project> {
        Log.d("Repository", "Fetching projects with more than 3 tasks using raw query")
        return projectDao.getProjectsWithMoreThan3TasksRaw(query)
    }
}