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
        val tvPercentage = findViewById<TextView>(R.id.tvPercentage)

        val progressBar = findViewById<ProgressBar>(
            R.id.progressCompleted
        )

        val btnBack = findViewById<Button>(R.id.btnBackMain)

        val tasks = TaskViewModel.getTasks()
        val stats = TaskViewModel.getTaskStats()
        val percentage = TaskViewModel.getCompletedPercentage()

        tvTotal.text = "Total de tareas: ${tasks.size}"

        tvPending.text =
            "Pendientes: ${stats[TaskStatus.PENDING] ?: 0}"

        tvInProgress.text =
            "En progreso: ${stats[TaskStatus.IN_PROGRESS] ?: 0}"

        tvCompleted.text =
            "Completadas: ${stats[TaskStatus.COMPLETED] ?: 0}"

        tvCancelled.text =
            "Canceladas: ${stats[TaskStatus.CANCELLED] ?: 0}"

        progressBar.progress = percentage
        tvPercentage.text = "$percentage% completadas"

        btnBack.setOnClickListener {
            finish()
        }
    }
}