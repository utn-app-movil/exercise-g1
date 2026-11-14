package cr.ac.utn.census

import cr.ac.utn.census.domain.model.Task

interface OnItemClickListener {
    fun onItemClick(task: Task)
}