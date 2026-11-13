package cr.ac.utn.taskmanager

import android.os.Bundle
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top,
                systemBars.right, systemBars.bottom)
            insets
        }


        val tasks = TaskViewModel.getTasks()
        val total = tasks.size
        val pending = tasks.count { it.status == TaskStatus.PENDING }
        val inProgress = tasks.count { it.status == TaskStatus.IN_PROGRESS }
        val completed = tasks.count { it.status == TaskStatus.COMPLETED }
        val cancelled = tasks.count { it.status == TaskStatus.CANCELLED }

        val percentage = if (total > 0) {
            completed * 100 / total
        } else {
            0
        }

        findViewById<TextView>(R.id.tvTotalTasks).text =
            "Total de tareas: $total"

        findViewById<TextView>(R.id.tvPendingTasks).text =
            "Pendientes: $pending"

        findViewById<TextView>(R.id.tvInProgressTasks).text =
            "En progreso: $inProgress"

        findViewById<TextView>(R.id.tvCompletedTasks).text =
            "Completadas: $completed"

        findViewById<TextView>(R.id.tvCancelledTasks).text =
            "Canceladas: $cancelled"

        findViewById<TextView>(R.id.tvCompletionPercentage).text =
            "Porcentaje completado: $percentage%"

        findViewById<ProgressBar>(R.id.progressCompletion).progress =
            percentage
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
