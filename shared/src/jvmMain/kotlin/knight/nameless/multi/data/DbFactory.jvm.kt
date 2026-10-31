package knight.nameless.multi.data

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import java.io.File

actual class DbFactory {
    actual fun createDatabase(): AppDatabase {

        val dbFile = File(System.getProperty("java.io.tmpdir"), dbFilename)

        return Room.databaseBuilder<AppDatabase>(dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}