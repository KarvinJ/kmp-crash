package knight.nameless.multi.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import knight.nameless.multi.entities.User
import knight.nameless.multi.util.NetworkError
import knight.nameless.multi.util.Result
import kotlinx.serialization.SerializationException

class UserClient (
    private val httpClient: HttpClient
) {

    suspend fun getUsers(): Result<String, NetworkError> {

        val response = try {

            httpClient.get("https://jsonplaceholder.typicode.com/users/1")

        } catch (exception: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        }
        catch (exception: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value) {

            in 200..299 -> {
                val user = response.body<User>()
                Result.Success(user.name)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)

        }
    }
}