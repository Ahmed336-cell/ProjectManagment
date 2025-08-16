package com.elm.projectmanagment.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elm.projectmanagment.data.local.entites.Project
import com.elm.projectmanagment.data.local.entites.Task
import com.elm.projectmanagment.data.local.entites.Attachment
import com.elm.projectmanagment.data.local.entites.User
import com.elm.projectmanagment.data.local.relations.ProjectWithTasks
import com.elm.projectmanagment.data.local.relations.TaskWithAttachment
import com.elm.projectmanagment.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProjectViewModel(private val repository: Repository) : ViewModel() {
    
    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects
    
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks
    
    private val _projectWithTasks = MutableStateFlow<ProjectWithTasks?>(null)
    val projectWithTasks: StateFlow<ProjectWithTasks?> = _projectWithTasks
    
    private val _taskWithAttachments = MutableStateFlow<TaskWithAttachment?>(null)
    val taskWithAttachments: StateFlow<TaskWithAttachment?> = _taskWithAttachments
    
    fun insertTestData() {
        viewModelScope.launch {
            try {
                val user1 = User(name = "John Doe", email = "john@example.com")
                val user2 = User(name = "Jane Smith", email = "jane@example.com")
                val userId1 = repository.insertUser(user1)
                val userId2 = repository.insertUser(user2)
                Log.d("DB_TEST", "Inserted: $user1 with ID: $userId1")
                Log.d("DB_TEST", "Inserted: $user2 with ID: $userId2")
                
                val project1 = Project(title = "Mobile App Development", ownerId = userId1.toInt())
                val project2 = Project(title = "Website Redesign", ownerId = userId2.toInt())
                val projectId1 = repository.insertProject(project1)
                val projectId2 = repository.insertProject(project2)
                Log.d("DB_TEST", "Inserted: $project1 with ID: $projectId1")
                Log.d("DB_TEST", "Inserted: $project2 with ID: $projectId2")
                
                val task1 = Task(description = "Design UI mockups", projectId = projectId1.toInt())
                val task2 = Task(description = "Implement login feature", projectId = projectId1.toInt())
                val task3 = Task(description = "Create database schema", projectId = projectId1.toInt())
                val task4 = Task(description = "Design homepage layout", projectId = projectId2.toInt())
                val task5 = Task(description = "Optimize images", projectId = projectId2.toInt())
                
                val taskId1 = repository.insertTask(task1)
                val taskId2 = repository.insertTask(task2)
                val taskId3 = repository.insertTask(task3)
                val taskId4 = repository.insertTask(task4)
                val taskId5 = repository.insertTask(task5)
                
                Log.d("DB_TEST", "Inserted: $task1 with ID: $taskId1")
                Log.d("DB_TEST", "Inserted: $task2 with ID: $taskId2")
                Log.d("DB_TEST", "Inserted: $task3 with ID: $taskId3")
                Log.d("DB_TEST", "Inserted: $task4 with ID: $taskId4")
                Log.d("DB_TEST", "Inserted: $task5 with ID: $taskId5")
                
                val attachment1 = Attachment(filePath = "/documents/mockup.pdf", taskId = taskId1.toInt())
                val attachment2 = Attachment(filePath = "/images/login_screen.png", taskId = taskId2.toInt())
                val attachment3 = Attachment(filePath = "/documents/schema.sql", taskId = taskId3.toInt())
                
                val attachmentId1 = repository.insertAttachment(attachment1)
                val attachmentId2 = repository.insertAttachment(attachment2)
                val attachmentId3 = repository.insertAttachment(attachment3)
                
                Log.d("DB_TEST", "Inserted: $attachment1 with ID: $attachmentId1")
                Log.d("DB_TEST", "Inserted: $attachment2 with ID: $attachmentId2")
                Log.d("DB_TEST", "Inserted: $attachment3 with ID: $attachmentId3")
                
                val projectWithTasks = repository.getProjectWithTasks(projectId1.toInt())
                Log.d("DB_TEST", "Project with Tasks: $projectWithTasks")
                
                loadAllProjects()
                loadAllTasks()
                
            } catch (e: Exception) {
                Log.e("DB_TEST", "Error inserting test data: ${e.message}")
            }
        }
    }
    
    fun testSuspendVsFlow() {
        viewModelScope.launch {
            try {
                val projects = repository.getAllProjectsOnce()
                Log.d("DAO_TEST", "Suspend projects: $projects")
                
                var emissionCount = 0
                repository.getAllProjectsFlow().collectLatest { projectsFlow ->
                    emissionCount++
                    Log.d("DAO_TEST", "Flow emission $emissionCount: $projectsFlow")
                    if (emissionCount >= 3) {
                        return@collectLatest
                    }
                }
            } catch (e: Exception) {
                Log.e("DAO_TEST", "Error testing suspend vs Flow: ${e.message}")
            }
        }
    }
    
    fun testQueryPerformance() {
        viewModelScope.launch {
            try {
                val startTimeRoom = System.nanoTime()
                repeat(100) {
                    repository.getProjectsWithMoreThan3Tasks()
                }
                val endTimeRoom = System.nanoTime()
                val roomTime = endTimeRoom - startTimeRoom
                
                val rawQuery = androidx.sqlite.db.SimpleSQLiteQuery(
                    "SELECT p.* FROM projects p INNER JOIN tasks t ON p.id = t.projectId GROUP BY p.id HAVING COUNT(t.id) > 3"
                )
                
                val startTimeRaw = System.nanoTime()
                repeat(100) {
                    repository.getProjectsWithMoreThan3TasksRaw(rawQuery)
                }
                val endTimeRaw = System.nanoTime()
                val rawTime = endTimeRaw - startTimeRaw
                
                Log.d("PERF", "Room query: ${roomTime}ns")
                Log.d("PERF", "Raw query: ${rawTime}ns")
                
                Log.d("PERF", "Performance Comparison:")
                Log.d("PERF", "Room Query: ${roomTime}ns")
                Log.d("PERF", "Raw Query: ${rawTime}ns")
                Log.d("PERF", "Difference: ${rawTime - roomTime}ns")
                
            } catch (e: Exception) {
                Log.e("PERF", "Error in performance test: ${e.message}")
            }
        }
    }
    
    fun loadAllProjects() {
        viewModelScope.launch {
            try {
                val projectList = repository.getAllProjectsOnce()
                _projects.value = projectList
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading projects: ${e.message}")
            }
        }
    }
    
    fun loadAllTasks() {
        viewModelScope.launch {
            try {
                val taskList = repository.getAllTasks()
                _tasks.value = taskList
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading tasks: ${e.message}")
            }
        }
    }
    
    fun loadTasksForProject(projectId: Int) {
        viewModelScope.launch {
            try {
                val taskList = repository.getTasksForProject(projectId)
                _tasks.value = taskList
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading tasks for project: ${e.message}")
            }
        }
    }
    
    fun loadProjectWithTasks(projectId: Int) {
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "Loading project with tasks for project ID: $projectId")
                val projectWithTasksData = repository.getProjectWithTasks(projectId)
                Log.d("ViewModel", "Loaded project: ${projectWithTasksData.project.title} with ${projectWithTasksData.tasks.size} tasks")
                _projectWithTasks.value = projectWithTasksData
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading project with tasks: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    fun loadTaskWithAttachments(taskId: Int) {
        viewModelScope.launch {
            try {
                val taskWithAttachmentsData = repository.getTaskWithAttachments(taskId)
                _taskWithAttachments.value = taskWithAttachmentsData
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading task with attachments: ${e.message}")
            }
        }
    }
    
    fun getAttachmentsForTask(taskId: Int): List<Attachment> {
        var attachments = emptyList<Attachment>()
        viewModelScope.launch {
            try {
                attachments = repository.getAttachmentsForTask(taskId)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error getting attachments for task: ${e.message}")
            }
        }
        return attachments
    }
    
    fun insertProject(project: Project) {
        viewModelScope.launch {
            try {
                val projectId = repository.insertProject(project)
                Log.d("ViewModel", "Project inserted with ID: $projectId")
                loadAllProjects()
            } catch (e: Exception) {
                Log.e("ViewModel", "Error inserting project: ${e.message}")
            }
        }
    }
    
    fun insertTask(task: Task, projectId: Int? = null) {
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "Inserting task: ${task.description} for project: ${task.projectId}")
                val taskId = repository.insertTask(task)
                Log.d("ViewModel", "Task inserted with ID: $taskId")
                
                loadAllTasks()
                
                projectId?.let { id ->
                    Log.d("ViewModel", "Refreshing project data for project ID: $id")
                    loadProjectWithTasks(id)
                }
                
                Log.d("ViewModel", "Task insertion completed successfully")
            } catch (e: Exception) {
                Log.e("ViewModel", "Error inserting task: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    fun insertAttachment(attachment: Attachment, taskId: Int? = null) {
        viewModelScope.launch {
            try {
                val attachmentId = repository.insertAttachment(attachment)
                Log.d("ViewModel", "Attachment inserted with ID: $attachmentId")
                
                taskId?.let { id ->
                    loadTaskWithAttachments(id)
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error inserting attachment: ${e.message}")
            }
        }
    }
}