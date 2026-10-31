package knight.nameless.multi.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import knight.nameless.multi.data.PersonRepository

fun personViewModelFactory(
    repository: PersonRepository
) = viewModelFactory {
    initializer {
        PersonViewModel(repository)
    }
}