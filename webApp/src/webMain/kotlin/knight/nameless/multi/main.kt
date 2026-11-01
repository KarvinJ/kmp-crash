package knight.nameless.multi

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.ktor.client.engine.js.Js
//import knight.nameless.multi.data.DbFactory
import knight.nameless.multi.networking.UserClient
import knight.nameless.multi.networking.createHttpClient

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        App(
            client = remember {
                UserClient(createHttpClient(Js.create()))
            },

//            Need to find a way to correctly implement the Db Factory, to have room database in the
            // wasmJs and js build
//            dbFactory = DbFactory()
        )
    }
}