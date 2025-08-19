package jinho.han.userservice.application.exception

import java.lang.RuntimeException

class UnauthorizedAccessException: RuntimeException {
    constructor(): super()
    constructor(message: String): super(message)
}