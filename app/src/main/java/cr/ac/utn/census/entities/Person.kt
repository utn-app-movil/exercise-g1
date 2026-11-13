package cr.ac.utn.census.entities

import java.time.LocalDate

// Entity: guarda los datos de una persona del censo (basada en Task)
data class Person(
    val id: String,
    val name: String,
    val lastName: String,
    val birthDate: LocalDate,
    val address: String
)
