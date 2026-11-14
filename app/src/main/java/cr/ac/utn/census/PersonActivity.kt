package cr.ac.utn.census

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.census.controllers.PersonController
import cr.ac.utn.census.data.MemoryDataManager
import cr.ac.utn.census.entities.Person
import cr.ac.utn.census.exceptions.CensusException
import java.time.LocalDate

/**
 * PersonActivity: Actividad encargada de gestionar la interfaz gr?fica
 * para el registro y control de personas dentro del sistema de censo.
 */
class PersonActivity : AppCompatActivity() {

    // Controlador que maneja la l?gica de negocio inyectando MemoryDataManager
    private lateinit var personController: PersonController

    // Referencias a los componentes de la interfaz de usuario (UI)
    private lateinit var etId: EditText
    private lateinit var etName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        // Inicializaci?n del controlador con el gestor de memoria
        personController = PersonController(MemoryDataManager())

        // Vincular los componentes visuales del XML
        etId = findViewById(R.id.etPersonId)
        etName = findViewById(R.id.etPersonName)
        etLastName = findViewById(R.id.etPersonLastName)
        etBirthDate = findViewById(R.id.etPersonBirthDate)
        etAddress = findViewById(R.id.etPersonAddress)
        btnSave = findViewById(R.id.btnSavePerson)

        // Configurar el evento de clic del bot?n guardar
        btnSave.setOnClickListener {
            savePerson()
        }
    }

    /**
     * Valida los datos y registra la persona a trav?s del controlador.
     */
    private fun savePerson() {
        val id = etId.text.toString().trim()
        val name = etName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val birthDateStr = etBirthDate.text.toString().trim()
        val address = etAddress.text.toString().trim()

        try {
            if (id.isEmpty() || name.isEmpty() || lastName.isEmpty() || birthDateStr.isEmpty()) {
                Toast.makeText(this, "Por favor complete los campos obligatorios", Toast.LENGTH_SHORT).show()
                return
            }

            // Convertir la fecha de String a LocalDate (formato esperado: YYYY-MM-DD)
            val birthDate = LocalDate.parse(birthDateStr)

            // Crear entidad con todos sus atributos requeridos
            val person = Person(
                id = id,
                name = name,
                lastName = lastName,
                birthDate = birthDate,
                address = address
            )
            
            // Registro mediante el m?todo correcto del controlador
            personController.registerPerson(person)

            Toast.makeText(this, "Persona registrada con ?xito", Toast.LENGTH_SHORT).show()
            clearFields()

        } catch (e: CensusException) {
            Toast.makeText(this, "Error de validaci?n: ${e.message}", Toast.LENGTH_LONG).show()
        } catch (e: java.time.format.DateTimeParseException) {
            Toast.makeText(this, "Formato de fecha inv?lido. Use YYYY-MM-DD", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Ocurri? un error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Limpia los campos de texto del formulario.
     */
    private fun clearFields() {
        etId.setText("")
        etName.setText("")
        etLastName.setText("")
        etBirthDate.setText("")
        etAddress.setText("")
        etId.requestFocus()
    }
}
