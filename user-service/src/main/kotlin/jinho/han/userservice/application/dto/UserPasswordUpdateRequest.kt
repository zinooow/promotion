package jinho.han.userservice.application.dto

class UserPasswordUpdateRequest(val id: Long, val currentPassword: String, val newPassword: String)