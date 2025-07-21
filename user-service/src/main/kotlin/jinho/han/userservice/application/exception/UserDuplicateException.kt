package jinho.han.userservice.application.exception

class UserDuplicateException: RuntimeException{
    constructor(): super()
    constructor(message: String): super(message)
}