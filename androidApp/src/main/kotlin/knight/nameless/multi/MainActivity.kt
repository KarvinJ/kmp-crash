package knight.nameless.multi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import knight.nameless.multi.networking.UserClient
import knight.nameless.multi.networking.createHttpClient
import io.ktor.client.engine.okhttp.OkHttp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                client = remember {
                    UserClient(createHttpClient(OkHttp.create()))
                }
            )
        }
    }
}

//If I pass an argument to my app file then I cant use preview
//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}