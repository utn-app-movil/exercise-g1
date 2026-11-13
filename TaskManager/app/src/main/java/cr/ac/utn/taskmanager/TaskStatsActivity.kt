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


        val tvTotal = findViewById<TextView>(R.id.tvTotalTareas)
        val tvPendientes = findViewById<TextView>(R.id.tvPendientes)
        val tvEnProgreso = findViewById<TextView>(R.id.tvEnProgreso)
        val tvCompletadas = findViewById<TextView>(R.id.tvCompletadas)
        val tvCanceladas = findViewById<TextView>(R.id.tvCanceladas)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarCompletadas)
        val btnVolver = findViewById<Button>(R.id.btnVolver_TaskStats)


        val total = TaskViewModel.getTotalTasks()
        val pendientes = TaskViewModel.getPendingCount()
        val enProgreso = TaskViewModel.getInProgressCount()
        val completadas = TaskViewModel.getCompletedCount()
        val canceladas = TaskViewModel.getCancelledCount()
        val porcentaje = TaskViewModel.getCompletedPercentage()


        tvTotal.text = getString(R.string.TextTotalTasks) + " " + total
        tvPendientes.text = getString(R.string.TextPending) + " " + pendientes
        tvEnProgreso.text = getString(R.string.TextInProgress) + " " + enProgreso
        tvCompletadas.text = getString(R.string.TextCompleted) + " " + completadas
        tvCanceladas.text = getString(R.string.TextCancelled) + " " + canceladas


        progressBar.progress = porcentaje


        btnVolver.setOnClickListener {
            finish()
        }
    }
}