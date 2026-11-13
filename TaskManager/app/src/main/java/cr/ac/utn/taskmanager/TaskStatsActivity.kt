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

        findViewById<Button>(R.id.btnStatsBack).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        showStats()
    }

    private fun showStats() {
        findViewById<TextView>(R.id.tvStatsTotal).text =
            TaskViewModel.countTotal().toString()

        findViewById<TextView>(R.id.tvStatsPending).text =
            "${getString(R.string.TextStatsPending)}: ${TaskViewModel.countByStatus(TaskStatus.PENDING)}"
        findViewById<TextView>(R.id.tvStatsInProgress).text =
            "${getString(R.string.TextStatsInProgress)}: ${TaskViewModel.countByStatus(TaskStatus.IN_PROGRESS)}"
        findViewById<TextView>(R.id.tvStatsCompleted).text =
            "${getString(R.string.TextStatsCompleted)}: ${TaskViewModel.countByStatus(TaskStatus.COMPLETED)}"
        findViewById<TextView>(R.id.tvStatsCancelled).text =
            "${getString(R.string.TextStatsCancelled)}: ${TaskViewModel.countByStatus(TaskStatus.CANCELLED)}"

        val percent = TaskViewModel.completedPercent()
        findViewById<ProgressBar>(R.id.pbStatsCompleted).progress = percent
        findViewById<TextView>(R.id.tvStatsPercent).text = "$percent%"
    }
}