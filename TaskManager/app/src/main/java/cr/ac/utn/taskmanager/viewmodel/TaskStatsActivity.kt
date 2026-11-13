package cr.ac.utn.taskmanager.viewmodel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.taskmanager.MainActivity
import cr.ac.utn.taskmanager.R

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

        loadStatistics()

        val btnBack: Button = findViewById(R.id.btnBackMain_stats)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
    }

    private fun loadStatistics() {
        val stats = TaskViewModel.getTaskStatistics()

        val tvTotal = findViewById<TextView>(R.id.tvTotalTasks_stats)
        val tvPending = findViewById<TextView>(R.id.tvPending_stats)
        val tvInProgress = findViewById<TextView>(R.id.tvInProgress_stats)
        val tvCompleted = findViewById<TextView>(R.id.tvCompleted_stats)
        val tvCancelled = findViewById<TextView>(R.id.tvCancelled_stats)
        val tvProgressLabel = findViewById<TextView>(R.id.tvProgressLabel_stats)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarCompleted_stats)

        tvTotal.text = getString(R.string.stats_total_tasks, stats.total)
        tvPending.text = getString(R.string.stats_pending, stats.pendingCount)
        tvInProgress.text = getString(R.string.stats_in_progress, stats.inProgressCount)
        tvCompleted.text = getString(R.string.stats_completed, stats.completedCount)
        tvCancelled.text = getString(R.string.stats_cancelled, stats.cancelledCount)
        tvProgressLabel.text = getString(R.string.stats_progress_label, stats.completedPercentage, stats.completedCount, stats.total)
        progressBar.progress = stats.completedPercentage
    }
}
