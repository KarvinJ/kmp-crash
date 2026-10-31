package knight.nameless.multi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import knight.nameless.multi.domain.PersonModel
import knight.nameless.multi.data.PersonRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//Esto se utiliza en una arquitecture mvvm (coroutines and flow) para manejar los datos de la ui
class PersonViewModel(
    private val personRepository: PersonRepository
) : ViewModel() {


    //    Una lista de flow te devolvera los datos en tiempo real, investigar mas sobre como funcionan los flow
    val persons = personRepository.getAll()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    fun upsertPerson(person: PersonModel) {

//        Launching the coroutine here to execute the upsert function. This viewmodel scope
//        is a coroutine scope that is tied to the lifecycle of the viewmodel.
        viewModelScope.launch {
            personRepository.upsert(person)
        }
    }

    fun deletePerson(person: PersonModel) {

        viewModelScope.launch {
            personRepository.delete(person)
        }
    }
}