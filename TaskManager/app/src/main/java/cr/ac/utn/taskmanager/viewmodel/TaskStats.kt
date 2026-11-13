package cr.ac.utn.taskmanager.viewmodel

data class TaskStats(
    val total: Int,
    val pending: Int,
    val inProgress: Int,
    val completed: Int,
    val cancelled: Int
) {
    val completedPercentage: Int
        get() = if (total == 0) 0 else (completed * 100) / total
}
