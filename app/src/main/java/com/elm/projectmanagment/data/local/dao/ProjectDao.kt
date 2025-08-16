package com.elm.projectmanagment.data.local.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.elm.projectmanagment.data.local.entites.Project
import com.elm.projectmanagment.data.local.entites.Task
import com.elm.projectmanagment.data.local.entites.User
import com.elm.projectmanagment.data.local.entites.Attachment
import com.elm.projectmanagment.data.local.entites.ProjectTaskCrossRef
import com.elm.projectmanagment.data.local.relations.ProjectWithTasks
import kotlinx.coroutines.flow.Flow
import com.elm.projectmanagment.data.local.relations.TaskWithAttachment
import android.util.Log

@Dao
interface ProjectDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long
    
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project): Long
    
    @Query("SELECT * FROM projects")
    suspend fun getAllProjectsOnce(): List<Project>
    
    @Query("SELECT * FROM projects")
    fun getAllProjectsFlow(): Flow<List<Project>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectTaskCrossRef(crossRef: ProjectTaskCrossRef)
    
    @Delete
    suspend fun deleteProjectTaskCrossRef(crossRef: ProjectTaskCrossRef)
    
    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<Task>
    
    @Query("SELECT t.* FROM tasks t INNER JOIN project_task_cross_ref ptcr ON t.id = ptcr.taskId WHERE ptcr.projectId = :projectId")
    suspend fun getTasksForProjectViaCrossRef(projectId: Int): List<Task>
    
    @Query("SELECT p.* FROM projects p INNER JOIN project_task_cross_ref ptcr ON p.id = ptcr.projectId WHERE ptcr.taskId = :taskId")
    suspend fun getProjectsForTask(taskId: Int): List<Project>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachment(attachment: Attachment): Long
    
    @Query("SELECT * FROM attachments WHERE taskId = :taskId")
    suspend fun getAttachmentsForTask(taskId: Int): List<Attachment>
    
    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskWithAttachments(taskId: Int): TaskWithAttachment
    
    @Transaction
    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getProjectWithTasks(projectId: Int): ProjectWithTasks
    
    @Query("SELECT p.* FROM projects p INNER JOIN tasks t ON p.id = t.id GROUP BY p.id HAVING COUNT(t.id) > 3")
    suspend fun getProjectsWithMoreThan3Tasks(): List<Project>
    
    @RawQuery
    suspend fun getProjectsWithMoreThan3TasksRaw(query: SupportSQLiteQuery): List<Project>
}