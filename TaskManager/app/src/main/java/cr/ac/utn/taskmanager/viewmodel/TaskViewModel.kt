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

        // Filtrado combinado: texto en título, descripción o ID y estado opcional.
        fun filterTasks(query: String, status: TaskStatus?): List<Task> {
            val text = query.trim()
            return repository.getTasks().filter { task ->
                val matchesText = text.isEmpty() ||
                    task.title.contains(text, ignoreCase = true) ||
                    task.description.contains(text, ignoreCase = true) ||
                    task.id.contains(text, ignoreCase = true)
                val matchesStatus = status == null || task.status == status
                matchesText && matchesStatus
            }
        }

        fun getStats(): TaskStats {
            val tasks = repository.getTasks()
            return TaskStats(
                total = tasks.count(),
                pending = tasks.count { it.status == TaskStatus.PENDING },
                inProgress = tasks.count { it.status == TaskStatus.IN_PROGRESS },
                completed = tasks.count { it.status == TaskStatus.COMPLETED },
                cancelled = tasks.count { it.status == TaskStatus.CANCELLED }
            )
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
    }

}