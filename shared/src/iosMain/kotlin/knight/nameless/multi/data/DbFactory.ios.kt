package knight.nameless.multi.data

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentationDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DbFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDatabase(): AppDatabase {
        val documentDir = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentationDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )

        val dbFile = documentDir?.path + dbFilename

        return Room.databaseBuilder<AppDatabase>(
            dbFile,
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()

    }
}