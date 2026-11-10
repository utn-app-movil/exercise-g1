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

        fun getTotalTasks(): Int {
            return repository.getTasks().count()
        }

        fun getTasksCountByStatus(status: TaskStatus): Int {
            return repository.getTasks().filter { it.status == status }.count()
        }

        fun getCompletedPercentage(): Int {
            val total = getTotalTasks()
            if (total == 0) return 0
            val completed = getTasksCountByStatus(TaskStatus.COMPLETED)
            return (completed * 100) / total
        }

        fun filterTasks(query: String, statusFilter: String): List<Task> {
            return getTasks().filter { task ->
                val matchesQuery = query.isBlank() ||
                        task.title.contains(query, ignoreCase = true) ||
                        task.description.contains(query, ignoreCase = true)
                val matchesStatus = statusFilter == "TODAS" || statusFilter == "ALL" || task.status.name == statusFilter
                matchesQuery && matchesStatus
            }
        }
    }

}