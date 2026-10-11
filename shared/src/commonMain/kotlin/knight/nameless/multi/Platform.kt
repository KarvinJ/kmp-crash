package knight.nameless.multi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform