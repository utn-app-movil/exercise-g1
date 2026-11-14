package cr.ac.utn.taskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskStatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_stats)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvTotal = findViewById<TextView>(R.id.tvTotalTasks_Stats)
        val tvPending = findViewById<TextView>(R.id.tvPending_Stats)
        val tvInProgress = findViewById<TextView>(R.id.tvInProgress_Stats)
        val tvCompleted = findViewById<TextView>(R.id.tvCompleted_Stats)
        val tvCancelled = findViewById<TextView>(R.id.tvCancelled_Stats)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar_Stats)
        val btnBack = findViewById<Button>(R.id.btnBack_Stats)

        val tasks = TaskViewModel.getTasks()
        val total = tasks.size
        val pendingCount = tasks.count { it.status == TaskStatus.PENDING }
        val inProgressCount = tasks.count { it.status == TaskStatus.IN_PROGRESS }
        val completedCount = tasks.count { it.status == TaskStatus.COMPLETED }
        val cancelledCount = tasks.count { it.status == TaskStatus.CANCELLED }

        val percentage = if (total > 0) (completedCount * 100) / total else 0

        tvTotal.text = "Total de Tareas: $total"
        tvPending.text = "Pendientes: $pendingCount"
        tvInProgress.text = "En Progreso: $inProgressCount"
        tvCompleted.text = "Completadas: $completedCount"
        tvCancelled.text = "Canceladas: $cancelledCount"
        progressBar.progress = percentage

        btnBack.setOnClickListener {
            finish()
        }
    }
}
