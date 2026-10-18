package knight.nameless.multi.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.util.logging.Logger

fun createHttpClient(engine: HttpClientEngine): HttpClient {

    return HttpClient(engine) {

//        install(Logger) {}

    }
}