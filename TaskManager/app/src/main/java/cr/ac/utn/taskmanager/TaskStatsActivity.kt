
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

        val btnBack = findViewById<Button>(R.id.btnBack_stats)

        btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        showStats()
    }

    private fun showStats() {
        val total = TaskViewModel.getTotalTasks()

        val pending = TaskViewModel.getTasksByStatus(
            TaskStatus.PENDING
        )

        val inProgress = TaskViewModel.getTasksByStatus(
            TaskStatus.IN_PROGRESS
        )

        val completed = TaskViewModel.getTasksByStatus(
            TaskStatus.COMPLETED
        )

        val cancelled = TaskViewModel.getTasksByStatus(
            TaskStatus.CANCELLED
        )

        val percentage = TaskViewModel.getCompletedPercentage()

        findViewById<TextView>(R.id.tvTotal_stats).text =
            total.toString()

        findViewById<TextView>(R.id.tvPending_stats).text =
            pending.toString()

        findViewById<TextView>(R.id.tvInProgress_stats).text =
            inProgress.toString()

        findViewById<TextView>(R.id.tvCompleted_stats).text =
            completed.toString()

        findViewById<TextView>(R.id.tvCancelled_stats).text =
            cancelled.toString()

        findViewById<TextView>(R.id.tvPercentage_stats).text =
            "$percentage%"

        val progressBar = findViewById<ProgressBar>(
            R.id.progressCompleted_stats
        )

        progressBar.max = 100
        progressBar.progress = percentage
    }
}
