package com.elm.projectmanagment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elm.projectmanagment.Screens.*
import com.elm.projectmanagment.ViewModel.ProjectViewModel
import com.elm.projectmanagment.ViewModel.ProjectViewModelFactory
import com.elm.projectmanagment.data.local.AppDatabase
import com.elm.projectmanagment.data.repository.Repository
import com.elm.projectmanagment.ui.theme.ProjectManagmentTheme
import android.util.Log
import com.elm.projectmanagment.data.local.entites.Task
import com.elm.projectmanagment.data.local.entites.Attachment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        val repository = Repository(database.projectDao())
        
        setContent {
            TaskManagementApp(repository)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagementApp(repository: Repository) {
    val navController = rememberNavController()
    val viewModel: ProjectViewModel = viewModel(
        factory = ProjectViewModelFactory(repository)
    )
    
    LaunchedEffect(Unit) {
        viewModel.insertTestData()
        viewModel.testSuspendVsFlow()
        viewModel.testQueryPerformance()
    }

    ProjectManagmentTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Menu, contentDescription = "Projects") },
                        label = { Text("Projects") },
                        selected = navController.currentDestination?.route == "projects",
                        onClick = { navController.navigate("projects") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Menu, contentDescription = "Tasks") },
                        label = { Text("Tasks") },
                        selected = navController.currentDestination?.route == "tasks",
                        onClick = { navController.navigate("tasks") }
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "projects",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("projects") {
                    ProjectScreen(
                        viewModel = viewModel,
                        onProjectClick = { project ->
                            navController.navigate("project_detail/${project.id}")
                        },
                        onAddProject = {
                        }
                    )
                }

                composable("tasks") {
                    TaskScreen(
                        viewModel = viewModel,
                        onTaskClick = { task ->
                            navController.navigate("task_detail/${task.id}")
                        },
                        onAddTask = {
                        }
                    )
                }

                composable(
                    route = "project_detail/{projectId}",
                    arguments = listOf(navArgument("projectId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val projectId = backStackEntry.arguments?.getInt("projectId") ?: 1
                    val projectWithTasks by viewModel.projectWithTasks.collectAsState()
                    var showAddTaskDialog by remember { mutableStateOf(false) }
                    var newTaskDescription by remember { mutableStateOf("") }
                    
                    LaunchedEffect(projectId) {
                        viewModel.loadProjectWithTasks(projectId)
                    }
                    
                    projectWithTasks?.let { projectData ->
                        ProjectDetailScreen(
                            projectWithTasks = projectData,
                            onBackClick = { navController.popBackStack() },
                            onAddTask = {
                                showAddTaskDialog = true
                            }
                        )
                        
                        if (showAddTaskDialog) {
                            AddTaskDialog(
                                taskDescription = newTaskDescription,
                                onDescriptionChange = { newTaskDescription = it },
                                onConfirm = {
                                    if (newTaskDescription.isNotBlank()) {
                                        Log.d("MainActivity", "Creating task for project $projectId: $newTaskDescription")
                                        val newTask = Task(
                                            description = newTaskDescription
                                        )
                                        viewModel.insertTask(newTask, projectId)
                                        newTaskDescription = ""
                                        showAddTaskDialog = false
                                    }
                                },
                                onDismiss = {
                                    newTaskDescription = ""
                                    showAddTaskDialog = false
                                }
                            )
                        }
                    }
                }

                composable(
                    route = "task_detail/{taskId}",
                    arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val taskId = backStackEntry.arguments?.getInt("taskId") ?: 1
                    val taskWithAttachments by viewModel.taskWithAttachments.collectAsState()
                    var showAddAttachmentDialog by remember { mutableStateOf(false) }
                    var newAttachmentPath by remember { mutableStateOf("") }
                    
                    LaunchedEffect(taskId) {
                        viewModel.loadTaskWithAttachments(taskId)
                    }
                    
                    taskWithAttachments?.let { taskData ->
                        TaskDetailScreen(
                            taskWithAttachments = taskData,
                            onBackClick = { navController.popBackStack() },
                            onAddAttachment = {
                                showAddAttachmentDialog = true
                            }
                        )
                        
                        if (showAddAttachmentDialog) {
                            AddAttachmentDialog(
                                attachmentPath = newAttachmentPath,
                                onPathChange = { newAttachmentPath = it },
                                onConfirm = {
                                    if (newAttachmentPath.isNotBlank()) {
                                        Log.d("MainActivity", "Creating attachment for task $taskId: $newAttachmentPath")
                                        val newAttachment = Attachment(
                                            filePath = newAttachmentPath,
                                            taskId = taskId
                                        )
                                        viewModel.insertAttachment(newAttachment, taskId)
                                        newAttachmentPath = ""
                                        showAddAttachmentDialog = false
                                    }
                                },
                                onDismiss = {
                                    newAttachmentPath = ""
                                    showAddAttachmentDialog = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    taskDescription: String,
    onDescriptionChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Create New Task",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = onDescriptionChange,
                    label = { Text("Task Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = taskDescription.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAttachmentDialog(
    attachmentPath: String,
    onPathChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Attachment",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = attachmentPath,
                    onValueChange = onPathChange,
                    label = { Text("File Path") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("e.g., /documents/file.pdf") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = attachmentPath.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}