package cr.ac.utn.taskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskStatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_stats)

        val tvTotal = findViewById<TextView>(R.id.tvTotalTasks)
        val tvPending = findViewById<TextView>(R.id.tvPending)
        val tvInProgress = findViewById<TextView>(R.id.tvInProgress)
        val tvCompleted = findViewById<TextView>(R.id.tvCompleted)
        val tvCancelled = findViewById<TextView>(R.id.tvCancelled)
        val progressBar = findViewById<ProgressBar>(R.id.progressCompleted)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // Obtener los datos a través de los métodos del TaskViewModel
        val total = TaskViewModel.getTotalTasks()
        val pending = TaskViewModel.getCountByStatus(TaskStatus.PENDING)
        val inProgress = TaskViewModel.getCountByStatus(TaskStatus.IN_PROGRESS)
        val completed = TaskViewModel.getCountByStatus(TaskStatus.COMPLETED)
        val cancelled = TaskViewModel.getCountByStatus(TaskStatus.CANCELLED)

        val percentage = TaskViewModel.getCompletedPercentage()

        // Asignar a la UI
        tvTotal.text = "Total de Tareas: $total"
        tvPending.text = "Pendientes (PENDING): $pending"
        tvInProgress.text = "En Progreso (IN_PROGRESS): $inProgress"
        tvCompleted.text = "Completadas (COMPLETED): $completed"
        tvCancelled.text = "Canceladas (CANCELLED): $cancelled"
        progressBar.progress = percentage

        btnBack.setOnClickListener {
            finish()
        }
    }
}