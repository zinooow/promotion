package jinho.han.couponservice.util

inline fun <reified T : Enum<T>> enumValueOfSafe(name: String): T {
    return try {
        enumValueOf<T>(name.uppercase())
    } catch (e: IllegalArgumentException) {
        throw InvalidStatusException("Invalid ${T::class.simpleName} value: $name")
    }
}

class InvalidStatusException: RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}