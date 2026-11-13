package cr.ac.utn.census.domain.repository

import cr.ac.utn.census.domain.model.Task

interface ITaskRepository {
    fun getTasks(): List<Task>
    fun getById(id: String): Task?
    fun addTask(task: Task)
    fun updateTask(task: Task)
    fun deleteTask(id: String)
}