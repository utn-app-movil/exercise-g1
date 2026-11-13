package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository

data class TaskStatistics(
    val total: Int,
    val pendingCount: Int,
    val inProgressCount: Int,
    val completedCount: Int,
    val cancelledCount: Int,
    val completedPercentage: Int
)

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

        fun getTaskStatistics(): TaskStatistics {
            val tasks = getTasks()
            val total = tasks.size
            val pendingCount = tasks.count { it.status == TaskStatus.PENDING }
            val inProgressCount = tasks.count { it.status == TaskStatus.IN_PROGRESS }
            val completedCount = tasks.count { it.status == TaskStatus.COMPLETED }
            val cancelledCount = tasks.count { it.status == TaskStatus.CANCELLED }

            val completedPercentage = if (total > 0) {
                (completedCount * 100) / total
            } else {
                0
            }

            return TaskStatistics(
                total = total,
                pendingCount = pendingCount,
                inProgressCount = inProgressCount,
                completedCount = completedCount,
                cancelledCount = cancelledCount,
                completedPercentage = completedPercentage
            )
        }
    }
}
