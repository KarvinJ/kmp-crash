package knight.nameless.multi.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import knight.nameless.multi.data.entities.User
import knight.nameless.multi.networking.util.NetworkError
import knight.nameless.multi.networking.util.Result
import kotlinx.serialization.SerializationException

class UserClient (
    private val httpClient: HttpClient
) {
    suspend fun getUser(): Result<User, NetworkError> {

        val response = try {

            httpClient.get("https://jsonplaceholder.typicode.com/users/1")

        } catch (_: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        }
        catch (_: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value) {

            in 200..299 -> {
                val user = response.body<User>()
                Result.Success(user)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)

        }
    }

    suspend fun getUsers(): Result<List<User>, NetworkError> {

        val response = try {

            httpClient.get("https://jsonplaceholder.typicode.com/users")

        } catch (_: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        }
        catch (_: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value) {

            in 200..299 -> {
                val user = response.body<List<User>>()
                Result.Success(user)
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