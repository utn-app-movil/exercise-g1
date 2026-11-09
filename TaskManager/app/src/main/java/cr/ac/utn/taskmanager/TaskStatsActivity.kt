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

        findViewById<Button>(R.id.btnBack_stats).setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        loadStats()
    }

    private fun loadStats() {
        findViewById<TextView>(R.id.txtTotal_stats).text =
            TaskViewModel.getTotalTasks().toString()
        findViewById<TextView>(R.id.txtPending_stats).text =
            "Pendientes: ${TaskViewModel.countByStatus(TaskStatus.PENDING)}"
        findViewById<TextView>(R.id.txtInProgress_stats).text =
            "En progreso: ${TaskViewModel.countByStatus(TaskStatus.IN_PROGRESS)}"
        findViewById<TextView>(R.id.txtCompleted_stats).text =
            "Completadas: ${TaskViewModel.countByStatus(TaskStatus.COMPLETED)}"
        findViewById<TextView>(R.id.txtCancelled_stats).text =
            "Canceladas: ${TaskViewModel.countByStatus(TaskStatus.CANCELLED)}"

        val percent = TaskViewModel.getCompletedPercentage()
        findViewById<TextView>(R.id.txtPercent_stats).text = "Completado: $percent%"
        findViewById<ProgressBar>(R.id.pbCompleted_stats).progress = percent
    }
}