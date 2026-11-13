package cr.ac.utn.taskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.view.View
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

        val tvTotal = findViewById<TextView>(R.id.tvTotal_Stats)
        val tvPending = findViewById<TextView>(R.id.tvPending_Stats)
        val tvInProgress = findViewById<TextView>(R.id.tvInProgress_Stats)
        val tvCompleted = findViewById<TextView>(R.id.tvCompleted_Stats)
        val tvCancelled = findViewById<TextView>(R.id.tvCancelled_Stats)
        val tvPercent = findViewById<TextView>(R.id.tvPercent_Stats)
        val pbCompleted = findViewById<ProgressBar>(R.id.pbCompleted_Stats)
        val btnBack = findViewById<Button>(R.id.btnBack_Stats)

        tvPending.text = getString(R.string.TextPending, TaskViewModel.countByStatus(TaskStatus.PENDING))
        tvInProgress.text = getString(R.string.TextInProgress, TaskViewModel.countByStatus(TaskStatus.IN_PROGRESS))
        tvCompleted.text = getString(R.string.TextCompleted, TaskViewModel.countByStatus(TaskStatus.COMPLETED))
        tvCancelled.text = getString(R.string.TextCancelled, TaskViewModel.countByStatus(TaskStatus.CANCELLED))

        val percent = TaskViewModel.getCompletedPercentage()
        pbCompleted.progress = percent
        tvPercent.text = getString(R.string.TextPercent, percent)

        btnBack.setOnClickListener(View.OnClickListener{ _ ->
            finish()
        })
    }
}