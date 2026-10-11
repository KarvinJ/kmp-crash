package knight.nameless.multi

class Greeting {

    private val platform = getPlatform()

    fun greet(): String {
        return "Hi there, ${platform.name}!"
    }
}