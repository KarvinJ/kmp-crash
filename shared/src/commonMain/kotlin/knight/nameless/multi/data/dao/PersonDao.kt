package knight.nameless.multi.data.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Query
import androidx.room3.Upsert
import knight.nameless.multi.data.entities.Person
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    //    This will insert the person if it doesn't exist or update it if it does exist
    //    it will check by id, this call works for both create and update
    @Upsert
    suspend fun upsert(person: Person)

    @Delete
    suspend fun delete(person: Person)

    @Query("SELECT * FROM Person")
    fun getAll(): Flow<List<Person>>

    @Query("SELECT * FROM Person WHERE id = :id")
    fun getById(id : Int): Flow<Person?>
}