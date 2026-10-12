package knight.nameless.multi.room

import androidx.room3.Database
import androidx.room3.RoomDatabase
import knight.nameless.multi.entities.Person
import knight.nameless.multi.entities.PersonDao

@Database(
    entities = [Person::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun personDao(): PersonDao
}