package com.elm.projectmanagment.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elm.projectmanagment.data.repository.Repository

class ProjectViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}