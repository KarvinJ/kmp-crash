package knight.nameless.multi.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import knight.nameless.multi.entities.User
import knight.nameless.multi.networking.UserClient
import knight.nameless.multi.util.NetworkError
import knight.nameless.multi.util.onError
import knight.nameless.multi.util.onSuccess
import kotlinx.coroutines.launch

@Composable
fun NetworkCall(client: UserClient) {

    var user by remember {
        mutableStateOf<User?>(null)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<NetworkError?>(null)
    }

//    This is how you implement coroutines in compose
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {

        Button(onClick = {

//            Calling the coroutine scope
            scope.launch {

                isLoading = true
                errorMessage = null

                client.getUsers().onSuccess {

                    user = it
                }.onError {
                    errorMessage = it
                }
                isLoading = false
            }
        }) {
            if (isLoading) {

//                Simple loading progress indicator
                CircularProgressIndicator(
                    modifier = Modifier.size(15.dp),
                    strokeWidth = 1.dp,
                    color = Color.White
                )
            } else {
                Text("Get User!")
            }
        }

        user?.let {

            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text("@${it.username}")
                    Text("Email: ${it.email}")
                    Text("Phone: ${it.phone}")
                    Text("Website: ${it.website}")
                }
            }
        }

        errorMessage?.let {
            Text(
                text = it.name,
                color = Color.Red
            )
        }
    }
}