package knight.nameless.multi.data

import androidx.room3.Room
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.Worker

actual class DbFactory {
    actual fun createDatabase(): AppDatabase {

        return Room.databaseBuilder<AppDatabase>(name = dbFilename)
            .setDriver(WebWorkerSQLiteDriver(createSQLiteWorker()))
            .setQueryCoroutineContext(Dispatchers.Default)
            .build()
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun(
    """() => new Worker(new URL("sqlite-web-worker/worker.js", import.meta.url))"""
)
private external fun createSQLiteWorker(): Worker