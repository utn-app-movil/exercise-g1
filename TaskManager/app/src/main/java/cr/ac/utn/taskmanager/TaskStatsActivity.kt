package cr.ac.utn.taskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskStatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Asociar la Activity con su diseño XML
        setContentView(R.layout.activity_task_stats)
        // Configurar el botón para regresar
        val btnBackMain = findViewById<Button>(
            R.id.btnBackMain_Stats
        )
        btnBackMain.setOnClickListener {
            finish()
        }
    }
    override fun onResume() {
        super.onResume()

        // Actualizar las estadísticas al mostrar la pantalla
        showStatistics()
    }
    private fun showStatistics() {

        // Obtener estadísticas calculadas en el ViewModel
        val stats = TaskViewModel.getTaskStatistics()

        // Mostrar el total de tareas
        findViewById<TextView>(R.id.tvTotal_Stats).text =
            getString(R.string.stats_total_count, stats.total)

        // Mostrar las tareas pendientes
        findViewById<TextView>(R.id.tvPending_Stats).text =
            getString(R.string.stats_pending_count, stats.pending)

        // Mostrar las tareas en progreso
        findViewById<TextView>(R.id.tvInProgress_Stats).text =
            getString(R.string.stats_in_progress_count, stats.inProgress)

        // Mostrar las tareas completadas
        findViewById<TextView>(R.id.tvCompleted_Stats).text =
            getString(R.string.stats_completed_count, stats.completed)

        // Mostrar las tareas canceladas
        findViewById<TextView>(R.id.tvCancelled_Stats).text =
            getString(R.string.stats_cancelled_count, stats.cancelled)

        // Mostrar el porcentaje completado
        findViewById<TextView>(R.id.tvProgress_Stats).text =
            getString(
                R.string.stats_progress_count,
                stats.completedPercentage
            )

        // Actualizar la barra de progreso horizontal
        findViewById<ProgressBar>(R.id.pbCompleted_Stats).progress =
            stats.completedPercentage
    }
}