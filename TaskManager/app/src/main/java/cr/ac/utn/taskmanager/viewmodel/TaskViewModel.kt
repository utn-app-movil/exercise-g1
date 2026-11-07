
package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository

class TaskViewModel : ViewModel() {

    companion object {
        private var repository: ITaskRepository

        init {
            repository = TaskMemoryManager()
        }

        fun getTasks(): List<Task> {
            return repository.getTasks()
        }

        fun getTotalTasks(): Int {
            return getTasks().count()
        }

        fun getTasksByStatus(status: TaskStatus): Int {
            return getTasks().filter {
                it.status == status
            }.count()
        }

        fun getCompletedPercentage(): Int {
            val total = getTotalTasks()

            if (total == 0) {
                return 0
            }

            val completed = getTasksByStatus(TaskStatus.COMPLETED)
            return completed * 100 / total
        }

        fun getTaskById(id: String): Task? {
            return repository.getById(id)
        }

        fun saveTask(task: Task) {
            repository.addTask(task)
        }

        fun updateTask(task: Task) {
            repository.updateTask(task)
        }

        fun removeTask(id: String) {
            repository.deleteTask(id)
        }
    }
}
