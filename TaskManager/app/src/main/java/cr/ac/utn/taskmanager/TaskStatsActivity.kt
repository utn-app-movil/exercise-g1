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

        loadStats()

        val btnBack: Button = findViewById(R.id.btnBack_stats)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadStats() {
        val totalTasks = TaskViewModel.getTotalTasks()
        val pendingCount = TaskViewModel.getTasksCountByStatus(TaskStatus.PENDING)
        val inProgressCount = TaskViewModel.getTasksCountByStatus(TaskStatus.IN_PROGRESS)
        val completedCount = TaskViewModel.getTasksCountByStatus(TaskStatus.COMPLETED)
        val cancelledCount = TaskViewModel.getTasksCountByStatus(TaskStatus.CANCELLED)
        val percentage = TaskViewModel.getCompletedPercentage()

        val tvTotal: TextView = findViewById(R.id.tvTotalTasks_stats)
        val tvPending: TextView = findViewById(R.id.tvPending_stats)
        val tvInProgress: TextView = findViewById(R.id.tvInProgress_stats)
        val tvCompleted: TextView = findViewById(R.id.tvCompleted_stats)
        val tvCancelled: TextView = findViewById(R.id.tvCancelled_stats)
        val progressBar: ProgressBar = findViewById(R.id.pbCompleted_stats)
        val tvPercentage: TextView = findViewById(R.id.tvPercentage_stats)

        tvTotal.text = "${getString(R.string.TextTotalTasks)} $totalTasks"
        tvPending.text = "${getString(R.string.TextPendingTasks)} $pendingCount"
        tvInProgress.text = "${getString(R.string.TextInProgressTasks)} $inProgressCount"
        tvCompleted.text = "${getString(R.string.TextCompletedTasks)} $completedCount"
        tvCancelled.text = "${getString(R.string.TextCancelledTasks)} $cancelledCount"

        progressBar.progress = percentage
        tvPercentage.text = "${getString(R.string.TextCompletedPercentage)} $percentage%"
    }
}