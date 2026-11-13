package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository

data class TaskStats(
    val total: Int,
    val pending: Int,
    val inProgress: Int,
    val completed: Int,
    val cancelled: Int
) {
    val completedPercent: Int
        get() = if (total == 0) 0 else (completed * 100) / total
}

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

        fun getStats(): TaskStats {
            val tasks = getTasks()
            return TaskStats(
                total = tasks.count(),
                pending = tasks.count { it.status == TaskStatus.PENDING },
                inProgress = tasks.count { it.status == TaskStatus.IN_PROGRESS },
                completed = tasks.filter { it.status == TaskStatus.COMPLETED }.size,
                cancelled = tasks.count { it.status == TaskStatus.CANCELLED }
            )
        }

        fun filterTasks(text: String, status: String): List<Task> {
            return getTasks().filter { task ->
                task.title.contains(text.trim(), ignoreCase = true)
                        && (status == "TODAS" || task.status.name == status)
            }
        }
    }

}