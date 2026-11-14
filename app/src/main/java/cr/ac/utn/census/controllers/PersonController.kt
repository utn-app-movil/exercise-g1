package cr.ac.utn.census.controllers

import cr.ac.utn.census.data.IDataManager
import cr.ac.utn.census.entities.Person
import cr.ac.utn.census.exceptions.CensusException

class PersonController(private val dataManager: IDataManager) {

    fun registerPerson(person: Person) {
        if (dataManager.getById(person.id) != null) {
            throw CensusException("Ya existe una persona con el ID: ${person.id}")
        }
        dataManager.addPerson(person)
    }

    fun getPeople(): List<Person> {
        return dataManager.getPersons()
    }

    fun findById(id: String): Person {
        return dataManager.getById(id) ?: throw CensusException("No se encontr? la persona con el ID: $id")
    }

    fun modifyPerson(person: Person) {
        findById(person.id)
        dataManager.updatePerson(person)
    }

    fun removePerson(id: String) {
        findById(id)
        dataManager.deletePerson(id)
    }
}
