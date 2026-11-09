package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository

class TaskViewModel: ViewModel() {
    companion object{
        private var repository: ITaskRepository
        init {
            repository = TaskMemoryManager()
        }

        fun getTasks(): List<Task>{
            return repository.getTasks()
        }

        fun getTaskById(id: String): Task?{
            return repository.getById(id)
        }

        fun saveTask(task: Task){
            repository.addTask(task)
        }

        fun updateTask(task: Task){
            repository.updateTask(task)
        }

        fun removeTask(id: String){
            repository.deleteTask(id)
        }

        // ---------- Fase 1: Estadísticas ----------
        fun getTotalTasks(): Int {
            return getTasks().count()
        }

        fun countByStatus(status: TaskStatus): Int {
            return getTasks().count { it.status == status }
        }

        fun getCompletedPercentage(): Int {
            val total = getTotalTasks()
            if (total == 0) return 0
            val completed = getTasks().filter { it.status == TaskStatus.COMPLETED }.size
            return (completed * 100) / total
        }

        // ---------- Fase 2: Búsqueda y filtro combinado ----------
        fun filterTasks(searchText: String, status: TaskStatus?): List<Task> {
            val text = searchText.trim().lowercase()
            return getTasks().filter { task ->
                val matchesText = text.isEmpty()
                        || task.title.lowercase().contains(text)
                        || task.description.lowercase().contains(text)
                val matchesStatus = status == null || task.status == status
                matchesText && matchesStatus
            }
        }
    }
}