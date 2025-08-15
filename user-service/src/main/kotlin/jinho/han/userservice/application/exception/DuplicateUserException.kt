package jinho.han.userservice.application.exception

import java.lang.RuntimeException

class DuplicateUserException: RuntimeException {
    constructor(): super()
    constructor(message: String): super(message)
}