package knight.nameless.multi.data

import androidx.room3.Room
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.Worker

actual class DbFactory {
    actual fun createDatabase(): AppDatabase {

        val workerUrl: String = js(
            """new URL("sqlite-web-worker/worker.js", import.meta.url).toString()"""
        )

        val worker = Worker(workerUrl)

        return Room.databaseBuilder<AppDatabase>(name = dbFilename)
            .setDriver(WebWorkerSQLiteDriver(worker))
            .setQueryCoroutineContext(Dispatchers.Default)
            .build()
    }
}