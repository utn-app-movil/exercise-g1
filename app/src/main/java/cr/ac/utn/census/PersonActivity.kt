package cr.ac.utn.census

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.census.controllers.PersonController
import cr.ac.utn.census.entities.Person
import cr.ac.utn.census.exceptions.CensusException

/**
 * PersonActivity: Actividad encargada de gestionar la interfaz gr?fica
 * para el registro y control de personas dentro del sistema de censo.
 */
class PersonActivity : AppCompatActivity() {

    // Controlador que maneja la l?gica de negocio y almacenamiento en memoria
    private lateinit var personController: PersonController

    // Referencias a los componentes de la interfaz de usuario (UI)
    private lateinit var etId: EditText
    private lateinit var etName: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        // Inicializaci?n de la instancia del controlador
        personController = PersonController()

        // Vincular los componentes visuales del XML con las variables de Kotlin
        etId = findViewById(R.id.etPersonId)
        etName = findViewById(R.id.etPersonName)
        btnSave = findViewById(R.id.btnSavePerson)

        // Configurar el evento de clic para el bot?n de guardar
        btnSave.setOnClickListener {
            savePerson()
        }
    }

    /**
     * Valida los datos ingresados por el usuario y los registra
     * a trav?s del controlador, manejando excepciones personalizadas.
     */
    private fun savePerson() {
        val id = etId.text.toString().trim()
        val name = etName.text.toString().trim()

        try {
            // Validaci?n b?sica de campos vac?os en la UI
            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return
            }

            // Creaci?n de la entidad y env?o al controlador
            val person = Person(id = id, name = name)
            personController.addPerson(person)

            Toast.makeText(this, "Persona registrada con ?xito", Toast.LENGTH_SHORT).show()
            clearFields()

        } catch (e: CensusException) {
            // Captura de errores de negocio definidos en la arquitectura
            Toast.makeText(this, "Error de validaci?n: ${e.message}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            // Captura de cualquier otro error imprevisto
            Toast.makeText(this, "Ocurri? un error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Limpia los campos de texto del formulario y devuelve el foco al campo de ID.
     */
    private fun clearFields() {
        etId.setText("")
        etName.setText("")
        etId.requestFocus()
    }
}
