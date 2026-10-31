package knight.nameless.multi.data

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

actual class DbFactory(
    private val context: Context
) {
    actual fun createDatabase(): AppDatabase {

        val dbFile = context.getDatabasePath(dbFilename)

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}