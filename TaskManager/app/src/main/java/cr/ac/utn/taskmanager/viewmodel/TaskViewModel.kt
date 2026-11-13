package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository
import cr.ac.utn.taskmanager.domain.model.TaskStatus

//almacenar las estadisticas

data class TaskStatistics(
    val total: Int,
    val pending: Int,
    val inProgress: Int,
    val completed: Int,
    val cancelled: Int,
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
        fun filterTasks(
            searchText: String,
            status: TaskStatus?
        ): List<Task> {

            return getTasks().filter { task ->

                val matchesText =
                    task.title.contains(searchText, ignoreCase = true) ||
                            task.description.contains(searchText, ignoreCase = true)

                val matchesStatus =
                    status == null || task.status == status

                matchesText && matchesStatus
            }
        }

        fun getTaskStatistics(): TaskStatistics {

            // Obtener todas las tareas registradas
            val tasks = getTasks()

            // Contar el total de tareas
            val total = tasks.count()

            // Contar tareas según su estado
            val pending = tasks.count {
                it.status == TaskStatus.PENDING
            }

            val inProgress = tasks.count {
                it.status == TaskStatus.IN_PROGRESS
            }

            val completedTasks = tasks.filter {
                it.status == TaskStatus.COMPLETED
            }

            val completed = completedTasks.count()

            val cancelled = tasks.count {
                it.status == TaskStatus.CANCELLED
            }

            // Calcular el porcentaje de tareas completadas
            val completedPercentage = if (total > 0) {
                (completed * 100) / total
            } else {
                0
            }

            // Devolver todas las estadísticas
            return TaskStatistics(
                total = total,
                pending = pending,
                inProgress = inProgress,
                completed = completed,
                cancelled = cancelled,
                completedPercentage = completedPercentage
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