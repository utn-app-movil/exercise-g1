package cr.ac.utn.taskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        val stats = TaskViewModel.getStats()
        findViewById<TextView>(R.id.tvTotal_Stats).text = stats.total.toString()
        findViewById<TextView>(R.id.tvPending_Stats).text = stats.pending.toString()
        findViewById<TextView>(R.id.tvInProgress_Stats).text = stats.inProgress.toString()
        findViewById<TextView>(R.id.tvCompleted_Stats).text = stats.completed.toString()
        findViewById<TextView>(R.id.tvCancelled_Stats).text = stats.cancelled.toString()
        findViewById<ProgressBar>(R.id.pbCompleted_Stats).progress = stats.completedPercent
        findViewById<TextView>(R.id.tvPercent_Stats).text =
            getString(R.string.TextCompletedPercent, stats.completedPercent)

        findViewById<Button>(R.id.btnBack_Stats).setOnClickListener { finish() }
    }
}
