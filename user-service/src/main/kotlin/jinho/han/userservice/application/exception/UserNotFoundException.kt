package jinho.han.userservice.application.exception

class UserNotFoundException : RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}