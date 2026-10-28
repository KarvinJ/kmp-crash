package knight.nameless.multi.data

import androidx.room3.Database
import androidx.room3.RoomDatabase
import knight.nameless.multi.data.dao.PersonDao
import knight.nameless.multi.data.entities.Person

@Database(
    entities = [Person::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun personDao(): PersonDao
}
