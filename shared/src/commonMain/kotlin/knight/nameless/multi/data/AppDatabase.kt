package knight.nameless.multi.data

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import knight.nameless.multi.data.dao.PersonDao
import knight.nameless.multi.data.entities.Person

@Database(
    entities = [Person::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun personDao(): PersonDao
}

//object to initialize my database
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor: RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

//Defining the name of my database
internal const val dbFilename = "kmp_room_db.db"
