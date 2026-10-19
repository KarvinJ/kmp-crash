package knight.nameless.multi.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(engine: HttpClientEngine): HttpClient {

    return HttpClient(engine) {

//        installing and configuring the ktor plugins
        install(ContentNegotiation) {

            json(
                json = Json {
//                    Configurations
                    prettyPrint = true // Specifies whether resulting JSON should be pretty-printed:
                    // formatted and optimized for human readability. false by default.
                    ignoreUnknownKeys = true // ignore key that are not define in the data class
                }
            )
        }

        install(Logging) {

//            Defining the logger
            logger = object : Logger {
                override fun log(message: String) {
                    print(message)
                }
            }

            level = LogLevel.ALL

        }

        defaultRequest {
//            defining the default content type for our requests
            contentType(ContentType.Application.Json)

//            We can also define the headers
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

    }
}