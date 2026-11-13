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

        fun getTasks(): List<Task> {
            return repository.getTasks()
        }

        fun countTasks(status: TaskStatus): Int {
            return getTasks().count { it.status == status }
        }

        fun getCompletionPercentage(): Int {
            val total = getTasks().size
            if (total == 0) return 0
            return countTasks(TaskStatus.COMPLETED) * 100 / total
        }

        fun filterTasks(query: String, status: TaskStatus?): List<Task> {
            val search = query.trim()

            return getTasks().filter { task ->
                val matchesText = task.title.contains(search, ignoreCase = true) ||
                        task.description.contains(search, ignoreCase = true)

                val matchesStatus = status == null || task.status == status

                matchesText && matchesStatus
            }
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