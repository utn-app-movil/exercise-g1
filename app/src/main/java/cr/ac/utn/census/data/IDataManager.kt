package cr.ac.utn.census.data

import cr.ac.utn.census.entities.Person

// Interface: contrato de almacenamiento. El controller depende de esta
// interface y no de una clase concreta (principio D de SOLID)
interface IDataManager {
    fun getPersons(): List<Person>
    fun getById(id: String): Person?
    fun addPerson(person: Person)
    fun updatePerson(person: Person)
    fun deletePerson(id: String)
}
