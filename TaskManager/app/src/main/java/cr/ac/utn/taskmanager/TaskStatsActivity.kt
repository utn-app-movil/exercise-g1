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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnBack_Stats).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        findViewById<TextView>(R.id.tvTotal_Stats).text =
            getString(R.string.TextStatsTotal, TaskViewModel.getTasks().size)

        findViewById<TextView>(R.id.tvPending_Stats).text =
            getString(R.string.TextStatsPending, TaskViewModel.countTasks(TaskStatus.PENDING))

        findViewById<TextView>(R.id.tvInProgress_Stats).text =
            getString(R.string.TextStatsInProgress, TaskViewModel.countTasks(TaskStatus.IN_PROGRESS))

        findViewById<TextView>(R.id.tvCompleted_Stats).text =
            getString(R.string.TextStatsCompleted, TaskViewModel.countTasks(TaskStatus.COMPLETED))

        findViewById<TextView>(R.id.tvCancelled_Stats).text =
            getString(R.string.TextStatsCancelled, TaskViewModel.countTasks(TaskStatus.CANCELLED))

        val percentage = TaskViewModel.getCompletionPercentage()

        findViewById<TextView>(R.id.tvPercentage_Stats).text =
            getString(R.string.TextStatsPercentage, percentage)

        findViewById<ProgressBar>(R.id.pbCompleted_Stats).progress = percentage
    }
}