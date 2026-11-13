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


        //implementacion de metodos de la integracion de estadisticas
        fun getTotalTasks(): Int {
            return getTasks().size
        }

        fun getCountByStatus(status: TaskStatus): Int {
            return getTasks().count { it.status == status }
        }

        fun getCompletedPercentage(): Int {
            val total = getTotalTasks()
            if (total == 0) return 0
            val completed = getCountByStatus(TaskStatus.COMPLETED)
            return (completed * 100) / total
        }


        fun filterTasks(query: String, statusFilter: TaskStatus?): List<Task> {
            val allTasks = getTasks()

            return allTasks.filter { task ->
                val matchesQuery = task.title.contains(query, ignoreCase = true) ||
                        task.description.contains(query, ignoreCase = true)

                val matchesStatus = if (statusFilter == null) {
                    true // Si no hay filtro de estado seleccionado, pasan todas
                } else {
                    task.status == statusFilter
                }

                matchesQuery && matchesStatus
            }
        }
    }

}