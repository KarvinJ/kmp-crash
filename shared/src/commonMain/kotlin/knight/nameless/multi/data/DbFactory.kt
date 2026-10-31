package knight.nameless.multi.data

//The expect class in Kotlin is used in multiplatform projects to declare a class that will have
// platform-specific implementations. It allows you to define a common interface in shared code
// while providing actual implementations for different platforms.
expect class DbFactory {

    fun createDatabase(): AppDatabase
}