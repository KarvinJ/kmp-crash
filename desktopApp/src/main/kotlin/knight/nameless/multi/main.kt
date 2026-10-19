package knight.nameless.multi

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.engine.okhttp.OkHttp
import knight.nameless.multi.networking.UserClient
import knight.nameless.multi.networking.createHttpClient

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Form",
    ) {
        App(
            client = remember {
                UserClient(createHttpClient(OkHttp.create()))
            }
        )
    }
}