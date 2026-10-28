package knight.nameless.multi.data

import knight.nameless.multi.data.dao.PersonDao
import knight.nameless.multi.data.entities.Person
import knight.nameless.multi.domain.PersonModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class PersonRepository(
    private val personDao: PersonDao
) {

    fun getAll(): Flow<List<PersonModel>> {

        val personModels = personDao.getAll().map { persons ->
            persons.map { it.toDomain() }
        }

        return personModels
    }

    fun getById(id: Int): Flow<PersonModel?> {

        val personModel = personDao.getById(id).map {
            it?.toDomain()
        }

        return personModel
    }

    suspend fun upsert(person: PersonModel) {
        personDao.upsert(person.toEntity())
    }

    suspend fun delete(person: PersonModel) {
        personDao.delete(person.toEntity())
    }
}

fun Person.toDomain() = PersonModel(id, name, lastName, phoneNumber)

fun PersonModel.toEntity() = Person(id, name, lastName, phoneNumber)