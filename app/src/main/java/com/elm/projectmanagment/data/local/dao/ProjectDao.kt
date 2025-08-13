package com.elm.projectmanagment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.elm.projectmanagment.data.local.entites.Project
import com.elm.projectmanagment.data.local.entites.Task
import com.elm.projectmanagment.data.local.relations.Relations
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project): Long


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long


    @Transaction
    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getProjectWithTasks(projectId: Int): Relations

    @Query("SELECT * FROM projects")
    suspend fun getAllProjectsOnce(): List<Project>

    @Query("SELECT * FROM projects")
    suspend fun getAllProjectcsFlow(): Flow<List<Project>>

    @Query(" SELECT p.* FROM projects p INNER JOIN tasks t ON p.id = t.projectId GROUP BY p.id HAVING COUNT(t.id) > 3")
    suspend fun getProjectsWithMoreThan3Tasks(): List<Project>

    @RawQuery
    suspend fun getProjectsWithMoreThan3TasksRaw(query: SupportSQLiteQuery): List<Project>


}