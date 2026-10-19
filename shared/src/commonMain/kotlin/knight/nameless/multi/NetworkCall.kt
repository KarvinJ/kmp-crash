package knight.nameless.multi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import knight.nameless.multi.networking.UserClient
import knight.nameless.multi.util.NetworkError
import knight.nameless.multi.util.onError
import knight.nameless.multi.util.onSuccess
import kotlinx.coroutines.launch

@Composable
fun NetworkCall(client: UserClient) {

    var censoredText by remember {
        mutableStateOf<String?>(null)
    }
    var uncensoredText by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf<NetworkError?>(null)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        TextField(
            value = uncensoredText,
            onValueChange = { uncensoredText = it },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text("Uncensored text")
            }
        )
        Button(onClick = {

            scope.launch {
                isLoading = true
                errorMessage = null

                client.getUsers()
                    .onSuccess {
                        censoredText = it
                    }
                    .onError {
                        errorMessage = it
                    }
                isLoading = false
            }
        }) {
            if(isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(15.dp),
                    strokeWidth = 1.dp,
                    color = Color.White
                )
            } else {
                Text("Censor!")
            }
        }
        censoredText?.let {
            Text(it)
        }
        errorMessage?.let {
            Text(
                text = it.name,
                color = Color.Red
            )
        }
    }
}