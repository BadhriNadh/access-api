package com.hash.access.service

import com.hash.access.model.User
import org.springframework.security.core.Authentication

interface UserService {
    fun saveUser(user: User): Boolean

    fun generateToken(authentication: Authentication): String

    fun getUserId(userName: String): User
}