package knight.nameless.multi

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import io.ktor.client.engine.darwin.Darwin
import knight.nameless.multi.data.DbFactory
import knight.nameless.multi.networking.UserClient
import knight.nameless.multi.networking.createHttpClient

//Ios app place
fun MainViewController() = ComposeUIViewController {
    App(
        client = remember {
            UserClient(createHttpClient(Darwin.create()))
        },
        dbFactory = DbFactory()
    )
}