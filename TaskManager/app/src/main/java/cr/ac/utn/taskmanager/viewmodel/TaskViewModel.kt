package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository
import cr.ac.utn.taskmanager.domain.model.TaskStatus

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

        fun getTotalTasks(): Int{
            return repository.getTasks().size
        }

        fun countByStatus(status: TaskStatus): Int{
            return repository.getTasks().count { it.status == status }
        }

        fun getCompletedPercentage(): Int{
            val total = getTotalTasks()
            if (total == 0) return 0
            val completed = repository.getTasks()
                .filter { it.status == TaskStatus.COMPLETED }.size
            return completed * 100 / total
        }

        fun filterTasks(text: String, status: TaskStatus?): List<Task>{
            return repository.getTasks().filter { task ->
                val matchesText = text.isBlank() ||
                        task.title.contains(text, ignoreCase = true) ||
                        task.description.contains(text, ignoreCase = true)
                val matchesStatus = status == null || task.status == status
                matchesText && matchesStatus
            }
        }
    }

}