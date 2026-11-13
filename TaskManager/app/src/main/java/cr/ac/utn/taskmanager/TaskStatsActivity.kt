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
        findViewById<Button>(R.id.btnBack_Stats).setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.tvTotal_Stats).text =
            "Total de tareas: ${TaskViewModel.getTasks().size}"
        findViewById<TextView>(R.id.tvPending_Stats).text =
            "Pendientes: ${TaskViewModel.getTaskCount(TaskStatus.PENDING)}"
        findViewById<TextView>(R.id.tvInProgress_Stats).text =
            "En progreso: ${TaskViewModel.getTaskCount(TaskStatus.IN_PROGRESS)}"
        findViewById<TextView>(R.id.tvCompleted_Stats).text =
            "Completadas: ${TaskViewModel.getTaskCount(TaskStatus.COMPLETED)}"
        findViewById<TextView>(R.id.tvCancelled_Stats).text =
            "Canceladas: ${TaskViewModel.getTaskCount(TaskStatus.CANCELLED)}"
        val percentage = TaskViewModel.getCompletedPercentage()
        findViewById<ProgressBar>(R.id.pbCompleted_Stats).progress = percentage
        findViewById<TextView>(R.id.tvPercentage_Stats).text = "$percentage% completadas"
    }
}
