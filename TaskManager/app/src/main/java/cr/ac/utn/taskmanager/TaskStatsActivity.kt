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

        findViewById<Button>(R.id.btnBackStats).setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        val stats = TaskViewModel.getStats()
        findViewById<TextView>(R.id.tvTotalStats).text =
            getString(R.string.StatsTotalValue, stats.total)
        findViewById<TextView>(R.id.tvPendingStats).text =
            getString(R.string.StatsPendingValue, stats.pending)
        findViewById<TextView>(R.id.tvInProgressStats).text =
            getString(R.string.StatsInProgressValue, stats.inProgress)
        findViewById<TextView>(R.id.tvCompletedStats).text =
            getString(R.string.StatsCompletedValue, stats.completed)
        findViewById<TextView>(R.id.tvCancelledStats).text =
            getString(R.string.StatsCancelledValue, stats.cancelled)
        findViewById<TextView>(R.id.tvPercentageStats).text =
            getString(R.string.StatsPercentageValue, stats.completedPercentage)
        findViewById<ProgressBar>(R.id.progressCompletedStats).progress = stats.completedPercentage
    }
}
