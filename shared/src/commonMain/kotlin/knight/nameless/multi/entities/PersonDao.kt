package knight.nameless.multi.entities

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

//    This will insert the person if it doesn't exist or update it if it does exist it will check by id
    @Upsert
    suspend fun upsert(person: Person)

    @Delete
    suspend fun delete(person: Person)

    @Query("SELECT * FROM Person")
    fun getAll(): Flow<List<Person>>
}