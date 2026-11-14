package cr.ac.utn.census.data

import cr.ac.utn.census.entities.Person

// DataManager: guarda las personas en memoria (en una lista)
class MemoryDataManager : IDataManager {
    private val persons = mutableListOf<Person>()

    override fun getPersons(): List<Person> = persons

    override fun getById(id: String): Person? {
        val result = persons.filter { it.id == id }
        return if (result.any()) result[0] else null
    }

    override fun addPerson(person: Person) {
        persons.add(person)
    }

    override fun updatePerson(person: Person) {
        deletePerson(person.id)
        addPerson(person)
    }

    override fun deletePerson(id: String) {
        persons.removeIf { it.id == id }
    }
}
