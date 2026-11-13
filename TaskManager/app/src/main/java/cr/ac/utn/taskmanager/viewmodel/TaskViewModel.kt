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
            return getTasks().count()
        }

        fun getPendingCount(): Int {
            return getTasks().count { it.status == TaskStatus.PENDING }
        }

        fun getInProgressCount(): Int {
            return getTasks().count { it.status == TaskStatus.IN_PROGRESS }
        }

        fun getCompletedCount(): Int {
            return getTasks().count { it.status == TaskStatus.COMPLETED }
        }

        fun getCancelledCount(): Int {
            return getTasks().filter { it.status == TaskStatus.CANCELLED }.size
        }

        fun getCompletedPercentage(): Int {
            val total = getTasks().size
            if (total == 0) return 0
            val completed = getTasks().count { it.status == TaskStatus.COMPLETED }
            return (completed * 100) / total
        }

    }

}